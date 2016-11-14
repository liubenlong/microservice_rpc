package com.freedom.rpc.thrift.common.extended.server;

import java.lang.reflect.Constructor;
import java.util.Set;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.reflections.Reflections;

import com.freedom.rpc.thrift.common.property.ServerProperties;
import com.freedom.rpc.thrift.common.annotation.Processor;
import com.freedom.rpc.thrift.common.utils.Logger;

public class ServerSideProcessorRegisterHelper {
	private static final Logger logger = Logger.getLogger(ServerSideProcessorRegisterHelper.class);
	private static final String IFACE = "$Iface";
	private static final String PROCESSOR = "$Processor";
	private static final String CLIENT = "$Client";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerProcessor(TMultiplexedProcessor processor) {
		// 切换成自动扫描方式
		try {
			// 扫描有注解Processor的类
			String scanPackage = ServerProperties.getInstance().getScanPackage();
			logger.info("scan package --- " + scanPackage);
			Reflections reflections = new Reflections(scanPackage);
			Set<Class<?>> annotationedClasses = reflections.getTypesAnnotatedWith(Processor.class);
			logger.info("***|*** annotationedClasses ---" + annotationedClasses);
			for (Class annotationedClass : annotationedClasses) {
				// 0)获取本身类
				logger.info("-----------------------------------------");
				String annotationedClassName = annotationedClass.getName();// HelloWorldService
				logger.info("current annotationedClass: " + annotationedClass);// HelloWorldService
				logger.info("annotationedClassName:" + annotationedClassName);// HelloWorldService
				// 1)获取接口类
				Class interfaceClz = Class.forName(annotationedClassName + IFACE);
				logger.info("interface Clz Name: " + interfaceClz);
				// 2)获取Processor类
				Class processorClz = Class.forName(annotationedClassName + PROCESSOR);
				logger.info("processor clz Name:" + processorClz.getName());
				// -----------下面开始实例化
				// 3)实例化实现类
				Object serviceInstance = null;
				// 尝试初始化
				Set<Class> implementedClasses = reflections.getSubTypesOf(interfaceClz);
				logger.info("implementedClasses: " + implementedClasses);
				for (Class implementedClass : implementedClasses) {
					// 去掉它自己的HelloWorldService$Client类
					if (implementedClass.getName().equals(annotationedClassName + CLIENT)) {
						continue;
					}
					serviceInstance = (Object) implementedClass.newInstance();// new
					// HelloWorldServiceImpl()
					break;
				}
				// 没有写实现类,不会去注册//执行下一个
				if (null == serviceInstance) {
					logger.error("no service instance for --- " + interfaceClz);
					continue;
				}
				logger.info("generated : " + serviceInstance);
				// 4)实例化Processor类
				Constructor constructor = processorClz.getConstructor(new Class[] { interfaceClz });
				TProcessor processorInstance = (TProcessor) constructor.newInstance(serviceInstance);
				logger.info("constructor: " + constructor);
				logger.info("generated:" + processorInstance);
				// 5)开始获取注解信息
				Processor annotation = (Processor) annotationedClass.getAnnotation(Processor.class);
				String group = annotation.group();
				String service = annotation.service();
				String edition = annotation.edition();
				logger.info("detail: " + group + " " + service + " " + edition);
				// 6)最后一步				
				processor.registerProcessor(group + ":" + service + ":" + edition, processorInstance);
				logger.info("---------------------------------------");
			}
			logger.info("注册结束...");
		} catch (Exception e) {
			logger.error(e.toString());
			System.exit(-1);
		}
	}
}

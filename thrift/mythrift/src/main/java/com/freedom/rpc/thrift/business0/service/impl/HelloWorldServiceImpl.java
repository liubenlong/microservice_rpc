package com.freedom.rpc.thrift.business0.service.impl;

import org.apache.thrift.TException;

import com.freedom.rpc.thrift.business0.service.HelloWorldService;

public class HelloWorldServiceImpl implements HelloWorldService.Iface {

	public String helloWorldString(String content) throws TException {
		return "欢迎star ---> " + content;
	}

	public boolean helloWorldBoolean(int number) throws TException {
		return false;
	}

}

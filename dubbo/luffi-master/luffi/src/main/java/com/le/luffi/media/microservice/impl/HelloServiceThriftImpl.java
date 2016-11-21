package com.le.luffi.media.microservice.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.demo.hello.Hello;
import com.alibaba.dubbo.demo.hello.PersonDemo;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个不能用于线上环境，因为thrift客户端不是线程安全的
 * 解决办法：1、使用commons-pools2  2、使用MyThrift：http://git.oschina.net/qiangzigege/MyThrift
 */
@Service(protocol = {"thriftNative"}, cluster = "failfast")
public class HelloServiceThriftImpl implements Hello.Iface {
   @Override
   public boolean helloBoolean(boolean para) throws TException {
       return para;
   }
   @Override
   public int helloInt(int para) throws TException {
       System.out.println("入参33: "+para);
       return 33;
   }
   @Override
   public String helloNull() throws TException {
       return null;
   }

    @Override
    public PersonDemo getOneUser(String para) throws TException {
        System.out.println("入参: "+para);
        return new PersonDemo(false, "wfghjkl", 23, 56);
    }

    @Override
    public List<PersonDemo> getUsers(String para) throws TException {
        List<PersonDemo> list = new ArrayList<PersonDemo>();
        for (short i = 0; i < 10; i++) {
            PersonDemo person = new PersonDemo();
            person.setName("wertyuiopasdfghjkl;'zxcvbnm,./;[p;oiuytrdcvbnm,kjhgfdcvbnm,lkjuyhgfdcvbnm,.").setAge(23).setChildrenCount(22).setSex(false);
            list.add(person);
        }
        return list;
    }

    @Override
   public String helloString(String para) throws TException {
       System.out.println("入参：" + para);
       return "hello  " + para;
   }
   @Override
   public void helloVoid() throws TException {
       System.out.println("Hello World");
   }
}
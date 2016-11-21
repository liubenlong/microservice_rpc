/**
 * Copyright 1999-2014 dangdang.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.le.luffi.media.microservice.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.demo.hello.HelloService;
import com.alibaba.dubbo.demo.hello.Person;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liubenlong
 */
@Service(protocol = {"dubbo"})
public class HelloServiceImpl implements HelloService {



    public String hello(String name) {
        Logger log = LogManager.getLogger(HelloServiceImpl.class);
        log.info("入参：name = " + name);

        System.out.println("入参：name = " + name);
        return "hello " + name;
    }


    public List<Person> hello1(String name) {
        List<Person> list = new ArrayList<Person>();
        for (short i = 0; i < 10; i++) {
            Person person = Person.builder().name("wertyuiopasdfghjkl;'zxcvbnm,./;[p;oiuytrdcvbnm,kjhgfdcvbnm,lkjuyhgfdcvbnm,.")
                    .age(23).childrenCount(22).sex(false).build();
            list.add(person);
        }
        return list;
    }

}

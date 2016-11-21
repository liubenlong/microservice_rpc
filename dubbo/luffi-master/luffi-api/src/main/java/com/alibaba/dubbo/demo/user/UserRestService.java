//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.dubbo.demo.user;

import javax.validation.constraints.Min;
import javax.ws.rs.QueryParam;

public interface UserRestService {
    User1 getUser(@Min(value = 1L, message = "User ID must be greater than 1") Long var1);

    RegistrationResult registerUser(User1 var1);

    String aaaaa(String var1);

    String sayHello(@QueryParam("name") String var1);
}

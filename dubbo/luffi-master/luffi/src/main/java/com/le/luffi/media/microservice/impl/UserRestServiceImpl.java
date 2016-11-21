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
import com.alibaba.dubbo.demo.user.RegistrationResult;
import com.alibaba.dubbo.demo.user.User1;
import com.alibaba.dubbo.demo.user.UserRestService;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author liubenlong
 */
@Service(protocol = {"rest"}, group = "annotationConfig", validation = "true")
@Path("users")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class UserRestServiceImpl implements UserRestService {

    private static Logger logger = LoggerFactory.getLogger(UserRestServiceImpl.class);

    /**
     *  测试URL：
     *  http://localhost:8888/services/users/2
     *  http://localhost:8888/services/users/2.json
     *  http://localhost:8888/services/users/2.xml
     * @param id
     * @return
     */
    @GET
    @Path("{id : \\d+}")
    public User1 getUser(@PathParam("id") Long id/*, @Context HttpServletRequest request*/) {
        // test context injection
//        System.out.println("Client address from @Context injection: " + (request != null ? request.getRemoteAddr() : ""));
//        System.out.println("Client address from RpcContext: " + RpcContext.getContext().getRemoteAddressString());

        if (RpcContext.getContext().getRequest(HttpServletRequest.class) != null) {
            System.out.println("Client IP address from RpcContext: " + RpcContext.getContext().getRequest(HttpServletRequest.class).getRemoteAddr());
        }
        if (RpcContext.getContext().getResponse(HttpServletResponse.class) != null) {
            System.out.println("Response object from RpcContext: " + RpcContext.getContext().getResponse(HttpServletResponse.class));
        }

        logger.info("com.letv.whatsliveExternal.serviceImpl.UserRestServiceImpl.getUser");

        User1 u = new User1();
        u.setUsername("erfgsshjk");
        return u;
    }

    /**
     * http://localhost:8888/services/users/greeting?name=a
     * @param name
     * @return
     */
    @GET
    @Path("greeting")
    @Produces("application/json; charset=UTF-8")
    public String sayHello(@QueryParam("name") String name) {
        logger.info("com.letv.whatsliveExternal.serviceImpl.UserRestServiceImpl.sayHello");
        return "Hello " + name;
    }



    @POST
    @Path("register")
    @Consumes({MediaType.APPLICATION_JSON})
    public RegistrationResult registerUser(User1 user) {
        System.out.println(user);
        return new RegistrationResult(6L);
    }


    @POST
    @Path("aaaaaaa")
    public String aaaaa(String a) {
        System.out.println("入参2：  "+a);
        return "sdx";
    }
}

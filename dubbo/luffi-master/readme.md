
这里只是简单的引用配置，具体详情参见dubbo官方文档：

[http://dubbo.io/User+Guide-zh.htm](http://dubbo.io/User+Guide-zh.htm)

[http://dubbo.io/Developer+Guide-zh.htm](http://dubbo.io/Developer+Guide-zh.htm)




注解方式：
**pom文件引入**
```xml
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.4.8</version>
    <exclusions>
        <exclusion>
            <artifactId>log4j</artifactId>
            <groupId>log4j</groupId>
        </exclusion>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
        <exclusion>
            <artifactId>slf4j-api</artifactId>
            <groupId>org.slf4j</groupId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>com.101tec</groupId>
    <artifactId>zkclient</artifactId>
    <version>0.9</version>
    <exclusions>
        <exclusion>
            <artifactId>slf4j-log4j12</artifactId>
            <groupId>org.slf4j</groupId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>dubbo</artifactId>
    <version>2.8.5</version>
    <exclusions>
        <exclusion>
            <artifactId>slf4j-log4j12</artifactId>
            <groupId>org.slf4j</groupId>
        </exclusion>
        <exclusion>
            <artifactId>spring</artifactId>
            <groupId>org.springframework</groupId>
        </exclusion>
        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>spring-expression</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>spring-expression</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

**spring配置文件application.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
        http://code.alibabatech.com/schema/dubbo
        http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
 
    <dubbo:application name="whatslive-cms"/>
    <dubbo:registry protocol="zookeeper" address="${zookeeper.address}"/>
    <dubbo:consumer check="false" />
 
    <!--注意：这里使用注解方式，只能讲dubbo服务的引用写到server中。不可以直接在controller中引用-->
    <dubbo:annotation package="项目package路径" />
 
</beans>
```

编写dubbo.properties文件。注意：这里只有当一个服务器上部署多个服务，并且多个服务引用了dubbo服务的时候才需要写 。

里面地址自定义即可

**bbo.properties**
```xml
dubbo.registry.file=/你的项目路径/dubbo-registry.properties
```

引用具体的dubbo服务，需要引用提供方的接口依赖：
**添加dubbo服务提供方依赖**

```xml
<dependency>
    <groupId>luffi</groupId>
    <artifactId>luffi-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

service文件中通过注解引用dubbo服务。下面示例中HelloService是一个dubbo服务
****.service**
```java
 @Reference
    private HelloService helloService;
  
  
public ResponseBody hello(Map<String, Object> params, String sid, RequestHeader header) {
 
    try {
        String string = JSON.toJSONString(helloService.hello("s收到d"));
        LOGGER.info(string);
        System.out.println(string);
 
        Map<String ,Object> result = new HashMap<String ,Object>();
        result.put("result", string);
        return getResponseBody(result);
    } catch (Exception e) {
        LogUtils.logError("fail to close getPersonalToken,[exception] ", e);
        return getErrorResponse(sid, e.getMessage());
 
    }
}
```











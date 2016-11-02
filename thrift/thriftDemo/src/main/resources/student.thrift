namespace java com.alibaba.dubbo.demo.hello

include "PersonDemo.thrift"

service Student{ 
  string helloString(1:string param) 
  PersonDemo.PersonDemo getOneUser(1:i64 para)
}
 
namespace java com.alibaba.dubbo.demo.hello

include "PersonDemo.thrift"

service Hello{ 
  string helloString(1:string para) 
  i32 helloInt(1:i32 para) 
  bool helloBoolean(1:bool para) 
  void helloVoid() 
  string helloNull() 
  PersonDemo.PersonDemo getOneUser(1:string para)
  list<PersonDemo.PersonDemo> getUsers(1:string para)
}
 
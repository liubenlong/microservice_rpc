package service.demo;

import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.List;

public class HelloServiceImpl implements Hello.Iface {
    @Override 
    public boolean helloBoolean(boolean para) throws TException { 
        return para;
    } 
    @Override 
    public int helloInt(int para) throws TException {
        System.out.println("rucan:" + para);
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return para; 
    } 
    @Override 
    public String helloNull() throws TException { 
        return null; 
    }

    @Override
    public Person getOneUser(String para) throws TException {
        System.out.println("入参: "+para);
        return new Person(false, "wfghjkl", 23, 56);
    }

    @Override
    public List<Person> getUsers(String para) throws TException {
        System.out.println("入参: "+para);
        List<Person> list = new ArrayList<Person>();
        for (short i = 0; i < 10; i++) {
            Person person = new Person();
            person.setName("wertyuiopasdfghjkl;'zxcvbnm,./;[p;oiuytrdcvbnm,kjhgfdcvbnm,lkjuyhgfdcvbnm,.").setAge(23).setChildrenCount(22).setAgeIsSet(false);
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
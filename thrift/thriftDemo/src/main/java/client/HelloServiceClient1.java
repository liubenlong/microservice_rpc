package client;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import service.demo.Hello;
import service.demo.Person;

import java.util.List;

public class HelloServiceClient1 {

    public static final String SERVER_IP = "localhost";
    public static final int SERVER_PORT = 8090;
    public static final int TIMEOUT = 30000;

/**
    * 调用 Hello 服务
    * @param args
    */
   public static void main(String[] args) {
       TTransport transport = null;
       try {
           transport = new TFramedTransport(new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT));
           // 协议要和服务端一致
           TProtocol protocol = new TCompactProtocol(transport);
           Hello.Client client = new Hello.Client(protocol);
           transport.open();

           int result = client.helloInt(232);
           System.out.println("Thrify client result =: " + result);

//           String result = client.helloString("hfjtydiyghfkjdhftgh");
//          Person p = client.getOneUser("aaaa");
           List<Person> p = client.getUsers("cccc");
           System.out.println("Thrify client result =: " + p);
       } catch (TTransportException e) {
           e.printStackTrace();
       } catch (TException e) {
           e.printStackTrace();
       } finally {
           if (null != transport) {
               transport.close();
           }
       }
   }
}
package server;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import service.demo.Hello;
import service.demo.HelloServiceImpl;

public class HelloServiceServer2 {

    public static final int SERVER_PORT = 8090;

    /** 
     * 启动 Thrift 服务器
     * @param args 
     */ 
    public static void main(String[] args) {
        try {
            System.out.println("HelloWorld TThreadedSelectorServer start ....");

            TProcessor tprocessor = new Hello.Processor(new HelloServiceImpl());
            TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(SERVER_PORT);
            TThreadedSelectorServer.Args tArgs = new TThreadedSelectorServer.Args(tnbSocketTransport);
            tArgs.processor(tprocessor);
            tArgs.transportFactory(new TFramedTransport.Factory());
            // 二进制协议
            tArgs.protocolFactory(new TCompactProtocol.Factory());

            TServer server = new TThreadedSelectorServer(tArgs);
            System.out.println("Hello TThreadedSelectorServer....");
            server.serve(); // 启动服务
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    } 
 } 
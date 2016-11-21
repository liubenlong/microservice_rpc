package yjmyzz.grpc.study.test;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Test;
import yjmyzz.grpc.study.dto.QueryParameter;

/**
 * Created by jimmy on 9/26/15.
 */
public class ContactTest {

    @Test
    public void test() throws InvalidProtocolBufferException {

        QueryParameter queryParameter = QueryParameter.newBuilder().setAgeStart(1).setAgeEnd(5).build();
        byte[] bytes1 = queryParameter.toByteArray();
        System.out.println("Protobuf 3.0 二进制序列后的byte数组长度：" + bytes1.length);

        QueryParameter result = QueryParameter.parseFrom(bytes1);
        System.out.println(queryParameter.getAgeStart() + " - " + result.getAgeStart());

    }
}

package com.alibaba.dubbo.demo.hello;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.io.Serializable;

/**
 * Created by liubenlong on 2016/11/9.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable{

    private boolean sex;
    private String name;
    private int age;
    private int childrenCount;

}

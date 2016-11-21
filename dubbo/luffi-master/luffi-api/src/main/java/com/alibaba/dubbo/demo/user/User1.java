//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.dubbo.demo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User1 implements Serializable {

    @NotNull
    @Min(1L)
    private Long id;
//    @JsonProperty("username")
    @XmlElement(
        name = "username"
    )
    @NotNull
    @Size(
        min = 6,
        max = 50
    )
    private String username;


    public String toString() {
        return "User (id=" + this.id + ", name=\'" + this.username + '\'' + ')';
    }
}

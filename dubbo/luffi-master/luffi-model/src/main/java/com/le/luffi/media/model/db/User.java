package com.le.luffi.media.model.db;

import lombok.experimental.Builder;

import java.io.Serializable;
import java.util.Date;

@Builder
public class User extends BaseModel   implements Serializable {
    private Integer id;

    private String nickname;

    private String icon;

    private Integer businessid;

    private String subplatformuserid;

    private Integer appid;

    private Integer sex;

    private String nationality;

    private Date createtime;

    private Date updatetime;

    public User(Integer id, String nickname, String icon, Integer businessid, String subplatformuserid, Integer appid, Integer sex, String nationality, Date createtime, Date updatetime) {
        this.id = id;
        this.nickname = nickname;
        this.icon = icon;
        this.businessid = businessid;
        this.subplatformuserid = subplatformuserid;
        this.appid = appid;
        this.sex = sex;
        this.nationality = nationality;
        this.createtime = createtime;
        this.updatetime = updatetime;
    }

    public User() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public Integer getBusinessid() {
        return businessid;
    }

    public void setBusinessid(Integer businessid) {
        this.businessid = businessid;
    }

    public String getSubplatformuserid() {
        return subplatformuserid;
    }

    public void setSubplatformuserid(String subplatformuserid) {
        this.subplatformuserid = subplatformuserid == null ? null : subplatformuserid.trim();
    }

    public Integer getAppid() {
        return appid;
    }

    public void setAppid(Integer appid) {
        this.appid = appid;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality == null ? null : nationality.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}
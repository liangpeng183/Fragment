package com.smart.fragment.bean;

import java.io.Serializable;

public class JD implements Serializable {   //实现序列化

    private String jdId;  // id
    private String jdName;  // 名称
    private String jdDesc;  // 描述
    private String position;  // 位置
    private String image;   // 图片

    public String getJdId() {
        return jdId;
    }

    public void setJdId(String jdId) {
        this.jdId = jdId;
    }

    public String getJdName() {
        return jdName;
    }

    public void setJdName(String jdName) {
        this.jdName = jdName;
    }

    public String getJdDesc() {
        return jdDesc;
    }

    public void setJdDesc(String jdDesc) {
        this.jdDesc = jdDesc;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
package com.smart.fragment.bean;

import java.io.Serializable;

public class Jd_pic implements Serializable {

    private String imgId;
    private String jdId;
    private String image;

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getJdId() {
        return jdId;
    }

    public void setJdId(String jdId) {
        this.jdId = jdId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

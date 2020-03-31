package com.smart.fragment.bean;

import java.io.Serializable;

public class ZhuSu implements Serializable {

    private String id;
    private String name;
    private String zsDesc;
    private String position;
    private String pic;
    private double price;
    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZsDesc() {
        return zsDesc;
    }

    public void setZsDesc(String zsDesc) {
        this.zsDesc = zsDesc;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

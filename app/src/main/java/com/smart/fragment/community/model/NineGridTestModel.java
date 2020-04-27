package com.smart.fragment.community.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 作者：HMY
 * 时间：2016/5/13
 */
public class NineGridTestModel implements Serializable {

    private static final long serialVersionUID = 2189052605715370758L; // 序列化

    // 图片 url 集合
    public List<String> urlList = new ArrayList<>();

    public boolean isShowAll = false;
    // getter  setter  在 NineGridLayout.java 中实现
}

package com.feirui.feiyunbangong.entity;

/**
 * Created by lice on 2017/9/28.
 */

public class Item {
    private String name;
    private int imageId;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public Item(String name, int imageId) {
        super();
        this.name = name;
        this.imageId = imageId;
    }
}

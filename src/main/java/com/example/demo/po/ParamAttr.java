package com.example.demo.po;

public class ParamAttr {

    private String id;
    private String manufactureId;
    private int indexno;
    private String name;
    private int type;
    private int isPrivate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getManufactureId() {
        return manufactureId;
    }
    public void setManufactureId(String manufactureId) {
        this.manufactureId = manufactureId;
    }
    public int getIndexno() {
        return indexno;
    }
    public void setIndexno(int indexno) {
        this.indexno = indexno;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getIsPrivate() {
        return isPrivate;
    }
    public void setIsPrivate(int isPrivate) {
        this.isPrivate = isPrivate;
    }
    @Override
    public String toString() {
        return "ParamAttr [id=" + id + ", manufactureId=" + manufactureId + ", indexno="
                + indexno + ", name=" + name   + ", type=" + type  + ", isPrivate=" + isPrivate + "]";
    }
    
}

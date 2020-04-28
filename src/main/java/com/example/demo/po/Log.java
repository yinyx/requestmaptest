package com.example.demo.po;

import java.util.Date;

public class Log {

    private Date datetime; 
    
    private int type;
    
    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
}

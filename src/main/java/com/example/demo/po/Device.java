package com.example.demo.po;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;

@ContentRowHeight(10)
@HeadRowHeight(20)
@ColumnWidth(25)
public class Device extends BaseRowModel{
    //@ExcelProperty(value="通信状态1",index=8)
    //private String State1;
    
    //@ExcelProperty(value="通信状态",index=0)
    private String cname;
    
    //@ExcelProperty(value="所属厂家",index=1)
    private String factory;
    
    //@ExcelProperty(value="所属线路",index=2)
    private String line;

    //@ExcelProperty(value="安装序号",index=3)
    private Integer indexno;
    
    //@ExcelProperty(value="装置标识",index=4)
    private String deviceName;
    
    //@ExcelProperty(value="杆塔名称 ",index=5)
    private String tower;
    
    //@ExcelProperty(value="相别",index=6)
    private String phase;
    
    //@ExcelProperty(value="协议类型",index=7)
    private String protocolType;
    
    
    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
    
    //public String getState1() { return State1; }
    
    //public void setState1(String State) { this.State1 = State; }
    
    public Integer getIndexno() {
        return indexno;
    }

    public void setIndexno(Integer indexno) {
        this.indexno = indexno;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }
    
    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getTower() {
        return tower;
    }

    public void setTower(String tower) {
        this.tower = tower;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }
     
}

package com.example.demo.service;

import java.util.Map;

import java.util.List;

import com.example.demo.po.DataTableModel;

public interface FaultService {

    DataTableModel queryWaveInfoList(Map<String, String> dataTableMap);
    
    DataTableModel queryFaultList(Map<String, String> dataTableMap);
    
    DataTableModel queryFaultListInGis(Map<String, String> dataTableMap);
    
    Map<String, Object> getWaveById(String userId);
    
    Map<String, Object> getFaultById(String userId);
    
    void setDealFaultById(String userId);
    
    public List<Map<String, Object>> queryLineFaultList(String userId, String type, String page, String pageSize);
    
    public long countLineFaultNum(String userId, String type);
    
    public int countLineUnReadNum(String userId);
    
    public List<Map<String, Object>> queryDeviceFaultList(String userId, String type, String page, String pageSize);
    
    public long countDeviceFaultNum(String userId, String type);
    
    public int countDeviceUnReadNum(String userId);
    
}

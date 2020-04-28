package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.po.DataTableModel;

public interface GisService {

    DataTableModel queryDeviceList(Map<String, String> dataTableMap);
    
    DataTableModel queryLineListForGis(Map<String, String> dataTableMap);
    
    DataTableModel queryFaultList(Map<String, String> dataTableMap);
    
    List<Map<String,Object>> queryDeviceListForGis(String userId);
    
    List<Map<String,Object>> queryTowerListForGis(String LineUserId);
    
    List<Map<String,Object>> queryFaultListForGis(String userId);
}

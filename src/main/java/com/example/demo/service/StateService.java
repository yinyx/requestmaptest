package com.example.demo.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.example.demo.po.DataTableModel;

public interface StateService {

    DataTableModel queryHeartBeatList(Map<String, String> dataTableMap);
    
    DataTableModel queryWorkConditionList(Map<String, String> dataTableMap); 
    
//    DataTableModel queryWorkConditionListOrder(Map<String, String> dataTableMap); 
    
    DataTableModel queryOrderStatusList(Map<String, String> dataTableMap);
    
    DataTableModel queryAlarmList(Map<String, String> dataTableMap);
      
    DataTableModel queryParameterAttrList(Map<String, String> dataTableMap);
    
    DataTableModel queryParameterList(Map<String, String> dataTableMap);
    
    DataTableModel querySelfCheckList(Map<String, String> dataTableMap);
    
    DataTableModel queryCommunicateState(Map<String, String> dataTableMap);
    
//    DataTableModel queryCommunicateStateNew(Map<String, String> dataTableMap);
    
    Map<String, Object> checkFactoryLogin(String username, String password ,String factoryId);
    
    void saveParamAttr(Map<String, Object> paramMap);
    
    int addReadOrderByDeviceIdanduserId(String recordId, String userId);
    
    int readSelfCheckById(String recordId, String userId);
    
    int sendDownloadLogById(String recordId, String userId);
    
    String getDeviceLogNameById(String recordId);
    
    //addResetOrderByDeviceIdanduserId
    int addResetOrderByDeviceIdanduserId(String recordId, String userId);
    
    int getAuhorityByUser(String userId);
    
    String getPasswordByDevice(String DeviceId);
    
    //addSetOrderByDeviceIdanduserId
    int addSetOrderByDeviceIdanduserId(String recordId, String userId, String content, int[] ValueLst);
    
    Map<String, Object> getParamAttrById(String regulatorId);
    
    //getParamById
    Map<String, Object> getParamById(String regulatorId); 
    
    //getcommonParamById
    Map<String, Object> getcommonParamById(String regulatorId); 
    
    //getParamNameById
    List<String> getParamNameById(String regulatorId); 
    
    boolean deleteParamAttrById(String regulatorId);
    
    void setDealAlarmById(String userId);
    
    Map<String, Object> queryWavePwd(Map<String, Object> paramMap);
}

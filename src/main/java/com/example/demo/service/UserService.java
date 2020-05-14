package com.example.demo.service;

import java.util.Map;

import com.example.demo.po.DataTableModel;
import com.example.demo.po.Line;
import com.example.demo.po.User;
import com.example.demo.po.Log;

import java.util.List;

public interface UserService {
    Map<String, Object> findUserByName(String username, String password);
    
    List<Line> queryLine();

    List<Line> queryLineByUser(String userId);
    
    List<Log> queryLog();
    
    List<Map<String, Object>> queryUserList();
    
    List<Map<String, Object>> getAllDeviceInfo(String line, String factory);
    
    DataTableModel queryLogList(Map<String, String> dataTableMap);
    
    DataTableModel queryUsersList(Map<String, String> dataTableMap);
    
    boolean queryUserNameIsRepeat(String userName);
    
    void saveSchoolUser(Map<String, Object> paramMap);
    
    Map<String, Object> getUserById(String userId);
    
    boolean deleteSchoolUser(String userId);
    
    boolean deleteRegulator(String userId);
    
    String findUserPwdById(String id);
    
    String getPassByDevId(String id);
    
    void saveNewPassWord(Map<String, Object> paramMap);
    
    void saveWaveWord(Map<String, Object> paramMap);
    
    void saveDevicePass(Map<String, Object> paramMap);
    
    void resetPassword(Map<String, Object> paramMap);
    
    Map<String, Object> getRegulatorById(String regulatorId);
    
    Map<String, Object> getWaveById(String userId);
    
    Map<String, Object> getFaultById(String userId);
    
}
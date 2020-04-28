package com.example.demo.service.impl;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.service.UserService;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.InfoMapper;
import com.example.demo.po.User;
import com.example.demo.po.Line;
import com.example.demo.po.Page;
import com.example.demo.po.DataTableModel;
import com.example.demo.po.Log;
import util.aes.PaginationUtil;

import util.aes.StringUtils;

@Service
@Transactional
public class UserServiceImple implements UserService{
    // 注入用户Mapper
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private InfoMapper infoMapper; 
    
    public Map<String, Object> findUserByName(String username, String password)
    {
        Map<String, Object> rstMap = new HashMap<String, Object>();
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap = userMapper.findUserByName(username);
        if ((userMap != null)&&(userMap.get("has_del").equals(0))) {
            if (userMap.get("password").equals(password)) {
                rstMap.put("states", true);
                rstMap.put("userMap",userMap);
                rstMap.put("msg", "登录成功");
            } else {
                rstMap.put("states", false);
                rstMap.put("msg", "密码不正确");
            }
        } else {
            rstMap.put("states", false);
            rstMap.put("msg", "用户不存在");
        }
        return rstMap;
    }
    
    public List<Line> queryLine() {
        List<Line> LineList = userMapper.queryLine();
        return LineList;
    }
    
    public DataTableModel queryUserList(Map<String, String> dataTableMap){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String sEcho = String.valueOf(dataTableMap.get("sEcho"));

        int start = Integer.parseInt(String.valueOf(dataTableMap
                .get("iDisplayStart")));
        int length = Integer.parseInt(String.valueOf(dataTableMap
                .get("iDisplayLength")));
        int currentPage = start / length + 1;

        Page<Map<String, Object>> page = PaginationUtil.setPageInfoStart(
                paramMap, currentPage, length);
        // 获取用户列表
        //List<Map<String, Object>> resList = userMapper.queryUserList();
        //Integer count = userMapper.queryUsersCount(paramMap);
        
        List<Map<String, Object>> userList = new LinkedList<>();
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("phone", "admin");
        userMap.put("user_name", "admin");
        userMap.put("lineName", "测试线路");
        userMap.put("role", "1");
        userMap.put("has_del", "0");
        //userList.add(userMap);
        
        Integer count = 0;
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(userList);
        return dataTableModel;
    }  
    
    public DataTableModel queryLogList(Map<String, String> dataTableMap) {
        DataTableModel dataTableModel = new DataTableModel();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        List<Map<String, Object>> resList;
        Integer count;
        
        String sEcho = dataTableMap.get("sEcho");
        
        System.out.println(sEcho);
        System.out.println(dataTableMap.get("iDisplayStart"));
        System.out.println(dataTableMap.get("iDisplayLength"));
        System.out.println(dataTableMap.get("stuCode_s"));
        System.out.println(dataTableMap.get("stuName_s"));
        System.out.println(dataTableMap.get("stuStates_s"));
        
        System.out.println("------------------------------");
        System.out.println(dataTableMap.get("QueryType"));
        String QueryType = dataTableMap.get("QueryType");
        
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
        //int currentPage = start/length + 1;
        paramMap.put("stuCode_s", dataTableMap.get("stuCode_s"));
        paramMap.put("stuName_s", dataTableMap.get("stuName_s"));
        paramMap.put("stuStates_s", dataTableMap.get("stuStates_s"));
        //Page<Map<String, Object>> page = PaginationUtil.setPageInfoStart(paramMap,currentPage,length);
        if(QueryType.isEmpty())
        {
            resList = userMapper.queryLogList(start,length);
            count = userMapper.queryLogListCount();
        }
        else
        {
            resList = userMapper.queryLogListByCondition(start,length,QueryType);
            count = userMapper.queryLogListCountByCondition(QueryType);
        }
        
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }
    
    public DataTableModel queryUsersList(Map<String, String> dataTableMap) 
    {

        DataTableModel dataTableModel = new DataTableModel();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        String sEcho = dataTableMap.get("sEcho");
        

        
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
        paramMap.put("stuCode_s", dataTableMap.get("stuCode_s"));
        paramMap.put("stuName_s", dataTableMap.get("stuName_s"));
        paramMap.put("stuStates_s", dataTableMap.get("stuStates_s"));

        List<Map<String, Object>> resList = userMapper.queryUserList(start,length);

        Integer count = userMapper.queryUsersCount();

        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }
    
    public List<Log> queryLog()
    {
        List<Log> LogList = userMapper.queryLog();
        return LogList;
    }
    
    public List<Map<String, Object>> queryUserList()
    {
        List<Map<String, Object>> UserList = userMapper.queryUserList(1,2);
        return UserList;
    }
    
    public List<Map<String, Object>> getAllDeviceInfo(String line, String factory)
    {
        List<Map<String, Object>> DeviceList = infoMapper.queryAllDeviceList(line,factory);
        return DeviceList;
    }
    
    public boolean queryUserNameIsRepeat(String userName) {
        Integer count = userMapper.queryUserNameIsRepeat(userName);
        if (count == 0) {
            return true;
        } else {
            return false;
        }

    }
    
    @Override
    public void saveSchoolUser(Map<String, Object> paramMap) {
        String userId = (String) paramMap.get("userId");
        if (StringUtils.isEmpty(userId)) {
            paramMap.put("userId", StringUtils.getUUId());
            userMapper.addSchoolUser(paramMap);
            List<String> lineslist = (List<String>) paramMap.get("lineslist");
            for(int i=0;i<lineslist.size();i++)
            {
                String LineId = lineslist.get(i);
                String RecordId = UUID.randomUUID().toString().replaceAll("\\-", "");
                paramMap.put("RecordId", RecordId);
                paramMap.put("LineId", LineId);
                userMapper.addSchoolUserRole(paramMap);
            }
        } else {
            userMapper.updateSchoolUser(paramMap);
            userMapper.delAllUserRole(paramMap);
            List<String> lineslist = (List<String>) paramMap.get("lineslist");
            for(int i=0;i<lineslist.size();i++)
            {
                String LineId = lineslist.get(i);
                String RecordId = UUID.randomUUID().toString().replaceAll("\\-", "");
                paramMap.put("RecordId", RecordId);
                paramMap.put("LineId", LineId);
                userMapper.addSchoolUserRole(paramMap);
            }
        }
    }
    
    public Map<String, Object> getUserById(String userId) {
        return userMapper.getUserById(userId);
    }
    
    public Map<String, Object> getRegulatorById(String regulatorId) {
        return userMapper.getRegulatorById(regulatorId);
    }
    
    public boolean deleteSchoolUser(String userId) {
        userMapper.deleteSchoolUser(userId);
        userMapper.delAllUserRoleByString(userId);
        return true;
    }
    
    public boolean deleteRegulator(String userId) {
        userMapper.deleteRegulator(userId);
        return true;
    }
    
    public void saveNewPassWord(Map<String, Object> paramMap) {
        userMapper.saveNewPassWord(paramMap);
    }

    public String findUserPwdById(String id) {
        
        return userMapper.findUserPwdById(id);
    }
    
    public String getPassByDevId(String id) {
        return userMapper.getPassByDevId(id);
    }
    
    public void saveWaveWord(Map<String, Object> paramMap) {
        userMapper.saveWaveWord(paramMap);
    }
    
    public void saveDevicePass(Map<String, Object> paramMap) {
        userMapper.saveDevicePass(paramMap);
    }
    
    public void resetPassword(Map<String, Object> paramMap) {
        userMapper.resetPassword(paramMap);
    }
    
    public Map<String, Object> getWaveById(String userId) {
        return userMapper.getWaveById(userId);
    }
    
    public Map<String, Object> getFaultById(String userId) {
        return userMapper.getFaultById(userId);
    }
    
}


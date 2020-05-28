package com.example.demo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.po.DataTableModel;
import com.example.demo.service.FaultService;
import com.example.demo.mapper.FaultMapper;
import com.example.demo.mapper.StateMapper;

import util.aes.StringUtils;

@Service
@Transactional
public class FaultServiceImple implements FaultService{
  
    // 注入用户Mapper
    @Autowired
    private FaultMapper faultMapper;
    
    @Autowired
    private StateMapper stateMapper;
    
    public static String FormatFaultTime(String sDateTime)
    {
        String sAnOther = sDateTime.substring(0, 4)+sDateTime.substring(5, 7)+sDateTime.substring(8, 10)
                         +sDateTime.substring(11, 13)+sDateTime.substring(14, 16);
        return sAnOther;
    }
    
    public static String FormatTime(String sDateTime)
    {
        String sAnOther = sDateTime.substring(0, 4)+sDateTime.substring(5, 7)+sDateTime.substring(8, 10)
                         +sDateTime.substring(11, 13)+sDateTime.substring(14, 16)+"00";
        return sAnOther;
    }
    
    public DataTableModel queryWaveInfoList(Map<String, String> dataTableMap)
    {       
        DataTableModel dataTableModel = new DataTableModel();

        List<Map<String, Object>> resList;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        String factory = dataTableMap.get("factory"); 
        String line = dataTableMap.get("line");
        String WaveType = dataTableMap.get("WaveType");
        String userID = dataTableMap.get("userID");  

        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
    
        if (StringUtils.isEmpty(factory))
        {
            factory = "";
        }
        if (StringUtils.isEmpty(line))
        {
            resList = faultMapper.queryWaveListByUser(start,length,factory,userID, WaveType);
            count = faultMapper.queryWaveListCountByUser(factory,userID, WaveType);
        }
        else
        {
            resList = faultMapper.queryWaveList(start,length,factory,line, WaveType);
            count = faultMapper.queryWaveListCount(factory,line, WaveType);
        }      
       
        for (int i = 0; i<resList.size(); i++)
        {
        String device1 = (String)resList.get(i).get("device");
        Map<String, Object> deviceMap = faultMapper.getDeviceById(device1);
        String towerID = (String)deviceMap.get("tower_id");
        String factoryID = (String)deviceMap.get("manufacture");
        int phase = (int)deviceMap.get("ied_phase");
        String lineID = (String)deviceMap.get("line_name");
        
        String deviceName = (String)deviceMap.get("name");
        String towerName = faultMapper.getTowerById(towerID);
        String factoryName = faultMapper.getFactoryById(factoryID);
        //String sample_rate = faultMapper.getFactorySRById(factoryID);
        String lineName = faultMapper.getLineById(lineID);
        
        String wave_type = (String)resList.get(i).get("wave_type");
        int sample_rate = 0;
        if (1 == faultMapper.queryParameterCount(device1))
        {
            if(wave_type.equals("0")){
                sample_rate = faultMapper.queryWaveFreqByDev(device1);
            }
            else
            {
                sample_rate = faultMapper.queryPfFreqByDev(device1);
            }
        }
        
        resList.get(i).put("device",deviceName);
        resList.get(i).put("line",lineName);
        resList.get(i).put("factory",factoryName);
        resList.get(i).put("tower",towerName);
        resList.get(i).put("phase",phase);
        resList.get(i).put("sample_rate",sample_rate);
        }
               
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }
    
    public DataTableModel queryFaultListInGis(Map<String, String> dataTableMap)
    {       
        DataTableModel dataTableModel = new DataTableModel();

        System.out.println(dataTableMap);
        List<Map<String, Object>> resList;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
        
        String userID = dataTableMap.get("userID"); 
        String line = dataTableMap.get("line");
        String StartTime = dataTableMap.get("StartTime");
        String EndTime = dataTableMap.get("EndTime");
        if ((StringUtils.isEmpty(StartTime))||(StringUtils.isEmpty(EndTime)))
        {
            StartTime = "000000000000";
            EndTime = "999999999999";
        }
        else
        {
            StartTime = FormatFaultTime(StartTime);
            EndTime = FormatFaultTime(EndTime);
        }
        
        if (StringUtils.isEmpty(line))//不用线路搜索条件
        {
            resList = faultMapper.queryFaultListByUserAndInterval(start,length,userID,StartTime,EndTime);
            count = faultMapper.queryFaultListCountByUserAndInterval(userID,StartTime,EndTime);
        }
        else//指定线路
        {
            resList = faultMapper.queryFaultListByLineAndInterval(start,length,line,StartTime,EndTime);
            count = faultMapper.queryFaultListCountByLineAndInterval(line,StartTime,EndTime);
        }
        
        if (null!=resList)
        {
           for (int i = 0; i<resList.size(); i++)
          {
             String lineID = (String)resList.get(i).get("line");       
             String lineName = faultMapper.getLineById(lineID);       
             resList.get(i).put("lineName",lineName);
          }
        }
               
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }    
    
    public DataTableModel queryFaultList(Map<String, String> dataTableMap)
    {       
        DataTableModel dataTableModel = new DataTableModel();

        System.out.println(dataTableMap);
        List<Map<String, Object>> resList;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
        
        String userID = dataTableMap.get("userID"); 
        String Regulator = dataTableMap.get("Regulator");
        String VoltageLevel = dataTableMap.get("VoltageLevel");
        String line = dataTableMap.get("line");
        String Kind = dataTableMap.get("Kind");
        String StartTime = dataTableMap.get("StartTime");
        String EndTime = dataTableMap.get("EndTime");
        if ((StringUtils.isEmpty(StartTime))||(StringUtils.isEmpty(EndTime)))
        {
            StartTime = "000000000000";
            EndTime = "999999999999";
        }
        else
        {
            StartTime = FormatFaultTime(StartTime);
            EndTime = FormatFaultTime(EndTime);
        }
        String deal = dataTableMap.get("deal");
        
        if (StringUtils.isEmpty(line))//不用线路搜索条件
        {
            resList = faultMapper.queryFaultListByMultiLine1(start,length,userID,Regulator,VoltageLevel,Kind,StartTime,EndTime,deal);
            count = faultMapper.queryFaultListCountByMultiLine1(userID,Regulator,VoltageLevel,Kind,StartTime,EndTime,deal);
        }
        //以下两个分支暂时不合并
        else if (line.equals("0"))//无线路
        {
            resList = faultMapper.queryFaultListByUniqueLine(start,length,line,Kind,StartTime,EndTime,deal);
            count = faultMapper.queryFaultListCountByUniqueLine(line,Kind,StartTime,EndTime,deal);
        }
        else//指定线路
        {
            resList = faultMapper.queryFaultListByUniqueLine(start,length,line,Kind,StartTime,EndTime,deal);
            count = faultMapper.queryFaultListCountByUniqueLine(line,Kind,StartTime,EndTime,deal);
        }
        
        if (null!=resList)
        {
           for (int i = 0; i<resList.size(); i++)
          {
             String lineID = (String)resList.get(i).get("line");       
             String lineName = faultMapper.getLineById(lineID);       
             resList.get(i).put("lineName",lineName);
          }
        }
               
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }    
 
    /*
    public DataTableModel queryFaultList(Map<String, String> dataTableMap)
    {       
        DataTableModel dataTableModel = new DataTableModel();

        System.out.println(dataTableMap);
        List<Map<String, Object>> resList;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
        
        String userID = dataTableMap.get("userID"); 
        String optionID = dataTableMap.get("option"); 
        
       if (optionID !=null)
       {     
        switch(optionID)
        {
           case "1":
               String Regulator = dataTableMap.get("Regulator");
               resList = faultMapper.queryFaultListByRegulator(start,length,userID,Regulator);
               count = faultMapper.queryFaultListCountByRegulator(userID,Regulator);
              break;
           case "2":
               String VoltageLevel = dataTableMap.get("VoltageLevel");
               resList = faultMapper.queryFaultListByVoltageLevel(start,length,userID,VoltageLevel);
               count = faultMapper.queryFaultListCountByVoltageLevel(userID,VoltageLevel);
               break;
           case "3":
               String line = dataTableMap.get("line");
               resList = faultMapper.queryFaultListByLine(start,length,userID,line);
               count = faultMapper.queryFaultListCountByLine(userID,line);
               break;
           case "4":
               String Kind = dataTableMap.get("Kind");
               resList = faultMapper.queryFaultListByKind(start,length,userID,Kind);
               count = faultMapper.queryFaultListCountByKind(userID,Kind);
               break;
           case "5":
               String StartTime = dataTableMap.get("StartTime");
               String EndTime = dataTableMap.get("EndTime");
               if ((StringUtils.isEmpty(StartTime))||(StringUtils.isEmpty(EndTime)))
               {
                   StartTime = "000000000000";
                   EndTime = "999999999999";
               }
               else
               {
                   StartTime = FormatFaultTime(StartTime);
                   EndTime = FormatFaultTime(EndTime);
               }
               resList = faultMapper.queryFaultListByTime(start,length,userID,StartTime,EndTime);
               count = faultMapper.queryFaultListCountByTime(userID,StartTime,EndTime);
               break;
           case "6":
               String deal = dataTableMap.get("deal");
               resList = faultMapper.queryFaultListByDeal(start,length,userID,deal);
               count = faultMapper.queryFaultListCountByDeal(userID,deal);
               break;
           default:
               resList = faultMapper.queryFaultListByUser(start,length,userID);
               count = faultMapper.queryFaultListCountByUser(userID);
               break;         
        }
       }
       else
       {
           String line = dataTableMap.get("line");          
           String StartTime = dataTableMap.get("StartTime");
           String EndTime = dataTableMap.get("EndTime");

           if (line==null)
           {
               line="";
           }
           
           if ((StringUtils.isEmpty(StartTime))||(StringUtils.isEmpty(EndTime)))
           {
               StartTime = "000000000000";
               EndTime = "999999999999";
           }
           else
           {
               StartTime = FormatTime(StartTime);
               EndTime = FormatTime(EndTime);
           }
           
           resList = faultMapper.queryFaultList1(start,length,line,StartTime,EndTime,userID);
           count = faultMapper.queryFaultListCount1(line,StartTime,EndTime,userID);
       }

       
        for (int i = 0; i<resList.size(); i++)
        {
            String lineID = (String)resList.get(i).get("line");       
            String lineName = faultMapper.getLineById(lineID);       
            resList.get(i).put("lineName",lineName);
        }
               
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }    
    */
    
    public Map<String, Object> getWaveById(String userId) {
        return faultMapper.getWaveById(userId);
    }
    
    public Map<String, Object> getFaultById(String userId) {
        Map<String, Object> FaultMap = faultMapper.getFaultById(userId);
        String leftTowerId = (String)FaultMap.get("left_tower");
        String rightTowerId = (String)FaultMap.get("right_tower");
        String leftTowerName = faultMapper.getFaultNameById(leftTowerId);
        String rightTowerName = faultMapper.getFaultNameById(rightTowerId);
        FaultMap.put("leftTowerName", leftTowerName);
        FaultMap.put("rightTowerName", rightTowerName);
        return FaultMap;
    }
    
    public void setDealFaultById(String userId) {
        faultMapper.setDealFaultById(userId);
    }
    
    public List<Map<String, Object>> queryLineFaultList(String userId, String type, String page, String pageSize) {

        int offset = Integer.valueOf(pageSize) * (Integer.parseInt(page) - 1);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("limit", Integer.valueOf(pageSize));
        param.put("offset", offset);
        if (type.equals("1")) {
            // 如果type = 1.则按可见范围查询
            param.put("userId", userId);
        }

        List<Map<String, Object>> lineInfos = faultMapper.queryFaultListByUser(offset, Integer.valueOf(pageSize), userId);

        return lineInfos;
    }
    
    @Override
    public long countLineFaultNum(String userId, String type) {
        long total = 0;
        if (type.equals("ALL")) {
            total = faultMapper.queryAllFaultListCount();
        } else {
            total = faultMapper.queryFaultListCountByUser(userId);
        }

        return total;
    }
    
    public int countLineUnReadNum(String userId) {
        int unReadNum = 0;
        unReadNum = faultMapper.countLineUnReadNum(userId);

        return unReadNum;
    }
    
    public List<Map<String, Object>> queryDeviceFaultList(String userId, String type, String page, String pageSize) {

        int offset = Integer.valueOf(pageSize) * (Integer.parseInt(page) - 1);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("limit", Integer.valueOf(pageSize));
        param.put("offset", offset);
        if (type.equals("1")) {
            // 如果type = 1.则按可见范围查询
            param.put("userId", userId);
        }

        List<Map<String, Object>> lineInfos = stateMapper.queryAlarmListByUserForApp(offset, Integer.valueOf(pageSize), userId);

        return lineInfos;
    }
    
    @Override
    public long countDeviceFaultNum(String userId, String type) {
        long total = 0;
        if (type.equals("ALL")) {
            total = stateMapper.queryAllAlarmListCountForApp();
        } else {
            total = stateMapper.queryAlarmListCountByUserForApp(userId);
        }

        return total;
    }
    
    public int countDeviceUnReadNum(String userId) {
        int unReadNum = 0;
        unReadNum = stateMapper.countAlarmUnReadNum(userId);
  
        return unReadNum;
    }
}

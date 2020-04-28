package com.example.demo.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.mapper.StateMapper;
import com.example.demo.po.DataTableModel;
import com.example.demo.service.GisService;

import util.aes.StringUtils;

@Service
@Transactional
public class GisServiceImple implements GisService{
    
    @Autowired
    private StateMapper stateMapper;
    
    public DataTableModel queryDeviceList(Map<String, String> dataTableMap)
    {       
        DataTableModel dataTableModel = new DataTableModel();

        List<Map<String, Object>> resList;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        String line = dataTableMap.get("line");
        String device_id = dataTableMap.get("device_id");
        
        if (null==line)
        {
            line = "";
        }
        if (null==device_id)
        {
            device_id = "";
        }
        
        String userID = dataTableMap.get("userID");  
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
        
         resList = stateMapper.querydeviceListByUser3(start,length,userID,line,device_id);
         count = stateMapper.querydeviceListCountByUser3(userID,line,device_id);
         
         for (int i = 0; i<resList.size(); i++)
         {
             String lineID = (String)resList.get(i).get("line_name");       
             String lineName = stateMapper.getLineNameByLineId(lineID);
             resList.get(i).put("lineName",lineName);
         }
    
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }
    
    public DataTableModel queryLineListForGis(Map<String, String> dataTableMap)
    {       
        DataTableModel dataTableModel = new DataTableModel();

        List<Map<String, Object>> resList;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        
        String userID = dataTableMap.get("userID");  
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
        
         resList = stateMapper.queryLineListByUser(start,length,userID);
         count = stateMapper.queryLineListCountByUser(userID);
         
         for (int i = 0; i<resList.size(); i++)
         {
             String lineID = (String)resList.get(i).get("line_name");       
             String lineName = stateMapper.getLineNameByLineId(lineID);
             resList.get(i).put("lineName",lineName);
         }
    
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }
    
    //queryFaultList
    public DataTableModel queryFaultList(Map<String, String> dataTableMap)
    {       
        DataTableModel dataTableModel = new DataTableModel();

        List<Map<String, Object>> resList;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        String userID = dataTableMap.get("userID");  
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
        
         resList = stateMapper.queryFaultListByUser(start,length,userID);
         count = stateMapper.queryFaultListCountByUser(userID);
         
         for (int i = 0; i<resList.size(); i++)
         {
             String lineID = (String)resList.get(i).get("line");       
             String lineName = stateMapper.getLineNameByLineId(lineID);
             resList.get(i).put("lineName",lineName);
         }
    
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }
    
    public List<Map<String,Object>> queryDeviceListForGis(String userId)
    {
        List<Map<String,Object>> resList = stateMapper.queryAlldeviceListByUser(userId);
        return resList;
    }
    
    public List<Map<String,Object>> queryTowerListForGis(String LineUserId)
    {
        String LineId = stateMapper.queryLineByLineuser(LineUserId);
        List<Map<String,Object>> resList = stateMapper.queryTowerListByLine(LineId);
        int EndTowerIndex = stateMapper.queryMaxTowerIndexByLine(LineId);
        for (int i = 0; i<resList.size(); i++)
        {
            String longitude = (String)resList.get(i).get("longitude"); 
            String latitude = (String)resList.get(i).get("latitude");
            if ((StringUtils.isEmpty(longitude))||(StringUtils.isEmpty(latitude)))
            {
                return null;
            }
            
            String towerID = (String)resList.get(i).get("id"); 
            
            int TowerIndex = (int)resList.get(i).get("indexno");
            if (TowerIndex == EndTowerIndex)
            {
                resList.get(i).put("endflag","true");
            }
            else
            {
                resList.get(i).put("endflag","false");
            }
            
            Map<String,Object> fault = stateMapper.getFaultByLefttowerId(towerID);
            if (null!=fault)
            {
                String FaultDesc = (String)fault.get("desc");
                String LeftDistance = (String)fault.get("left_distance");
                String RightDistance = (String)fault.get("right_distance");
                resList.get(i).put("FaultDesc",FaultDesc);
                resList.get(i).put("LeftDistance",LeftDistance);
                resList.get(i).put("RightDistance",RightDistance);
                resList.get(i).put("flag","true");
            }
            else
            {
                resList.get(i).put("flag","false");
            }

        }
        return resList;
    }    
        
    public List<Map<String,Object>> queryFaultListForGis(String userId)
    {
        List<Map<String,Object>> resList = stateMapper.queryAllFaultListByUser(userId);
        for (int i = 0; i<resList.size(); i++)
        {
            String leftTowerID = (String)resList.get(i).get("left_tower");   
            String rightTowerID = (String)resList.get(i).get("right_tower"); 
            Map<String, Object> leftTowerMap = stateMapper.getTowerInfoById(leftTowerID);
            Map<String, Object> rightTowerMap = stateMapper.getTowerInfoById(rightTowerID);
            if ((leftTowerMap!=null)&&(rightTowerMap!=null))
            {
            String leftLatitude = (String)leftTowerMap.get("latitude");
            String leftLongitude = (String)leftTowerMap.get("longitude"); 
            String rightLatitude = (String)rightTowerMap.get("latitude");
            String rightLongitude = (String)rightTowerMap.get("longitude"); 
            resList.get(i).put("leftLongitude",leftLongitude);
            resList.get(i).put("leftLatitude",leftLatitude);
            resList.get(i).put("rightLongitude",rightLongitude);
            resList.get(i).put("rightLatitude",rightLatitude);
            }
            else
            {
                resList.get(i).put("leftLongitude","0");
                resList.get(i).put("leftLatitude","0");
                resList.get(i).put("rightLongitude","0");
                resList.get(i).put("rightLatitude","0");
            }
        }
        return resList;
    }
}

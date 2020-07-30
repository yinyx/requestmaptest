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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public List<Map<String,Object>> queryDeviceListForGis(String userId)
    {
        List<Map<String,Object>> resList = stateMapper.queryAlldeviceListByUser(userId);
        return resList;
    }

    @Override
    public List<Map<String,Object>> queryTowerListForGis(String LineUserId)
    {
        String LineId = stateMapper.queryLineByLineuser(LineUserId);
        Map<String,Object> StationMap = stateMapper.queryStationNameByLineId(LineId);
        String LeftStationName = (String)StationMap.get("LeftStationName");
        String RightStationName = (String)StationMap.get("RightStationName");
        List<Map<String,Object>> resList = stateMapper.queryTowerListByLine(LineId);
        int EndTowerIndex = stateMapper.queryMaxTowerIndexByLine(LineId);
        for (int i = 0; i<resList.size(); i++)
        {
            String longitude = (String)resList.get(i).get("longitude");
            String latitude = (String)resList.get(i).get("latitude");
            if ((StringUtils.isEmpty(longitude))||(StringUtils.isEmpty(latitude))||longitude.equals("0")||latitude.equals("0"))
            {
                String towerID = (String)resList.get(i).get("id");
                //先找到关联的装置有没有经纬度
                Map<String,Object> deviceMap = stateMapper.getDevMapByTowerId(towerID);
                if (deviceMap!=null)
                {
                    longitude = (String)deviceMap.get("longitude");
                    latitude = (String)deviceMap.get("latitude");
                    resList.get(i).put("longitude",longitude);
                    resList.get(i).put("latitude",latitude);
                    if ((StringUtils.isEmpty(longitude))||(StringUtils.isEmpty(latitude)))
                    {
                        return null;
                    }
                }
                else
                {
                    return null;
                }
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
                //String FaultDesc = (String)fault.get("desc");
                String LeftDistance = (String)fault.get("left_distance");
                String RightDistance = (String)fault.get("right_distance");
                String left_station_distance = (String)fault.get("left_station_distance");
                String right_station_distance = (String)fault.get("right_station_distance");
                String occurr_time = (String)fault.get("occurr_time");
                String RightTowerName = (String)resList.get(i+1).get("name");
                String faultType = (String)fault.get("type");
                if (faultType.equals("-1"))
                {
                    resList.get(i).put("FaultDesc","判定失败");
                }
                else if ((faultType.equals("2"))||(faultType.equals("3")))
                {
                    resList.get(i).put("FaultDesc","雷击");
                }
                else if (faultType.equals("4"))
                {
                    resList.get(i).put("FaultDesc","非雷击");
                }
                else {
                    resList.get(i).put("FaultDesc","数据非法");
                }
                resList.get(i).put("LeftDistance",LeftDistance);
                resList.get(i).put("RightDistance",RightDistance);
                resList.get(i).put("occurr_time",occurr_time);
                resList.get(i).put("RightTowerName",RightTowerName);
                resList.get(i).put("RightStationName",RightStationName);
                resList.get(i).put("LeftStationName",LeftStationName);
                resList.get(i).put("left_station_distance",left_station_distance);
                resList.get(i).put("right_station_distance",right_station_distance);
                resList.get(i).put("flag","true");
            }
            else
            {
                resList.get(i).put("flag","false");
            }

        }
        return resList;
    }

    @Override
    public List<Map<String,Object>> queryFaultListForGis(String userId)
    {
        List<Map<String,Object>> resList = stateMapper.queryAllFaultListByUser(userId);
        for (int i = 0; i<resList.size(); i++)
        {
            String LineId = (String)resList.get(i).get("line");
            String LineName = stateMapper.queryLineNameByLineId(LineId);
            Map<String,Object> StationMap = stateMapper.queryStationNameByLineId(LineId);
            String LeftStationName = (String)StationMap.get("LeftStationName");
            String RightStationName = (String)StationMap.get("RightStationName");

            resList.get(i).put("LeftStationName",LeftStationName);
            resList.get(i).put("RightStationName",RightStationName);
            resList.get(i).put("LineName",LineName);

            String faultType = (String)resList.get(i).get("type");
            if (StringUtils.isEmpty(faultType))
            {
                resList.get(i).put("FaultDesc","数据为空");
            }
            else if (faultType.equals("-1"))
            {
                resList.get(i).put("FaultDesc","判定失败");
            }
            else if ((faultType.equals("2"))||(faultType.equals("3")))
            {
                resList.get(i).put("FaultDesc","雷击");
            }
            else if (faultType.equals("4"))
            {
                resList.get(i).put("FaultDesc","非雷击");
            }
            else {
                    resList.get(i).put("FaultDesc","数据非法");
            }

            String occurr_time = (String)resList.get(i).get("occurr_time");
            resList.get(i).put("occurr_time",occurr_time);

            String leftTowerID = (String)resList.get(i).get("left_tower");
            String rightTowerID = (String)resList.get(i).get("right_tower");
            Map<String, Object> leftTowerMap = stateMapper.getTowerInfoById(leftTowerID);
            Map<String, Object> rightTowerMap = stateMapper.getTowerInfoById(rightTowerID);
            if ((leftTowerMap!=null)&&(rightTowerMap!=null))
            {
            String leftLatitude = (String)leftTowerMap.get("latitude");
            String leftLongitude = (String)leftTowerMap.get("longitude");
            String leftTowerName = (String)leftTowerMap.get("name");
            String rightLatitude = (String)rightTowerMap.get("latitude");
            String rightLongitude = (String)rightTowerMap.get("longitude");
            String rightTowerName = (String)rightTowerMap.get("name");
            resList.get(i).put("leftLongitude",leftLongitude);
            resList.get(i).put("leftLatitude",leftLatitude);
            resList.get(i).put("rightLongitude",rightLongitude);
            resList.get(i).put("rightLatitude",rightLatitude);
            resList.get(i).put("rightTowerName",rightTowerName);
            resList.get(i).put("leftTowerName",leftTowerName);
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

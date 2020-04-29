package com.example.demo.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.po.DataTableModel;
import com.example.demo.service.StateService;
import com.example.demo.mqtt.MsgSender;
//import com.example.demo.mapper.FaultMapper;
import com.example.demo.mapper.StateMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.po.ParamAttrManage;
import com.example.demo.po.ParamAttr;

import util.aes.StringUtils;



@Service
@Transactional
public class StateServiceImple  implements StateService{
    
    // 注入用户Mapper
    @Autowired
    private StateMapper stateMapper;
    
    @Autowired
    private ParamAttrManage paramAttrManage;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    MsgSender sender;
    
    //文件下载相关代码
    public static String downloadFile(HttpServletRequest request, HttpServletResponse response) {
      String fileName = "aim_test.txt";// 设置文件名，根据业务需要替换成要下载的文件名
      if (fileName != null) {
        //设置文件路径
        String realPath = "D://aim//";
        File file = new File(realPath , fileName);
        if (file.exists()) {
          response.setContentType("application/force-download");// 设置强制下载不打开
          response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
          byte[] buffer = new byte[1024];
          FileInputStream fis = null;
          BufferedInputStream bis = null;
          try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
              os.write(buffer, 0, i);
              i = bis.read(buffer);
            }
            System.out.println("success");
          } catch (Exception e) {
            e.printStackTrace();
          } finally {
            if (bis != null) {
              try {
                bis.close();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
            if (fis != null) {
              try {
                fis.close();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }
        }
      }
      return null;
    }
    
    public static byte[] int2Bytes(int integer)
    {
            byte[] bytes=new byte[4];

            bytes[3]=(byte)(integer>>24);
            bytes[2]=(byte)(integer>>16);
            bytes[1]=(byte)(integer>>8);
            bytes[0]=(byte)integer;

            return bytes;
    }
    
    public static int bytes2Int(byte[] bytes )
    {
        int int1=bytes[0]&0xff;
        int int2=(bytes[1]&0xff)<<8;
        int int3=(bytes[2]&0xff)<<16;
        int int4=(bytes[3]&0xff)<<24;

        return int1|int2|int3|int4;
    }
    
    public static float byte2float(byte[] b, int index) 
    {  
     int l;  
     l = b[index];  
     l &= 0xff;  
     l |= ((long) b[index + 1] << 8);  
     l &= 0xffff;  
     l |= ((long) b[index + 2] << 16);  
     l &= 0xffffff;  
     l |= ((long) b[index + 3] << 24);  
     
     Float fl = Float.valueOf(Float.intBitsToFloat(l));
     if(fl.isInfinite()||fl.isNaN()) {
      return 0;
     }else {
      return fl;
     }
     
     //return Float.intBitsToFloat(l);  
    } 
    
    public static float byte2floatAnti(byte[] b, int index) 
    {  
     int l;  
     l = b[index+3];  
     l &= 0xff;  
     l |= ((long) b[index + 2] << 8);  
     l &= 0xffff;  
     l |= ((long) b[index + 1] << 16);  
     l &= 0xffffff;  
     l |= ((long) b[index] << 24);  
     //return Float.intBitsToFloat(l); 
     Float fl = Float.valueOf(Float.intBitsToFloat(l));
     if(fl.isInfinite()||fl.isNaN()) {
      return 0;
     }else {
      return fl;
     }
    } 
    
    //byte תint
    public static int getInt(byte[] b, int index) 
    {  
        int l;  
        l = b[index + 3];  
        l &= 0xff;  
        l |= ((long) b[index + 2] << 8);  
        l &= 0xffff;  
        l |= ((long) b[index + 1] << 16);  
        l &= 0xffffff;  
        l |= ((long) b[index] << 24);  
        return l;//Float.intBitsToFloat(l);  
    }

    //byte תfloat
    public static float getFloat(byte[] b, int index) 
    {  
        int l;  
        l = b[index + 0];  
        l &= 0xff;  
        l |= ((long) b[index + 1] << 8);  
        l &= 0xffff;  
        l |= ((long) b[index + 2] << 16);  
        l &= 0xffffff;  
        l |= ((long) b[index + 3] << 24);  
        Float fl = Float.valueOf(Float.intBitsToFloat(l));
        if(fl.isInfinite()) {
            return 0;
        }else {
            return fl;
        }
    } 
    
    public static String FormatTime(String sDateTime)
    {
        String sAnOther = sDateTime.substring(0, 4)+"."+sDateTime.substring(5, 7)+"."+sDateTime.substring(8, 10)+" "
                         +sDateTime.substring(11, 16)+"00.000000000";
        return sAnOther;
    }
    
    
    public DataTableModel queryHeartBeatList(Map<String, String> dataTableMap)
    {       
        DataTableModel dataTableModel = new DataTableModel();

        List<Map<String, Object>> resList;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        String QueryType = dataTableMap.get("factory");
        String deviceIdTmp = dataTableMap.get("device_id");
        String device_name = "";
    	String device_id = "";
        if(stateMapper.isQueryRightById(deviceIdTmp)) {
        	device_id = deviceIdTmp;
        }
        if(stateMapper.isQueryRightByName(deviceIdTmp)){
        	device_name = deviceIdTmp;
        }
        String CommLine = dataTableMap.get("CommLine");
        String userID = dataTableMap.get("userID");  
        String StartTime = dataTableMap.get("StartTime");
        String EndTime = dataTableMap.get("EndTime");


        if ((StringUtils.isEmpty(StartTime))||(StringUtils.isEmpty(EndTime)))
        {
            StartTime = "0000-00-00 00:00:00";
            EndTime = "9999-99-99 99:99:99";
        }
        /*
        else
        {
            StartTime = FormatTime(StartTime);
            EndTime = FormatTime(EndTime);
        }
        
        Long iStartTime = Long.parseLong(StartTime);
        Long iEndTime   = Long.parseLong(EndTime);
        */
        
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
    
        if ((StringUtils.isEmpty(QueryType))&&(StringUtils.isEmpty(deviceIdTmp))&&(StringUtils.isEmpty(CommLine)))
        {
            resList = stateMapper.queryHeartBeatListByUser(start,length,userID,StartTime,EndTime);
            count = stateMapper.queryHeartBeatListCountByUser(userID,StartTime,EndTime);
        }
        else
        {
            resList = stateMapper.queryHeartBeatList(start,length,QueryType,device_name,device_id,userID,CommLine,StartTime,EndTime);
            count = stateMapper.queryHeartBeatListCount(QueryType,device_name,device_id,userID,CommLine,StartTime,EndTime);
        }      
       
        for (int i = 0; i<resList.size(); i++)
        {
            String deviceID = (String)resList.get(i).get("device");       
            Map<String, Object> deviceMap = stateMapper.getDeviceInfoById(deviceID); 
            String deviceName = (String)deviceMap.get("name");
            String factoryId = (String)deviceMap.get("manufacture");
            String factoyName = stateMapper.getFactoryNameByFactoryId(factoryId);  
            resList.get(i).put("deviceName",deviceName);
            resList.get(i).put("factory",factoyName);
        }
               
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        //paramAttrManage.print();
        return dataTableModel;
    } 
    
    public DataTableModel queryWorkConditionList(Map<String, String> dataTableMap)
    {       
        DataTableModel dataTableModel = new DataTableModel();

        List<Map<String, Object>> resList;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        String QueryType = dataTableMap.get("factory");
        String deviceIdTmp = dataTableMap.get("device_id");
        String device_name = "";
    	String device_id = "";
        if(stateMapper.isQueryRightById(deviceIdTmp)) {
        	device_id = deviceIdTmp;
        }
        if(stateMapper.isQueryRightByName(deviceIdTmp)){
        	device_name = deviceIdTmp;
        }
        String userID = dataTableMap.get("userID");
        String CommLine = dataTableMap.get("CommLine");

        String StartTime = dataTableMap.get("StartTime");
        String EndTime = dataTableMap.get("EndTime");

        if ((StringUtils.isEmpty(StartTime))||(StringUtils.isEmpty(EndTime)))
        {
            StartTime = "0000.00.00 00:00:00.000000000";
            EndTime =   "9999.11.30 23:59:59.999999999";
        }      
        else
        {
            StartTime = FormatTime(StartTime);
            EndTime = FormatTime(EndTime);
        }
        
        
        //Long iStartTime = Long.parseLong(StartTime);
        //Long iEndTime   = Long.parseLong(EndTime);
        
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
    
        if ((StringUtils.isEmpty(QueryType))&&(StringUtils.isEmpty(deviceIdTmp))&&(StringUtils.isEmpty(CommLine)))
        {
            resList = stateMapper.queryWorkConditionListByTime(start,length,userID);
            count = stateMapper.queryWorkConditionListCountByTime(userID,StartTime,EndTime);
        }
        else
        {
            resList = stateMapper.queryWorkConditionList(start,length,QueryType,device_name,device_id,userID,CommLine,StartTime,EndTime);
            count = stateMapper.queryWorkConditionListCount(QueryType,device_name,device_id,userID,CommLine,StartTime,EndTime);
        }      
       
        for (int i = 0; i<resList.size(); i++)
        {
            String deviceID = (String)resList.get(i).get("device");       
            Map<String, Object> deviceMap = stateMapper.getDeviceInfoById(deviceID); 
            String deviceName = (String)deviceMap.get("name");
            String factoryId = (String)deviceMap.get("manufacture");
            String factoyName = stateMapper.getFactoryNameByFactoryId(factoryId);  
            resList.get(i).put("deviceName",deviceName);
            resList.get(i).put("factory",factoyName);
        }
               
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    } 
    
//    public DataTableModel queryWorkConditionListOrder(Map<String, String> dataTableMap)
//    {       
//        DataTableModel dataTableModel = new DataTableModel();
//        List<Map<String, Object>> resList;
//        Integer count;
//        String sEcho = dataTableMap.get("sEcho");
//        String QueryType = dataTableMap.get("factory");
//        String device_id = dataTableMap.get("device_id");
//        String userID = dataTableMap.get("userID");
//        String CommLine = dataTableMap.get("CommLine");
//
//        String StartTime = dataTableMap.get("StartTime");
//        String EndTime = dataTableMap.get("EndTime");
//        System.out.println("——————————"+CommLine);
//        if ((StringUtils.isEmpty(StartTime))||(StringUtils.isEmpty(EndTime)))
//        {
//            StartTime = "0000.00.00 00:00:00.000000000";
//            EndTime =   "9999.11.30 23:59:59.999999999";
//        }      
//        else
//        {
//            StartTime = FormatTime(StartTime);
//            EndTime = FormatTime(EndTime);
//        }
//        
//        
//        //Long iStartTime = Long.parseLong(StartTime);
//        //Long iEndTime   = Long.parseLong(EndTime);
//        
//        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
//        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
//    
//        if ((StringUtils.isEmpty(QueryType))&&(StringUtils.isEmpty(device_id))&&(StringUtils.isEmpty(CommLine)))
//        {
//            resList = stateMapper.queryWorkConditionListByTime(start,length,userID);
//            count = stateMapper.queryWorkConditionListCountByTime(userID,StartTime,EndTime);
//        }
//        else
//        {
//            resList = stateMapper.queryWorkConditionListOrderById(start,length,QueryType,device_id,userID,CommLine,StartTime,EndTime);
//            count = stateMapper.queryWorkConditionListCountOrderById(QueryType,device_id,userID,CommLine,StartTime,EndTime);
//        }      
//        
//        for (int i = 0; i<resList.size(); i++)
//        {
//            String deviceID = (String)resList.get(i).get("device");       
//            Map<String, Object> deviceMap = stateMapper.getDeviceInfoById(deviceID); 
//            String deviceName = (String)deviceMap.get("name");
//            String factoryId = (String)deviceMap.get("manufacture");
//            String factoyName = stateMapper.getFactoryNameByFactoryId(factoryId);  
//            resList.get(i).put("deviceName",deviceName);
//            resList.get(i).put("factory",factoyName);
//        }
//               
//        dataTableModel.setiTotalDisplayRecords(count);
//        dataTableModel.setiTotalRecords(count);
//        dataTableModel.setsEcho(Integer.valueOf(sEcho));
//        dataTableModel.setAaData(resList);
//
//        return dataTableModel;
//    } 
    
    
    public DataTableModel queryOrderStatusList(Map<String, String> dataTableMap)
    {       
        DataTableModel dataTableModel = new DataTableModel();

        List<Map<String, Object>> resList;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        String QueryType = dataTableMap.get("factory");
        String deviceIdTmp = dataTableMap.get("device_id");
        String device_name = "";
    	String device_id = "";
        if(stateMapper.isQueryRightById(deviceIdTmp)) {
        	device_id = deviceIdTmp;
        }
        if(stateMapper.isQueryRightByName(deviceIdTmp)){
        	device_name = deviceIdTmp;
        }
        
        String CommLine = dataTableMap.get("CommLine");
        String userID = dataTableMap.get("userID"); 
        String StartTime = dataTableMap.get("StartTime");
        String EndTime = dataTableMap.get("EndTime");

        if ((StringUtils.isEmpty(StartTime))||(StringUtils.isEmpty(EndTime)))
        {
            StartTime = "0000-00-00 00:00:00";
            EndTime = "9999-99-99 99:99:99";
        }
        /*
        else
        {
            StartTime = FormatTime(StartTime);
            EndTime = FormatTime(EndTime);
        }
        
        Long iStartTime = Long.parseLong(StartTime);
        Long iEndTime   = Long.parseLong(EndTime);
        */

        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
    
        if ((StringUtils.isEmpty(QueryType))&&(StringUtils.isEmpty(deviceIdTmp))&&(StringUtils.isEmpty(CommLine)))
        {
            resList = stateMapper.queryOrderStatusListByUser(start,length,userID,StartTime,EndTime);
            count = stateMapper.queryOrderStatusListCountByUser(userID,StartTime,EndTime);
        }
        else
        {
            resList = stateMapper.queryOrderStatusList(start,length,QueryType,device_name,device_id,userID,CommLine,StartTime,EndTime);
            count = stateMapper.queryOrderStatusListCount(QueryType,device_name,device_id,userID,CommLine,StartTime,EndTime);
        }      
       
        for (int i = 0; i<resList.size(); i++)
        {
            String deviceID = (String)resList.get(i).get("device");       
            String userIDofRecord = (String)resList.get(i).get("user");  
            /*
            Date  timeofRecord = (Date )resList.get(i).get("time"); 
            System.out.println("time");
            System.out.println(timeofRecord);
            String value = null;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            value = dateFormat.format(timeofRecord);
            System.out.println(value);
            */
            
            Map<String, Object> deviceMap = stateMapper.getDeviceInfoById(deviceID); 
            String deviceName = (String)deviceMap.get("name");
            String factoryId = (String)deviceMap.get("manufacture");
            String factoyName = stateMapper.getFactoryNameByFactoryId(factoryId);  
            String userName = stateMapper.getUserNameByUserId(userIDofRecord);  
            resList.get(i).put("deviceName",deviceName);
            resList.get(i).put("factory",factoyName);
            resList.get(i).put("user_name",userName);
            //resList.get(i).put("time_sring",value);
        }
               
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }     
    
    public DataTableModel queryAlarmList(Map<String, String> dataTableMap)
    {       
        DataTableModel dataTableModel = new DataTableModel();

        List<Map<String, Object>> resList;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        String QueryType = dataTableMap.get("factory");
        String deviceIdTmp = dataTableMap.get("device_id");
        String device_name = "";
    	String device_id = "";
        if(stateMapper.isQueryRightById(deviceIdTmp)) {
        	device_id = deviceIdTmp;
        }
        if(stateMapper.isQueryRightByName(deviceIdTmp)){
        	device_name = deviceIdTmp;
        }        
        String CommLine = dataTableMap.get("CommLine");
        String userID = dataTableMap.get("userID");  
        String StartTime = dataTableMap.get("StartTime");
        String EndTime = dataTableMap.get("EndTime");
        String deal = dataTableMap.get("deal");

        if ((StringUtils.isEmpty(StartTime))||(StringUtils.isEmpty(EndTime)))
        {
            StartTime = "0000.00.00 00:00:00.000000000";
            EndTime =   "9999.11.30 23:59:59.999999999";
        }      
        else
        {
            StartTime = FormatTime(StartTime);
            EndTime = FormatTime(EndTime);
        }
        
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
    
        if ((StringUtils.isEmpty(QueryType))&&(StringUtils.isEmpty(deviceIdTmp))&&(StringUtils.isEmpty(CommLine)))
        {
            resList = stateMapper.queryAlarmListByUser(start,length,userID,StartTime,EndTime,deal);
            count = stateMapper.queryAlarmListCountByUser(userID,StartTime,EndTime,deal);
        }
        else
        {
            resList = stateMapper.queryAlarmList(start,length,QueryType,device_name,device_id,userID,CommLine,StartTime,EndTime,deal);
            count = stateMapper.queryAlarmListCount(QueryType,device_name,device_id,userID,CommLine,StartTime,EndTime,deal);
        }      
       
        for (int i = 0; i<resList.size(); i++)
        {
            String deviceID = (String)resList.get(i).get("device");       
            Map<String, Object> deviceMap = stateMapper.getDeviceInfoById(deviceID); 
            String deviceName = (String)deviceMap.get("name");
            String factoryId = (String)deviceMap.get("manufacture");
            String factoyName = stateMapper.getFactoryNameByFactoryId(factoryId);  
            resList.get(i).put("deviceName",deviceName);
            resList.get(i).put("factory",factoyName);
        }
               
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }  

    public DataTableModel queryParameterAttrList(Map<String, String> dataTableMap)
    {       
        DataTableModel dataTableModel = new DataTableModel();

        List<Map<String, Object>> resList;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        String QueryType = dataTableMap.get("factory");
        String protocal = dataTableMap.get("protocal");
        int iProtocal = Integer.parseInt(protocal);
        String userID = dataTableMap.get("userID");  
        System.out.println("QueryType");
        System.out.println(QueryType);
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));

        if ((QueryType == null)||(QueryType.equals("0")))
        {
            count = 0;
            resList= new ArrayList();
        }
        else
        {
            resList = stateMapper.queryParameterAttrListByFactory(start,length,QueryType,iProtocal);
            count = stateMapper.queryParameterAttrListCountByFactory(QueryType,iProtocal);
        }
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
        
        
    }  
    
    public DataTableModel queryCommunicateState(Map<String, String> dataTableMap)
    {
        DataTableModel dataTableModel = new DataTableModel();

        List<Map<String, Object>> resList;
        List<Map<String, Object>> deviceList;
        Map<String, Object> deviceMap;
        Map<String, Object> resMap;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        String QueryType = dataTableMap.get("factory");
        
        String deviceIdTmp = dataTableMap.get("device_id");
        String device_name = "";
    	String device_id = "";
        if(stateMapper.isQueryRightById(deviceIdTmp)) {
        	device_id = deviceIdTmp;
        }
        if(stateMapper.isQueryRightByName(deviceIdTmp)){
        	device_name = deviceIdTmp;
        }
        String CommState = dataTableMap.get("CommState");
        String CommLine = dataTableMap.get("CommLine");
        Integer iCommState = Integer.parseInt(CommState);
        String userID = dataTableMap.get("userID");  
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
        //首先获取设备列表
        if ((StringUtils.isEmpty(QueryType))&&(StringUtils.isEmpty(deviceIdTmp))&&(StringUtils.isEmpty(CommLine)))
        {
        	if(iCommState != 2) {
                deviceList = stateMapper.querydeviceListByUser(start,length,iCommState,userID);
                count = stateMapper.querydeviceListCountByUser(iCommState,userID);
        	}else {
        		deviceList = stateMapper.querydeviceListByUser1(start,length,userID);
                count = stateMapper.querydeviceListCountByUser1(userID);
        	}
        }
        else
        {
        	if(iCommState != 2) {
                deviceList = stateMapper.querydeviceList(start,length,QueryType,device_name,device_id,CommLine,iCommState,userID);
                count = stateMapper.querdeviceListCount(QueryType,device_name,device_id,CommLine,iCommState,userID);
        	}
            else
            {
                deviceList = stateMapper.querydeviceList2(start,length,QueryType,device_name,device_id,CommLine,userID);
                count = stateMapper.querdeviceListCount2(QueryType,device_name,device_id,CommLine,userID);
            }
        } 
        
        for (int i=0; i<deviceList.size();i++)
        {
            deviceMap = deviceList.get(i);
            String factoryid = (String)deviceMap.get("manufacture");
            String factoryName = stateMapper.getFactoryNameByFactoryId(factoryid);
            deviceMap.put("factory", factoryName);
            String deviceid = (String)deviceMap.get("id");
            //最新通信时间暂时关闭
            //String commTime = stateMapper.getCommTimeByDeviceId(deviceid);
            //deviceMap.put("commTime", commTime);
        }
        
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(deviceList);

        return dataTableModel;
    }  
//    /**
//     *	通信状态界面DataTable
//     * @author yinyx
//     * @param dataTableMap
//     * @return
//     */
//    public DataTableModel queryCommunicateStateNew(Map<String, String> dataTableMap)
//    {
//        DataTableModel dataTableModel = new DataTableModel();
//        List<Map<String, Object>> resList;
//        List<Map<String, Object>> deviceList = null;
//        Map<String, Object> deviceMap;
//        Map<String, Object> resMap;
//        Integer count = null;
//        String sEcho = dataTableMap.get("sEcho");
//        String QueryType = dataTableMap.get("factory");
//        String device_id = dataTableMap.get("device_id");
//        String CommState = dataTableMap.get("CommState");
//        String commLine = dataTableMap.get("CommLine");
//        Integer iCommState = Integer.parseInt(CommState);
//        String userID = dataTableMap.get("userID");  
//        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
//        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
//        //首先获取设备列表
//        if ((StringUtils.isEmpty(QueryType))&&(StringUtils.isEmpty(device_id)))
//        {
//                deviceList = stateMapper.querydeviceListByUserNew(start,length,userID);
//                count = stateMapper.querydeviceListCountByUserNew(userID);
//        }
//        else
//        {
//                deviceList = stateMapper.querydeviceListNew(start,length,QueryType,device_id,commLine,iCommState,userID);
//                count = stateMapper.querdeviceListCountNew(QueryType,device_id,commLine,iCommState,userID);
//        } 
//        
//        for (int i=0; i<deviceList.size();i++)
//        {
//            deviceMap = deviceList.get(i);
//            String factoryid = (String)deviceMap.get("manufacture");
//            String factoryName = stateMapper.getFactoryNameByFactoryId(factoryid);
//            deviceMap.put("factory", factoryName);
//            String deviceid = (String)deviceMap.get("id");
//            String commTime = stateMapper.getCommTimeByDeviceId(deviceid);
//            deviceMap.put("commTime", commTime);
//        }
//        
//        dataTableModel.setiTotalDisplayRecords(count);
//        dataTableModel.setiTotalRecords(count);
//        dataTableModel.setsEcho(Integer.valueOf(sEcho));
//        dataTableModel.setAaData(deviceList);
//
//        return dataTableModel;
//    }
    
    
    
    
    
    
    public DataTableModel querySelfCheckList(Map<String, String> dataTableMap)
    {
        DataTableModel dataTableModel = new DataTableModel();

        List<Map<String, Object>> resList;
        List<Map<String, Object>> deviceList;
        Map<String, Object> deviceMap;
        Map<String, Object> resMap;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        String QueryType = dataTableMap.get("factory");
        String deviceIdTmp = dataTableMap.get("device_id");
        String device_name = "";
    	String device_id = "";
        if(stateMapper.isQueryRightById(deviceIdTmp)) {
        	device_id = deviceIdTmp;
        }
        if(stateMapper.isQueryRightByName(deviceIdTmp)){
        	device_name = deviceIdTmp;
        }

        String CommState = dataTableMap.get("CommState");
        String CommLine = dataTableMap.get("CommLine");
        Integer iCommState = Integer.parseInt(CommState);
        String userID = dataTableMap.get("userID");  
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
        
        //首先获取设备列表
        if ((StringUtils.isEmpty(QueryType))&&(StringUtils.isEmpty(deviceIdTmp))&&(StringUtils.isEmpty(CommLine)))
        {
        	if(iCommState != 2) {
                deviceList = stateMapper.querydeviceListByUser(start,length,iCommState,userID);
                count = stateMapper.querydeviceListCountByUser(iCommState,userID);
        	}else {
        		deviceList = stateMapper.querydeviceListByUser1(start,length,userID);
                count = stateMapper.querydeviceListCountByUser1(userID);
        	}
        }
        else
        {
        	if(iCommState != 2) {
                deviceList = stateMapper.querydeviceList(start,length,QueryType,device_name,device_id,CommLine,iCommState,userID);
                count = stateMapper.querdeviceListCount(QueryType,device_name,device_id,CommLine,iCommState,userID);
        	}
            else
            {
                deviceList = stateMapper.querydeviceList2(start,length,QueryType,device_name,device_id,CommLine,userID);
                count = stateMapper.querdeviceListCount2(QueryType,device_name,device_id,CommLine,userID);
            }
        } 
        
        for (int i=0; i<deviceList.size();i++)
        {
            deviceMap = deviceList.get(i);
            String factoryid = (String)deviceMap.get("manufacture");
            String factoryName = stateMapper.getFactoryNameByFactoryId(factoryid);
            deviceMap.put("factory", factoryName);
        }
        
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(deviceList);

        return dataTableModel;
    }
    
    //queryParameterList
    //此处以设备为标准，不以参数为标准，因为第一次有可能参数还未获取
    public DataTableModel queryParameterList(Map<String, String> dataTableMap)
    {       
        DataTableModel dataTableModel = new DataTableModel();

        List<Map<String, Object>> resList;
        List<Map<String, Object>> deviceList;
        Map<String, Object> deviceMap;
        Map<String, Object> resMap;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        String QueryType = dataTableMap.get("factory");
        String deviceIdTmp = dataTableMap.get("device_id");
        String device_name = "";
    	String device_id = "";
        if(stateMapper.isQueryRightById(deviceIdTmp)) {
        	device_id = deviceIdTmp;
        }
        if(stateMapper.isQueryRightByName(deviceIdTmp)){
        	device_name = deviceIdTmp;
        }
        String CommState = dataTableMap.get("CommState");
        String CommLine = dataTableMap.get("CommLine");
        Integer iCommState = Integer.parseInt(CommState);
        String userID = dataTableMap.get("userID");  
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
        
        //首先获取设备列表
        if ((StringUtils.isEmpty(QueryType))&&(StringUtils.isEmpty(deviceIdTmp))&&(StringUtils.isEmpty(CommLine)))
        {
        	if(iCommState != 2) {
                deviceList = stateMapper.querydeviceListByUser(start,length,iCommState,userID);
                count = stateMapper.querydeviceListCountByUser(iCommState,userID);
        	}else {
        		deviceList = stateMapper.querydeviceListByUser1(start,length,userID);
                count = stateMapper.querydeviceListCountByUser1(userID);
        	}
        }
        else
        {
        	if(iCommState != 2) {
                deviceList = stateMapper.querydeviceList(start,length,QueryType,device_name,device_id,CommLine,iCommState,userID);
                count = stateMapper.querdeviceListCount(QueryType,device_name,device_id,CommLine,iCommState,userID);
        	}
            else
            {
                deviceList = stateMapper.querydeviceList2(start,length,QueryType,device_name,device_id,CommLine,userID);
                count = stateMapper.querdeviceListCount2(QueryType,device_name,device_id,CommLine,userID);
            }
        } 
        
        resList = new LinkedList<>();
        //遍历设备列表，获取对应的参数
        for (int i=0; i<deviceList.size();i++)
        {
            deviceMap = deviceList.get(i);
            String deviceId = (String)deviceMap.get("id");
            Integer comm_state = (Integer)deviceMap.get("comm_state");
            String deviceName = (String)deviceMap.get("name");
            String factoryid = (String)deviceMap.get("manufacture");
            String factoryName = stateMapper.getFactoryNameByFactoryId(factoryid);
            //resList.get(i).put("device", deviceName);
            resMap= new HashMap<String, Object>();
            resMap.put("device", deviceName);
            resMap.put("factory", factoryName);
            Map<String, Object> paramMap = stateMapper.getParamByDeviceId(deviceId);
            if (paramMap!=null)
            {
                resMap.put("work_status_time", (int)paramMap.get("work_status_time"));
                resMap.put("work_data_collection_interval", (int)paramMap.get("work_data_collection_interval"));
                resMap.put("wave_current_time", (int)paramMap.get("wave_current_time"));
                resMap.put("wave_current_threshold", (int)paramMap.get("wave_current_threshold"));
                resMap.put("wave_current_time_collection", (int)paramMap.get("wave_current_time_collection"));
                resMap.put("wave_current_freq_collection", (int)paramMap.get("wave_current_freq_collection"));
                resMap.put("pf_current_time", (int)paramMap.get("pf_current_time"));
                resMap.put("pf_current_threshold", (int)paramMap.get("pf_current_threshold"));
                resMap.put("pf_current_time_collection", (int)paramMap.get("pf_current_time_collection"));
                resMap.put("pf_current_freq_collection", (int)paramMap.get("pf_current_freq_collection"));
                resMap.put("id", deviceId);
                resMap.put("comm_state", comm_state);
            }
            else
            {
                resMap.put("work_status_time", "0");
                resMap.put("work_data_collection_interval", "0");
                resMap.put("wave_current_time", "0");
                resMap.put("wave_current_threshold","0");
                resMap.put("wave_current_time_collection", "0");
                resMap.put("wave_current_freq_collection", "0");
                resMap.put("pf_current_time","0");
                resMap.put("pf_current_threshold", "0");
                resMap.put("pf_current_time_collection","0");
                resMap.put("pf_current_freq_collection","0");
                resMap.put("id",deviceId);
                resMap.put("comm_state", comm_state);
            }

            resList.add(resMap);
        }
    /*
        if (StringUtils.isEmpty(QueryType))
        {
            resList = stateMapper.queryParameterListByUser(start,length,userID);
            count = stateMapper.queryParameterListCountByUser(userID);
        }
        else
        {
            resList = stateMapper.queryParameterList(start,length,QueryType,userID);
            count = stateMapper.queryParameterListCount(QueryType,userID);
        }      
       
        for (int i = 0; i<resList.size(); i++)
        {            
            String deviceID = (String)resList.get(i).get("device");       
            Map<String, Object> deviceMap = stateMapper.getDeviceInfoById(deviceID); 
            String deviceName = (String)deviceMap.get("name");
            String factoryId = (String)deviceMap.get("manufacture");
            String factoyName = stateMapper.getFactoryNameByFactoryId(factoryId);  
            resList.get(i).put("device",deviceName);
            resList.get(i).put("factory",factoyName);
            
            
            //以下是要迁移到更多参数里面单独处理
            //---------------------------------------------------------------
            //blob
            byte[] privateData = (byte[])resList.get(i).get("private_data"); 
            int paramNum = (int)(resList.get(i).get("number"));
            Map<String, Object> privateparaMap = new HashMap<String, Object>();
            
            for (int j = 0;j<paramNum; j++)
            {
                ParamAttr paramAttr = paramAttrManage.getParamAttr(factoryId, j+11);
                if(paramAttr.getType()==1)//此处先假设参数类型为int型
                {
                    byte[] ParamUnit = {privateData[4*j],privateData[4*j+1],privateData[4*j+2],privateData[4*j+3]};
                    int intpara = bytes2Int(ParamUnit);
                    String paraName = paramAttr.getName();
                    privateparaMap.put(paraName, intpara);
                }
            }
            
            System.out.println(privateparaMap);
        }
           */
               
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }  
    
    public Map<String, Object> checkFactoryLogin(String username, String password ,String factoryId)
    {
        Map<String, Object> rstMap = new HashMap<String, Object>();
        Map<String, Object> factoryMap = new HashMap<String, Object>();
        factoryMap = stateMapper.getFactoryById(factoryId);
        if (factoryMap != null) {
            if ((factoryMap.get("login_name").equals(username))&&(factoryMap.get("login_password").equals(password))) {
                rstMap.put("status", "success");
                rstMap.put("msg", "验证成功");
            } else {
                rstMap.put("status", "fail");
                rstMap.put("msg", "验证失败");
            }
        } else {
            rstMap.put("status", "fail");
            rstMap.put("msg", "厂家不存在");
        }
        return rstMap;       
    }
    
    public void saveParamAttr(Map<String, Object> paramMap) {
        String towerId = (String) paramMap.get("recordId");
        if (StringUtils.isEmpty(towerId)) {
            String recordId = StringUtils.getUUId();
            paramMap.put("recordId", recordId);
            stateMapper.addParamAttr(paramMap);
            /*
            ParamAttr paramAttr = stateMapper.getParamAttrIdById(recordId);
            System.out.println("-----------------beforeadd");
            paramAttrManage.print();
            paramAttrManage.addParamAttr(paramAttr);
            System.out.println("-----------------afteradd");
            paramAttrManage.print();
            */
        } else {
            stateMapper.updateParamAttr(paramMap);
            /*
            ParamAttr paramAttr = stateMapper.getParamAttrIdById(towerId);
            System.out.println("-----------------beforeupdate");
            paramAttrManage.print();
            paramAttrManage.updateParamAttr(paramAttr);
            System.out.println("-----------------afterupdate");
            paramAttrManage.print();
            */
        }
    }
    
    public String getDeviceLogNameById(String recordId)
    {
        return stateMapper.getDeviceLogNameById(recordId);
    }
    
    //sendDownloadLogById
    public int sendDownloadLogById(String recordId, String userId)
    {
        //return 0;
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        int protocol_version = stateMapper.getProtocolVerByDeviceId(recordId);
        sender.downloadLog(uuid,userId,recordId,protocol_version);
        int loopCount = 0; 
        while(loopCount<200)
        {
            try {
                Thread.sleep(5000);
                System.out.println("-----------------loopCount");
                System.out.println(loopCount);
               }catch(InterruptedException e){
                e.printStackTrace();
               }
            
            String tempuuid = UUID.randomUUID().toString().replaceAll("-", "");
            stateMapper.addReadParamOrder(tempuuid,1,"2","2");
            stateMapper.Commit();
            stateMapper.deleteReadParamOrder(tempuuid);
            stateMapper.Commit();

            Map<String, Object> stateMap = stateMapper.getStateByOrderid(uuid);
            if (stateMap != null)
            {
                return (int)stateMap.get("state");   
            }
            loopCount++;
        }    
        return -1;
    }
    
    public int readSelfCheckById(String recordId, String userId)
    {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        int protocol_version = stateMapper.getProtocolVerByDeviceId(recordId);
        sender.readSelfCheckInfo(uuid,userId,recordId,protocol_version);
        int loopCount = 0; 
        while(loopCount<10)
        {
            try {
                Thread.sleep(3000);
                System.out.println("-----------------loopCount");
                System.out.println(loopCount);
               }catch(InterruptedException e){
                e.printStackTrace();
               }
            
            String tempuuid = UUID.randomUUID().toString().replaceAll("-", "");
            stateMapper.addReadParamOrder(tempuuid,1,"2","2");
            stateMapper.Commit();
            stateMapper.deleteReadParamOrder(tempuuid);
            stateMapper.Commit();

            Map<String, Object> stateMap = stateMapper.getStateByOrderid(uuid);
            if (stateMap != null)
            {
                return (int)stateMap.get("state");   
            }
            loopCount++;
        }    
        return -1;
    }
        
    public int addReadOrderByDeviceIdanduserId(String recordId, String userId)
    {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //System.out.println(uuid);
        int protocol_version = stateMapper.getProtocolVerByDeviceId(recordId);
        sender.readDevParameter(uuid,userId,recordId,protocol_version);
        int loopCount = 0; 
        while(loopCount<10)
        {
            try {
                Thread.sleep(3000);
                System.out.println("-----------------loopCount");
                System.out.println(loopCount);
               }catch(InterruptedException e){
                e.printStackTrace();
               }
            
            String tempuuid = UUID.randomUUID().toString().replaceAll("-", "");
            stateMapper.addReadParamOrder(tempuuid,1,"2","2");
            stateMapper.Commit();
            stateMapper.deleteReadParamOrder(tempuuid);
            stateMapper.Commit();
            //int temp = (int)(Math.random()*1000);
            //System.out.println(temp);
            Map<String, Object> stateMap = stateMapper.getStateByOrderid(uuid);
            if (stateMap != null)
            {
                return (int)stateMap.get("state");   
            }
            loopCount++;
        }    
        return -1;
    }
    
    //addResetOrderByDeviceIdanduserId
    public int addResetOrderByDeviceIdanduserId(String recordId, String userId)
    {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        int protocol_version = stateMapper.getProtocolVerByDeviceId(recordId);
        sender.resetDevice(uuid,userId,recordId,protocol_version);
        int loopCount = 0; 
        while(loopCount<20)
        {
            try {
                Thread.sleep(3000);
                System.out.println("-----------------loopCount");
                System.out.println(loopCount);
               }catch(InterruptedException e){
                e.printStackTrace();
               }
            String tempuuid = UUID.randomUUID().toString().replaceAll("-", "");
            stateMapper.addReadParamOrder(tempuuid,1,"2","2");
            stateMapper.Commit();
            stateMapper.deleteReadParamOrder(tempuuid);
            stateMapper.Commit();
            Map<String, Object> stateMap = stateMapper.getStateByOrderid(uuid);
            if (stateMap != null)
            {
                return (int)stateMap.get("state");   
            }
            loopCount++;
        }    
        return -1;
    }
    
    public int getAuhorityByUser(String userId)
    {
        return stateMapper.getAuhorityByUser(userId);
    }
    
    public String getPasswordByDevice(String DeviceId)
    {
        return stateMapper.getPasswordByDevice(DeviceId);
    }
    
    public String getFactoryByDevice(String DeviceId)
    {
        return stateMapper.getFactoryByDevice(DeviceId);
    }
    
    public int getProtocalByDevice(String DeviceId)
    {
        return stateMapper.getProtocolVerByDeviceId(DeviceId);
    }
    
    //addSetOrderByDeviceIdanduserId
    public int addSetOrderByDeviceIdanduserId(String recordId, String userId, String content,int []IndexLst,int []TypeLst,String[]ValueLst)
    {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        int protocol_version = stateMapper.getProtocolVerByDeviceId(recordId);
        sender.setDevParameter(uuid, userId,recordId, IndexLst,TypeLst,ValueLst,protocol_version);
        int loopCount = 0; 
        while(loopCount<10)
        {
            try {
                Thread.sleep(3000);
                System.out.println("-----------------loopCount");
                System.out.println(loopCount);
               }catch(InterruptedException e){
                e.printStackTrace();
               }
            String tempuuid = UUID.randomUUID().toString().replaceAll("-", "");
            stateMapper.addReadParamOrder(tempuuid,1,"2","2");
            stateMapper.Commit();
            stateMapper.deleteReadParamOrder(tempuuid);
            stateMapper.Commit();
            Map<String, Object> stateMap = stateMapper.getStateByOrderid(uuid);
            if (stateMap != null)
            {
                return (int)stateMap.get("state");   
            }
            loopCount++;
        }    
        return -1;
    }
    
    public Map<String, Object> getParamAttrById(String userId) {
        return stateMapper.getParamAttrById(userId);
    }
    
    //getParamById
    public Map<String, Object> getParamById(String userId) {
        Map<String, Object> ParamMap = stateMapper.getParamById(userId);//此处可能为空，需处理
        Map<String, Object> privateparaMap = new HashMap<String, Object>();
        if ((ParamMap != null)&&(ParamMap.get("private_data")!=null)&&((((byte[])ParamMap.get("private_data")).length/6)==((int)(ParamMap.get("number"))-10)))
        {
            //先赋值默认值
            Map<String, Object> deviceMap = stateMapper.getDeviceInfoById(userId);
            String factoryId1 = (String)deviceMap.get("manufacture");
            int protocal1 = (int)deviceMap.get("protocol_version");
            List<Map<String, Object>> ParamMapList = stateMapper.getParamInfoListByfactoryId(factoryId1,protocal1);
            for (int k = 0; k<ParamMapList.size();k++)
            {
                Map<String, Object> defaultParamMap = ParamMapList.get(k);
                if ((boolean)defaultParamMap.get("type"))
                {
                    String paraName = (String)(defaultParamMap.get("name"));
                    int intpara = 0;
                    privateparaMap.put(paraName, intpara);
                }
                else
                {
                    String paraName = (String)(defaultParamMap.get("name"));
                    float floatpara = 0.1F;
                    privateparaMap.put(paraName, floatpara);
                }
            }
            
          //再解析私有数据
            byte[] privateData = (byte[])ParamMap.get("private_data");
            int paramsize = privateData.length/6;
            for(int n=0;n<paramsize;n++)
            {

                int pos1 = n*6;
                int nParaNum = privateData[pos1]*256+privateData[pos1+1];//index
                
                int pos2 = n*6+2;
                Map<String, Object> paramAttrMap = stateMapper.getParamAttr(factoryId1,protocal1, nParaNum); 
                String paramName = (String)paramAttrMap.get("name");
                
                //intэ
                if((boolean)(paramAttrMap.get("type"))){
                int nValue = getInt(privateData,pos2);//getFloat(tmpData,pos2);
                privateparaMap.put(paramName, nValue);
                }else{
                float fValue = getFloat(privateData,pos2);//getFloat(tmpData,pos2);
                privateparaMap.put(paramName, fValue);                
                }   
            }
            return privateparaMap;
        }
        else
        {
            Map<String, Object> deviceMap = stateMapper.getDeviceInfoById(userId);
            String factoryId1 = (String)deviceMap.get("manufacture");
            int protocal1 = (int)deviceMap.get("protocol_version");
            List<Map<String, Object>> ParamMapList = stateMapper.getParamInfoListByfactoryId(factoryId1,protocal1);
            for (int k = 0; k<ParamMapList.size();k++)
            {
                Map<String, Object> defaultParamMap = ParamMapList.get(k);
                if ((boolean)defaultParamMap.get("type"))
                {
                    String paraName = (String)(defaultParamMap.get("name"));
                    int intpara = 0;
                    privateparaMap.put(paraName, intpara);
                }
                else
                {
                    String paraName = (String)(defaultParamMap.get("name"));
                    float floatpara = 0.1F;
                    privateparaMap.put(paraName, floatpara);
                }
            }
            return privateparaMap;
        }
    }
    
    //getcommonParamById
    public Map<String, Object> getcommonParamById(String userId) {
        Map<String, Object> ParamMap = stateMapper.getParamById(userId);//此处可能为空，需处理
        	int re_work_status_time = (int) ParamMap.get("work_status_time");
        	
        	int s_hour =re_work_status_time/(256*256);
        	int s_minute = (re_work_status_time - s_hour*(256*256))/256;
        	int s_second = (re_work_status_time - s_hour*(256*256) - s_minute*256);
    	String mix_work_status_time = String.format("%02d",s_hour)+":"+String.format("%02d",s_minute)+":"+String.format("%02d",s_second);
    	ParamMap.put("work_status_time", mix_work_status_time);
        
	        int re_wave_current_time = (int) ParamMap.get("wave_current_time");
	      	int c_hour = re_wave_current_time/(256*256);
	      	int c_minute = (re_wave_current_time - c_hour*(256*256))/256;
	      	int c_second = (re_wave_current_time - c_hour*(256*256) - c_minute*256);
      	String mix_wave_current_time = String.format("%02d",c_hour)+":"+String.format("%02d",c_minute)+":"+String.format("%02d",c_second);
      	ParamMap.put("wave_current_time", mix_wave_current_time);
        
	        int re_pf_current_time = (int) ParamMap.get("pf_current_time");
	      	int p_hour =re_pf_current_time/(256*256);	
	      	int p_minute = (re_pf_current_time - p_hour*(256*256))/256;
	      	int p_second = (re_pf_current_time - p_hour*(256*256) - p_minute*256);
      	String mix_pf_current_time = String.format("%02d",p_hour)+":"+String.format("%02d",p_minute)+":"+String.format("%02d", p_second);
        ParamMap.put("pf_current_time", mix_wave_current_time);
          
        if (ParamMap == null)
        {
            ParamMap = new HashMap<String, Object>();
            ParamMap.put("wave_current_time",0);
            ParamMap.put("wave_current_threshold",0);
            ParamMap.put("wave_current_time_collection",0);
            ParamMap.put("wave_current_freq_collection",0);
            ParamMap.put("pf_current_time",0);
            ParamMap.put("pf_current_threshold",0);
            ParamMap.put("pf_current_time_collection",0);
            ParamMap.put("pf_current_freq_collection",0);
            ParamMap.put("work_status_time",0);
            ParamMap.put("work_data_collection_interval",0);
        }
        
        return ParamMap;
    }
    
    //getParamNameById
    public List<String> getParamNameById(String regulatorId)
    {
        Map<String, Object> deviceMap = stateMapper.getDeviceInfoById(regulatorId);
        String factoryId = (String)deviceMap.get("manufacture");
        int protocol_version = (int)deviceMap.get("protocol_version");
        List<String> ParamNameList = stateMapper.getParamNameListByfactoryId(factoryId, protocol_version);
        return ParamNameList;
    }
    
    public boolean deleteParamAttrById(String regulatorId)
    {
        /*
        System.out.println("-----------------before");
        paramAttrManage.print();
        //相应的map内存删除操作
        ParamAttr paramAttr = stateMapper.getParamAttrIdById(regulatorId);
        System.out.println(paramAttr);
        paramAttrManage.deleteParamAttr(paramAttr);
        System.out.println("-----------------after");
        paramAttrManage.print();
        */
        stateMapper.deleteParamAttrById(regulatorId);
        return true;
    }
  
    public void setDealAlarmById(String userId) {
        stateMapper.setDealAlarmById(userId);
    }
    
    public Map<String, Object> queryWavePwd(Map<String, Object> paramMap)
    {
        String userId = (String)paramMap.get("userId");
        String wave_pwd = (String)paramMap.get("wave_pwd");
        Map<String, Object> rstMap = new HashMap<String, Object>();
        String  wave_pwdofdb = userMapper.findUserWavePwdById(userId);

            if (wave_pwd.equals(wave_pwdofdb)) {
                rstMap.put("status", "success");
                rstMap.put("msg", "查询录波文件成功");
            } else {
                rstMap.put("status", "fail");
                rstMap.put("msg", "查询录波文件密码错误");
            }
        return rstMap;
    }
    
    public List<Map<String, Object>> getParamInfoListByfactoryId(String factoryId,int protocal)
    {
        return stateMapper.getParamInfoListByfactoryId(factoryId, protocal);
    }
}

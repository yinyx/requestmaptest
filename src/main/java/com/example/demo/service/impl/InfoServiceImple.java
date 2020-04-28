package com.example.demo.service.impl;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.service.InfoService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.example.demo.mapper.InfoMapper;
import com.example.demo.mapper.StateMapper;
import com.example.demo.po.DataTableModel;
import com.example.demo.po.Device;

import util.aes.StringUtils;

@Service
@Transactional
public class InfoServiceImple implements InfoService{

    @Autowired
    private InfoMapper infoMapper;
    
    @Autowired
    private StateMapper stateMapper;
    
    public DataTableModel queryRegulatorInfoList(Map<String, String> dataTableMap)
    {
        DataTableModel dataTableModel = new DataTableModel();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        List<Map<String, Object>> resList;
        Integer count;        
        String sEcho = dataTableMap.get("sEcho");
        String QueryType = dataTableMap.get("QueryType");
        
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));

        paramMap.put("stuCode_s", dataTableMap.get("stuCode_s"));
        paramMap.put("stuName_s", dataTableMap.get("stuName_s"));
        paramMap.put("stuStates_s", dataTableMap.get("stuStates_s"));

        if(QueryType.isEmpty())
        {
            resList = infoMapper.queryRegulatorList(start,length);
            count = infoMapper.queryRegulatorListCount()-1;
        }
        else
        {
            resList = infoMapper.queryRegulatorListByCondition(start,length,QueryType);
            count = infoMapper.queryRegulatorListCountByCondition(QueryType)-1;
        }
        
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);
        return dataTableModel;
    }
    
    public DataTableModel queryFactoryInfoList(Map<String, String> dataTableMap)
    {
        DataTableModel dataTableModel = new DataTableModel();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        List<Map<String, Object>> resList;
        Integer count;        
        String sEcho = dataTableMap.get("sEcho");
        String QueryType = dataTableMap.get("QueryType");
        
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));

        paramMap.put("stuCode_s", dataTableMap.get("stuCode_s"));
        paramMap.put("stuName_s", dataTableMap.get("stuName_s"));
        paramMap.put("stuStates_s", dataTableMap.get("stuStates_s"));

        resList = infoMapper.queryFactoryList(start,length,QueryType);
        count = infoMapper.queryFactoryListCount(QueryType);
        
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);
        return dataTableModel;
    }
    
    //queryStationList
    public DataTableModel queryStationList(Map<String, String> dataTableMap)
    {
        DataTableModel dataTableModel = new DataTableModel();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        List<Map<String, Object>> resList;
        Integer count;        
        String sEcho = dataTableMap.get("sEcho");
        String QueryType = dataTableMap.get("QueryType");
        
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));

        paramMap.put("stuCode_s", dataTableMap.get("stuCode_s"));
        paramMap.put("stuName_s", dataTableMap.get("stuName_s"));
        paramMap.put("stuStates_s", dataTableMap.get("stuStates_s"));

        resList = infoMapper.queryStationList(start,length,QueryType);
        count = infoMapper.queryStationListCount(QueryType);
       
        
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);
        return dataTableModel;
    }
    
    public DataTableModel queryTowerInfoList(Map<String, String> dataTableMap)
    {
        DataTableModel dataTableModel = new DataTableModel();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        List<Map<String, Object>> resList;
        Integer count;        
        String sEcho = dataTableMap.get("sEcho");
        String QueryType = dataTableMap.get("QueryType");

        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));

        paramMap.put("stuCode_s", dataTableMap.get("stuCode_s"));
        paramMap.put("stuName_s", dataTableMap.get("stuName_s"));
        paramMap.put("stuStates_s", dataTableMap.get("stuStates_s"));

        if(StringUtils.isEmpty(QueryType)||QueryType.equals("0"))
        {
            resList = infoMapper.queryTowerList(start,length);
            count = infoMapper.queryTowerListCount();
        }
        else
        {
            resList = infoMapper.queryTowerListByCondition(start,length,QueryType);
            count = infoMapper.queryTowerListCountByCondition(QueryType);
        }
        
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);
        return dataTableModel;
    }
    
    public List<Map<String,Object>> queryRegulator()
    {
        List<Map<String, Object>> allCollege = infoMapper.queryRegulator();
        return allCollege;
    }
   
    public List<Map<String,Object>> queryLineByUser(String userId)
    {
        List<Map<String, Object>> resList = new ArrayList<>();
        
        List<String> allCollege = infoMapper.queryLineListByUser(userId);
        String lineId = "";
        String lineName = "";
        for (int i=0; i<allCollege.size(); i++)
        {
            Map<String,Object> LineMap = new HashMap<String,Object>();
            lineId = allCollege.get(i);
            lineName = (String)infoMapper.getLineById(lineId).get("name");
            LineMap.put("id", lineId);
            LineMap.put("name", lineName);
            resList.add(LineMap);
        }
        return resList;
    }
    
    public List<Map<String,String>> queryLineByUserForApp(String userId)
    {
        List<Map<String, String>> resList = new ArrayList<>();
        
        List<String> allCollege = infoMapper.queryLineListByUser(userId);
        String lineId = "";
        String lineName = "";
        for (int i=0; i<allCollege.size(); i++)
        {
            Map<String,String> LineMap = new HashMap<String,String>();
            lineId = allCollege.get(i);
            lineName = (String)infoMapper.getLineById(lineId).get("name");
            LineMap.put("id", lineId);
            LineMap.put("name", lineName);
            LineMap.put("falg", "true");
            resList.add(LineMap);
        }
        return resList;
    }
    
    public List<Map<String,Object>> queryLineByMultiCondition(String userId, String regulatorId, String voltageId)
    {
        List<Map<String, Object>> resList = new ArrayList<>();
        
        List<String> allCollege = infoMapper.queryLineByMultiCondition(userId, regulatorId, voltageId);
        String lineId = "";
        String lineName = "";
        for (int i=0; i<allCollege.size(); i++)
        {
            Map<String,Object> LineMap = new HashMap<String,Object>();
            lineId = allCollege.get(i);
            lineName = (String)infoMapper.getLineById(lineId).get("name");
            LineMap.put("id", lineId);
            LineMap.put("name", lineName);
            resList.add(LineMap);
        }
        return resList;
    }
    
    public List<Map<String,Object>> queryLine()
    {
        List<Map<String, Object>> allCollege = infoMapper.queryLine();
        return allCollege;
    }
    
    public List<Map<String,Object>> queryTower()
    {
        List<Map<String, Object>> allCollege = infoMapper.queryTower();
        return allCollege;
    }
    
    public List<Map<String,Object>> queryTowerByLineId(String lineId)
    {
        List<Map<String, Object>> allCollege = infoMapper.queryTowerByLineId(lineId);
        return allCollege;
    }
    
    public List<Map<String,Object>> queryFactory()
    {
        List<Map<String, Object>> allCollege = infoMapper.queryFactory();
        return allCollege;
    }
    
    public List<Map<String,Object>> queryStation()
    {
        List<Map<String, Object>> allCollege = infoMapper.queryStation();
        return allCollege;
    }
    
    public List<Map<String,Object>> queryDeviceFaultDetails(String deviceId)
    {

        List<Map<String, Object>> deviceMapList = infoMapper.queryDeviceFaultDetails(deviceId);
        List<Map<String, Object>> deviceFaultMapList = new ArrayList<Map<String,Object>>();
        //故障诊断系统到原手机APP系统的适配，每个装置发生的故障列表
        for (int k = 0; k<deviceMapList.size();k++)
        {
            Map<String, Object> deviceMap = deviceMapList.get(k);
            Map<String, Object> deviceFaultMap = new HashMap<String, Object>();
            deviceFaultMap.put("id", deviceMap.get("id"));
            deviceFaultMap.put("occur_time", deviceMap.get("alarm_time"));
            deviceFaultMap.put("content", deviceMap.get("alarm_content"));
            deviceFaultMap.put("device_id", deviceMap.get("device"));
            deviceFaultMap.put("created_time", deviceMap.get("alarm_time"));
            deviceFaultMapList.add(deviceFaultMap);
        }
        return deviceFaultMapList;
    }
    
    public void saveRegulator(Map<String, Object> paramMap) {
        String userId = (String) paramMap.get("regulatorId");
        if (StringUtils.isEmpty(userId)) {
            paramMap.put("regulatorId", StringUtils.getUUId());
            infoMapper.addRegulator(paramMap);
        } else {
            infoMapper.updateRegulator(paramMap);
        }
    }
    
    public void saveFactory(Map<String, Object> paramMap) {
        String regulatorId = (String) paramMap.get("regulatorId");
        String num = (String) paramMap.get("num");
        String name = (String) paramMap.get("name");
        String contact = (String) paramMap.get("contact");
        String call = (String) paramMap.get("call");
        String login_name = (String) paramMap.get("login_name");
        String login_password = (String) paramMap.get("login_password");
        //String uuid = (String) paramMap.get("uuid");
        String sample_Rate = (String) paramMap.get("sample_Rate");

        if (StringUtils.isEmpty(regulatorId)) {
            paramMap.put("regulatorId", StringUtils.getUUId());
            regulatorId = (String) paramMap.get("regulatorId");
            infoMapper.addFactory(regulatorId, num, name, contact, call, login_name,
                                  login_password, /*uuid,*/ sample_Rate);
        } else {
            infoMapper.updateFactory(regulatorId, num, name, contact, call, login_name,
                    login_password, /*uuid,*/ sample_Rate);
        }
    }
    
    public void saveStation(Map<String, Object> paramMap) {
        String towerId = (String) paramMap.get("regulatorId");
        if (StringUtils.isEmpty(towerId)) {
            paramMap.put("regulatorId", StringUtils.getUUId());
            infoMapper.addStation(paramMap);
        } else {
            infoMapper.updateStation(paramMap);
        }
    }
    
    public void saveTower(Map<String, Object> paramMap) {
        String towerId = (String) paramMap.get("recordId");
        if (StringUtils.isEmpty(towerId)) {
            paramMap.put("recordId", StringUtils.getUUId());
            infoMapper.addTower(paramMap);
        } else {
            infoMapper.updateTower(paramMap);
        }
    }
    
    public void saveDevice(Map<String, Object> paramMap) {
        String towerId = (String) paramMap.get("recordId");
        String deviceId = (String) paramMap.get("device");
        String deviceIP = (String) paramMap.get("IP");
        String name = (String) paramMap.get("name");
        String factory = (String) paramMap.get("factory");
        int InstallIndex = Integer.parseInt((String) paramMap.get("InstallIndex"));
        int phase = Integer.parseInt((String) paramMap.get("phase"));
        int ProtocalType = Integer.parseInt((String) paramMap.get("ProtocalType"));
        String IedType = (String) paramMap.get("IedType");
        String version = (String) paramMap.get("version");
        String ManuDate = (String) paramMap.get("ManuDate");
        String InstallTime = (String) paramMap.get("InstallTime");
        String longitude = (String) paramMap.get("longitude");
        String latitude = (String) paramMap.get("latitude");
        String altitude = (String) paramMap.get("altitude");
        //String FrequencyFactor = (String) paramMap.get("FrequencyFactor");
        //String BigWaveRange = (String) paramMap.get("BigWaveRange");
        //String SmallWaveRange = (String) paramMap.get("SmallWaveRange");
        String line = (String) paramMap.get("line");
        String tower = (String) paramMap.get("tower");
        /*
         * if (tower.length() == 0) { tower = "0"; }
         */
        //String zero_drift_comps_small = (String) paramMap.get("zero_drift_comps_small");

        if (StringUtils.isEmpty(towerId)) {
            paramMap.put("recordId", StringUtils.getUUId());
            towerId = (String) paramMap.get("recordId");
            infoMapper.addDevice(deviceId, deviceIP,InstallIndex, name, line, factory, ManuDate,
                    IedType, version, tower, InstallTime, phase, longitude, latitude,
                    altitude,ProtocalType);
        } else {
            infoMapper.updateDevice(towerId,deviceIP,InstallIndex, name, line, factory, ManuDate,
                    IedType, version, tower, InstallTime, phase, longitude, latitude,
                    altitude,ProtocalType);
        }
    }
    
    public DataTableModel queryLineInfoList(Map<String, String> dataTableMap)
    {       
        DataTableModel dataTableModel = new DataTableModel();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        List<Map<String, Object>> resList;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        String QueryType = dataTableMap.get("QueryType");
        
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));

        paramMap.put("stuCode_s", dataTableMap.get("stuCode_s"));
        paramMap.put("stuName_s", dataTableMap.get("stuName_s"));
        paramMap.put("stuStates_s", dataTableMap.get("stuStates_s"));

        if(QueryType.isEmpty())
        {
            resList = infoMapper.queryLineList(start,length);
            count = infoMapper.queryLineListCount();
        }
        else
        {
            resList = infoMapper.queryLineListByCondition(start,length,QueryType);
            count = infoMapper.queryLineListCountByCondition(QueryType);
        }
        
        System.out.println(resList);
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }
    
    public DataTableModel queryDeviceInfoList(Map<String, String> dataTableMap)
    {       
        DataTableModel dataTableModel = new DataTableModel();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        List<Map<String, Object>> resList;
        Integer count;
        String sEcho = dataTableMap.get("sEcho");
        String QueryType = dataTableMap.get("QueryType");
        String QueryType1 = dataTableMap.get("QueryType1");       
        
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));

        paramMap.put("stuCode_s", dataTableMap.get("stuCode_s"));
        paramMap.put("stuName_s", dataTableMap.get("stuName_s"));
        paramMap.put("stuStates_s", dataTableMap.get("stuStates_s"));
    
        if (StringUtils.isEmpty(QueryType))
        {
            QueryType = "";
        }
        if (StringUtils.isEmpty(QueryType1))
        {
            QueryType1 = "";
        }
        resList = infoMapper.queryDeviceList(start,length,QueryType,QueryType1);
        count = infoMapper.queryDeviceListCount(QueryType,QueryType1);

        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }
    
    public void saveLine(Map<String, Object> paramMap) {
        String lineId = (String) paramMap.get("recordId");
        if (StringUtils.isEmpty(lineId)) {
            paramMap.put("recordId", StringUtils.getUUId());
            infoMapper.addLine(paramMap);
            System.out.println("----------add");
        } else {
            infoMapper.updateLine(paramMap);
            System.out.println("----------update");
        }
    }
    
    public Map<String, Object> getLineById(String userId) {
        return infoMapper.getLineById(userId);
    }
    
    public Map<String, Object> getTowerById(String userId) {
        return infoMapper.getTowerById(userId);
    }
    
    public Map<String, Object> getDeviceById(String userId) {
        return infoMapper.getDeviceById(userId);
    }    
    
    public Map<String, String> getDeviceInfoById(String deviceId){
        //此处需要合并装置基本信息，工况信息，参数信息
        Map<String, String> allDeviceInfoMap = new HashMap<String, String>();
        //获取装置基本信息
        Map<String, Object> basicMap = infoMapper.getDeviceById(deviceId);
        //获取装置工况信息
        Map<String, Object> WorkConditionMap = stateMapper.getWorkConditionById(deviceId);
        //获取装置参数信息
        Map<String, Object> paramMap = stateMapper.getParamById(deviceId);
        //合并
        allDeviceInfoMap.put("latitude",(String)basicMap.get("latitude"));
        allDeviceInfoMap.put("ied_phase",(String)basicMap.get("ied_phase"));
        allDeviceInfoMap.put("device_name",(String)basicMap.get("name"));
        allDeviceInfoMap.put("e_w_longitude",(String)basicMap.get("longitude"));
        allDeviceInfoMap.put("fpga",(String)basicMap.get("fpgaRatio"));
        allDeviceInfoMap.put("ied_type",(String)basicMap.get("ied_type"));
        allDeviceInfoMap.put("line_name",(String)basicMap.get("line_name"));
        allDeviceInfoMap.put("ide_point_no",(String)basicMap.get("ide_point_no"));
        allDeviceInfoMap.put("longitude",(String)basicMap.get("longitude"));
        allDeviceInfoMap.put("manu_date",(String)basicMap.get("manu_date"));
        allDeviceInfoMap.put("s_n_latitude",(String)basicMap.get("latitude"));
        allDeviceInfoMap.put("device_id",(String)basicMap.get("device"));
        allDeviceInfoMap.put("key_id",(String)basicMap.get("id"));
        allDeviceInfoMap.put("zero_drift_coefficient",(String)basicMap.get("zero_drift_comps_small"));
        allDeviceInfoMap.put("version",(String)basicMap.get("version"));
        allDeviceInfoMap.put("manufacture",(String)basicMap.get("manufacture"));
        allDeviceInfoMap.put("manu_no",(String)basicMap.get("manu_no"));
        
        allDeviceInfoMap.put("work_upload_time",(String)WorkConditionMap.get("workcondition_time"));
        allDeviceInfoMap.put("device_temp",(String)WorkConditionMap.get("device_tempature"));
        allDeviceInfoMap.put("current_valid_value",(String)WorkConditionMap.get("current_valid_value"));
        allDeviceInfoMap.put("battery_status",(String)WorkConditionMap.get("battery_status"));
        allDeviceInfoMap.put("battery_vol",(String)WorkConditionMap.get("battery_vol"));
        
        allDeviceInfoMap.put("tw_current_time_collection",(String)paramMap.get("wave_current_time"));
        allDeviceInfoMap.put("tw_current_freq_collection",(String)paramMap.get("wave_current_freq_collection"));
        allDeviceInfoMap.put("tw_current_threshold",(String)paramMap.get("wave_current_threshold"));
        allDeviceInfoMap.put("pf_current_threshold",(String)paramMap.get("pf_current_threshold"));
        allDeviceInfoMap.put("work_data_collection_interval",(String)paramMap.get("work_data_collection_interval"));
        allDeviceInfoMap.put("pf_current_freq_collection",(String)paramMap.get("pf_current_freq_collection"));
        allDeviceInfoMap.put("pf_current_time",(String)paramMap.get("pf_current_time"));
        allDeviceInfoMap.put("pf_current_time_collection",(String)paramMap.get("pf_current_time_collection"));
        allDeviceInfoMap.put("tw_current_time",(String)paramMap.get("wave_current_time"));
        allDeviceInfoMap.put("work_status_time",(String)paramMap.get("work_status_time"));
        return allDeviceInfoMap;
    }
    
    public Map<String, Object> getStationById(String userId) {
        return infoMapper.getStationById(userId);
    }  
    
    public boolean deleteLine(String userId) {
        infoMapper.deleteLine(userId);
        infoMapper.deleteUser_LineByLine(userId);
        return true;
    }
    
    public boolean deleteTower(String userId) {
        infoMapper.deleteTower(userId);
        return true;
    }
    
    public boolean deleteDevice(String userId) {
        infoMapper.deleteDevice(userId);
        return true;
    }
    
    public boolean deleteFactory(String userId) {
        infoMapper.deleteFactory(userId);
        return true;
    }
    
    //deleteStation
    public boolean deleteStation(String userId) {
        infoMapper.deleteStation(userId);
        return true;
    }
    
    public String getDeviceNameById(String deviceId) {
        return infoMapper.getDeviceNameById(deviceId);
    }
    
    public Map<String, Object> getFactoryById(String regulatorId) {
        return infoMapper.getFactoryById(regulatorId);
    }
    
    public int queryallDeviceNum() {
        return infoMapper.queryallDeviceNum();
    }
    
    public int queryonlineDeviceNum() {
        return infoMapper.queryonlineDeviceNum();
    }
    
    public int queryofflineDeviceNum() {
        return infoMapper.queryofflineDeviceNum();
    }
    
    public int querynoReadAlarmNumByUser(String userId) {
        return infoMapper.querynoReadAlarmNumByUser(userId);
    }
    
    public int querynoReadFaultNumByUser(String userId) {
        return infoMapper.querynoReadFaultNumByUser(userId);
    }
    
    public Map<String, Object> getTowerAndDeviceInfoByAclineId(String acLineId) {
        // 获取杆塔的经纬度
        List<Map<String, String>> towerList = infoMapper.getTowerLonLat(acLineId);
        // 获取终端信息
        List<Map<String, String>> deviceList = infoMapper.getDeviceIdNameLonLat(acLineId);
        
        
        Map<String, Object> map = new HashMap<>();
        map.put("towerList", towerList);
        map.put("deviceList", deviceList);
        return map;
    }
    
    public void exportStu(List<Device> clsList)
    {
        System.out.print(clsList);
        try(OutputStream out=new FileOutputStream("D://装置详细信息.xlsx")) {
            ExcelWriter writer=new ExcelWriter(out,ExcelTypeEnum.XLSX);
            
            if(!clsList.isEmpty()) {

                Sheet sheet=new Sheet(1,0,clsList.get(0).getClass());
                
                sheet.setSheetName("装置详细信息");
                writer.write(clsList, sheet);
            }
            writer.finish();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void export1(List<Device> clsList) {
        try{
            String fileName = "D://装置详细信息.xlsx";
            EasyExcel.write(fileName)
                // 这里放入动态头
                .head(head()).sheet("装置详细信息")
                // 当然这里数据也可以用 List<List<String>> 去传入
                .doWrite(clsList);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
   private List<List<String>> head() {
       List<List<String>> list = new ArrayList<List<String>>();
       int allDeviceNum = queryallDeviceNum();
       int onlineDeviceNum = queryonlineDeviceNum();
       int offlineDeviceNum = allDeviceNum - onlineDeviceNum;
       String showDevice ="系统接入设备："+allDeviceNum+"台  系统正常设备："
               +onlineDeviceNum+"台  系统异常设备："+offlineDeviceNum+"台 ";
       
       List<String> head0 = new ArrayList<String>();
       head0.add(showDevice);
       head0.add("通信状态");
       List<String> head1 = new ArrayList<String>();
       head1.add(showDevice);
       head1.add("所属厂家");
       List<String> head2 = new ArrayList<String>();
       head2.add(showDevice);
       head2.add("所属线路");
       List<String> head3 = new ArrayList<String>();
       head3.add(showDevice);
       head3.add("安装序号");
       List<String> head4 = new ArrayList<String>();
       head4.add(showDevice);
       head4.add("装置标识");
       List<String> head5 = new ArrayList<String>();
       head5.add(showDevice);
       head5.add("杆塔名称");
       List<String> head6 = new ArrayList<String>();
       head6.add(showDevice);
       head6.add("相别");
       List<String> head7 = new ArrayList<String>();
       head7.add(showDevice);
       head7.add("协议类型");
       
       List<String> head8 = new ArrayList<String>();
       head8.add(showDevice);
       head8.add("最近工频波形时间");
       List<String> head9 = new ArrayList<String>();
       head9.add(showDevice);
       head9.add("最近故障波形时间");
       List<String> head10 = new ArrayList<String>();
       head10.add(showDevice);
       head10.add("最近工况报文时间");
       
       list.add(head0);
       list.add(head1);
       list.add(head2);
       list.add(head3);
       list.add(head4);
       list.add(head5);
       list.add(head6);
       list.add(head7);
       list.add(head8);
       list.add(head9);
       list.add(head10);

       return list;
   }
}

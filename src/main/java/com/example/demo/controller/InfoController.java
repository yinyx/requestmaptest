package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.po.DataTableModel;
import com.example.demo.po.DataTableParam;
import com.example.demo.service.InfoService;

import io.swagger.annotations.ApiOperation;
import util.aes.DatatableUtil;
import util.aes.AesUtil;

@RestController
@RequestMapping(value = "/info")
public class InfoController {

    // 注入用户Service
    @Resource
    private InfoService infoService;

    @RequestMapping(value="/queryRegulatorList",method=RequestMethod.POST)
    public Object  queryRegulatorList(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = infoService.queryRegulatorInfoList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("infoData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询监管单位列表信息异常!");
        }
        return resultMap;
    }

    @RequestMapping(value="/queryFactoryList",method=RequestMethod.POST)
    public Object  queryFactoryList(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = infoService.queryFactoryInfoList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("infoData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询终端厂家列表信息异常!");
        }
        return resultMap;
    }

    @RequestMapping(value="/queryStationList",method=RequestMethod.POST)
    public Object  queryStationList(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = infoService.queryStationList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("infoData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询变电站列表信息异常!");
        }
        return resultMap;
    }

    @RequestMapping(value="/queryTowerList",method=RequestMethod.POST)
    public Object  queryTowerList(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = infoService.queryTowerInfoList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("infoData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询杆塔列表信息异常!");
        }
        return resultMap;
    }

    @RequestMapping(value="/queryRegulator",method=RequestMethod.POST)
    public Object  queryRegulator(){
        Map<String, Object> resultMap=new HashMap<String,Object>();
        //Map<String, Object> paramMap = new HashMap<String, Object>();
        try {
            List<Map<String,Object>> dataList = infoService.queryRegulator();
            resultMap.put("status", "success");
            resultMap.put("dataList",dataList);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取监管单位信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }


    @RequestMapping(value="/queryMarqueeInfo",method=RequestMethod.POST)
    @ResponseBody
    public Object  queryMarqueeInfo(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String userId = (String) paramObj.get("userId");
        Map<String, Object> resultMap=new HashMap<String,Object>();
        //Map<String, Object> paramMap = new HashMap<String, Object>();
        try {
            int allDeviceNum = infoService.queryallDeviceNumByUser(userId);
            int onlineDeviceNum = infoService.queryonlineDeviceNumByUser(userId);
            int offlineDeviceNum = allDeviceNum - onlineDeviceNum;
                    //infoService.queryofflineDeviceNum();
            //int noReadFaultNum = infoService.querynoReadFaultNumByUser(userId);
            //int noReadAlarmNum = infoService.querynoReadAlarmNumByUser(userId);
            resultMap.put("status", "success");
            resultMap.put("allDeviceNum",allDeviceNum);
            resultMap.put("onlineDeviceNum",onlineDeviceNum);
            resultMap.put("offlineDeviceNum",offlineDeviceNum);
            //resultMap.put("noReadAlarmNum",noReadAlarmNum);
            //resultMap.put("noReadFaultNum",noReadFaultNum);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取监管单位信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/queryFactory",method=RequestMethod.POST)
    public Object  queryFactory(){
        Map<String, Object> resultMap=new HashMap<String,Object>();
        //Map<String, Object> paramMap = new HashMap<String, Object>();
        try {
            List<Map<String,Object>> dataList = infoService.queryFactory();
            resultMap.put("status", "success");
            resultMap.put("dataList",dataList);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取终端厂家信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    //queryStation
    @RequestMapping(value="/queryStation",method=RequestMethod.POST)
    public Object  queryStation(){
        Map<String, Object> resultMap=new HashMap<String,Object>();
        //Map<String, Object> paramMap = new HashMap<String, Object>();
        try {
            List<Map<String,Object>> dataList = infoService.queryStation();
            resultMap.put("status", "success");
            resultMap.put("dataList",dataList);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取变电站信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/saveRegulator",method=RequestMethod.POST)
    public Map<String, Object> saveSchoolUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();

        paramMap.put("regulatorId", request.getParameter("regulatorId"));
        paramMap.put("num", request.getParameter("num"));
        paramMap.put("name", request.getParameter("name"));
        paramMap.put("address", request.getParameter("address"));
        paramMap.put("parent", request.getParameter("parent"));

        try {
            infoService.saveRegulator(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "监管单位保存成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "监管单位保存失败!");
        }
        return resultMap;
    }

    @RequestMapping(value="/saveFactory",method=RequestMethod.POST)
    public Map<String, Object> saveFactory(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();

        paramMap.put("regulatorId", request.getParameter("regulatorId"));
        paramMap.put("num", request.getParameter("num"));
        paramMap.put("name", request.getParameter("name"));
        paramMap.put("contact", request.getParameter("contact"));
        paramMap.put("call", request.getParameter("call"));
        paramMap.put("login_name", request.getParameter("login_name"));
        paramMap.put("login_password", request.getParameter("login_password"));
        //paramMap.put("uuid", request.getParameter("uuid"));
        paramMap.put("sample_Rate", request.getParameter("sample_Rate"));
        paramMap.put("wave_length", request.getParameter("wave_length"));

        try {
            infoService.saveFactory(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "终端厂家保存成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "终端厂家保存失败!");
        }
        return resultMap;
    }

    @RequestMapping(value="/saveStation",method=RequestMethod.POST)
    public Map<String, Object> saveStation(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();

        paramMap.put("regulatorId", request.getParameter("regulatorId"));
        paramMap.put("num", request.getParameter("num"));
        paramMap.put("name", request.getParameter("name"));
        paramMap.put("address", request.getParameter("address"));
        paramMap.put("remark", request.getParameter("remark"));

        try {
            infoService.saveStation(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "变电站保存成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "变电站保存失败!");
        }
        return resultMap;
    }

    @RequestMapping(value="/saveTower",method=RequestMethod.POST)
    public Map<String, Object> saveTower(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();

        paramMap.put("recordId", request.getParameter("recordId"));
        paramMap.put("name", request.getParameter("name"));
        paramMap.put("index", request.getParameter("index"));
        paramMap.put("distance", request.getParameter("distance"));
        paramMap.put("longitude", request.getParameter("longitude"));
        paramMap.put("latitude", request.getParameter("latitude"));
        paramMap.put("altitude", request.getParameter("altitude"));
        paramMap.put("tower_to_m", request.getParameter("tower_to_m"));
        paramMap.put("tower_to_n", request.getParameter("tower_to_n"));
        paramMap.put("line", request.getParameter("line"));

        try {
            infoService.saveTower(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "杆塔信息保存成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "杆塔信息保存失败!");
        }
        return resultMap;
    }

    @RequestMapping(value="/saveDevice",method=RequestMethod.POST)
    public Map<String, Object> saveDevice(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();

        paramMap.put("recordId", request.getParameter("recordId"));
        paramMap.put("device", request.getParameter("device"));
        paramMap.put("IP", request.getParameter("IP"));
        paramMap.put("name", request.getParameter("name"));
        paramMap.put("factory", request.getParameter("factory"));
        paramMap.put("line", request.getParameter("line"));
        paramMap.put("InstallIndex", request.getParameter("InstallIndex"));
        paramMap.put("tower", request.getParameter("tower"));
        paramMap.put("ProtocalType", request.getParameter("ProtocalType"));
        paramMap.put("phase", request.getParameter("phase"));
        paramMap.put("IedType", request.getParameter("IedType"));
        paramMap.put("version", request.getParameter("version"));
        paramMap.put("ManuDate", request.getParameter("ManuDate"));
        paramMap.put("InstallTime", request.getParameter("InstallTime"));
        paramMap.put("longitude", request.getParameter("longitude"));
        paramMap.put("latitude", request.getParameter("latitude"));
        paramMap.put("altitude", request.getParameter("altitude"));
        paramMap.put("FrequencyFactor", request.getParameter("FrequencyFactor"));
        paramMap.put("BigWaveRange", request.getParameter("BigWaveRange"));
        paramMap.put("SmallWaveRange", request.getParameter("SmallWaveRange"));
        paramMap.put("zero_drift_comps_small", request.getParameter("zero_drift_comps_small"));

        try {
            infoService.saveDevice(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "装置信息保存成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "装置信息保存失败!");
        }
        return resultMap;
    }

	@RequestMapping(value="/queryLineList",method=RequestMethod.POST)
    public Object  queryLineList(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = infoService.queryLineInfoList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("infoData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询线路列表信息异常!");
        }
        return resultMap;
    }

	   @RequestMapping(value="/queryDeviceList",method=RequestMethod.POST)
	    public Object  queryDeviceList(@RequestBody DataTableParam[] dataTableParams){
	        DataTableModel dataTableModel = new DataTableModel();
	        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
	        Map<String, Object> resultMap = new HashMap<String, Object>();
	        try {
	            dataTableModel = infoService.queryDeviceInfoList(dataTableMap);
	            resultMap.put("status", "success");
	            resultMap.put("infoData", dataTableModel);
	        }
	        catch(Exception e)
	        {
	            resultMap.put("status", "error");
	            resultMap.put("msg", "查询装置列表信息异常!");
	        }
	        return resultMap;
	    }

	@RequestMapping(value="/saveLine",method=RequestMethod.POST)
    public Map<String, Object> saveLine(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();

        //类型检查
        String LineLength = (String)request.getParameter("length");
        try {
            float   f   =   Float.parseFloat(LineLength);
        }catch(Exception e){
            resultMap.put("status", "error");
            resultMap.put("msg", "线路长度应填写数值:整数或者小数!");
            return resultMap;
        }

        paramMap.put("recordId", request.getParameter("recordId"));
        paramMap.put("name", request.getParameter("name"));
        paramMap.put("regulator", request.getParameter("regulator"));
        paramMap.put("length", request.getParameter("length"));
		paramMap.put("voltage_level", request.getParameter("voltage_level"));
        paramMap.put("ac_dc", request.getParameter("ac_dc"));
        paramMap.put("LeftStation", request.getParameter("LeftStation"));
        paramMap.put("RightStation", request.getParameter("RightStation"));

        try {
            infoService.saveLine(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "线路信息保存成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "线路信息保存失败!");
        }
        return resultMap;
    }

    @RequestMapping(value="/getLineById",method=RequestMethod.POST)
    @ResponseBody
    public Object getLineById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("recordId");
        Map<String, Object> usersData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            usersData = infoService.getLineById(recordId);
            resultMap.put("status", "success");
            resultMap.put("usersData", usersData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询线路列表异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/getTowerById",method=RequestMethod.POST)
    @ResponseBody
    public Object getTowerById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("recordId");
        Map<String, Object> usersData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            usersData = infoService.getTowerById(recordId);
            resultMap.put("status", "success");
            resultMap.put("usersData", usersData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询杆塔信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    //getStationById
    @RequestMapping(value="/getStationById",method=RequestMethod.POST)
    @ResponseBody
    public Object getStationById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("regulatorId");
        Map<String, Object> usersData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            usersData = infoService.getStationById(recordId);
            resultMap.put("status", "success");
            resultMap.put("usersData", usersData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询变电站信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/getDeviceById",method=RequestMethod.POST)
    @ResponseBody
    public Object getDeviceById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("recordId");
        Map<String, Object> usersData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            usersData = infoService.getDeviceById(recordId);
            resultMap.put("status", "success");
            resultMap.put("usersData", usersData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询装置信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/getMoreDeviceById",method=RequestMethod.POST)
    @ResponseBody
    public Object getMoreDeviceById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("recordId");
        Map<String, Object> usersData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            usersData = infoService.getDeviceById(recordId);
            resultMap.put("status", "success");
            resultMap.put("usersData", usersData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询更多装置信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    /**
     * 根据deviceId获取他的全部消息
     * @param deviceId
     * @return {altitude=0, work_upload_time=2226-01-01 14:58:13-700-33725-4, latitude=0, work_status_time=786432, ied_phase=0, device_name=三跨终端, e_w_longitude=0, fpga=1, device_temp=0, ied_type=/型号/, tw_current_time_collection=1000, line_name=116530640358221594, ide_point_no=10, tw_current_freq_collection=1000, longitude=0, manu_date=3328210913457090872, s_n_latitude=0, tw_current_threshold=100, device_id=12345678901234568, key_id=168322036072972290, zero_drift_coefficient=20, current_valid_value=0, version=0, pf_current_threshold=450, battery_status=87, manufacture=国电南瑞, work_data_collection_interval=10, pf_current_freq_collection=1000, pf_current_time=786432, manu_no=00000001, pf_current_time_collection=1000, tw_current_time=721152, battery_vol=5}
     */
    @RequestMapping(value="getDeviceInfoById",method=RequestMethod.POST)
    @ResponseBody
    public String getDeviceInfoById(@RequestParam Map<String, Object> map){
        JSONObject obj = new JSONObject();
        try {
            JSONObject paramObj=AesUtil.GetParam(map);
            String deviceId = (String) paramObj.get("deviceId");
            Map<String, String> deviceInfoMap = infoService.getDeviceInfoById(deviceId);
            obj.put("result", "success");
            obj.put("reason", "");
            obj.put("deviceInfoMap", deviceInfoMap);

        } catch (Exception e) {
            obj.put("result", "failure");
            obj.put("reason", "查询设备信息失败");
        }

        String enResult = AesUtil.enCodeByKey(obj.toString());
        return enResult;
    }

    @RequestMapping(value="/deleteLine",method=RequestMethod.POST)
    @ResponseBody
    public Object deleteLine(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String userId = request.getParameter("userId");

        try {
            boolean flag = infoService.deleteLine(userId);
            if(flag){
                resultMap.put("status", "success");
                resultMap.put("msg", "删除成功!");
            }else{
                resultMap.put("status", "error");
                resultMap.put("msg", "删除失败,该用户已被使用!");
            }
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "删除失败!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/deleteDevice",method=RequestMethod.POST)
    @ResponseBody
    public Object deleteDevice(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String userId = request.getParameter("userId");

        try {
            boolean flag = infoService.deleteDevice(userId);
            if(flag){
                resultMap.put("status", "success");
                resultMap.put("msg", "删除成功!");
            }else{
                resultMap.put("status", "error");
                resultMap.put("msg", "删除失败,该用户已被使用!");
            }
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "删除失败!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/deleteTower",method=RequestMethod.POST)
    @ResponseBody
    public Object deleteTower(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String userId = request.getParameter("userId");

        try {
            boolean flag = infoService.deleteTower(userId);
            if(flag){
                resultMap.put("status", "success");
                resultMap.put("msg", "删除成功!");
            }else{
                resultMap.put("status", "error");
                resultMap.put("msg", "删除失败,该用户已被使用!");
            }
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "删除失败!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/deleteFactory",method=RequestMethod.POST)
    @ResponseBody
    public Object deleteFactory(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String userId = request.getParameter("userId");

        try {
            boolean flag = infoService.deleteFactory(userId);
            if(flag){
                resultMap.put("status", "success");
                resultMap.put("msg", "删除成功!");
            }else{
                resultMap.put("status", "error");
                resultMap.put("msg", "删除失败,该用户已被使用!");
            }
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "删除失败!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/deleteStation",method=RequestMethod.POST)
    @ResponseBody
    public Object deleteStation(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String userId = request.getParameter("userId");

        try {
            boolean flag = infoService.deleteStation(userId);
            if(flag){
                resultMap.put("status", "success");
                resultMap.put("msg", "删除成功!");
            }else{
                resultMap.put("status", "error");
                resultMap.put("msg", "删除失败,该用户已被使用!");
            }
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "删除失败!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/queryLineByUser",method=RequestMethod.POST)
    @ResponseBody
    public Object  queryLineByUser(@RequestParam Map<String, Object> map){

        JSONObject paramObj=AesUtil.GetParam(map);
        String userId = (String) paramObj.get("userId");

        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List<Map<String,Object>> dataList = infoService.queryLineByUser(userId);
            resultMap.put("status", "success");
            resultMap.put("dataList",dataList);

        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取线路信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/queryLineByMultiCondition",method=RequestMethod.POST)
    @ResponseBody
    public Object  queryLineByMultiCondition(@RequestParam Map<String, Object> map){

        JSONObject paramObj=AesUtil.GetParam(map);
        String userId = (String) paramObj.get("userId");
        String regulatorId = (String) paramObj.get("regulatorId");
        String voltageId = (String) paramObj.get("voltageId");

        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List<Map<String,Object>> dataList = infoService.queryLineByMultiCondition(userId, regulatorId, voltageId);
            resultMap.put("status", "success");
            resultMap.put("dataList",dataList);

        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取线路信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/queryLine",method=RequestMethod.POST)
    public Object  queryLine(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap=new HashMap<String,Object>();
        try {
            List<Map<String,Object>> dataList = infoService.queryLine();
            resultMap.put("status", "success");
            resultMap.put("dataList",dataList);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取线路信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/queryTower",method=RequestMethod.POST)
    public Object  queryTower(){
        Map<String, Object> resultMap=new HashMap<String,Object>();
        try {
            List<Map<String,Object>> dataList = infoService.queryTower();
            resultMap.put("status", "success");
            resultMap.put("dataList",dataList);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取杆塔信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/queryTowerByLine",method=RequestMethod.POST)
    @ResponseBody
    public Object  queryTowerByLine(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String lineId = (String) paramObj.get("lineId");
        Map<String, Object> resultMap=new HashMap<String,Object>();
        try {
            List<Map<String,Object>> dataList = infoService.queryTowerByLineId(lineId);
            resultMap.put("status", "success");
            resultMap.put("dataList",dataList);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取杆塔信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/getFactoryById",method=RequestMethod.POST)
    @ResponseBody
    public Object getFactoryById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String regulatorId = (String) paramObj.get("regulatorId");
        Map<String, Object> usersData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            usersData = infoService.getFactoryById(regulatorId);
            resultMap.put("status", "success");
            resultMap.put("usersData", usersData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询厂家信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    /**
     * 获取指定用户负责的所有线路信息
     * @param userId
     * @return
     */
    @RequestMapping(value="userGetAclineSegment",method=RequestMethod.POST)
    @ResponseBody
    public String userGetAclineSegment(@RequestParam Map<String, Object> map){
        JSONObject obj = new JSONObject();
        try {
            JSONObject paramObj=AesUtil.GetParam(map);
            String userId = (String) paramObj.get("userId");
            List<Map<String, String>> lineList = infoService.queryLineByUserForApp(userId);
            obj.put("result", "success");
            obj.put("reason", "");
            obj.put("lineList", lineList);

        } catch (Exception e) {
            obj.put("result", "failure");
            obj.put("reason", "查询指定用户负责的全部线路信息失败");
        }

        String enResult = AesUtil.enCodeByKey(obj.toString());
        return enResult;
    }

    /**
     * 通过线路id获取杆塔经纬度、终端id和name和经纬度
     * @param acLineId
     * @return {deviceList=[{device_name=三跨终端, key_id=168322036072972290, latitude=0, longitude=0}, {device_name=三跨终端, key_id=168322036072972291, latitude=0, longitude=0}], towerList=[{latitude=, longitude=}, {latitude=, longitude=}, {latitude=, longitude=}]}
     */
    @ApiOperation("通过线路id获取杆塔经纬度、终端id和name和经纬度")
    @RequestMapping(value="getTowerAndDeviceInfoByAclineId",method=RequestMethod.POST)
    @ResponseBody
    public String getTowerAndDeviceInfoByAclineId(@RequestParam Map<String, Object> map){
        JSONObject obj = new JSONObject();
        try {
            JSONObject paramObj=AesUtil.GetParam(map);
            String acLineId = (String) paramObj.get("acLineId");
            Map<String, Object> towerAndDeviceInfoMap = infoService.getTowerAndDeviceInfoByAclineId(acLineId);
            obj.put("result", "success");
            obj.put("reason", "");
            obj.put("towerAndDeviceInfoMap", towerAndDeviceInfoMap);

        } catch (Exception e) {
            obj.put("result", "failure");
            obj.put("reason", "查询杆塔和设备信息失败");
        }

        String enResult = AesUtil.enCodeByKey(obj.toString());
        return enResult;
    }
}

package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.po.DataTableModel;
import com.example.demo.po.DataTableParam;
import com.example.demo.service.FaultService;
import com.example.demo.service.StateService;
import com.example.demo.service.InfoService;

import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import util.aes.AesUtil;
import util.aes.DatatableUtil;



@RestController
@RequestMapping(value = "/fault")
public class FaultController {
    protected  Logger logger = LogManager.getLogger(getClass());
    
    // 注入用户Service
    @Resource
    private FaultService faultService;
    
    @Resource
    private StateService stateService;
    
    @Resource
    private InfoService infoService;
    
    @RequestMapping(value="/queryWaveList")
    public Object  queryRegulatorList(@RequestBody DataTableParam[] dataTableParams){ 
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = faultService.queryWaveInfoList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("infoData", dataTableModel);
            logger.info("查询波形列表信息成功!");
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询波形列表信息异常!");
            logger.error("查询波形列表信息失败!");
        }
        return resultMap;
    }  
    
	@RequestMapping(value="/queryFaultList")
    public Object  queryFaultList(@RequestBody DataTableParam[] dataTableParams){ 
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = faultService.queryFaultList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("infoData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询故障列表信息异常!");
        }
        return resultMap;
    }  
	
	   @RequestMapping(value="/queryFaultListInGis")
	    public Object  queryFaultListInGis(@RequestBody DataTableParam[] dataTableParams){ 
	        DataTableModel dataTableModel = new DataTableModel();
	        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
	        Map<String, Object> resultMap = new HashMap<String, Object>();
	        try {
	            dataTableModel = faultService.queryFaultListInGis(dataTableMap);
	            resultMap.put("status", "success");
	            resultMap.put("infoData", dataTableModel);
	        }
	        catch(Exception e)
	        {
	            resultMap.put("status", "error");
	            resultMap.put("msg", "查询故障列表信息异常!");
	        }
	        return resultMap;
	    }
	
    @RequestMapping(value="/getWaveById",method=RequestMethod.POST)
    @ResponseBody
    public Object getWaveById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("recordId");
        Map<String, Object> usersData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            usersData = faultService.getWaveById(recordId);
            resultMap.put("status", "success");
            resultMap.put("usersData", usersData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询波形信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    } 
    
    @RequestMapping(value="/getFaultById",method=RequestMethod.POST)
    @ResponseBody
    public Object getFaultById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("recordId");
        Map<String, Object> usersData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            usersData = faultService.getFaultById(recordId);
            resultMap.put("status", "success");
            resultMap.put("usersData", usersData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询故障信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    } 
    
    //setDealFaultById
    @RequestMapping(value="/setDealFaultById",method=RequestMethod.POST)
    @ResponseBody
    public Object setDealFaultById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("recordId");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            faultService.setDealFaultById(recordId);
            resultMap.put("status", "success");
            resultMap.put("msg", "设置故障已处理成功!");
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "设置故障已处理失败!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    } 
    
    @RequestMapping(value="/queryHeartBeatList")
    public Object  queryHeartBeatList(@RequestBody DataTableParam[] dataTableParams){ 
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = stateService.queryHeartBeatList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("infoData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询波形列表信息异常!");
        }
        return resultMap;
    } 
    
    @RequestMapping(value="/queryWavePwd")
    public Map<String, Object> queryWavePwd(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();

        paramMap.put("wave_pwd", request.getParameter("wave_pwd"));
        paramMap.put("userId", request.getParameter("userId"));     

        resultMap =stateService.queryWavePwd(paramMap);

        return resultMap;
    } 
    
    /**
     * 默认未读是0，点击进去发送lineId，改数据库is_read为1，，，
     * 
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("获取手机端线路故障列表")
    @RequestMapping(value="/queryLineFaultList",method=RequestMethod.POST)
    @ResponseBody
    public Object queryLineFaultList(@RequestParam Map<String, Object> map) {
        JSONObject paramObj=AesUtil.GetParam(map);
        String userId = (String) paramObj.get("userId");
        String type = (String) paramObj.get("type");
        String page = (String) paramObj.get("page");
        String pageSize = (String) paramObj.get("pageSize");

        JSONObject obj = new JSONObject();

        try {

            List<Map<String, Object>> lineInfos = faultService.queryLineFaultList(userId, type, page, pageSize);
            long total = faultService.countLineFaultNum(userId, type);
            //long unReadNum = faultService.countLineUnReadNum(lineInfos);
            
            int unReadNum = faultService.countLineUnReadNum(userId);
            
            obj.put("result", "success");
            obj.put("reason", "");
            obj.put("total", total);
            obj.put("unReadNum", unReadNum);
            obj.put("lineInfos", lineInfos);

        } catch (Exception e) {

            obj.put("result", "failure");
            obj.put("reason", "查询线路故障列表失败");

        }

        String enResult = AesUtil.enCodeByKey(obj.toString());
        return enResult;
    }
    
    //@RequestMapping(value="/getFaultById",method=RequestMethod.POST)
    //@ResponseBody
    @ApiOperation("获取手机端终端故障列表")
    @RequestMapping(value="/queryDeviceFaultList",method=RequestMethod.POST)
    @ResponseBody
    public Object queryDeviceFaultList(@RequestParam Map<String, Object> map) {
        JSONObject paramObj=AesUtil.GetParam(map);
        String userId = (String) paramObj.get("userId");
        String type = (String) paramObj.get("type");
        String page = (String) paramObj.get("page");
        String pageSize = (String) paramObj.get("pageSize");

        JSONObject obj = new JSONObject();

        try {
            //TODO total 是错的 分页是使用
            List<Map<String, Object>> deviceInfos = faultService.queryDeviceFaultList(userId, type, page, pageSize);
            long total = faultService.countDeviceFaultNum(userId, type);
            //long unReadNum = faultService.countDeviceUnReadNum(deviceInfos);
            int unReadNum = faultService.countDeviceUnReadNum(userId);
            obj.put("result", "success");
            obj.put("reason", "");
            obj.put("total", total);
            obj.put("unReadNum", unReadNum);
            obj.put("deviceInfos", deviceInfos);
        } catch (Exception e) {

            obj.put("result", "failure");
            obj.put("reason", "查询设备故障列表失败");
        }

        String enResult = AesUtil.enCodeByKey(obj.toString());
        return enResult;
    }
    
    @ApiOperation("获取手机端终端故障详情")
    @RequestMapping(value="/queryDeviceFaultDetails",method=RequestMethod.POST)
    @ResponseBody
    public Object queryDeviceFaultDetails(@RequestParam Map<String, Object> map) {
        JSONObject paramObj=AesUtil.GetParam(map);
        String deviceId = (String) paramObj.get("deviceId");

        JSONObject obj = new JSONObject();

        try {

            List<Map<String, Object>> mapList = infoService.queryDeviceFaultDetails(deviceId);
            String deviceName = infoService.getDeviceNameById(deviceId);

            // TODO 将map里面的数据拿出来，放入obj

            obj.put("result", "success");
            obj.put("reason", "");
            obj.put("deviceName", deviceName);
            obj.put("deviceInfos", mapList);

        } catch (Exception e) {

            obj.put("result", "failure");
            obj.put("reason", "查询设备故障详情失败");
        }

        String enResult = AesUtil.enCodeByKey(obj.toString());
        return enResult;
    }
}

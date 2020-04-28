package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.po.DataTableModel;
import com.example.demo.po.DataTableParam;
import com.example.demo.service.GisService;

import net.sf.json.JSONObject;
import util.aes.AesUtil;
import util.aes.DatatableUtil;

@RestController
@RequestMapping(value = "/gis")
public class GisController {
    
    @Resource
    private GisService gisService;
    
    @RequestMapping(value="/queryDeviceList")
    public Object  queryDeviceList(@RequestBody DataTableParam[] dataTableParams){ 
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = gisService.queryDeviceList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("infoData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询设备列表信息异常!");
        }
        return resultMap;
    } 
    
    @RequestMapping(value="/queryLineListForGis")
    public Object  queryLineListForGis(@RequestBody DataTableParam[] dataTableParams){ 
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = gisService.queryLineListForGis(dataTableMap);
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
    
    @RequestMapping(value="/queryFaultList")
    public Object  queryFaultList(@RequestBody DataTableParam[] dataTableParams){ 
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = gisService.queryFaultList(dataTableMap);
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
    
    @RequestMapping(value="/queryDeviceListForGis",method=RequestMethod.POST)
    public Object  queryDeviceListForGis(@RequestParam Map<String, Object> map){ 
        JSONObject paramObj=AesUtil.GetParam(map);
        String userId = (String) paramObj.get("userId");
        Map<String, Object> resultMap=new HashMap<String,Object>();
        try {
            List<Map<String,Object>> dataList = gisService.queryDeviceListForGis(userId);
            resultMap.put("status", "success");
            resultMap.put("dataList",dataList);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取设备列表信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    } 
    
    @RequestMapping(value="/queryTowerListForGis",method=RequestMethod.POST)
    public Object  queryTowerListForGis(@RequestParam Map<String, Object> map){ 
        JSONObject paramObj=AesUtil.GetParam(map);
        String LineUserId = (String) paramObj.get("LineUserId");
        Map<String, Object> resultMap=new HashMap<String,Object>();
        try {
            List<Map<String,Object>> dataList = gisService.queryTowerListForGis(LineUserId);
            resultMap.put("status", "success");
            resultMap.put("dataList",dataList);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取GIS杆塔列表信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    } 
    
    @RequestMapping(value="/queryFaultListForGis",method=RequestMethod.POST)
    public Object  queryFaultListForGis(@RequestParam Map<String, Object> map){ 
        JSONObject paramObj=AesUtil.GetParam(map);
        String userId = (String) paramObj.get("userId");
        Map<String, Object> resultMap=new HashMap<String,Object>();
        try {
            List<Map<String,Object>> dataList = gisService.queryFaultListForGis(userId);
            resultMap.put("status", "success");
            resultMap.put("dataList",dataList);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取故障列表信息异常121!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    } 
}

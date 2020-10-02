package com.example.demo.controller;

import util.aes.TestProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.demo.po.DataTableModel;
import com.example.demo.po.DataTableParam;
import com.example.demo.service.FaultService;
import com.example.demo.service.StateService;

import io.swagger.annotations.ApiOperation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import util.aes.AesUtil;
import util.aes.DatatableUtil;

@RestController
@RequestMapping(value = "/state")
public class StateController {

    // 注入用户Service
    @Resource
    private StateService stateService;

    @ApiOperation("查询心跳列表信息")
    @RequestMapping(value="/queryHeartBeatList",method=RequestMethod.POST)
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
            resultMap.put("msg", "查询心跳列表信息异常!");
        }
        return resultMap;
    }

    @ApiOperation("查询工况列表信息")
    @RequestMapping(value="/queryWorkConditionList",method=RequestMethod.POST)
    public Object  queryWorkConditionList(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = stateService.queryWorkConditionList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("infoData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询工况列表信息异常!");
        }
        return resultMap;
    }

//    @ApiOperation("查询工况列表信息显示装置最新工况")
//    @RequestMapping(value="/queryWorkConditionListOrder",method=RequestMethod.POST)
//    public Object  queryWorkConditionListOrder(@RequestBody DataTableParam[] dataTableParams){
//        DataTableModel dataTableModel = new DataTableModel();
//        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
//        Map<String, Object> resultMap = new HashMap<String, Object>();
//        try {
//            dataTableModel = stateService.queryWorkConditionListOrder(dataTableMap);
//            resultMap.put("status", "success");
//            resultMap.put("infoData", dataTableModel);
//        }
//        catch(Exception e)
//        {
//            resultMap.put("status", "error");
//            resultMap.put("msg", "查询工况列表信息异常!");
//        }
//        return resultMap;
//    }
//
    @ApiOperation("查询命令状态列表信息")
    @RequestMapping(value="/queryOrderStatusList",method=RequestMethod.POST)
    public Object  queryOrderStatusList(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = stateService.queryOrderStatusList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("infoData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询命令列表信息异常!");
        }
        return resultMap;
    }

    @ApiOperation("查询设备告警列表信息")
    @RequestMapping(value="/queryAlarmList",method=RequestMethod.POST)
    public Object  queryAlarmList(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = stateService.queryAlarmList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("infoData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询告警列表信息异常!");
        }
        return resultMap;
    }

    @ApiOperation("查询参数属性列表信息")
    @RequestMapping(value="/queryParameterAttrList",method=RequestMethod.POST)
    public Object  queryParameterAttrList(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = stateService.queryParameterAttrList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("infoData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询参数属性列表信息异常!");
        }
        return resultMap;
    }

    @ApiOperation("查询参数列表信息")
    @RequestMapping(value="/queryParameterList",method=RequestMethod.POST)
    public Object  queryParameterList(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = stateService.queryParameterList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("infoData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询参数列表信息异常!");
        }
        return resultMap;
    }

    @ApiOperation("查询设备自检列表信息")
    @RequestMapping(value="/querySelfCheckList",method=RequestMethod.POST)
    public Object  querySelfCheckList(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = stateService.querySelfCheckList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("infoData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询自检列表信息异常!");
        }
        return resultMap;
    }

    @ApiOperation("查询通信状态信息")
    @RequestMapping(value="/queryCommunicateState",method=RequestMethod.POST)
    public Object  queryCommunicateState(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = stateService.queryCommunicateState(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("infoData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询通信状态信息异常!");
        }
        return resultMap;
    }

//    @ApiOperation("查询通信状态信息")
//    @RequestMapping(value="/queryCommunicateStateNew",method=RequestMethod.POST)
//    public Object  queryCommunicateStateNew(@RequestBody DataTableParam[] dataTableParams){
//
//        DataTableModel dataTableModel = new DataTableModel();
//        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
//        Map<String, Object> resultMap = new HashMap<String, Object>();
//    	System.out.println("++++++++++++++++"+dataTableMap+"++++++++++++++++");
//        try {
//            dataTableModel = stateService.queryCommunicateStateNew(dataTableMap);
//            resultMap.put("status", "success");
//            resultMap.put("infoData", dataTableModel);
//        }
//        catch(Exception e)
//        {
//            resultMap.put("status", "error");
//            resultMap.put("msg", "查询通信状态信息异常!");
//        }
//        return resultMap;
//    }

    @ApiOperation("验证厂家登录，配置对应厂家的装置的私有参数属性")
    @RequestMapping(value="/checkFactoryLogin",method=RequestMethod.POST)
    public String checkFactoryLogin(@RequestParam Map<String, Object> map,HttpSession session) {
        JSONObject paramObj=AesUtil.GetParam(map);
        String username = (String) paramObj.get("username");
        String password = (String) paramObj.get("password");
        String factoryId = (String) paramObj.get("factoryId");
        Map<String, Object> userMap = stateService.checkFactoryLogin(username, password, factoryId);
        JSONObject jsonObject = JSONObject.fromObject(userMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @ApiOperation("增加参数属性")
    @RequestMapping(value="/addParamAttr",method=RequestMethod.POST)
    public String addParamAttr(@RequestParam Map<String, Object> map,HttpSession session) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();

        JSONObject paramObj=AesUtil.GetParam(map);
        paramMap.put("recordId", (String) paramObj.get("recordId"));
        paramMap.put("indexno", (String) paramObj.get("indexno"));
        paramMap.put("name", (String) paramObj.get("name"));
        paramMap.put("type", (Integer) paramObj.get("type"));
        paramMap.put("isPrivate", (Integer) paramObj.get("isPrivate"));
        paramMap.put("factoryId", (String) paramObj.get("factoryId"));
        paramMap.put("protocalId", (String) paramObj.get("protocalId"));

        try {
            stateService.saveParamAttr(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "参数属性保存成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "参数属性保存失败!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @ApiOperation("根据ID查询某条参数属性")
    @RequestMapping(value="/getParamAttrById",method=RequestMethod.POST)
    @ResponseBody
    public Object getParamAttrById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("recordId");
        Map<String, Object> usersData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            usersData = stateService.getParamAttrById(recordId);
            resultMap.put("status", "success");
            resultMap.put("usersData", usersData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询参数属性异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    //getParamById
    @ApiOperation("根据ID获取某条参数")
    @RequestMapping(value="/getParamById",method=RequestMethod.POST)
    @ResponseBody
    public Object getParamById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("recordId");
        Map<String, Object> usersData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<String> paraNameList = new LinkedList<>();

        try {
            usersData = stateService.getParamById(recordId);
            paraNameList = stateService.getParamNameById(recordId);
            System.out.print("paraNameList");
            System.out.print(paraNameList);
            int paracount = usersData.size();
            resultMap.put("status", "success");
            resultMap.put("usersData", usersData);
            resultMap.put("paracount", paracount);
            resultMap.put("paraNameList", paraNameList);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询参数值异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    //getSysVerById
    @ApiOperation("根据ID获取装置的系统版本")
    @RequestMapping(value="/getSysVerById",method=RequestMethod.POST)
    @ResponseBody
    public Object getSysVerById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("recordId");
        Map<String, Object> usersData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();

        try {
            int result = stateService.getSysVerById(recordId);
            resultMap.put("status", "success");
            resultMap.put("result", result);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    //readParamById
    @ApiOperation("向某台装置发送读取参数的命令")
    @RequestMapping(value="/readParamById",method=RequestMethod.POST)
    @ResponseBody
    public Object readParamById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("recordId");
        String userId = (String) paramObj.get("userId");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int authority = stateService.getAuhorityByUser(userId);
     if (1 == authority) {
         try {
         Thread.sleep(1000);
         resultMap.put("status", "error");
         resultMap.put("msg", "当前登录用户没有操作设备的权限，请与系统管理员联系!");
         }
         catch(Exception e)
         {
             resultMap.put("status", "error");
             resultMap.put("msg", "增加复位装置记录异常!");
         }
     }
     else
     {
         //装置访问控制
         String device_access = (String) paramObj.get("devicePassword");
         //String device_password = "123";//此处后期要实现接口，从数据库中获取
         String device_password = stateService.getPasswordByDevice(recordId);
         if (!device_access.equals(device_password))
         {
             resultMap.put("status", "success");
             int result = -2;
             resultMap.put("result", result);
             JSONObject jsonObject = JSONObject.fromObject(resultMap);
             String enResult = AesUtil.enCodeByKey(jsonObject.toString());
             return enResult;
         }
        try {
            int result = stateService.addReadOrderByDeviceIdanduserId(recordId, userId);
            resultMap.put("status", "success");
            resultMap.put("result", result);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "增加读取参数记录异常!");
        }
     }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    //downloadLogById
    @ApiOperation("根据ID从网页下载某台装置的日志文件")
    @RequestMapping(value="/downloadLogById")
    @ResponseBody
    public Object downloadLogById(String file_name, String pathname
            ,HttpServletRequest request, HttpServletResponse response){

        //String fileName = "aim_test.txt";// 设置文件名，根据业务需要替换成要下载的文件名
        String fileName = file_name;
        if (fileName != null) {
          //设置文件路径
          //String realPath = "D://aim//";
            String realPath = pathname;
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


    //sendDownloadLogById
    @ApiOperation("向某台装置发送上召日志的命令")
    @RequestMapping(value="/sendDownloadLogById",method=RequestMethod.POST)
    @ResponseBody
    public Object sendDownloadLogById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("recordId");
        String userId = (String) paramObj.get("userId");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int authority = stateService.getAuhorityByUser(userId);
     if (1 == authority) {
         try {
         Thread.sleep(3000);
         resultMap.put("status", "error");
         resultMap.put("msg", "当前登录用户没有操作设备的权限，请与系统管理员联系!");
         }
         catch(Exception e)
         {
             resultMap.put("status", "error");
             resultMap.put("msg", "下载装置自检日志异常!");
         }
     }
     else
     {
         //装置访问控制
         String device_access = (String) paramObj.get("devicePassword");
         //String device_password = "123";//此处后期要实现接口，从数据库中获取
         String device_password = stateService.getPasswordByDevice(recordId);
         if (!device_access.equals(device_password))
         {
             resultMap.put("status", "success");
             int result = -2;
             resultMap.put("result", result);
             JSONObject jsonObject = JSONObject.fromObject(resultMap);
             String enResult = AesUtil.enCodeByKey(jsonObject.toString());
             return enResult;
         }
        try {
            int result = stateService.sendDownloadLogById(recordId, userId);
            String FileName = stateService.getDeviceLogNameById(recordId);
            //System.out.println(result);
            String cfgPath_windows = TestProperties.getProperties_1("path.properties","devicelogpath_w");
            String cfgPath_linux = TestProperties.getProperties_1("path.properties","devicelogpath_l");
            String serverip = TestProperties.getProperties_1("path.properties","serverip");
            cfgPath_linux += recordId+"/";
            if(TestProperties.isOSLinux()){
                resultMap.put("PathName", cfgPath_linux);
            }else{
                resultMap.put("PathName", cfgPath_windows);
            }
            resultMap.put("status", "success");
            resultMap.put("result", result);
            resultMap.put("FileName", "log.txt");
            resultMap.put("serverip", serverip);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "下载装置自检日志异常!");
        }
     }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    //readSelfCheckById
    @ApiOperation("向某台装置发送读取自检信息的命令")
    @RequestMapping(value="/readSelfCheckById",method=RequestMethod.POST)
    @ResponseBody
    public Object readSelfCheckById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("recordId");
        String userId = (String) paramObj.get("userId");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int authority = stateService.getAuhorityByUser(userId);
     if (1 == authority) {
         try {
         Thread.sleep(3000);
         resultMap.put("status", "error");
         resultMap.put("msg", "当前登录用户没有操作设备的权限，请与系统管理员联系!");
         }
         catch(Exception e)
         {
             resultMap.put("status", "error");
             resultMap.put("msg", "增加复位装置记录异常!");
         }
     }
     else
     {
         //装置访问控制
         String device_access = (String) paramObj.get("devicePassword");
         //String device_password = "123";//此处后期要实现接口，从数据库中获取
         String device_password = stateService.getPasswordByDevice(recordId);
         if (!device_access.equals(device_password))
         {
             resultMap.put("status", "success");
             int result = -2;
             resultMap.put("result", result);
             JSONObject jsonObject = JSONObject.fromObject(resultMap);
             String enResult = AesUtil.enCodeByKey(jsonObject.toString());
             return enResult;
         }

        try {
            int result = stateService.readSelfCheckById(recordId, userId);
            //System.out.println(result);
            resultMap.put("status", "success");
            resultMap.put("result", result);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "读取装置自检记录异常!");
        }
     }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    //getAllParamById
    @ApiOperation("读取某台装置的所有参数")
    @RequestMapping(value="/getAllParamById",method=RequestMethod.POST)
    @ResponseBody
    public Object getAllParamById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("recordId");
        String userId = (String) paramObj.get("userId");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> commonData = new HashMap<String, Object>();
        Map<String, Object> privateData = new HashMap<String, Object>();

        int authority = stateService.getAuhorityByUser(userId);
     if (1 == authority) {
         try {
         Thread.sleep(1000);
         resultMap.put("status", "error");
         resultMap.put("msg", "当前登录用户没有操作设备的权限，请与系统管理员联系!");
         }
         catch(Exception e)
         {
             resultMap.put("status", "error");
             resultMap.put("msg", "增加读取装置参数记录异常!");
         }
     }
     else
     {

        try {
            commonData = stateService.getcommonParamById(recordId);
            privateData = stateService.getParamById(recordId);
            int paracount = privateData.size();
            resultMap.put("commonData", commonData);
            resultMap.put("privateData", privateData);
            resultMap.put("paracount", paracount);
            resultMap.put("status", "success");
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "读取全部参数记录异常!");
        }
     }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    //resetDeviceById
    @ApiOperation("向某台装置发送复位的命令")
    @RequestMapping(value="/resetDeviceById",method=RequestMethod.POST)
    @ResponseBody
    public Object resetDeviceById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("recordId");
        String userId = (String) paramObj.get("userId");
        Map<String, Object> resultMap = new HashMap<String, Object>();

        int authority = stateService.getAuhorityByUser(userId);
     if (1 == authority) {
         try {
         Thread.sleep(1000);
         resultMap.put("status", "error");
         resultMap.put("msg", "当前登录用户没有操作设备的权限，请与系统管理员联系!");
         }
         catch(Exception e)
         {
             resultMap.put("status", "error");
             resultMap.put("msg", "增加复位装置记录异常!");
         }
     }
     else
     {
         //装置访问控制
         String device_access = (String) paramObj.get("devicePassword");
         //String device_password = "123";//此处后期要实现接口，从数据库中获取
         String device_password = stateService.getPasswordByDevice(recordId);
         if (!device_access.equals(device_password))
         {
             resultMap.put("status", "success");
             int result = -2;
             resultMap.put("result", result);
             JSONObject jsonObject = JSONObject.fromObject(resultMap);
             String enResult = AesUtil.enCodeByKey(jsonObject.toString());
             return enResult;
         }
        try {
            int result = stateService.addResetOrderByDeviceIdanduserId(recordId, userId);
            resultMap.put("status", "success");
            resultMap.put("result", result);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "增加复位装置记录异常!");
        }
     }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @ApiOperation("根据ID删除某条参数属性记录")
    @RequestMapping(value="/deleteParamAttrById",method=RequestMethod.POST)
    @ResponseBody
    public Object deleteParamAttrById(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String userId = request.getParameter("recordId");

        try {
            boolean flag = stateService.deleteParamAttrById(userId);
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

    @ApiOperation("向某台装置发送设置参数的命令")
    @RequestMapping(value="/setParamById",method=RequestMethod.POST)
    public Map<String, Object> setParamById(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();


        //装置访问控制
        String device_access = request.getParameter("device_access");
        String recordId = request.getParameter("recordId");

        //String device_password = "123";//此处后期要实现接口，从数据库中获取
        String device_password = stateService.getPasswordByDevice(recordId);
        if (!device_access.equals(device_password))
        {
            resultMap.put("status", "success");
            int result = -2;
            resultMap.put("result", result);
            return resultMap;
        }

        String factoryString = stateService.getFactoryByDevice(recordId);
        int Protocal = stateService.getProtocalByDevice(recordId);
        List<Map<String, Object>> ParamMapList =stateService.getParamInfoListByfactoryId(factoryString,Protocal);

        int privateLength = ParamMapList.size();

        int nSize = 10+privateLength;
        String[] ValueLst = new String[nSize];
        int[] IndexLst = new int[nSize];
        int[] TypeLst = new int[nSize];
        for(int ii =0;ii<10;ii++)
        {
            IndexLst[ii] = ii+1;
            TypeLst[ii] = 1;
        }

        for (int k = 0; k<privateLength;k++)
        {
            Map<String, Object> defaultParamMap = ParamMapList.get(k);
            boolean bParaType = (boolean)defaultParamMap.get("type");
            int paraType =0;
            if (bParaType)
            {
                paraType = 1;
            }
            else {
                paraType = 0;
            }
            int indexno = (int)defaultParamMap.get("indexno");
            String paraName = (String)(defaultParamMap.get("name"));
            String paraVal = request.getParameter(paraName);
            if (paraVal == null)
            {
                paraVal = "";
            }

            ValueLst[10+k] = paraVal;
            TypeLst[10+k] = paraType;
            IndexLst[10+k] = indexno;

        }


        String userId = request.getParameter("userId");
        String wave_current_time = request.getParameter("wave_current_time");
        String wave_current_threshold = request.getParameter("wave_current_threshold");
        String wave_current_time_collection = request.getParameter("wave_current_time_collection");
        String wave_current_freq_collection = request.getParameter("wave_current_freq_collection");
        String pf_current_time = request.getParameter("pf_current_time");
        String pf_current_threshold = request.getParameter("pf_current_threshold");
        String pf_current_time_collection = request.getParameter("pf_current_time_collection");
        String pf_current_freq_collection = request.getParameter("pf_current_freq_collection");
        String work_status_time = request.getParameter("work_status_time");
        String work_data_collection_interval = request.getParameter("work_data_collection_interval");

        ValueLst[0] = wave_current_time;
        ValueLst[1] = wave_current_threshold;
        ValueLst[2] = wave_current_time_collection;
        ValueLst[3] = wave_current_freq_collection;
        ValueLst[4] = pf_current_time;
        ValueLst[5] = pf_current_threshold;
        ValueLst[6] = pf_current_time_collection;
        ValueLst[7] = pf_current_freq_collection;
        ValueLst[8] = work_status_time;
        ValueLst[9] = work_data_collection_interval;


        String content = "";

        String[] wave_current_time_lst = wave_current_time.split(":",0);
        int mix_wave_current_time = Integer.parseInt(wave_current_time_lst[0])*256*256
        		+Integer.parseInt(wave_current_time_lst[1])*256
        		+Integer.parseInt(wave_current_time_lst[2]);

        String[] work_status_time_lst = work_status_time.split(":",0);
        int mix_work_status_time_lst = Integer.parseInt(work_status_time_lst[0])*256*256
        		+Integer.parseInt(work_status_time_lst[1])*256
        		+Integer.parseInt(work_status_time_lst[2]);

        String[] pf_current_time_lst = pf_current_time.split(":",0);
        int mix_pf_current_time_lst = Integer.parseInt(pf_current_time_lst[0])*256*256
        		+Integer.parseInt(pf_current_time_lst[1])*256
        		+Integer.parseInt(pf_current_time_lst[2]);

        content+="wave_current_time: ";
        content+=wave_current_time;
        content+=" wave_current_threshold: ";
        content+=wave_current_threshold;
        content+=" wave_current_time_collection: ";
        content+=wave_current_time_collection;
        content+=" wave_current_freq_collection: ";
        content+=wave_current_freq_collection;
        content+=" pf_current_time: ";
        content+=pf_current_time;
        content+=" pf_current_threshold: ";
        content+=pf_current_threshold;
        content+=" pf_current_time_collection: ";
        content+=pf_current_time_collection;
        content+=" pf_current_freq_collection: ";
        content+=pf_current_freq_collection;
        content+=" work_status_time: ";
        content+=work_status_time;
        content+=" work_data_collection_interval: ";
        content+=work_data_collection_interval;



        /*
        int Iwave_current_time =mix_wave_current_time;
        ValueLst[0] = Iwave_current_time;
        int Iwave_current_threshold = Integer.valueOf(wave_current_threshold).intValue();
        ValueLst[1] = Iwave_current_threshold;
        int Iwave_current_time_collection = Integer.valueOf(wave_current_time_collection).intValue();
        ValueLst[2] = Iwave_current_time_collection;
        int Iwave_current_freq_collection = Integer.valueOf(wave_current_freq_collection).intValue();
        ValueLst[3] = Iwave_current_freq_collection;
        int Ipf_current_time = mix_pf_current_time_lst;
        ValueLst[4] = Ipf_current_time;
        int Ipf_current_threshold = Integer.valueOf(pf_current_threshold).intValue();
        ValueLst[5] = Ipf_current_threshold;
        int Iwork_status_time = mix_work_status_time_lst;
        ValueLst[8] = Iwork_status_time;
        int Ipf_current_freq_collection = Integer.valueOf(pf_current_freq_collection).intValue();
        ValueLst[7] = Ipf_current_freq_collection;
        int Ipf_current_time_collection = Integer.valueOf(pf_current_time_collection).intValue();
        ValueLst[6] = Ipf_current_time_collection;
        int Iwork_data_collection_interval = Integer.valueOf(work_data_collection_interval).intValue();
        ValueLst[9] = Iwork_data_collection_interval;
        */

        /*获取私有参数
        String paraName = "南瑞私有参数7";
        String userId = request.getParameter(paraName);
        System.out.println(userId);
        */

        try {
            int result = stateService.addSetOrderByDeviceIdanduserId(recordId, userId, content,IndexLst,TypeLst,ValueLst);
            resultMap.put("status", "success");
            //resultMap.put("msg", "增加设置参数记录成功!");
            resultMap.put("result", result);
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "增加设置参数记录失败!");
        }
        return resultMap;
    }

    //setDealAlarmById
    @ApiOperation("根据ID设置某条故障报警已处理")
    @RequestMapping(value="/setDealAlarmById",method=RequestMethod.POST)
    @ResponseBody
    public Object setDealAlarmById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String recordId = (String) paramObj.get("recordId");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            stateService.setDealAlarmById(recordId);
            resultMap.put("status", "success");
            resultMap.put("msg", "设置故障报警已处理成功!");
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "设置报警已处理失败!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
}

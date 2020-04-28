package com.example.demo.controller;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.po.DataTableModel;
import com.example.demo.po.DataTableParam;
import com.example.demo.po.Line;
import com.example.demo.po.Device;
import com.example.demo.service.FaultService;
import com.example.demo.service.UserService;
import com.example.demo.service.InfoService;

import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import util.aes.AesUtil;
import util.aes.DatatableUtil;
import util.aes.NrUtil;
import util.aes.TestProperties;

@RestController
@RequestMapping(value = "/sys")
public class SysController {
    // 注入用户Service
    @Resource
    private UserService userService;
    
    @Resource
    private InfoService infoService;
    
    @ApiOperation("同步线路，杆塔信息")
    @RequestMapping(value="/syncProcess",method=RequestMethod.POST)
    public String  syncProcess(){ 
        Map<String, Object> resultMap=new HashMap<String,Object>();
        try {
            //String cmd = "F:\\fr\\pubSub\\pubSub.exe";
            
            String cmd = TestProperties.getProperties_1("path.properties","syncpath");
            System.out.println(cmd);
            final Process p = Runtime.getRuntime().exec(cmd);

           // final Process p = Runtime.getRuntime().exec("fg3pf batch C:/tomcat/webapps/FaceGen/img/batch.csv f");
           
            try{
                BufferedInputStream br = new BufferedInputStream(p.getInputStream());
                BufferedOutputStream br1 = new BufferedOutputStream(p.getOutputStream());
                int ch;
                StringBuffer text = new StringBuffer("获得的信息是: \n");
 
                while ((ch = br.read()) != -1) {
                    text.append((char) ch);
                }
                int   retval   =   p.waitFor();
 
                System.out.println(text+br1.toString());
                System.out.println(retval);
                if (1 == retval)
                {
                    resultMap.put("status", "success");
                    resultMap.put("msg", "同步数据成功!");
                }
                else
                {
                    resultMap.put("status", "error");
                    resultMap.put("msg", "同步数据失败!");
                }
           
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                System.out.print(p.exitValue());
            }            
               
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "同步数据失败!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
    
    @ApiOperation("同步终端信息")
    @RequestMapping(value="/syncDeviceProcess",method=RequestMethod.POST)
    public String  syncDeviceProcess(){ 
        Map<String, Object> resultMap=new HashMap<String,Object>();
        try {
            //String cmd = "F:\\fr\\pubSub\\pubSub.exe";
            
            String cmd = TestProperties.getProperties_1("path.properties","jobpath");
            System.out.println(cmd);
            final Process p = Runtime.getRuntime().exec(cmd);

           // final Process p = Runtime.getRuntime().exec("fg3pf batch C:/tomcat/webapps/FaceGen/img/batch.csv f");
           
            try{
                BufferedInputStream br = new BufferedInputStream(p.getInputStream());
                BufferedOutputStream br1 = new BufferedOutputStream(p.getOutputStream());
                int ch;
                StringBuffer text = new StringBuffer("获得的信息是: \n");
 
                while ((ch = br.read()) != -1) {
                    text.append((char) ch);
                }
                int   retval   =   p.waitFor();
 
                System.out.println(text+br1.toString());
                System.out.println(retval);
                if (1 == retval)
                {
                    resultMap.put("status", "success");
                    resultMap.put("msg", "同步异构数据成功!");
                }
                else
                {
                    resultMap.put("status", "error");
                    resultMap.put("msg", "同步异构数据失败!");
                }
           
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                System.out.print(p.exitValue());
            }            
               
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "同步数据失败!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
 
    @ApiOperation("同步终端信息1，暂时未用")
    @RequestMapping(value="/syncDeviceProcess1",method=RequestMethod.POST)
    public String  syncDeviceProcess1(){ 
        System.out.println("job run successfully!1");
        Map<String, Object> resultMap=new HashMap<String,Object>();
        System.out.println("job run successfully!2");
        try {
            //String cmd = "F:\\fr\\pubSub\\pubSub.exe";
            
            String jobPath = TestProperties.getProperties_1("path.properties","jobpath");
            System.out.println("job run successfully!3");
            try {
                KettleEnvironment.init();
                System.out.println("job run successfully!4");
                JobMeta jm = new JobMeta(jobPath, null);
                System.out.println("job run successfully!5");
                Job job = new Job(null, jm);
                System.out.println("job run successfully!6");
                job.start();
                System.out.println("job run successfully!7");
                job.waitUntilFinished();
                System.out.println("job run successfully!8");
                if(job.getErrors()>0)
                {
                    System.err.println("job run Failure!");
                }
                else
                {
                    System.out.println("job run successfully!");
                }
            } catch (KettleException e) {
                e.printStackTrace();
            }         
               
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "同步数据失败!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
    
    @ApiOperation("获取线路列表信息")
    @RequestMapping(value="/queryLine",method=RequestMethod.POST)
    public String  queryLine(){ 
        Map<String, Object> resultMap=new HashMap<String,Object>();
        try {
            List<Line> LineList= this.userService.queryLine();  
            resultMap.put("status", "success");
            resultMap.put("dataList",LineList);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取线路信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
    
    @ApiOperation("获取用户列表信息")
    @RequestMapping(value="/UserList",method=RequestMethod.POST)
    public String  queryUsers(){ 
        Map<String, Object> resultMap=new HashMap<String,Object>();
        try {
            List<Map<String, Object>> UserList= this.userService.queryUserList();  
            System.out.println("UserList");
            System.out.println(UserList);
            resultMap.put("status", "success");
            resultMap.put("dataList",UserList);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取用户信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }    
    
    @ApiOperation("获取用户信息")
    @RequestMapping(value="/queryUsersList",method=RequestMethod.POST)
    public Object  queryUsersList(@RequestBody DataTableParam[] dataTableParams){ 
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = userService.queryUsersList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("usersData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询用户信息异常!");
        }
                System.out.println("resultMap");
        System.out.println(resultMap);
        return resultMap;
    }

    @ApiOperation("获取日志列表信息")
    @RequestMapping(value="/queryLogDList",method=RequestMethod.POST)
    public Object  queryLogDList(/*HttpServletRequest request,HttpServletResponse response,*/
            @RequestBody DataTableParam[] dataTableParams){ 
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = userService.queryLogList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("stuData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询日志列表信息异常!");
        }
                System.out.println("resultMap");
        System.out.println(resultMap);
        return resultMap;
    }    
   

    @ApiOperation("获取波形文件信息")
    @RequestMapping(value="/queryWaveData",method=RequestMethod.POST)
    @ResponseBody
    public Object queryWaveData(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        Map<String, Object> resultMap=new HashMap<String,Object>();
        String recordId = (String) paramObj.get("recordId");
        Map<String, Object> usersData = new HashMap<String, Object>();
       
        usersData = userService.getWaveById(recordId);
        String fileName = (String) usersData.get("path");
        String deviceId = (String) usersData.get("device");
        int si =  (int) paramObj.get("si");
        int ei =  (int) paramObj.get("ei");
           
        String cfgDoublePath = "";
        String datDoublePath = "";
        String cfgPath = TestProperties.getProperties_1("path.properties","cfgpath");
        String cfgPath_windows = TestProperties.getProperties_1("path.properties","socketpath");
        cfgDoublePath = cfgPath+deviceId+"/"+fileName+".cfg";
        datDoublePath = cfgPath+deviceId+"/"+fileName+".dat";
        if(TestProperties.isOSLinux()){
            //cfgDoublePath = cfgPath+deviceId+"/"+fileName+".cfg";
            //datDoublePath = cfgPath+deviceId+"/"+fileName+".dat";
            cfgDoublePath = fileName+".cfg";
            datDoublePath = fileName+".dat";
        }else{
            cfgDoublePath = cfgPath_windows+fileName+".cfg";
            datDoublePath = cfgPath_windows+fileName+".dat";
        }
        try {
         // 获取cfg content params
           // Date date0=new Date();
            //System.out.println("现在的时间是0："+date0.toString()); 
            Map<String, String> mapNr = NrUtil.getInstace().ReadCFGAndGetParams(cfgDoublePath);
            // 获取Shex content
            
            //Date date00=new Date();
            //System.out.println("现在的时间是00："+date00.toString()); 
            
            String hexContent = NrUtil.getInstace().readDatFile(datDoublePath);
            
            //Date date01=new Date();
            //System.out.println("现在的时间是01："+date01.toString()); 
            
            if(hexContent==null){
                resultMap.put("status", "error");
                resultMap.put("msg", datDoublePath+"\n该录波文件不存在");
                //logger.error("[查看录波文件失败-ErrorMsg:]", "datDoublePath");
            }else{
                //List<Object> list = NrUtil.getInstace().datH2D(Integer.parseInt(mapNr.get("TT_channelNum")), hexContent,  Float.parseFloat(mapNr.get("a1")), Float.parseFloat(mapNr.get("b1")));
                //Date date=new Date(); //这个地方需要导包，如上说是：①
                //System.out.println("现在的时间是："+date.toString()); 
                List<Object> list = NrUtil.getInstace().getDatByI(Integer.parseInt(mapNr.get("TT_channelNum")), hexContent,  Float.parseFloat(mapNr.get("a1")), Float.parseFloat(mapNr.get("b1")),si,ei);
                //Date date1=new Date(); //这个地方需要导包，如上说是：①
                //System.out.println("现在的时间是："+date1.toString()); 
                if(list==null){
                    resultMap.put("status", "error");
                    resultMap.put("msg",datDoublePath+"\n读取录波文件失败\n录播文件为单通道");
                }else{
                    //int wave_ponit_num = 0;
                    //System.out.println("wave_ponit_num");
                    //System.out.println(list);
                   //if (wave_ponit_num<20000)
                   // {
                        resultMap.put("status", "success");
                        resultMap.put("msg","查看录波文件成功");
                        resultMap.put("data",mapNr);
                        resultMap.put("dataList",list);
                   // }
                   // else {
                   //     resultMap.put("status", "error");
                   //     resultMap.put("msg","波形文件采样数据量太大，总计"+String.valueOf(wave_ponit_num)+"点，请确认！");
                  //  }

                }
                
            }
            
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "查看录波文件失败");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
    
    @ApiOperation("获取故障信息")
    @RequestMapping(value="/queryFaultData",method=RequestMethod.POST)
    @ResponseBody
    public Object queryFaultData(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        Map<String, Object> resultMap=new HashMap<String,Object>();
        String recordId = (String) paramObj.get("recordId");
        Map<String, Object> usersData = new HashMap<String, Object>();
       
        usersData = userService.getFaultById(recordId);
        String fileName = (String) usersData.get("path");
        int si =  (int) paramObj.get("si");
        int ei =  (int) paramObj.get("ei");
           
        String cfgDoublePath = "";
        String datDoublePath = "";
        String cfgPath = TestProperties.getProperties_1("path.properties","cfgpath");
        String cfgPath_windows = TestProperties.getProperties_1("path.properties","socketpath");
        if(TestProperties.isOSLinux()){
            cfgDoublePath = cfgPath+fileName+".cfg";
            datDoublePath = cfgPath+fileName+".dat";
        }else{
            cfgDoublePath = cfgPath_windows+fileName+".cfg";
            datDoublePath = cfgPath_windows+fileName+".dat";
        }
        try {
         // 获取cfg content params
            Map<String, String> mapNr = NrUtil.getInstace().ReadCFGAndGetParams(cfgDoublePath);
            // 获取Shex content
            String hexContent = NrUtil.getInstace().readDatFile(datDoublePath);
            if(hexContent==null){
                resultMap.put("status", "error");
                resultMap.put("msg", datDoublePath+"\n该录波文件不存在");
                //logger.error("[查看录波文件失败-ErrorMsg:]", "datDoublePath");
            }else{
                //List<Object> list = NrUtil.getInstace().datH2D(Integer.parseInt(mapNr.get("TT_channelNum")), hexContent,  Float.parseFloat(mapNr.get("a1")), Float.parseFloat(mapNr.get("b1")));
                List<Object> list = NrUtil.getInstace().getDatByI(Integer.parseInt(mapNr.get("TT_channelNum")), hexContent,  Float.parseFloat(mapNr.get("a1")), Float.parseFloat(mapNr.get("b1")),si,ei);
                if(list==null){
                    resultMap.put("status", "error");
                    resultMap.put("msg",datDoublePath+"\n读取录波文件失败\n录播文件为单通道");
                }else{
                    resultMap.put("status", "success");
                    resultMap.put("msg","查看录波文件成功");
                    resultMap.put("data",mapNr);
                    resultMap.put("dataList",list);
                }
                
            }
            
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "查看录波文件失败");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
    
    @ApiOperation("查询用户名称是否重复")
    @RequestMapping(value="/queryUserNameIsRepeat",method=RequestMethod.POST)
    public Object  queryUserNameIsRepeat(HttpServletRequest request,HttpServletResponse response){ 
        Map<String, Object> resultMap=new HashMap<String,Object>();
        String userName = request.getParameter("userName");
        boolean flag = false;
        try {
            flag = userService.queryUserNameIsRepeat(userName);
            resultMap.put("status", "success");
            resultMap.put("flag",flag);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取用户名是否重复失败!");
        }
        return resultMap;
    }
    
    @ApiOperation("保存用户信息")
    @RequestMapping(value="/saveSchoolUser",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveSchoolUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String lines = request.getParameter("line");
        String[] lineArr = lines.split(",");
        List<String> lineslist = java.util.Arrays.asList(lineArr);
        //平台类型
        paramMap.put("userId", request.getParameter("userId"));
        paramMap.put("userName", request.getParameter("realName"));
        paramMap.put("phone", request.getParameter("userName"));     
        paramMap.put("role", request.getParameter("role"));   
        paramMap.put("status", request.getParameter("states")); 
        paramMap.put("authority", request.getParameter("authority")); 
        paramMap.put("lineslist",lineslist);
        try {
            userService.saveSchoolUser(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "用户保存成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "用户保存失败!");
        }
        return resultMap;
    }
    
    @ApiOperation("在服务器上生成DEVICE信息的Excel文件")
    @RequestMapping(value="/createDeviceExcel",method=RequestMethod.POST)
    @ResponseBody
    public Object createDeviceExcel(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String line = (String) paramObj.get("line");
        String factory = (String) paramObj.get("factory");
        List<Map<String, Object>> resList;
        List<Device> DevceList=new ArrayList<Device>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            resList = userService.getAllDeviceInfo(line,factory);
            for(int i=0;i<resList.size();i++)
            {
                Map<String, Object> devMap = resList.get(i);
                Device dev=new Device();
                //dev.setState1("11111"); 
                //dev.setCname("12344");
                Integer comm_state = (Integer)devMap.get("comm_state"); 
                if(comm_state == 0){
                  dev.setCname("在线"); 
                  } 
                else if(comm_state == 1){ 
                    dev.setCname("离线"); 
                    }
                  else{ 
                      dev.setCname("未知");
                 } 
                String lineNameString = (String)devMap.get("lineName");
                dev.setLine(lineNameString);
                String factoryNameString = (String)devMap.get("factoryName");
                dev.setFactory(factoryNameString);
                Integer indexnoInteger = (Integer)devMap.get("indexno");
                dev.setIndexno(indexnoInteger); 
                String deviceNameString = (String)devMap.get("name");
                dev.setDeviceName(deviceNameString);
                String towerNameString = (String)devMap.get("towerName");
                dev.setTower(towerNameString);
                Integer ied_phase = (Integer)devMap.get("ied_phase");
                if(ied_phase == 1){
                    dev.setPhase("A");
                }
                else if(ied_phase == 2){
                    dev.setPhase("B");
                }
                else if(ied_phase == 3){
                    dev.setPhase("C");
                }
                else{
                    dev.setPhase("未知");
                }
                Integer protocol_version = (Integer)devMap.get("protocol_version");
                if(protocol_version == 0){
                    dev.setProtocolType("2018版"); 
                }
                else if(protocol_version == 1){
                    dev.setProtocolType("2019版"); 
                }
                else{
                    dev.setProtocolType("未知");
                }

                DevceList.add(dev);
            }
            infoService.export1(DevceList);
            //Device dev111 = DevceList.get(0);
            //String comm = dev111.getCommState();
            //System.out.println("comm---"+comm);
            resultMap.put("status", "success");
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "在服务器上生成DEVICE信息的Excel文件失败!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    } 
    
    @ApiOperation("根据ID获取某条用户信息")
    @RequestMapping(value="/getUserById",method=RequestMethod.POST)
    @ResponseBody
    public Object getUserById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String userId = (String) paramObj.get("userId");
        Map<String, Object> usersData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            usersData = userService.getUserById(userId);
            System.out.println("usersData");
            System.out.println(usersData);
            resultMap.put("status", "success");
            resultMap.put("usersData", usersData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询学校用户列表信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }    
    
    @ApiOperation("删除用户")
    @RequestMapping(value="/deleteSchoolUser",method=RequestMethod.POST)
    @ResponseBody
    public Object deleteSchoolUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String userId = request.getParameter("userId");
        
        try {
            boolean flag = userService.deleteSchoolUser(userId);
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
    
    @ApiOperation("删除监管单位")
    @RequestMapping(value="/deleteRegulator",method=RequestMethod.POST)
    @ResponseBody
    public Object deleteRegulator(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String userId = request.getParameter("userId");
        
        try {
            boolean flag = userService.deleteRegulator(userId);
            if(flag){
                resultMap.put("status", "success");
                resultMap.put("msg", "删除成功!");
            }else{
                resultMap.put("status", "error");
                resultMap.put("msg", "删除失败,该监管单位已被使用!");
            }
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "删除失败!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }    
    
    @ApiOperation("保存波形访问密码")
    @RequestMapping(value="/saveWaveWord",method=RequestMethod.POST)
    @ResponseBody
    public String saveWaveWord(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", paramObj.get("id"));              
        paramMap.put("password", paramObj.get("password"));
        System.out.println(paramMap);
        try {
            userService.saveWaveWord(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "密码保存成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "密码保存失败!");
        }
        
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
    
    @ApiOperation("保存装置访问密码")
    @RequestMapping(value="/saveDevicePassword",method=RequestMethod.POST)
    @ResponseBody
    public String saveDevicePassword(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        
        String id = (String) paramObj.get("id");
        String password_old = (String) paramObj.get("password_old");      
        String password_old_db = userService.getPassByDevId(id);
        if (!password_old.equals(password_old_db))
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "您输入的原密码不正确!");
            JSONObject jsonObject = JSONObject.fromObject(resultMap);
            String enResult = AesUtil.enCodeByKey(jsonObject.toString());
            return enResult;
        }
        
        paramMap.put("password", paramObj.get("password"));
        System.out.println(paramMap);
        try {
            userService.saveDevicePass(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "密码保存成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "密码保存失败!");
        }
        
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
    
    @ApiOperation("重置密码")
    @RequestMapping(value="/resetPassword",method=RequestMethod.POST)
    @ResponseBody
    public String resetPassword(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", paramObj.get("id"));              
        try {
            userService.resetPassword(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "重置密码成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "重置密码失败!");
        }
        
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
    
    @ApiOperation("根据ID获取某条监管单位信息")
    @RequestMapping(value="/getRegulatorById",method=RequestMethod.POST)
    @ResponseBody
    public Object getRegulatorById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String regulatorId = (String) paramObj.get("regulatorId");
        Map<String, Object> usersData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            usersData = userService.getRegulatorById(regulatorId);
            resultMap.put("status", "success");
            resultMap.put("usersData", usersData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询监管单位列表信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    } 
    
    @ApiOperation("导出Excel")
    @RequestMapping(value="/export",method=RequestMethod.GET)
    public void exportStu(HttpServletResponse response){
        //设置默认的下载文件名
        String name = "装置信息表.xlsx";
        try {
            //避免文件名中文乱码，将UTF8打散重组成ISO-8859-1编码方式
            name = new String (name.getBytes("UTF8"),"ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //设置响应头的类型
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        //让浏览器下载文件,name是上述默认文件下载名
        response.addHeader("Content-Disposition","attachment;filename=\"" + name + "\"");
        InputStream inputStream=null;
        OutputStream outputStream=null;
        //在service层中已经将数据存成了excel临时文件，并返回了临时文件的路径
        //String downloadPath = userServiceI.exportStu();
        String downloadPath = "D://装置详细信息.xlsx";
        //根据临时文件的路径创建File对象，FileInputStream读取时需要使用
        File file = new File(downloadPath);
        try {
            //通过FileInputStream读临时文件，ServletOutputStream将临时文件写给浏览器
            inputStream = new FileInputStream(file);
            outputStream = response.getOutputStream();
            int len = -1;
            byte[] b = new byte[1024];
            while((len = inputStream.read(b)) != -1){
                outputStream.write(b);
            }
            //刷新
            outputStream.flush();
         } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭输入输出流
            try {
                if(inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //最后才能，删除临时文件，如果流在使用临时文件，file.delete()是删除不了的
        file.delete();
    }
}

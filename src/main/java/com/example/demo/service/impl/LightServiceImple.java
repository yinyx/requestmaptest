package com.example.demo.service.impl;

import com.example.demo.mapper.FaultMapper;
import com.example.demo.mapper.InfoMapper;
import com.example.demo.service.LightService;
import com.example.demo.mapper.LightMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONString;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.pentaho.di.trans.steps.webservices.wsdl.Wsdl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.io.*;

import springfox.documentation.spring.web.json.Json;
import util.aes.TestProperties;

import javax.swing.text.StyledEditorKit;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;


@Service
@Transactional
public class LightServiceImple implements LightService{

    protected Logger logger = LogManager.getLogger(getClass());
    @Value("${aUser}")
    private String aUser;
    @Value("${aPwd}")
    private String aPwd;
    @Value("${aComId}")
    private String aComId;
    @Value("${wsdlPath}")
    private String wsdlPath;
    @Value("${nameSpace}")
    private String nameSpace;

    @Autowired
    private LightMapper lightMapper;

    @Autowired
    private FaultMapper faultMapper;

    @Autowired
    private InfoMapper infoMapper;

    //获取基础数据更新信息
    @Override
    public String GetTableUpdateTime(int aTableID){
        logger.info("=== === 获取基础数据更新信息 wsdl解析报文 ---- ");
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        //对方的wsdl地址
        Client client = dcf.createClient(wsdlPath);
        String rtn = null;
        try {
            QName qName = new QName(nameSpace, "GetTableUpdateTime");
            Object[] jsonArray = client.invoke(qName, aUser,aPwd,aComId,aTableID); //参数1，参数2，参数3......按顺序放就看可以
            String temp = Arrays.toString(jsonArray);
//            System.out.println("GetTableUpdateTime ===>"+temp);
            String[] lines = temp.split("\\r?\\n");
            if(lines.length >=2) {
                String[] datas = new String[lines.length - 1];
                System.arraycopy(lines, 1, datas, 0, datas.length);
                int var6 = datas.length;
//                System.out.println("GetTableUpdateTime Length ===>"+var6);
                for (int i = 0; i < var6; ++i) {
                    String data = datas[i];
                    String[] items = data.trim().split("\t");
                    if (items.length == 3) {
                        rtn = items[2];
                    }
                }
            }
            logger.info("返回数据:" + rtn);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("服务器断开连接，请稍后再试");
        }
        return rtn;
    }

    //获取系统线路信息
    @Override
    public List<Map<String, Object>> GetMLineList(){
        logger.info("=== === 获取系统线路信息 wsdl解析报文 ---- ");
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        //对方的wsdl地址
        Client client = dcf.createClient(wsdlPath);
        List<Map<String, Object>> lineList = new ArrayList<>();
        int count =0;
        try {
            QName qName = new QName(nameSpace, "GetMLineList");
            Object[] jsonArray = client.invoke(qName, aUser,aPwd,""); //参数1，参数2，参数3......按顺序放就看可以
            String temp = Arrays.toString(jsonArray);
//            System.out.println("GetMLineList ===>"+temp);
            String[] lines = temp.split("\\r?\\n");
            count += (lines.length-1);
            if(lines.length >=2) {
                String[] datas = new String[lines.length - 1];
                System.arraycopy(lines, 1, datas, 0, datas.length);
                int var6 = datas.length;

                for (int i = 0; i < var6; ++i) {
                    String data = datas[i];
                    String[] items = data.trim().split("\t");
                    if(items.length == 5 ){
                        Map<String, Object> lineMap = new HashMap<>();
                        lineMap.put("MLineID",items[0]);
                        lineMap.put("MLName",items[1]);
                        lineMap.put("Volevel",items[2]);
                        lineMap.put("Station1",items[3]);
                        lineMap.put("Station2",items[4]);
                        lineList.add(lineMap);
                    }
                }
            }
            logger.info("获取系统线路信息:" + count+ "条记录");
//            logger.info("返回数据:" + lineList.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("服务器断开连接，请稍后再试");
        }
        return lineList;
//        lineMap1.put("MLineID",1);
//        lineMap1.put("MLName","tttyyyytt");
//        lineMap1.put("Volevel",5);
//        lineMap1.put("Station1",6);
//        lineMap1.put("Station2",7);
//        lineList.add(lineMap1);

//        Map<String, Object> lineMap2 = new HashMap<>();
//        lineMap2.put("MLineID",1955);
//        lineMap2.put("MLName","tttxxxtt");
//        lineMap2.put("Volevel",2);
//        lineMap2.put("Station1",3);
//        lineMap2.put("Station2",4);
//        lineList.add(lineMap2);
    }

    //获取线路杆塔信息
    @Override
    public List<Map<String, Object>> GetElePoList (){
        logger.info("=== === 获取线路杆塔信息 wsdl解析报文 ---- ");
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        //对方的wsdl地址
        Client client = dcf.createClient(wsdlPath);
        List<Map<String, Object>> poList = new ArrayList<>();
        int count = 0;
        try {
            QName qName = new QName(nameSpace, "GetElePoList");
            List<Map<String, Object>> lineList = infoMapper.queryLine();
            for (Map<String,Object> lineMap:lineList
                 ) {
                String lineId = (String) lineMap.get("id");
            Object[] jsonArray = client.invoke(qName, aUser,aPwd,lineId); //参数1，参数2，参数3......按顺序放就看可以
            String temp = Arrays.toString(jsonArray);
//            System.out.println("GetElePoList ===>"+ temp);
            String[] lines = temp.split("\\r?\\n");
            count += (lines.length-1);
            if(lines.length >=2) {
                String[] datas = new String[lines.length - 1];
                System.arraycopy(lines, 1, datas, 0, datas.length);
                int var6 = datas.length;
                    for (int i = 0; i < var6; ++i) {
                        String data = datas[i];
                        String[] items = data.trim().split("\t");
//                        System.out.println("获取线路杆塔信息 items length" + items.length);
                        if(items.length == 4) {
                            Map<String, Object> poMap = new HashMap<>();
                            String lineName = faultMapper.getLineById(items[0]);
                            poMap.put("ElePoID", items[1]);
                            poMap.put("Name", lineName + "-" + (i+1)+"号杆塔");
//                            logger.info("******* 杆塔："+poMap.get("Name") + "*******");
                            poMap.put("Line", lineName);
                            poMap.put("Serial", items[2]);
                            poMap.put("LineLen", items[3]);
                            poList.add(poMap);
                        }
                    }
                }
            }
            logger.info("获取线路杆塔信息:" + count+ "条记录");
//            logger.info("返回数据:" + poList.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("服务器断开连接，请稍后再试");
        }
        return poList;
//        Map<String, Object> poMap1 = new HashMap<>();

        //获取杆塔数据时需要按线路分别获取，最后再拼接在一起
//        Map<String, Object> poMap1 = new HashMap<>();
//        poMap1.put("ElePoID","tower1");
//        poMap1.put("Name","line1-3号杆塔");
//        poMap1.put("Line","line1");
//        poMap1.put("Serial",1);
//        poMap1.put("LineLen",10);
//        poList.add(poMap1);

//        Map<String, Object> poMap2 = new HashMap<>();
//        poMap2.put("ElePoID","tower2");
//        poMap2.put("Name","line1-2号杆塔");
//        poMap2.put("Line","line1");
//        poMap2.put("Serial",2);
//        poMap2.put("LineLen",12.1);
//        poList.add(poMap2);

    }

    //获取设备安装信息
    @Override
    public List<Map<String, Object>> GetDTUList (){
        logger.info("=== === 获取设备安装信息 wsdl解析报文 ---- ");
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        //对方的wsdl地址
        Client client = dcf.createClient(wsdlPath);
        List<Map<String, Object>> dtuList = new ArrayList<>();
        int count = 0;
        try {
            QName qName = new QName(nameSpace, "GetDTUList");
            List<Map<String, Object>> lineList = infoMapper.queryLine();
            for (Map<String,Object> lineMap:lineList
            ) {
                String lineId = (String) lineMap.get("id");
                Object[] jsonArray = client.invoke(qName, aUser, aPwd, aComId, lineId); //参数1，参数2，参数3......按顺序放就看可以
                String temp = Arrays.toString(jsonArray);
//                System.out.println("GetDTUList ===>"+ temp);
                String[] lines = temp.split("\\r?\\n");
                count += (lines.length-1);
                if (lines.length >= 2) {
                    String[] datas = new String[lines.length - 1];
                    System.arraycopy(lines, 1, datas, 0, datas.length);
                    int var6 = datas.length;

                    for (int i = 0; i < var6; ++i) {
                        String data = datas[i];
                        String[] items = data.trim().split("\t");
                        if(items.length == 4){
                            Map<String, Object> dtuMap = new HashMap<>();
                            String lineName = faultMapper.getLineById(items[1]);
                            dtuMap.put("DTUID", items[0]);
                            dtuMap.put("Name", lineName + "-" + "装置"+(i+1));
//                            logger.info("****** 设备信息 DTUID"+ dtuMap.get("Name") + "******" );
                            dtuMap.put("MLineID", items[1]);
                            dtuMap.put("ElePoID", items[2]);
                            dtuMap.put("SLineID", items[3]);
                            dtuList.add(dtuMap);
                        }
                    }
                }
            }
            logger.info("获取设备安装信息:" + count+ "条记录");
//            logger.info("返回数据:" + dtuList.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("服务器断开连接，请稍后再试");
        }
        return dtuList;
//        Map<String, Object> dtuMap = new HashMap<>();


        //获取设备数据时需要按线路分别获取，最后再拼接在一起

//        Map<String, Object> dtuMap1 = new HashMap<>();
//        dtuMap1.put("DTUID","dtu1");
//        dtuMap1.put("Name","line1-3号装置1");
//        dtuMap1.put("MLineID","line1");
//        dtuMap1.put("ElePoID","tower1");
//        dtuMap1.put("SLineID",1);
//        dtuList.add(dtuMap1);
//
//        Map<String, Object> dtuMap2 = new HashMap<>();
//        dtuMap2.put("DTUID","dtu2");
//        dtuMap2.put("Name","line1-2号装置2");
//        dtuMap2.put("MLineID","line1");
//        dtuMap2.put("ElePoID","tower2");
//        dtuMap2.put("SLineID",2);
//        dtuList.add(dtuMap2);
    }

    //获取监管单位信息
    @Override
    public List<Map<String, Object>> GetEleOffList(){
        logger.info("=== === 获取监管单位信息 wsdl解析报文 ---- ");

        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        //对方的wsdl地址
        Client client = dcf.createClient(wsdlPath);
        List<Map<String, Object>> officeList = new ArrayList<>();
        int count = 0;
        try {
            QName qName = new QName(nameSpace, "GetEleOffList");
            Object[] jsonArray = client.invoke(qName, aUser,aPwd,aComId); //参数1，参数2，参数3......按顺序放就看可以
            String temp = Arrays.toString(jsonArray);
//            System.out.println("GetEleOffList ===>"+ temp);
            String[] lines = temp.split("\\r?\\n");
            count += (lines.length-1);
            if(lines.length >=2) {
                String[] datas = new String[lines.length - 1];
                System.arraycopy(lines, 1, datas, 0, datas.length);
                int var6 = datas.length;
                for (int i = 0; i < var6; ++i) {
                    Map<String, Object> officeMap = new HashMap<>();
                    String data = datas[i];
                    String[] items = data.trim().split("\t");
                    if(items.length == 3){
                        officeMap.put("EleOffID",items[0]);
                        officeMap.put("EName",items[1]);
                        officeMap.put("PEleOffID",items[2]);
                        officeList.add(officeMap);
                    }
                }
            }
            logger.info("获取监管单位信息:" + count+ "条记录");
//            logger.info("返回数据:" + officeList.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("服务器断开连接，请稍后再试");
        }
        return officeList;
//        Map<String, Object> officeMap1 = new HashMap<>();
//        officeMap1.put("EleOffID","office1");
//        officeMap1.put("EName","1监管单位1");
//        officeMap1.put("PEleOffID","office0");
//        officeList.add(officeMap1);
//
//        Map<String, Object> officeMap2 = new HashMap<>();
//        officeMap2.put("EleOffID","office2");
//        officeMap2.put("EName","2监管单位2");
//        officeMap2.put("PEleOffID","office0");
//        officeList.add(officeMap2);
    }

    @Override
    public String getLocalLastUpdateTimeById(int id){
        String time = lightMapper.getLocalLastUpdateTimeById(id);
        return time;
    }

    @Override
    public void setLocalLastUpdateTimeById(int id, String updateTime){
        try{
            lightMapper.setLocalLastUpdateTimeById(id, updateTime);
        }catch (Exception e){
            logger.error("setLocalLastUpdateTimeById fail."+e.toString());
        }
    }

    //同步线路信息
    @Override
    public void SyncLine(List<Map<String, Object>> lineList){
        try{
            lightMapper.syncLine(lineList);
        }catch (Exception e){
            logger.error("SyncLine fail."+e.toString());
        }
    }

    //同步杆塔信息
    @Override
    public void SyncTower(List<Map<String, Object>> towerList){
        try{
            lightMapper.syncTower(towerList);
        }catch (Exception e){
            logger.error("syncTower fail."+e.toString());
        }
    }

    //同步装置信息
    @Override
    public void SyncDtu(List<Map<String, Object>> dtuList){
        try{
            lightMapper.syncDtu(dtuList);
        }catch (Exception e){
            logger.error("syncDtu fail."+e.toString());
        }
    }

    //同步监管单位信息
    @Override
    public void SyncOffice(List<Map<String, Object>> officeList){
        try{
            lightMapper.syncOffice(officeList);
        }catch (Exception e){
            logger.error("syncOffice fail."+e.toString());
        }
    }

    //同步线路，杆塔配置信息
    @Override
    public void SyncLineAndTower(){
        boolean callFlag = false;
        try {
            String cmd = TestProperties.getProperties_1("path.properties","syncpath");
            final Process p = Runtime.getRuntime().exec(cmd);
            try{
                String oldProcess ="";
                String newProcess="";
                Scanner in;
                List<String> processList = new ArrayList<>();
                try {
                    if(TestProperties.isOSLinux()){
                        in = new Scanner(new File("ConfigSyncRet.txt"), "utf-8");
                    }else{
                        in = new Scanner(new File("ConfigSyncRet.txt"), "gbk");
                    }

                    while (in.hasNextLine()) {
                        String proceeContnt = in.nextLine();
                        processList.add(proceeContnt);
                    }

                    if (processList.size()==2)
                    {
                        oldProcess = processList.get(0);
                        newProcess = processList.get(1);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if ((!newProcess.equals(oldProcess))&&(!newProcess.equals("")))
                {
                    callFlag =true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally{
                System.out.print(p.exitValue());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            String err = e.getMessage();
            if (err.equals("process hasn't exited"))
            {
                if (callFlag)
                {
                    logger.warn("同步配置进程调用成功！");
                }
                else
                {
                    logger.error("同步配置进程调用是败！");
                }
            }
            else
            {
                logger.error("同步杆塔，线路失败！");
            }
        }
    }

    //同步装置配置信息
    @Override
    public void SyncDevice(){
        try {
            String cmd = TestProperties.getProperties_1("path.properties","jobpath");
            final Process p = Runtime.getRuntime().exec(cmd);
                String str ="";
                Scanner in;
                try {
                    if(TestProperties.isOSLinux()){
                        in = new Scanner(new File("result.txt"), "utf-8");
                    }else{
                        in = new Scanner(new File("result.txt"), "gbk");
                    }
                    while (in.hasNextLine()) {
                        str = in.nextLine();
                    }
                    System.out.println(str);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (str.equals("job run successfully!"))
                {
                    logger.warn("同步异构数据成功！");
                }
                else if (str.equals("job run Failure!"))
                {
                    logger.error("同步异构数据失败！");
                }
                else
                {
                    logger.error("调用同步异构数据进程失败！");
                }
        } catch (Exception e) {
            logger.error("调用同步异构数据进程失败！");
        }
    }

    //重启通信服务
    @Override
    public  void RestartCommServer(){

    }

}


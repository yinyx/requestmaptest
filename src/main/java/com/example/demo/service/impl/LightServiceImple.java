package com.example.demo.service.impl;

import com.example.demo.service.LightService;
import com.example.demo.mapper.LightMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.*;

import util.aes.TestProperties;


@Service
@Transactional
public class LightServiceImple implements LightService{

    protected Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private LightMapper lightMapper;

    //获取基础数据更新信息
    @Override
    public String GetTableUpdateTime(int aTableID){
        return "111";
    }

    //获取系统线路信息
    @Override
    public List<Map<String, Object>> GetMLineList(){
        List<Map<String, Object>> lineList = new ArrayList<>();
        Map<String, Object> lineMap1 = new HashMap<>();
        lineMap1.put("MLineID",1);
        lineMap1.put("MLName","tttyyyytt");
        lineMap1.put("Volevel",5);
        lineMap1.put("Station1",6);
        lineMap1.put("Station2",7);
        lineList.add(lineMap1);

        Map<String, Object> lineMap2 = new HashMap<>();
        lineMap2.put("MLineID",1955);
        lineMap2.put("MLName","tttxxxtt");
        lineMap2.put("Volevel",2);
        lineMap2.put("Station1",3);
        lineMap2.put("Station2",4);
        lineList.add(lineMap2);
        return lineList;
    }

    //获取线路杆塔信息
    @Override
    public List<Map<String, Object>> GetElePoList (){
        //获取杆塔数据时需要按线路分别获取，最后再拼接在一起
        List<Map<String, Object>> poList = new ArrayList<>();
        Map<String, Object> poMap1 = new HashMap<>();
        poMap1.put("ElePoID","tower1");
        poMap1.put("Name","line1-3号杆塔");
        poMap1.put("Line","line1");
        poMap1.put("Serial",1);
        poMap1.put("LineLen",10);
        poList.add(poMap1);

        Map<String, Object> poMap2 = new HashMap<>();
        poMap2.put("ElePoID","tower2");
        poMap2.put("Name","line1-2号杆塔");
        poMap2.put("Line","line1");
        poMap2.put("Serial",2);
        poMap2.put("LineLen",12.1);
        poList.add(poMap2);
        return poList;
    }

    //获取设备安装信息
    @Override
    public List<Map<String, Object>> GetDTUList (){
        //获取设备数据时需要按线路分别获取，最后再拼接在一起
        List<Map<String, Object>> dtuList = new ArrayList<>();
        Map<String, Object> dtuMap1 = new HashMap<>();
        dtuMap1.put("DTUID","dtu1");
        dtuMap1.put("Name","line1-3号装置1");
        dtuMap1.put("MLineID","line1");
        dtuMap1.put("ElePoID","tower1");
        dtuMap1.put("SLineID",1);
        dtuList.add(dtuMap1);

        Map<String, Object> dtuMap2 = new HashMap<>();
        dtuMap2.put("DTUID","dtu2");
        dtuMap2.put("Name","line1-2号装置2");
        dtuMap2.put("MLineID","line1");
        dtuMap2.put("ElePoID","tower2");
        dtuMap2.put("SLineID",2);
        dtuList.add(dtuMap2);
        return dtuList;
    }

    //获取监管单位信息
    @Override
    public List<Map<String, Object>> GetEleOffList(){
        List<Map<String, Object>> officeList = new ArrayList<>();
        Map<String, Object> officeMap1 = new HashMap<>();
        officeMap1.put("EleOffID","office1");
        officeMap1.put("EName","1监管单位1");
        officeMap1.put("PEleOffID","office0");
        officeList.add(officeMap1);

        Map<String, Object> officeMap2 = new HashMap<>();
        officeMap2.put("EleOffID","office2");
        officeMap2.put("EName","2监管单位2");
        officeMap2.put("PEleOffID","office0");
        officeList.add(officeMap2);
        return officeList;
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


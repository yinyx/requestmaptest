package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.example.demo.service.LightService;
import util.aes.StringUtils;

@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SyncTask {
    boolean bLineFlag = false;
    boolean bTowerFlag = false;
    boolean bDtuFlag = false;
    boolean bOfficeFlag = false;

    protected  Logger logger = LogManager.getLogger(getClass());

    @Resource
    private LightService lightService;

    @Scheduled(fixedRate=2000)
    public void configureTasks() {
            CheckLine();
            CheckTower();
            CheckDevice();
            CheckOffice();
            if (bLineFlag||bTowerFlag||bDtuFlag)
            {
                //重启通信服务
                lightService.RestartCommServer();
            }
            if (bLineFlag||bTowerFlag)
            {
                //重启配置同步服务
                lightService.SyncLineAndTower();
            }
            if (bDtuFlag)
            {
                //重启异构装置数据同步服务
                lightService.SyncDevice();
            }
            resetFlags();
    }

    public void resetFlags(){
         bLineFlag = false;
         bTowerFlag = false;
         bDtuFlag = false;
         bOfficeFlag = false;
    }

    public void CheckLine(){
        //获取武汉线路数据表最新更新时间
        String sRemoteLastUpdateTime = lightService.GetTableUpdateTime(2);
        if (StringUtils.isNotEmpty(sRemoteLastUpdateTime))
        {
            //获取本地线路数据表最新更新时间
            String sLocalLastUpdateTime = lightService.getLocalLastUpdateTimeById(2);
            if ((StringUtils.isEmpty(sLocalLastUpdateTime))||(!sLocalLastUpdateTime.equals(sRemoteLastUpdateTime)))
            {

                List<Map<String, Object>> lineList = lightService.GetMLineList();
                lightService.SyncLine(lineList);
                logger.warn("执行同步线路任务时间: " + LocalDateTime.now());
                lightService.setLocalLastUpdateTimeById(2, sRemoteLastUpdateTime);
                bLineFlag = true;
            }
        }
    }

    public void CheckTower(){
        //获取武汉杆塔数据表最新更新时间
        String sRemoteLastUpdateTime = lightService.GetTableUpdateTime(3);
        if (StringUtils.isNotEmpty(sRemoteLastUpdateTime))
        {
            //获取本地线路数据表最新更新时间
            String sLocalLastUpdateTime = lightService.getLocalLastUpdateTimeById(3);
            if ((StringUtils.isEmpty(sLocalLastUpdateTime))||(!sLocalLastUpdateTime.equals(sRemoteLastUpdateTime)))
            {

                List<Map<String, Object>> towerList = lightService.GetElePoList();
                lightService.SyncTower(towerList);
                logger.warn("执行同步杆塔任务时间: " + LocalDateTime.now());
                lightService.setLocalLastUpdateTimeById(3, sRemoteLastUpdateTime);
                bTowerFlag = true;
            }
        }
    }

    public void CheckDevice(){
        //获取武汉终端数据表最新更新时间
        String sRemoteLastUpdateTime = lightService.GetTableUpdateTime(4);
        if (StringUtils.isNotEmpty(sRemoteLastUpdateTime))
        {
            //获取本地线路数据表最新更新时间
            String sLocalLastUpdateTime = lightService.getLocalLastUpdateTimeById(4);
            if ((StringUtils.isEmpty(sLocalLastUpdateTime))||(!sLocalLastUpdateTime.equals(sRemoteLastUpdateTime)))
            {

                List<Map<String, Object>> dtuList = lightService.GetDTUList();
                lightService.SyncDtu(dtuList);
                logger.warn("执行同步装置任务时间: " + LocalDateTime.now());
                lightService.setLocalLastUpdateTimeById(4, sRemoteLastUpdateTime);
                bDtuFlag = true;
            }
        }
    }

    public void CheckOffice(){
        //获取武汉监管单位数据表最新更新时间
        String sRemoteLastUpdateTime = lightService.GetTableUpdateTime(1);
        if (StringUtils.isNotEmpty(sRemoteLastUpdateTime))
        {
            //获取本地线路数据表最新更新时间
            String sLocalLastUpdateTime = lightService.getLocalLastUpdateTimeById(1);
            if ((StringUtils.isEmpty(sLocalLastUpdateTime))||(!sLocalLastUpdateTime.equals(sRemoteLastUpdateTime)))
            {

                List<Map<String, Object>> officeList = lightService.GetEleOffList();
                lightService.SyncOffice(officeList);
                logger.warn("执行同步监管单位任务时间: " + LocalDateTime.now());
                lightService.setLocalLastUpdateTimeById(1, sRemoteLastUpdateTime);
                bOfficeFlag = true;
            }
        }
    }
}

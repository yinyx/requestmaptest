package com.example.demo.service;

import java.util.List;
import java.util.Map;

public interface LightService {
    //获取基础数据更新信息
    String GetTableUpdateTime(int aTableID);

    //获取系统线路信息
    List<Map<String, Object>> GetMLineList();

    //获取线路杆塔信息
    List<Map<String, Object>> GetElePoList ();

    //获取设备安装信息
    List<Map<String, Object>> GetDTUList ();

    //获取监管单位信息
    List<Map<String, Object>> GetEleOffList();

    //获取本地表更新时间
    String getLocalLastUpdateTimeById(int id);

    //设置本地表更新时间
    void setLocalLastUpdateTimeById(int id, String updateTime);

    //同步线路信息
    void SyncLine(List<Map<String, Object>> lineList);

    //同步杆塔信息
    void SyncTower(List<Map<String, Object>> towerList);

    //同步装置信息
    void SyncDtu(List<Map<String, Object>> dtuList);

    //同步监管单位信息
    void SyncOffice(List<Map<String, Object>> officeList);

    //同步线路，杆塔配置信息
    void SyncLineAndTower();

    //同步装置配置信息
    void SyncDevice();

    //重启通信服务
    void RestartCommServer();
}

package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface InfoMapper {
    @Select("SELECT t1.*,GROUP_CONCAT(t2.`name`) AS \"parentName\" FROM info_regulator t1,info_regulator t2 WHERE t1.parent = t2.id GROUP BY t1.id limit #{start},#{length}")
    List<Map<String, Object>> queryRegulatorList(@Param("start") Integer start, @Param("length") Integer length);
    
    @Select("SELECT t1.*,GROUP_CONCAT(t2.`name`) AS \"parentName\" FROM info_regulator t1,info_regulator t2 WHERE t1.parent = t2.id and t1.name = #{QueryType} GROUP BY t1.id limit #{start},#{length}")
    //@Select("SELECT* FROM info_regulator WHERE name = #{QueryType} limit #{start},#{length}")
    List<Map<String, Object>> queryRegulatorListByCondition(@Param("start") Integer start, @Param("length") Integer length, @Param("QueryType") String QueryType);
    
    @Select("SELECT COUNT(1) FROM info_regulator")
    int queryRegulatorListCount();
    
    @Select("SELECT COUNT(1) FROM info_regulator WHERE name = #{QueryType}")
    int queryRegulatorListCountByCondition(@Param("QueryType") String QueryType);
   
    @Select("SELECT* FROM info_regulator")
    List<Map<String, Object>> queryRegulator();
    
    @Select("SELECT* FROM info_line")
    List<Map<String, Object>> queryLine();
    
    @Select("SELECT line FROM line_user where user=#{userId}")
    List<String> queryLineListByUser(String userId);
    
    @Select("SELECT id FROM info_line where (regulator=#{regulatorId} or #{regulatorId}='') and (voltage_level=#{voltageId} or #{voltageId}='') and (id in (SELECT line FROM line_user where user=#{userId}))")
    List<String> queryLineByMultiCondition(@Param("userId") String userId, @Param("regulatorId") String regulatorId, @Param("voltageId") String voltageId);
    
    @Select("SELECT* FROM info_tower")
    List<Map<String, Object>> queryTower();
    
    @Select("SELECT* FROM info_tower where line=#{lineId}")
    List<Map<String, Object>> queryTowerByLineId(String lineId);
    
    @Select("SELECT* FROM info_factory")
    List<Map<String, Object>> queryFactory();
    
    @Select("SELECT* FROM info_station")
    List<Map<String, Object>> queryStation();
    
    @Select("SELECT* FROM dev_alarm")
    List<Map<String, Object>> queryDeviceFaultDetails(String deviceId);
    
    @Insert("INSERT info_regulator (id,num,name,parent,address) VALUES (#{regulatorId},#{num},#{name},#{parent},#{address})")
    void addRegulator(Map<String, Object> paramMap);
    
    @Update("UPDATE info_regulator SET num=#{num},name=#{name},parent=#{parent},address=#{address} WHERE id = #{regulatorId}")
    void updateRegulator(Map<String, Object> paramMap);
    
    @Insert("INSERT info_factory (id,num,name,contact,call1,login_name,login_password,uuid,sample_Rate) VALUES (#{regulatorId},#{num},#{name},#{contact},#{call},#{login_name},#{login_password},#{regulatorId},#{sample_Rate})")
    void addFactory(@Param("regulatorId") String regulatorId,@Param("num") String num,@Param("name") String name,
            @Param("contact") String contact,@Param("call") String call,@Param("login_name") String login_name,
            @Param("login_password") String login_password/*,@Param("uuid") String uuid*/,@Param("sample_Rate") String sample_Rate);
    
    @Update("UPDATE info_factory SET num=#{num},name=#{name},contact=#{contact},call1=#{call},login_name=#{login_name},login_password=#{login_password},sample_Rate=#{sample_Rate} WHERE id = #{regulatorId}")
    void updateFactory(@Param("regulatorId") String regulatorId,@Param("num") String num,@Param("name") String name,
            @Param("contact") String contact,@Param("call") String call,@Param("login_name") String login_name,
            @Param("login_password") String login_password,@Param("sample_Rate") String sample_Rate);
    
    @Insert("INSERT info_tower (id,name,indexno,line,distance,longitude,latitude,altitude,tower_to_m,tower_to_n) VALUES (#{recordId},#{name},#{index},#{line},#{distance},#{longitude},#{latitude},#{altitude},#{tower_to_m},#{tower_to_n})")
    void addTower(Map<String, Object> paramMap);
    
    @Update("UPDATE info_tower SET name=#{name},indexno=#{index},line=#{line},distance=#{distance},longitude=#{longitude},latitude=#{latitude},altitude=#{altitude},tower_to_m=#{tower_to_m},tower_to_n=#{tower_to_n} WHERE id = #{recordId}")
    void updateTower(Map<String, Object> paramMap);
    
    @Insert("INSERT info_station (id,num,name,address,remark) VALUES (#{regulatorId},#{num},#{name},#{address},#{remark})")
    void addStation(Map<String, Object> paramMap);
    
    @Update("UPDATE info_station SET num=#{num},name=#{name},address=#{address},remark=#{remark} WHERE id = #{regulatorId}")
    void updateStation(Map<String, Object> paramMap);
    
    @Insert("INSERT info_device (id,device,indexno,name,line_name,manufacture,manu_date,ied_type,version,tower_id,install_time,ied_phase,longitude,latitude,altitude,remark,protocol_version) VALUES (#{recordId},#{recordId},#{InstallIndex},#{name},#{line},#{factory},#{ManuDate},#{IedType},#{version},#{tower},#{InstallTime},#{phase},#{longitude},#{latitude},#{altitude},#{deviceIp},#{ProtocalType})")
    //@Insert("INSERT info_device (id,indexno,name,line_name,manufacture,manu_date,ied_type,version,tower_id,install_time,ied_phase,longitude,latitude,altitude,maxCoefficient,minCoefficient,PowerFrequencyCoefficient,remark) VALUES (#{recordId}, '1', 'a', '2d1cdb9c8b6740bdb8f42dec19ef1e81', '厂商1', 'a', 'b', 'c', '503b0d8e967e45e584d7bdc233dca56c', 'd', '1', 'e', 'f', 'g', 'h', 'i', 'j', 'k')")
    void addDevice(@Param("recordId") String recordId, @Param("deviceIp") String deviceIp, @Param("InstallIndex") int InstallIndex,@Param("name") String name,
            @Param("line") String line,@Param("factory") String factory,@Param("ManuDate") String ManuDate,
            @Param("IedType") String IedType,@Param("version") String version,@Param("tower") String tower,
            @Param("InstallTime") String InstallTime,@Param("phase") int phase,@Param("longitude") String longitude,
            @Param("latitude") String latitude,@Param("altitude") String altitude,@Param("ProtocalType") int ProtocalType);
    
    @Update("UPDATE info_device SET name=#{name},indexno=#{InstallIndex},line_name=#{line},manufacture=#{factory},manu_date=#{ManuDate},ied_type=#{IedType},version=#{version},tower_id=#{tower},install_time=#{InstallTime},ied_phase=#{phase},longitude=#{longitude},latitude=#{latitude},altitude=#{altitude},remark=#{deviceIp},protocol_version=#{ProtocalType} WHERE id = #{recordId}")
    void updateDevice(@Param("recordId") String recordId,@Param("deviceIp") String deviceIp,@Param("InstallIndex") int InstallIndex,@Param("name") String name,
            @Param("line") String line,@Param("factory") String factory,@Param("ManuDate") String ManuDate,
            @Param("IedType") String IedType,@Param("version") String version,@Param("tower") String tower,
            @Param("InstallTime") String InstallTime,@Param("phase") int phase,@Param("longitude") String longitude,
            @Param("latitude") String latitude,@Param("altitude") String altitude,@Param("ProtocalType") int ProtocalType);
    
    //@Select("SELECT t1.*,GROUP_CONCAT(t2.`name`) AS \"regulatorName\",GROUP_CONCAT(t3.`name`) AS \"leftStationName\",GROUP_CONCAT(t4.`name`) AS \"rightStationName\"   FROM info_line t1,info_regulator t2,info_station t3,info_station t4 WHERE t1.regulator = t2.id and t1.left_station =t3.id and t1.right_station=t4.id GROUP BY t1.id limit #{start},#{length}")
    @Select("SELECT t1.*,GROUP_CONCAT(t2.`name`) AS \"regulatorName\" FROM info_line t1,info_regulator t2 WHERE t1.regulator = t2.id GROUP BY t1.id limit #{start},#{length}")
    List<Map<String, Object>> queryLineList(@Param("start") Integer start, @Param("length") Integer length);
    
    //@Select("SELECT t1.*,GROUP_CONCAT(t2.`name`) AS \"regulatorName\",GROUP_CONCAT(t3.`name`) AS \"leftStationName\" ,GROUP_CONCAT(t4.`name`) AS \"rightStationName\"  FROM info_line t1,info_regulator t2,info_station t3,info_station t4 WHERE t1.regulator = t2.id and t1.name = #{QueryType} and t1.left_station =t3.id and t1.right_station=t4.id GROUP BY t1.id limit #{start},#{length}")
    @Select("SELECT t1.*,GROUP_CONCAT(t2.`name`) AS \"regulatorName\" FROM info_line t1,info_regulator t2 WHERE t1.regulator = t2.id and t1.name = #{QueryType} GROUP BY t1.id limit #{start},#{length}")
    List<Map<String, Object>> queryLineListByCondition(@Param("start") Integer start, @Param("length") Integer length, @Param("QueryType") String QueryType);
    
    @Select("SELECT COUNT(1) FROM info_line")
    int queryLineListCount();
    
    @Select("SELECT COUNT(1) FROM info_line WHERE name = #{QueryType}")
    int queryLineListCountByCondition(@Param("QueryType") String QueryType);
    
    @Insert("INSERT info_line (id,name,regulator,length,voltage_level,ac_dc,remark,left_station,right_station) VALUES (#{recordId},#{name},#{regulator},#{length},#{voltage_level},#{ac_dc},\"remark\",#{LeftStation},#{RightStation})")
    void addLine(Map<String, Object> paramMap);
    
    @Update("UPDATE info_line SET name=#{name},regulator=#{regulator},length=#{length},voltage_level=#{voltage_level},ac_dc=#{ac_dc},left_station=#{LeftStation},right_station=#{RightStation} WHERE id = #{recordId}")
    void updateLine(Map<String, Object> paramMap);
    
    @Select("SELECT * FROM info_line WHERE id = #{regulatorId}")
    Map<String, Object> getLineById(String regulatorId);
    
    @Select("SELECT * FROM info_tower WHERE id = #{regulatorId}")
    Map<String, Object> getTowerById(String regulatorId);
    
    @Select("SELECT * FROM info_device WHERE id = #{regulatorId}")
    Map<String, Object> getDeviceById(String regulatorId);
    
    //getStationById
    @Select("SELECT * FROM info_station WHERE id = #{regulatorId}")
    Map<String, Object> getStationById(String regulatorId);
    
    @Delete("DELETE FROM info_line WHERE id = #{userId}")
    void deleteLine(String userId);
    
    @Delete("DELETE FROM line_user WHERE line = #{userId}")
    void deleteUser_LineByLine(String userId);
    
    @Delete("DELETE FROM info_tower WHERE id = #{userId}")
    void deleteTower(String userId);    
    
    @Delete("DELETE FROM info_device WHERE id = #{userId}")
    void deleteDevice(String userId);    
    
    @Delete("DELETE FROM info_factory WHERE id = #{userId}")
    void deleteFactory(String userId);   
    
    //deleteStation
    @Delete("DELETE FROM info_station WHERE id = #{userId}")
    void deleteStation(String userId);   
    
    @Select("select name from info_device where id=#{deviceId}")
    String getDeviceNameById(String deviceId);  
    
    @Select("SELECT t1.*,GROUP_CONCAT(t2.`name`) AS \"lineName\" FROM info_tower t1,info_line t2 WHERE t1.line = t2.id GROUP BY t1.id limit #{start},#{length}")
    List<Map<String, Object>> queryTowerList(@Param("start") Integer start, @Param("length") Integer length);
    
    @Select("SELECT t1.*,GROUP_CONCAT(t2.`name`) AS \"lineName\" FROM info_tower t1,info_line t2 WHERE t1.line = t2.id and t1.line = #{QueryType} GROUP BY t1.id limit #{start},#{length}")
    List<Map<String, Object>> queryTowerListByCondition(@Param("start") Integer start, @Param("length") Integer length, @Param("QueryType") String QueryType);
    
    @Select("SELECT COUNT(1) FROM info_tower")
    int queryTowerListCount();
    
    @Select("SELECT COUNT(1) FROM info_tower WHERE line = #{QueryType}")
    int queryTowerListCountByCondition(@Param("QueryType") String QueryType);
    
    @Select("SELECT t1.*,GROUP_CONCAT(t2.`name`) AS \"lineName\",GROUP_CONCAT(t3.`name`) AS \"towerName\",GROUP_CONCAT(t4.`name`) AS \"factoryName\" FROM info_device t1,info_line t2,info_tower t3,info_factory t4 where (t1.line_name =#{QueryType} or #{QueryType} ='') and (t1.manufacture =#{QueryType1} or #{QueryType1} ='') and (t1.line_name = t2.id) and (t1.tower_id = t3.id) and (t1.manufacture = t4.id) GROUP BY t1.id limit #{start},#{length}")
    List<Map<String, Object>> queryDeviceList(@Param("start") Integer start, @Param("length") Integer length,@Param("QueryType") String QueryType,@Param("QueryType1") String QueryType1);
    
    @Select("SELECT t1.*,GROUP_CONCAT(t2.`name`) AS \"lineName\",GROUP_CONCAT(t3.`name`) AS \"towerName\",GROUP_CONCAT(t4.`name`) AS \"factoryName\" FROM info_device t1,info_line t2,info_tower t3,info_factory t4 where (t1.line_name =#{QueryType} or #{QueryType} ='') and (t1.manufacture =#{QueryType1} or #{QueryType1} ='') and (t1.line_name = t2.id) and (t1.tower_id = t3.id) and (t1.manufacture = t4.id) GROUP BY t1.id")
    List<Map<String, Object>> queryAllDeviceList(@Param("QueryType") String QueryType,@Param("QueryType1") String QueryType1);
    
    @Select("SELECT COUNT(1) FROM info_device WHERE (line_name =#{QueryType} or #{QueryType}='') and (manufacture =#{QueryType1} or #{QueryType1}='') ")
    int queryDeviceListCount(@Param("QueryType") String QueryType, @Param("QueryType1") String QueryType1);
    
    @Select("SELECT * FROM info_factory WHERE (name =#{QueryType} or #{QueryType}='') GROUP BY id limit #{start},#{length}")
    List<Map<String, Object>> queryFactoryList(@Param("start") Integer start, @Param("length") Integer length,@Param("QueryType") String QueryType);
    
    @Select("SELECT COUNT(1) FROM info_factory WHERE (name =#{QueryType} or #{QueryType}='') ")
    int queryFactoryListCount(@Param("QueryType") String QueryType);
    
    //queryStationList
    @Select("SELECT * FROM info_station WHERE (name =#{QueryType} or #{QueryType}='') GROUP BY id limit #{start},#{length}")
    List<Map<String, Object>> queryStationList(@Param("start") Integer start, @Param("length") Integer length,@Param("QueryType") String QueryType);
    
    @Select("SELECT COUNT(1) FROM info_station WHERE (name =#{QueryType} or #{QueryType}='') ")
    int queryStationListCount(@Param("QueryType") String QueryType);
    
    @Select("SELECT * FROM info_factory WHERE id = #{regulatorId}")
    Map<String, Object> getFactoryById(String regulatorId);
    
    @Select("SELECT COUNT(1) FROM info_device")
    int queryallDeviceNum();
    
    @Select("SELECT COUNT(1) FROM info_device where comm_state = 0")
    int queryonlineDeviceNum();
    
    @Select("SELECT COUNT(1) FROM info_device where comm_state = 1")
    int queryofflineDeviceNum();
    
    @Select("SELECT COUNT(1) FROM dev_alarm where isdealed = 0 and device in (select id from info_device where line_name in (select line from line_user where user=#{userId}))")
    int querynoReadAlarmNumByUser(String userId);
    
    @Select("SELECT COUNT(1) FROM report_fault where isRead = 0 and line in (select line from line_user where user=#{userId})") 
    int querynoReadFaultNumByUser(String userId);
   
    @Select("SELECT longitude, latitude FROM info_tower where line = #{acLineId}")
    List<Map<String, String>> getTowerLonLat(String acLineId);

    @Select("SELECT id,device,name,longitude,latitude,ide_point_no,ied_phase,distancetom,distanceton FROM info_device where line_name = #{acLineId}")
    List<Map<String, String>> getDeviceIdNameLonLat(String acLineId);
}

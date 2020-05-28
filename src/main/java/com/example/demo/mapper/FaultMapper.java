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
public interface FaultMapper {
    //@Select("select * from report_wave where device in(select id from info_device where (manufacture =#{factory} or #{factory}='') and (line_name=#{line} or #{line}='') ) and (device=#{device} or #{devcie}='') and (wave_num=#{wavenum} or #{wavenum}='') limit #{start},#{length}")
    //@Select("select * from report_wave where device in(select id from info_device where manufacture =#{factory} and line_name=#{line} ) and device=#{device} and wave_num=#{wavenum} limit #{start},#{length}")
    //@Select("select * from report_wave")
    @Select("select * from report_wave where device in(select id from info_device where (manufacture =#{factory} or #{factory}='') and (line_name=#{line} or #{line}='')) and (wave_type=#{waveType} or #{waveType}='') order by create_time desc limit #{start},#{length}")
    //@Select("select t1.*, GROUP_CONCAT(t2.`name`) AS \"device\", GROUP_CONCAT(t2.`line_name`) AS \"line\", GROUP_CONCAT(t2.`manufacture`) AS \"factory\" from report_wave t1, info_device t2 where t1.device in(select id from info_device where (manufacture =#{factory} or #{factory}='') and (line_name=#{line} or #{line}='') and (t1.name=#{device} or #{device}='')) and (t1.wave_num=#{wavenum} or #{wavenum}='')  and (t1.wave_type=#{waveType} or #{waveType}='') limit #{start},#{length}")
    List<Map<String, Object>> queryWaveList(@Param("start") Integer start, @Param("length") Integer length, @Param("factory") String factory, @Param("line") String line, @Param("waveType") String waveType);
    
    @Select("select COUNT(1) from report_wave where device in(select id from info_device where (manufacture =#{factory} or #{factory}='') and (line_name=#{line} or #{line}='')) and (wave_type=#{waveType} or #{waveType}='') ")
    int queryWaveListCount(@Param("factory") String factory, @Param("line") String line, @Param("waveType") String waveType);
    
    @Select("select * from report_fault where (occurr_time between #{StartTime} and #{EndTime}) and (line = #{line}) and (isRead=#{deal}) order by occurr_time desc limit #{start},#{length}")
    List<Map<String, Object>> queryFaultList(@Param("start") Integer start, @Param("length") Integer length, @Param("line") String line, @Param("deal") Integer deal, @Param("StartTime") String StartTime, @Param("EndTime") String EndTime);
    
    @Select("select COUNT(1) from report_fault where (occurr_time between #{StartTime} and #{EndTime}) and line = #{line} and (isRead=#{deal})")
    int queryFaultListCount(@Param("line") String line, @Param("deal") Integer deal, @Param("StartTime") String StartTime, @Param("EndTime") String EndTime);

    @Select("select * from report_fault where (occurr_time between #{StartTime} and #{EndTime}) and (line = #{line} or #{line} = '') and line in (select line from line_user where user = #{userID}) order by occurr_time desc limit #{start},#{length}")
    List<Map<String, Object>> queryFaultList1(@Param("start") Integer start, @Param("length") Integer length, @Param("line") String line, @Param("StartTime") String StartTime, @Param("EndTime") String EndTime, @Param("userID") String userID);
    
    @Select("select COUNT(1) from report_fault where (occurr_time between #{StartTime} and #{EndTime}) and (line = #{line} or #{line} = '') and line in (select line from line_user where user = #{userID}) ")
    int queryFaultListCount1(@Param("line") String line, @Param("StartTime") String StartTime, @Param("EndTime") String EndTime, @Param("userID") String userID);
    
    @Select("SELECT * FROM info_device WHERE id = #{regulatorId}")
    Map<String, Object> getDeviceById(String regulatorId);
    
    @Select("SELECT name FROM info_tower WHERE id = #{regulatorId}")
    String getTowerById(String regulatorId);
    
    @Select("SELECT name FROM info_factory WHERE id = #{regulatorId}")
    String getFactoryById(String regulatorId);
    
    @Select("SELECT sample_Rate FROM info_factory WHERE id = #{regulatorId}")
    String getFactorySRById(String regulatorId);
    
    @Select("SELECT name FROM info_line WHERE id = #{regulatorId}")
    String getLineById(String regulatorId);
    
    @Select("SELECT regulator FROM info_line WHERE id = #{regulatorId}")
    String getRegulatorByLineId(String regulatorId);
    
    @Select("SELECT name FROM info_tower WHERE id = #{regulatorId}")
    String getFaultNameById(String regulatorId);
    
    @Select("select * from report_wave where device in(select id from info_device where (manufacture =#{factory} or #{factory}='') and (line_name in (select line from line_user where user = #{userID}))) and (wave_type=#{waveType} or #{waveType}='') order by create_time desc limit #{start},#{length}")
    List<Map<String, Object>> queryWaveListByUser(@Param("start") Integer start, @Param("length") Integer length, @Param("factory") String factory, @Param("userID") String userID, @Param("waveType") String waveType);
    
    @Select("select COUNT(1) from report_wave where (wave_type=#{waveType} or #{waveType}='') and device in(select id from info_device where (manufacture =#{factory} or #{factory}='') and (line_name in (select line from line_user where user = #{userID})))")
    int queryWaveListCountByUser(@Param("factory") String factory, @Param("userID") String userID, @Param("waveType") String waveType);

    /*
    @Select("select * from report_fault where (occurr_time between #{StartTime} and #{EndTime}) and (isRead=#{deal}) and line in (select line from line_user where user = #{userID}) order by occurr_time desc limit #{start},#{length}")
    List<Map<String, Object>> queryFaultListByUser(@Param("start") Integer start, @Param("length") Integer length, @Param("userID") String userID, @Param("deal") Integer deal, @Param("StartTime") String StartTime, @Param("EndTime") String EndTime);
    
    @Select("select COUNT(1) from report_fault where (occurr_time between #{StartTime} and #{EndTime}) and (isRead=#{deal}) and line in (select line from line_user where user = #{userID})")
    int queryFaultListCountByUser(@Param("userID") String userID, @Param("deal") Integer deal, @Param("StartTime") String StartTime, @Param("EndTime") String EndTime);
    */
    
    @Select("select * from report_fault where line in (select line from line_user where user = #{userID}) order by occurr_time desc limit #{start},#{length}")
    List<Map<String, Object>> queryFaultListByUser(@Param("start") Integer start, @Param("length") Integer length, @Param("userID") String userID);
    
    @Select("select COUNT(1) from report_fault where line in (select line from line_user where user = #{userID})")
    int queryFaultListCountByUser(@Param("userID") String userID);
    
    @Select("select COUNT(1) from report_fault)")
    int queryAllFaultListCount();
    
    @Select("select COUNT(1) from report_fault where line in (select line from line_user where user = #{userID}) and isRead =0")
    int countLineUnReadNum(@Param("userID") String userID);
    
    @Select("select * from report_fault where line in (select line from line_user where user = #{userID}) and (occurr_time between #{StartTime} and #{EndTime}) order by occurr_time desc limit #{start},#{length}")
    List<Map<String, Object>> queryFaultListByUserAndInterval(@Param("start") Integer start, @Param("length") Integer length, @Param("userID") String userID, @Param("StartTime") String StartTime, @Param("EndTime") String EndTime);
    
    @Select("select COUNT(1) from report_fault where line in (select line from line_user where user = #{userID}) and (occurr_time between #{StartTime} and #{EndTime})")
    int queryFaultListCountByUserAndInterval(@Param("userID") String userID, @Param("StartTime") String StartTime, @Param("EndTime") String EndTime);
    
    @Select("select * from report_fault where line=#{line}  and (occurr_time between #{StartTime} and #{EndTime}) order by occurr_time desc limit #{start},#{length}")
    List<Map<String, Object>> queryFaultListByLineAndInterval(@Param("start") Integer start, @Param("length") Integer length, @Param("line") String line, @Param("StartTime") String StartTime, @Param("EndTime") String EndTime);
    
    @Select("select COUNT(1) from report_fault where line=#{line} and (occurr_time between #{StartTime} and #{EndTime})")
    int queryFaultListCountByLineAndInterval(@Param("line") String line, @Param("StartTime") String StartTime, @Param("EndTime") String EndTime);
    
    @Select("select * from report_fault where (occurr_time between #{StartTime} and #{EndTime}) and line in (select line from line_user where user = #{userID}) order by occurr_time desc limit #{start},#{length}")
    List<Map<String, Object>> queryFaultListByTime(@Param("start") Integer start, @Param("length") Integer length, @Param("userID") String userID, @Param("StartTime") String StartTime, @Param("EndTime") String EndTime);
    
    @Select("select COUNT(1) from report_fault where (occurr_time between #{StartTime} and #{EndTime}) and line in (select line from line_user where user = #{userID})")
    int queryFaultListCountByTime(@Param("userID") String userID, @Param("StartTime") String StartTime, @Param("EndTime") String EndTime);
    
    @Select("select * from report_fault where (isRead = #{StartTime}) and line in (select line from line_user where user = #{userID}) order by occurr_time desc limit #{start},#{length}")
    List<Map<String, Object>> queryFaultListByDeal(@Param("start") Integer start, @Param("length") Integer length, @Param("userID") String userID, @Param("StartTime") String StartTime);
    
    @Select("select COUNT(1) from report_fault where (isRead = #{StartTime}) and line in (select line from line_user where user = #{userID})")
    int queryFaultListCountByDeal(@Param("userID") String userID, @Param("StartTime") String StartTime);
    
    @Select("select * from report_fault where (line in (select id from info_line where regulator = #{Regulator})) and line in (select line from line_user where user = #{userID}) limit #{start},#{length}")
    List<Map<String, Object>> queryFaultListByRegulator(@Param("start") Integer start, @Param("length") Integer length, @Param("userID") String userID, @Param("Regulator") String Regulator);
    
    @Select("select COUNT(1) from report_fault where (line in (select id from info_line where regulator = #{Regulator})) and line in (select line from line_user where user = #{userID})")
    int queryFaultListCountByRegulator(@Param("userID") String userID, @Param("Regulator") String Regulator);
    
    @Select("select * from report_fault where (line in (select id from info_line where voltage_level = #{Regulator})) and line in (select line from line_user where user = #{userID}) limit #{start},#{length}")
    List<Map<String, Object>> queryFaultListByVoltageLevel(@Param("start") Integer start, @Param("length") Integer length, @Param("userID") String userID, @Param("Regulator") String Regulator);
    
    @Select("select COUNT(1) from report_fault where (line in (select id from info_line where voltage_level = #{Regulator})) and line in (select line from line_user where user = #{userID})")
    int queryFaultListCountByVoltageLevel(@Param("userID") String userID, @Param("Regulator") String Regulator);    
    
    @Select("select * from report_fault where (isLightning =#{Regulator}) and line in (select line from line_user where user = #{userID}) limit #{start},#{length}")
    List<Map<String, Object>> queryFaultListByKind(@Param("start") Integer start, @Param("length") Integer length, @Param("userID") String userID, @Param("Regulator") String Regulator);
    
    @Select("select COUNT(1) from report_fault where (isLightning =#{Regulator}) and line in (select line from line_user where user = #{userID})")
    int queryFaultListCountByKind(@Param("userID") String userID, @Param("Regulator") String Regulator); 
   
    @Select("select * from report_fault where (line =#{Regulator}) and line in (select line from line_user where user = #{userID}) limit #{start},#{length}")
    List<Map<String, Object>> queryFaultListByLine(@Param("start") Integer start, @Param("length") Integer length, @Param("userID") String userID, @Param("Regulator") String Regulator);
    
    @Select("select COUNT(1) from report_fault where (line =#{Regulator}) and line in (select line from line_user where user = #{userID})")
    int queryFaultListCountByLine(@Param("userID") String userID, @Param("Regulator") String Regulator); 
    
    @Select("SELECT * FROM report_wave WHERE id = #{regulatorId}")
    Map<String, Object> getWaveById(String regulatorId);
    
    @Select("SELECT * FROM report_fault WHERE id = #{regulatorId}")
    Map<String, Object> getFaultById(String regulatorId);
    
    @Update("UPDATE report_fault SET isRead=1 WHERE id = #{userId}")
    void setDealFaultById(String userId);
    
    @Select("select * from report_fault where (line=#{line}) and (isLightning =#{Kind} or #{Kind}='') and (occurr_time between #{StartTime} and #{EndTime}) and (isRead=#{deal} or #{deal}='') order by occurr_time desc limit #{start},#{length}")
    List<Map<String, Object>> queryFaultListByUniqueLine(@Param("start") Integer start, @Param("length") Integer length,@Param("line") String line,@Param("Kind") String Kind,@Param("StartTime") String StartTime,@Param("EndTime") String EndTime,@Param("deal") String deal);
    
    @Select("select COUNT(1) from report_fault where (line=#{line}) and (isLightning =#{Kind} or #{Kind}='') and (occurr_time between #{StartTime} and #{EndTime}) and (isRead=#{deal} or #{deal}='')")
    int queryFaultListCountByUniqueLine(@Param("line") String line,@Param("Kind") String Kind,@Param("StartTime") String StartTime,@Param("EndTime") String EndTime,@Param("deal") String deal);
    
    @Select("select * from report_fault where (line in (select id from info_line where ((regulator=#{regulatorId} or #{regulatorId}='') and (voltage_level=#{voltage_level} or #{voltage_level}='') and (id in (SELECT line FROM line_user where user=#{userID}))))) and (isLightning =#{Kind} or #{Kind}='') and (occurr_time between #{StartTime} and #{EndTime}) and (isRead=#{deal} or #{deal}='') order by occurr_time desc limit #{start},#{length}")
    List<Map<String, Object>> queryFaultListByMultiLine1(@Param("start") Integer start, @Param("length") Integer length,@Param("userID") String userID,@Param("regulatorId") String regulatorId,@Param("voltage_level") String voltage_level,@Param("Kind") String Kind,@Param("StartTime") String StartTime,@Param("EndTime") String EndTime,@Param("deal") String deal);
    
    @Select("select COUNT(1) from report_fault where (line in (select id from info_line where ((regulator=#{regulatorId} or #{regulatorId}='') and (voltage_level=#{voltage_level} or #{voltage_level}='') and (id in (SELECT line FROM line_user where user=#{userID}))))) and (isLightning =#{Kind} or #{Kind}='') and (occurr_time between #{StartTime} and #{EndTime}) and (isRead=#{deal} or #{deal}='')")
    int queryFaultListCountByMultiLine1(@Param("userID") String userID,@Param("regulatorId") String regulatorId,@Param("voltage_level") String voltage_level,@Param("Kind") String Kind,@Param("StartTime") String StartTime,@Param("EndTime") String EndTime,@Param("deal") String deal);

    //List<Map<String, Object>> queryLineFaultList(Map<String, Object> param);
    
    //long countLineFaultNum();
    
    //long countLineFaultNumInUser(String userId);
    
    //int countLineUnReadNum(String userId);
    
    @Select("select wave_current_freq_collection from dev_parameter where device = #{devId}")
    int queryWaveFreqByDev(String devId);
    
    @Select("select pf_current_freq_collection from dev_parameter where device = #{devId}")
    int queryPfFreqByDev(String devId);

    @Select("select count(1) from dev_parameter where device = #{devId}")
    int queryParameterCount(String devId);
}

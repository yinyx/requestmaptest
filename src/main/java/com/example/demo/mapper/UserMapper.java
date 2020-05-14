package com.example.demo.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import com.example.demo.po.User;
import com.example.demo.po.Line;
import com.example.demo.po.Log;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    @Select("select role from sys_user WHERE id =#{userId}")
    int queryRoleByUserId(String userId);

    //根据id查询用户
    @Select("select * from sys_user WHERE phone =#{name}")
    Map<String, Object> findUserByName(String name);
    
    @Select("SELECT t1.*,GROUP_CONCAT(t3.`name`) AS \"lineName\" FROM sys_user t1,line_user t2,info_line t3 WHERE t1.id = t2.user AND t3.id = t2.line GROUP BY t1.id limit #{start},#{length}")
    List<Map<String, Object>> queryUserList(@Param("start") Integer start, @Param("length") Integer length);
    
    @Select("SELECT COUNT(1) FROM sys_user")
    int queryUsersCount();

    @Select("SELECT t1.*,GROUP_CONCAT(t3.`name`) AS \"lineName\" FROM sys_user t1,line_user t2,info_line t3 WHERE t1.id = t2.user AND t3.id = t2.line AND (t1.create_by =#{userId}) GROUP BY t1.id limit #{start},#{length}")
    List<Map<String, Object>> queryUserListByUser(@Param("start") Integer start, @Param("length") Integer length, @Param("userId") String userId);

    @Select("SELECT COUNT(1) FROM sys_user where create_by =#{userId}")
    int queryUsersCountByUser(@Param("userId") String userId);
    
    @Select("select * from info_line")
    List<Line> queryLine();

    @Select("select * from info_line where id in (select line from line_user where user=#{userId})")
    List<Line> queryLineByUser(@Param("userId") String userId);
    
    @Select("SELECT* FROM log_syn ORDER BY syn_time DESC")
    List<Log> queryLog();
    
    @Select("SELECT* FROM log_syn ORDER BY syn_time DESC limit #{start},#{length}")
    List<Map<String, Object>> queryLogList(@Param("start") Integer start, @Param("length") Integer length);
    
    @Select("SELECT* FROM log_syn WHERE type = #{QueryType} ORDER BY syn_time DESC limit #{start},#{length}")
    List<Map<String, Object>> queryLogListByCondition(@Param("start") Integer start, @Param("length") Integer length, @Param("QueryType") String QueryType);
    
    @Select("SELECT COUNT(1) FROM log_syn")
    int queryLogListCount();
    
    @Select("SELECT COUNT(1) FROM log_syn WHERE type = #{QueryType}")
    int queryLogListCountByCondition(@Param("QueryType") String QueryType);
    
    @Select("SELECT COUNT(1) FROM sys_user WHERE phone = #{name}")
    int queryUserNameIsRepeat(String name);
    
    @Insert("INSERT sys_user (id,user_name,phone,has_del,role,create_by,create_time,update_by,update_time,operate_authority) VALUES (#{userId},#{userName},#{phone},#{status},#{role},#{loginUser},NOW(),'admin',NOW(),#{authority})")
    void addSchoolUser(Map<String, Object> paramMap);
    
    @Insert("INSERT line_user (id,user,line,author) VALUES (#{RecordId},#{userId},#{LineId},1)")
    void addSchoolUserRole(Map<String, Object> paramMap);
    
    @Select("SELECT t1.*,GROUP_CONCAT(t2.line) AS \"lineName\" FROM sys_user t1, line_user t2 WHERE t1.id = t2.user AND t1.id =  #{userId} GROUP BY t1.id")
    Map<String, Object> getUserById(String userId);
    
    @Select("SELECT * FROM info_regulator WHERE id = #{regulatorId}")
    Map<String, Object> getRegulatorById(String regulatorId);
    
    @Update("UPDATE sys_user SET user_name=#{userName},phone=#{phone},has_del=#{status},role=#{role},update_by=#{loginUser},update_time=NOW(),operate_authority=#{authority} WHERE id = #{userId}")
    void updateSchoolUser(Map<String, Object> paramMap);
    
    @Delete("DELETE FROM line_user WHERE user = #{userId} ")
    void delAllUserRole(Map<String, Object> paramMap);
    
    @Delete("DELETE FROM line_user WHERE user = #{userId} ")
    void delAllUserRoleByString(String userId);
    
    @Delete("DELETE FROM sys_user WHERE id = #{userId}")
    void deleteSchoolUser(String userId);
    
    @Delete("DELETE FROM info_regulator WHERE id = #{userId}")
    void deleteRegulator(String userId);
    
    @Update("UPDATE sys_user SET PASSWORD = #{newPassword} WHERE id = #{id}")
    public void saveNewPassWord(Map<String, Object> paramMap);
    
    @Select("SELECT password FROM sys_user WHERE id = #{id}")
    public String findUserPwdById(String id);
   
    @Select("SELECT access_password FROM info_device limit 1")    
    String getPassByDevId(String id);
    
    @Select("SELECT wave_psd FROM sys_user WHERE id = #{id}")
    public String findUserWavePwdById(String id);
    
    @Update("UPDATE sys_user SET wave_psd = #{password}")
    void saveWaveWord(Map<String, Object> paramMap);
 
    @Update("UPDATE info_device SET access_password = #{password}")  
    void saveDevicePass(Map<String, Object> paramMap);
    
    @Update("UPDATE sys_user SET password='e10adc3949ba59abbe56e057f20f883e' WHERE id = #{id} ")
    void resetPassword(Map<String, Object> paramMap);
    
    @Select("SELECT name FROM info_regulator WHERE id = #{id}")
    public String findParentRegulatorNameById(String id);
    
    @Select("SELECT * FROM report_wave WHERE id = #{regulatorId}")
    Map<String, Object> getWaveById(String regulatorId);
    
    @Select("SELECT * FROM report_fault WHERE id = #{regulatorId}")
    Map<String, Object> getFaultById(String regulatorId);
    
}

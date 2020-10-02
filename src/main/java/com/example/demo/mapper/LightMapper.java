package com.example.demo.mapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface LightMapper {
    @Select("select UpdateTime from sync_time WHERE TableIndex =#{tableId}")
    String getLocalLastUpdateTimeById(@Param("tableId") Integer tableId);

    @Update("update sync_time set UpdateTime=#{updateTime} where TableIndex =#{tableId}")
    void setLocalLastUpdateTimeById(@Param("tableId") Integer tableId, @Param("updateTime") String updateTime);

    @Insert({
            "<script>"
                    + "INSERT INTO info_line VALUES"
                    + "<foreach item='item' index='index' collection='lines' separator=','>"
                    + "(#{item.MLineID},#{item.MLName},0,0,#{item.Volevel},0,\"\",#{item.Station1},#{item.Station2})"
                    + "</foreach>"
                    + "ON DUPLICATE KEY UPDATE name = values(name),voltage_level = values(voltage_level),left_station = values(left_station),right_station = values(right_station)"
                    + "</script>"
    })
    void syncLine(@Param("lines") List<Map<String, Object>> lines);

    @Insert({
            "<script>"
                    + "INSERT INTO info_tower VALUES"
                    + "<foreach item='item' index='index' collection='towers' separator=','>"
                    + "(#{item.ElePoID},#{item.Name},#{item.Serial},#{item.Line},#{item.LineLen},\"\",\"\",\"\",0,0)"
                    + "</foreach>"
                    + "ON DUPLICATE KEY UPDATE name = values(name),line = values(line),indexno = values(indexno),distance = values(distance)"
                    + "</script>"
    })
    void syncTower(@Param("towers") List<Map<String, Object>> towers);

    @Insert({
            "<script>"
                    + "INSERT INTO info_device VALUES"
                    + "<foreach item='item' index='index' collection='dtus' separator=','>"
                   // + "('1112', '1112', '1', '同步测试2', '3de081127e6d491f8d0a36cd8fe7fd2f', '248a971ed1ba4f83bc7f9804b04ba078', '11', '1', '11', '0', '2', '0', null, '111', '111', '1111', '1.000', '1.000', '1.000', null, null, '1', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '1', '321', null, null, null, null, null, null, null, null, null, null, '', '', '2020-09-29 11:13:00')"
                    + "(#{item.DTUID},#{item.DTUID},0,#{item.Name},#{item.MLineID}, '248a971ed1ba4f83bc7f9804b04ba078', 'manu_date', 'ied_type', 'version', #{item.ElePoID}, 'install_time', #{item.SLineID}, null, '111', '111', '1111', '1.000', '1.000', '1.000', null, null, '1', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '1', '321', null, null, null, null, null, null, null, null, null, null, '', '', '2020-09-29 11:13:00')"
                    + "</foreach>"
                    + "ON DUPLICATE KEY UPDATE name = values(name),line_name = values(line_name),tower_id = values(tower_id),ied_phase = values(ied_phase)"
                    + "</script>"
    })
    void syncDtu(@Param("dtus") List<Map<String, Object>> dtus);

    @Insert({
            "<script>"
                    + "INSERT INTO info_regulator VALUES"
                    + "<foreach item='item' index='index' collection='offices' separator=','>"
                    + "(#{item.EleOffID},'0',#{item.EName},#{item.PEleOffID},'remark')"
                    + "</foreach>"
                    + "ON DUPLICATE KEY UPDATE name = values(name),parent = values(parent)"
                    + "</script>"
    })
    void syncOffice(@Param("offices") List<Map<String, Object>> offices);
}

package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

import com.example.demo.po.Page;

public interface SysMapper {

    List<Map<String, Object>> queryLogList(int start, int length);

    Integer queryLogListCount(Map<String, Object> paramMap);
    
}

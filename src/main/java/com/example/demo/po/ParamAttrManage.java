package com.example.demo.po;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.po.ParamAttr;
import com.example.demo.mapper.StateMapper; 

@Component
public class ParamAttrManage {

    // 注入用户Mapper
    @Autowired
    private StateMapper stateMapper;
    private List<ParamAttr> ParamAttrList;
    private Map<String, List<ParamAttr>> ParamAttrMap= new HashMap<String, List<ParamAttr>>();; 
    
    public void init()
    {
        if(ParamAttrList!=null)
        {
            ParamAttrList.clear();
        }
        if(ParamAttrMap!=null)
        {
            ParamAttrMap.clear();
        }

        ParamAttrList = stateMapper.queryParamAttrList();
        groupListToMap(ParamAttrList, ParamAttrMap);
    }
    
    public void print()
    {
        System.out.println("*********list");
        for (int i=0;i<ParamAttrList.size();i++)
        {
            System.out.println(ParamAttrList.get(i));
        }
        
        System.out.println("*********map");
        System.out.println(ParamAttrMap);
    }
    
    public void clear()
    {
        ParamAttrList.clear();
    }
    
    public void deleteParamAttrById(String id)
    {
        for (int i=0;i<ParamAttrList.size();i++)
        {
            if(ParamAttrList.get(i).getId().equals(id))
            {
                ParamAttrList.remove(i);
            }
        }
        
    }
    
    public void deleteParamAttr(ParamAttr paramAttr)
    {
        String key;
        key =  paramAttr.getManufactureId();
        List<ParamAttr> listTmp = ParamAttrMap.get(key);
        if (listTmp!=null)
        {
            for (ParamAttr val : listTmp)
            {
                if (paramAttr.getId().equals(val.getId()))
                {
                    listTmp.remove(val);
                    if (0==listTmp.size())
                    {
                        ParamAttrMap.remove(key);
                    }
                    return;
                }
            }
        }
    }
    
    public void addParamAttr(ParamAttr paramAttr)
    {
        List<ParamAttr> listTmp;
        String key;
        key = paramAttr.getManufactureId();//按这个属性分组，map的Key
        listTmp = ParamAttrMap.get(key);
        if (null == listTmp) {
            listTmp = new ArrayList<ParamAttr>();
            ParamAttrMap.put(key, listTmp);
        }
        listTmp.add(paramAttr);
    }
    
    public void updateParamAttr(ParamAttr paramAttr)
    {
        String key;
        key =  paramAttr.getManufactureId();
        List<ParamAttr> listTmp = ParamAttrMap.get(key);
        if (listTmp!=null)
        {
            for (ParamAttr val : listTmp)
            {
                if (paramAttr.getId().equals(val.getId()))
                {
                    listTmp.remove(val);
                    listTmp.add(paramAttr);
                    return;
                }
            }
        }
    }
    
    public ParamAttr getParamAttr(String factoryId, int paraIndex)
    {
        List<ParamAttr> listTmp = ParamAttrMap.get(factoryId);
        if (listTmp!=null)
        {
            for (ParamAttr val : listTmp)
            {
                if (paraIndex == val.getIndexno())
                {
                    return val;
                }
            }
        }  
        return null;
    }
    
    public void groupListToMap(List<ParamAttr> ParamAttrList, Map<String, List<ParamAttr>> ParamAttrMap)
    {
        if (null == ParamAttrList || null == ParamAttrMap)
        {
            return;
        }
        
        String key;

        List<ParamAttr> listTmp;
        for (ParamAttr val : ParamAttrList) {
            key = val.getManufactureId();//按这个属性分组，map的Key
            listTmp = ParamAttrMap.get(key);
            if (null == listTmp) {
                listTmp = new ArrayList<ParamAttr>();
                ParamAttrMap.put(key, listTmp);
            }
            listTmp.add(val);
        }

    }
}

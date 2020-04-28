package com.example.demo.mqtt;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;


import com.example.demo.mqtt.ConstStr;
import com.example.demo.mqtt.mqttSender;
import net.sf.json.JSONObject;
@ComponentScan(basePackages={"com.example.coreserver.mqtt.mqttSender"})
@Component
public class MsgSender {
	@Autowired
	private mqttSender sender;
	
	static boolean m_bInit = false;
	//private static MsgSender inst = new MsgSender();
	

	

	
	
	//初始化连接
	public void init() {
		try {

			sender.init();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//复位 版本号 0为旧版本，1为新版本
	public void resetDevice(String uuid,String strUser,String strDevID,int protocolVersion){
		/*if(!m_bInit)
		{
			init();
			m_bInit = true;
		}*/
		JSONObject obj = new JSONObject();
		//String uuid = UUID.randomUUID().toString().replaceAll("-","");
		obj.put(ConstStr.ID,uuid);
		obj.put(ConstStr.DEVID,strDevID);
		obj.put(ConstStr.TYPE,5);
		obj.put(ConstStr.USER, strUser);
		obj.put(ConstStr.PROCOLVERSION, protocolVersion);//新增协议版本号
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
		String strSDate =df.format(System.currentTimeMillis());   
		obj.put(ConstStr.SENDTIME, strSDate);
		
		String strText = obj.toString();
		String strSend = strText;
		
		
		sender.sendToMqtt(strSend);
		
	}
	
	//设置装置参数 版本号 0为旧版本，1为新版本
	public void setDevParameter(String uuid,String User,String strDevID,int[]ValueLst,int protocolVersion) {
		/*if(!m_bInit)
		{
			init();
			m_bInit = true;
		}*/
		JSONObject obj = new JSONObject();
		//String uuid = UUID.randomUUID().toString().replaceAll("-","");
		obj.put(ConstStr.ID,uuid);
		obj.put(ConstStr.DEVID,strDevID);
		obj.put(ConstStr.TYPE,7);
		obj.put(ConstStr.USER,User);
		obj.put(ConstStr.PROCOLVERSION, protocolVersion);//新增协议版本号
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
		String strSDate =df.format(System.currentTimeMillis());   
		obj.put(ConstStr.SENDTIME, strSDate);
		
		JSONObject para = new JSONObject();
		for(int nIndex=0;nIndex<ValueLst.length;nIndex++)
		{
			String strParaName = "Para"+String.valueOf(nIndex+1);
			para.put(strParaName, ValueLst[nIndex]);
		}
		obj.put(ConstStr.PARAMETERS,para);
		
		sender.sendToMqtt(obj.toString());	
	}

	//读取装置参数 版本号 0为旧版本，1为新版本
	public void readDevParameter(String uuid,String strUser,String strDevID,int protocolVersion) {
		/*if(!m_bInit)
		{
			init();
			m_bInit = true;
		}*/
		JSONObject obj = new JSONObject();
		//String uuid = UUID.randomUUID().toString().replaceAll("-","");
		obj.put(ConstStr.ID,uuid);
		obj.put(ConstStr.DEVID,strDevID);
		obj.put(ConstStr.TYPE,9);
		obj.put(ConstStr.USER, strUser);
		obj.put(ConstStr.PROCOLVERSION, protocolVersion);//新增协议版本号
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
		String strSDate =df.format(System.currentTimeMillis());   
		obj.put(ConstStr.SENDTIME, strSDate);
		
		sender.sendToMqtt(obj.toString());

	}
	
	//发送自检请求 版本号 0为旧版本，1为新版本
	public void readSelfCheckInfo(String uuid,String strUser,String strDevID,int protocolVersion) {
		/*if(!m_bInit)
		{
			init();
			m_bInit = true;
		}*/
		JSONObject obj = new JSONObject();
		//String uuid = UUID.randomUUID().toString().replaceAll("-","");
		obj.put(ConstStr.ID,uuid);
		obj.put(ConstStr.DEVID,strDevID);
		obj.put(ConstStr.TYPE,21);
		obj.put(ConstStr.USER, strUser);
		obj.put(ConstStr.PROCOLVERSION, protocolVersion);//新增协议版本号
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
		String strSDate =df.format(System.currentTimeMillis());   
		obj.put(ConstStr.SENDTIME, strSDate);
		
		sender.sendToMqtt(obj.toString());

	}
	
	//发送log文件下载请求 版本号 0为旧版本，1为新版本
	public void downloadLog(String uuid,String strUser,String strDevID,int protocolVersion) {
		/*if(!m_bInit)
		{
			init();
			m_bInit = true;
		}*/
		JSONObject obj = new JSONObject();
		//String uuid = UUID.randomUUID().toString().replaceAll("-","");
		obj.put(ConstStr.ID,uuid);
		obj.put(ConstStr.DEVID,strDevID);
		obj.put(ConstStr.TYPE,23);
		obj.put(ConstStr.USER, strUser);
		obj.put(ConstStr.PROCOLVERSION, protocolVersion);//新增协议版本号
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
		String strSDate =df.format(System.currentTimeMillis());   
		obj.put(ConstStr.SENDTIME, strSDate);
		
		sender.sendToMqtt(obj.toString());

	}
	
	
	//发送结果给APP 版本号 0为旧版本，1为新版本
	public void SendResult(String id,String factoryid,int faultNum,int nType,int phase,
			String occurr_time,String line,String leftTowerId,String rightTowerId,
			float left_distance,float right_distance,String left_path,String right_path) {
		/*if(!m_bInit)
		{
			init();
			m_bInit = true;
		}*/
		JSONObject obj = new JSONObject();
		obj.put(ConstStr.ID,id);
		obj.put("fSLType", nType);
		//String uuid = UUID.randomUUID().toString().replaceAll("-","");
		obj.put(ConstStr.FACTORY,factoryid);

		obj.put("fault_num",faultNum);
		obj.put(ConstStr.PHASE,phase);
		obj.put("occurr_time",occurr_time);
		obj.put("line",line);
		obj.put("left_tower",leftTowerId);
		obj.put("right_tower",rightTowerId);
		obj.put("left_distance", left_distance);
		obj.put("right_distance",right_distance);
		obj.put("left_path",left_path);
		obj.put("right_path",right_path);
		obj.put("isread",0);
		
		
		sender.sendToMqtt(sender.commClientTopic,1,obj.toString());

	}
}

package com.example.demo.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class mqttSender {
	  @Value("${mqtt.username}")
	  private String username;
	  
	  @Value("${mqtt.password}")
	  private String password;

	  @Value("${mqtt.url}")
	  private String url;

	  @Value("${mqtt.coreserver.clientId}")
	  private String commserverClientId;

	  @Value("${mqtt.coreserver.defaultTopic}")
	  private String commDefaultTopic;
	 
	  @Value("${mqtt.coreserver.clientTopic}")
	  public String commClientTopic;
	  	  
	  @Value("${mqtt.ip}")
	  private String host;
	  
	  @Value("${mqtt.port}")
	  private int port;	

	  private MqttClient client;
	  private MqttTopic topic;
	  MqttConnectOptions options;
	  //private MqttTopic topic125;
	  
	  MqttMessage message;
	  
	  static boolean bInit = false;
	  
	  public void init() throws MqttException {
          // MemoryPersistence设置clientid的保存形式，默认为以内存保存
		  if(bInit)
		  {
			  return;
		  }
		  bInit = true;
           client = new MqttClient(url, commserverClientId, new MemoryPersistence());
           connect();

       }

      private void connect() {
          options = new MqttConnectOptions();
          options.setCleanSession(false);
          options.setUserName(username);
          options.setPassword(password.toCharArray());
          // 设置超时时间
          options.setConnectionTimeout(10);
          // 设置会话心跳时间
          options.setKeepAliveInterval(20);
          try {
              //client.setCallback(new PushCallback());
              client.connect(options);
              topic = client.getTopic(commDefaultTopic);
              //client.subscribe(coreDefaultTopic);
              //topic125 = client.getTopic(TOPIC125);
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
      
      public void sendToMqtt(String strText) {
      	  if(!bInit)
    	  {
    		  try {
				init();
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	  }
    	  MqttMessage msg = new MqttMessage();
    	  msg.setQos(1);
    	  msg.setPayload(strText.getBytes());
    	  msg.setRetained(false);
    	  
    	  try {
			publish(topic,msg);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
      
      public void sendToMqtt(String strTopic,int qos,String strMsg){
       	  
    	  if(!bInit)
    	  {
    		  try {
				init();
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	  }
    	  
    	  MqttMessage msg = new MqttMessage();
    	  msg.setQos(1);
    	  msg.setPayload(strMsg.getBytes());
    	  msg.setRetained(false);
    	  
    	  MqttTopic topicTmp = client.getTopic(strTopic);
    	  try {
			publish(topicTmp,msg);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
      public void publish(MqttTopic topic , MqttMessage message) throws MqttPersistenceException,
      MqttException {    	  
    	  topic.publish(message);
    	  //token.waitForCompletion();
    	  //System.out.println("message is published completely! "
          //+ token.isComplete());
}
}

package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//import com.example.demo.po.ParamAttrManage;
@SpringBootApplication
public class RequestmaptestApplication {	
    public static void main(String[] args) {
	    ApplicationContext applicationContext = SpringApplication.run(RequestmaptestApplication.class, args);
	    //ParamAttrManage paramAttrManage = applicationContext.getBean(ParamAttrManage.class);
	    //paramAttrManage.init();        
	}
    
 //   protected SpringApplicationBuilder configure(
 //           SpringApplicationBuilder builder) {
 //       return builder.sources(this.getClass());
 //   }
    
}


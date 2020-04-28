package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
//import com.example.demo.po.*;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:wisely.properties")
@SpringBootTest
public class RequestmaptestApplicationTests {
    @Value("${person.name}")
    private String apiKey;
    
	@Test
	public void contextLoads() {
	    System.out.println(apiKey);
	}

}

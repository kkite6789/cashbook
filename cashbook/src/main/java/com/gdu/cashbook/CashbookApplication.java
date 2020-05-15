package com.gdu.cashbook;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
// @SpringBootApplication
@PropertySource("classpath:google.properties")
public class CashbookApplication {
	@Value("${google.username}")
	public String username;
	@Value("${google.password}")
	public String password;
	
	public static void main(String[] args) {
		SpringApplication.run(CashbookApplication.class, args);
	}

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost("smtp.gmail.com");//메일서버 이름
		javaMailSender.setPort(587);//보낸다
		System.out.println(username+"<--username");
		System.out.println(password+"<--userpassword");
		javaMailSender.setUsername(username);
		javaMailSender.setPassword(password);
		
		Properties prop = new Properties();//Properties == HashMap<String,Object>
		prop.setProperty("mail.smtp.auth", "true");
		prop.setProperty("mail.smtp.starttls.enable", "true");
		javaMailSender.setJavaMailProperties(prop);
		return javaMailSender;
				
	}
}

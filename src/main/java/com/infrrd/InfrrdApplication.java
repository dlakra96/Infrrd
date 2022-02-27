package com.infrrd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.infrrd.*"})
public class InfrrdApplication {
    public static void main(String args[]){
        SpringApplication.run(InfrrdApplication.class,args);
    }

}


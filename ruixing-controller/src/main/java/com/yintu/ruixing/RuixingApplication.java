package com.yintu.ruixing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class RuixingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuixingApplication.class, args);
    }

}

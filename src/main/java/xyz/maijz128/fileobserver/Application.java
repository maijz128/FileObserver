package xyz.maijz128.fileobserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}

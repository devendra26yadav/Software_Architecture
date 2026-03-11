package edu.miu.sa.lab11.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;

@SpringBootApplication
@EnableKafkaRetryTopic
public class KafkaLab11Application {

    public static void main(String[] args) {
        SpringApplication.run(KafkaLab11Application.class, args);
    }
}

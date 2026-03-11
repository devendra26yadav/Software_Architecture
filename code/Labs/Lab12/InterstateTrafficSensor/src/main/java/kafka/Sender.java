package kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Sender {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, Object payload){
        kafkaTemplate.send(topic, payload);
    }
}

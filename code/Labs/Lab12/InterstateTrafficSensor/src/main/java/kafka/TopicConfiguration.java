package kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.apache.kafka.clients.admin.NewTopic;

@Configuration
public class TopicConfiguration {

    @Bean
    public NewTopic cameraTopic1() {
        return TopicBuilder.name("cameratopic1").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic cameraTopic2() {
        return TopicBuilder.name("cameratopic2").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic tooFastTopic(@Value("${app.topic.tofasttopic}") String topicName) {
        return TopicBuilder.name(topicName).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic ownerInfoTopic(@Value("${app.topic.ownerinfotopic}") String topicName) {
        return TopicBuilder.name(topicName).partitions(1).replicas(1).build();
    }
}

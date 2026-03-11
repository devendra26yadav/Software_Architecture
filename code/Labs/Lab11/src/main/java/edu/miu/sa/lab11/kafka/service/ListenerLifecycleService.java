package edu.miu.sa.lab11.kafka.service;

import java.util.List;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
public class ListenerLifecycleService {

    public static final String OFFSET_LATEST_LISTENER = "offset-latest-listener";
    public static final String OFFSET_EARLIEST_LISTENER = "offset-earliest-listener";

    private final KafkaListenerEndpointRegistry registry;

    public ListenerLifecycleService(KafkaListenerEndpointRegistry registry) {
        this.registry = registry;
    }

    public void startOffsetListeners() {
        changeState(List.of(OFFSET_LATEST_LISTENER, OFFSET_EARLIEST_LISTENER), true);
    }

    public void stopOffsetListeners() {
        changeState(List.of(OFFSET_LATEST_LISTENER, OFFSET_EARLIEST_LISTENER), false);
    }

    private void changeState(List<String> listenerIds, boolean start) {
        for (String listenerId : listenerIds) {
            MessageListenerContainer container = registry.getListenerContainer(listenerId);
            if (container == null) {
                continue;
            }
            if (start && !container.isRunning()) {
                container.start();
            }
            if (!start && container.isRunning()) {
                container.stop();
            }
        }
    }
}

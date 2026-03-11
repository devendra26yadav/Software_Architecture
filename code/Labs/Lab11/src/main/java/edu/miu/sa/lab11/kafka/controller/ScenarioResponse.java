package edu.miu.sa.lab11.kafka.controller;

import java.util.List;

public record ScenarioResponse(
        String scenario,
        String topic,
        String message,
        List<String> orderNumbers
) {
}

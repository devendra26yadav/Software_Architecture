package edu.miu.sa.lab9.part1.shared;

public interface DomainEventPublisher {
    void publish(Object event);
}


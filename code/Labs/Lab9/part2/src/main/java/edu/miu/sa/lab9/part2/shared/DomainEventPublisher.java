package edu.miu.sa.lab9.part2.shared;

public interface DomainEventPublisher {
    void publish(Object event);
}


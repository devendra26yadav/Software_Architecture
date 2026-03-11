package edu.miu.sa.lab9.part4.shared;

public interface DomainEventPublisher {
    void publish(Object event);
}


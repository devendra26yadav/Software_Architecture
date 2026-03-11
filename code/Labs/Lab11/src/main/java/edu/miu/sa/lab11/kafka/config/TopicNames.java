package edu.miu.sa.lab11.kafka.config;

public final class TopicNames {

    public static final String BASIC_ORDERS = "orders.basic";
    public static final String OFFSET_ORDERS = "orders.offsets";
    public static final String COMPETING_ORDERS = "orders.competing";
    public static final String PARTITIONED_ORDERS = "orders.partitioned";
    public static final String BLOCKING_RETRY_ORDERS = "orders.error.blocking";
    public static final String BLOCKING_RETRY_DLT = BLOCKING_RETRY_ORDERS + ".DLT";
    public static final String RETRYABLE_ORDERS = "orders.error.retryable";
    public static final String PRODUCER_BATCH_ORDERS = "orders.batch.producer";
    public static final String CONSUMER_BATCH_ORDERS = "orders.batch.consumer";
    public static final String TRANSACTIONAL_ORDERS = "orders.transactional";

    private TopicNames() {
    }
}

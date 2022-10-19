package com.capol.component.framework.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "capol.rabbitmq")
public class RabbitMqProperties {
    private String host = "";
    private int port = 5672;
    private String username = "";
    private String password = "";

    private String exchange = "capol-rabbit-exchange-bak";
    private String syncQueue = "capol-sync-queue-bak";
    private String deleteQueue = "capol-delete-queue-bak";
    private String syncRouting = "capol-sync-routing-bak";
    private String deleteRouting = "capol-delete-routing-bak";

    private Integer maxConcurrentConsumers = 10;
    private Integer concurrentConsumers = 2;

    @Override
    public String toString() {
        return String.format("Host: %s, Port: %s, UserName: %s, Password: %s, Exchange: %s, Sync-Queue: %s, Delete-Queue: %s, Sync-Routing: %s, Delete-ROUTING: %s, MaxConcurrentConsumers: %d, ConcurrentConsumers: %d",
                host,
                port,
                username,
                password,
                exchange,
                syncQueue,
                deleteQueue,
                syncRouting,
                deleteRouting,
                maxConcurrentConsumers,
                concurrentConsumers);
    }
}
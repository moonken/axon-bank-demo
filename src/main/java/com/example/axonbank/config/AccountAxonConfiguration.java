package com.example.axonbank.config;

import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventsourcing.AbstractAggregateFactory;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.AggregateSnapshotter;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.GenericAggregateFactory;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.spring.config.EnableAxon;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.axonbank.account.domain.Account;
import com.mongodb.MongoClient;

@Configuration
@EnableAxon
public class AccountAxonConfiguration {
    @Bean
    public EventStorageEngine eventStoreEngine(MongoClient client) {
        return new MongoEventStorageEngine(new DefaultMongoTemplate(client));
    }

    @Bean
    public TransactionManager axonTransactionManager(PlatformTransactionManager platformTransactionManager) {
        return new SpringTransactionManager(platformTransactionManager);
    }

    @Bean
    public EventStore embeddedEventStore(EventStorageEngine eventStorageEngine) {
        return new EmbeddedEventStore(eventStorageEngine);
    }

    @Bean
    public AbstractAggregateFactory<Account> springPrototypeAggregateFactory(){
        return new GenericAggregateFactory<>(Account.class);
    }

    @Bean
    public AggregateSnapshotter snapShotter(EventStore eventStore, AggregateFactory<Account> accountRepository) {
        return new AggregateSnapshotter(eventStore, accountRepository);
    }

    @Bean
    public SnapshotTriggerDefinition snapshotTriggerDefinition(AggregateSnapshotter snapShotter) {
        return new EventCountSnapshotTriggerDefinition(snapShotter, 100);
    }
}

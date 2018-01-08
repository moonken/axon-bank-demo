package com.example.axonbank.account.intrastructure;

import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.axonbank.account.domain.Account;

@Repository("AccountRepository")
public class AccountRepository extends EventSourcingRepository<Account> {

    @Autowired
    public AccountRepository(EventStore eventStore, SnapshotTriggerDefinition snapshotTriggerDefinition) {
        super(Account.class, eventStore, snapshotTriggerDefinition);
    }

    public Account findAccound(String contactId) {
        return load(contactId).getWrappedAggregate().getAggregateRoot();
    }
}
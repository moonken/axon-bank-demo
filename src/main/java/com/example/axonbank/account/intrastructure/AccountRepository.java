package com.example.axonbank.account.intrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path="accounts")
public interface AccountRepository extends JpaRepository<Account, String> {
}

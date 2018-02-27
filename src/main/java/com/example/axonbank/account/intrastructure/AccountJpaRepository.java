package com.example.axonbank.account.intrastructure;

import com.example.axonbank.account.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountJpaRepository extends CrudRepository<Account, String> {
}

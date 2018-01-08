package com.example.axonbank.account.intrastructure;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Account {
    @Id
    private String id;
    private String nrb;
    private String user;
}

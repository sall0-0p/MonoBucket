package com.lordbucket.bucketbank.model.listeners;

import com.lordbucket.bucketbank.model.Account;
import com.lordbucket.bucketbank.util.CardNumberUtil;
import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountEntityListener {
    @PostPersist
    public void assignCardNumber(Account account) {
        account.setCardNumber( CardNumberUtil.generateCardNumber(account.getId()));
    }
}

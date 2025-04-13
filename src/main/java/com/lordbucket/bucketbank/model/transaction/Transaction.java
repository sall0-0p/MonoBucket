package com.lordbucket.bucketbank.model.transaction;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transaction_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column
    private boolean refunded = false;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

    public Transaction() {}

    public int getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public boolean isRefunded() { return refunded; }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setRefunded(boolean refunded) { this.refunded = refunded; }
}

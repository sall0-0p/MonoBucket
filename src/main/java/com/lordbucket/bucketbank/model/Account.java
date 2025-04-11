package com.lordbucket.bucketbank.model;

import com.lordbucket.bucketbank.util.CreditCardUtil;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "accounts")
@EntityListeners(AuditingEntityListener.class)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, updatable = false)
    private String cardNumber = CreditCardUtil.generateCardNumber(this.id);

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "account_authorizations",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> authorizedUsers;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column
    private boolean suspended = false;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTimestamp;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTimestamp;

    public Account() {};

    public int getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public User getOwner() {
        return owner;
    }

    public Set<User> getAuthorizedUsers() {
        return authorizedUsers;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public Date getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setAuthorizedUsers(Set<User> authorizedUsers) {
        this.authorizedUsers = authorizedUsers;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;
        return id == account.id && suspended == account.suspended && cardNumber.equals(account.cardNumber) && owner.equals(account.owner) && authorizedUsers.equals(account.authorizedUsers) && balance.equals(account.balance) && createdTimestamp.equals(account.createdTimestamp) && updatedTimestamp.equals(account.updatedTimestamp);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", owner=" + owner +
                ", authorizedUsers=" + authorizedUsers +
                ", balance=" + balance +
                ", suspended=" + suspended +
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                '}';
    }
}

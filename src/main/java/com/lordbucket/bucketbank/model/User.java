package com.lordbucket.bucketbank.model;

import com.lordbucket.bucketbank.util.Role;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String pinHash;

    @Column
    private boolean suspended = false;

    @OneToMany(mappedBy="owner", cascade=CascadeType.ALL)
    private List<Account> accountsOwned = new ArrayList<>();

    @ManyToMany(mappedBy="authorizedUsers")
    private Set<Account> accessibleAccounts = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", unique = true)
    private Account primaryAccount;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTimestamp;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTimestamp;

    @Column
    private Role role = Role.USER;

    public User() {}

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPinHash() {
        return pinHash;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public Account getPrimaryAccount() { return primaryAccount; }

    public List<Account> getAccountsOwned() {
        return accountsOwned;
    }

    public Set<Account> getAccessibleAccounts() {
        return accessibleAccounts;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setUsername(String username) { this.username = username; }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public void setPrimaryAccount(Account account) { this.primaryAccount = account; }

    public void setAccountsOwned(List<Account> accountsOwned) {
        this.accountsOwned = accountsOwned;
    }

    public void setAccessibleAccounts(Set<Account> accessibleAccounts) {
        this.accessibleAccounts = accessibleAccounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return id == user.id && suspended == user.suspended && username.equals(user.username) && pinHash.equals(user.pinHash) && accountsOwned.equals(user.accountsOwned) && accessibleAccounts.equals(user.accessibleAccounts) && createdTimestamp.equals(user.createdTimestamp) && updateTimestamp.equals(user.updateTimestamp);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", pinHash='" + pinHash + '\'' +
                ", suspended=" + suspended +
                ", accountsOwned=" + accountsOwned +
                ", accessibleAccounts=" + accessibleAccounts +
                ", createdTimestamp=" + createdTimestamp +
                ", updateTimestamp=" + updateTimestamp +
                '}';
    }
}

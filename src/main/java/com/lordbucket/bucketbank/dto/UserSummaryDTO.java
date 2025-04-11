package com.lordbucket.bucketbank.dto;

import com.lordbucket.bucketbank.model.User;

public class UserSummaryDTO {
    private int id;
    private String username;
    private boolean suspended;

    public UserSummaryDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.suspended = user.isSuspended();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSuspended() {
        return suspended;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSummaryDTO that = (UserSummaryDTO) o;
        return id == that.id && suspended == that.suspended && username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "UserSummaryDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", suspended=" + suspended +
                '}';
    }
}

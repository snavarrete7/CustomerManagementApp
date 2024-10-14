package com.example.customerservice.domain;

import jakarta.persistence.*;

@Entity
@Table(name="CUSTOMERS")
public class Customers {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public long id;

    @Column(name="USERNAME")
    public String username;
    @Column(name="PASSWORD")
    public String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

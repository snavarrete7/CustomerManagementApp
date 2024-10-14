package com.example.customerservice.domain;

import jakarta.persistence.*;

@Entity
@Table(name="CUSTOMERS")
public class Customers {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public long id;

    @Column(name="NAME")
    public String name;
    @Column(name="PASSWORD")
    public String password;

    @Column(name = "EMAIL")
    public String email;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

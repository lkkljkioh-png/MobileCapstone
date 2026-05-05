package com.tp.user;

import jakarta.persistence.*;

@Entity
@Table(name = "userdata")

//사용자 id와 사용자 닉네임
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_serialNumber")
    private Long serialNumber;
    @Column(name = "user_id")
    private String id;
    @Column(name = "user_name")
    private String name;

    public User() {}

    public void setSerialNumber(Long serialNumber) { this.serialNumber = serialNumber; }
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    public Long getSerialNumber() { return serialNumber; }
    public String getId() { return id; }
    public String getName() { return name; }

    public User(long serialNumber, String id, String name) {
        this.serialNumber = serialNumber;
        this.id = id;
        this.name = name;
    }
}

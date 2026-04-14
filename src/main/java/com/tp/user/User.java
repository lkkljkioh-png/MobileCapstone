package com.tp.user;

//사용자 id와 사용자 이름(닉네임?)
public class User {

    public User() {}

    private String id;
    private String name;

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    public String getId() { return id; }
    public String getName() { return name; }

    public User(long sequence, String id, String name) {
        this.id = id;
        this.name = name;
    }
}

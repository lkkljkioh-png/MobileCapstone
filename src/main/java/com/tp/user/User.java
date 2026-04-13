package com.tp.user;

//사용자 id와 사용자 이름(닉네임?)
public class User extends UserApplication {

    private String id;
    private String name;

    public User(String id, String name) {
        this.id = id;
        this.name  = name;
    }

    public User() {}

    public User(long sequence, String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
}

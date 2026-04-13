package com.tp.user;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private final Map<String, UserApplication> store = new HashMap<>();
    private long sequence = 0L;

    public User join(String id,String name) {
        sequence++;
        User user = new User(sequence, id, name); // user객체 생성
        store.put(user.getId(), user); // 저장소에 보관
        return user;
    }
}
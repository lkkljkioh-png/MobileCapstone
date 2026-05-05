package com.tp.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public String createUser(@RequestBody UserRequest request) {
        String id = request.getId();
        String name = request.getName();
        return "정상적으로 회원가입이 완료되었습니다 !";
    }

}

//test !
//@GetMapping("/test")
//public User testJoin() {
//    return userService.join("test_user", "권은재");
//}

package com.habibi.modern.controller;

import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping()
    public void signUp(@RequestBody UserSignUpDto userSignUpDto){
        if(!userService.isValid(userSignUpDto))
            return; //todo throw an exception

        userService.signUp(userSignUpDto);
    }
}
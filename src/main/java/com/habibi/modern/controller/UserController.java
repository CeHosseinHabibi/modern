package com.habibi.modern.controller;

import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.service.UserServiceProxy;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserServiceProxy userServiceProxy;

    @PostMapping()
    public void signUp(@RequestBody UserSignUpDto userSignUpDto){
        if(!userServiceProxy.isValid(userSignUpDto))
            return; //todo throw an exception

        userServiceProxy.signUp(userSignUpDto);
    }
}
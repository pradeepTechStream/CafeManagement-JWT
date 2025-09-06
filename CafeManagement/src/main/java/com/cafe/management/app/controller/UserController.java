package com.cafe.management.app.controller;

import com.cafe.management.app.service.UserService;
import com.cafe.management.app.utils.CafeUtils;
import com.cafe.management.app.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cafe.management.app.contant.CafeConstant.SOMETHING_WENT_WRONG;

@RestController
@RequestMapping(path="/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path="/signup")
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String,String> requestMap){
        try {
           return userService.signUp(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path="/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String,String> requestMap){
        try {
            return userService.login(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path="/get")
    public ResponseEntity<List<UserWrapper>> getAllUser(){
        try{
            return userService.getAllUser();
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return  new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path="/update")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String,String> requestMap){
        try {
            return userService.update(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path="/checkToken")
    public ResponseEntity<String> checkToken(){
        try {
            return userService.checkToke();
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path="/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody(required = true) Map<String,String> requestMap){
        try {
            return userService.changePassword(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path="/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody(required = true) Map<String,String> requestMap){
        try {
            return userService.forgotPassword(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

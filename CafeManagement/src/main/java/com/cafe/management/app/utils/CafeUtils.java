package com.cafe.management.app.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class CafeUtils {

    private CafeUtils(){

    }

    public static ResponseEntity<String> getResponseEntity(String message, HttpStatus httpStatus){
        return  new ResponseEntity<String>("{\"message\":\""+message+"\"}", httpStatus);
    }
}

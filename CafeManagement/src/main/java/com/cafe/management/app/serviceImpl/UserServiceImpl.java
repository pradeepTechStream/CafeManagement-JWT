package com.cafe.management.app.serviceImpl;

import com.cafe.management.app.contant.CafeConstant;
import com.cafe.management.app.jwt.CustomerUsersDetailsService;
import com.cafe.management.app.jwt.JwtFilter;
import com.cafe.management.app.jwt.JwtUtil;
import com.cafe.management.app.pojo.User;
import com.cafe.management.app.repository.UserRepository;
import com.cafe.management.app.service.UserService;
import com.cafe.management.app.utils.CafeUtils;
import com.cafe.management.app.utils.EmailUtil;
import com.cafe.management.app.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

import static com.cafe.management.app.contant.CafeConstant.INVALID_DATA;
import static com.cafe.management.app.contant.CafeConstant.SOMETHING_WENT_WRONG;
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired(required = true)
    private EmailUtil emailUtil;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try{
            if(validateSignUpMap(requestMap)){
                User user=userRepository.findByEmailId(requestMap.get("email"));
                if(Objects.isNull(user)){
                    userRepository.save(getUserFromMap(requestMap));
                    return  CafeUtils.getResponseEntity("Successfully Register",HttpStatus.OK);
                }else{
                    return  CafeUtils.getResponseEntity("User Already exist with this email",HttpStatus.BAD_REQUEST);
                }
            }else{
                return CafeUtils.getResponseEntity(INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private boolean validateSignUpMap(Map<String, String> requestMap){
        if(requestMap.containsKey("name")&&requestMap.containsKey("password")&&requestMap.containsKey("contactNumber")
        && requestMap.containsKey("email")){
            return  true;
        }
        return  false;
    }

    private User getUserFromMap(Map<String, String> requestMap){
        User user= User.builder()
                .name(requestMap.get("name"))
                .contactNumber(requestMap.get("contactNumber"))
                .password(requestMap.get("password"))
                .email(requestMap.get("email"))
                .status("false")
                .role("user")
                .build();
        return  user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
     log.info("Inside login implementation");
         try{
             Authentication auth = authenticationManager.authenticate(
                     new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
             );
             if(auth.isAuthenticated()){
                if(customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    String tokenValue = jwtUtil.generateToke(customerUsersDetailsService.getUserDetail().getEmail(),customerUsersDetailsService.getUserDetail().getRole());
                    return new ResponseEntity<String>("{\"token\":\""+tokenValue+"\"}",HttpStatus.OK);
                }else{
                    return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval"+"\"}",HttpStatus.BAD_REQUEST);
                }
             }
         }catch (Exception exception){
             exception.printStackTrace();
         }
        return new ResponseEntity<String>("{\"message\":\""+"Bas Credential"+"\"}",HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userRepository.getAllUser(),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                Optional<User> user = userRepository.findById(Long.parseLong(requestMap.get("id")));
                if(!user.isEmpty()){
                    userRepository.updateStatus(requestMap.get("status"),Long.parseLong(requestMap.get("id")));
                    //sendEmailToAll(requestMap.get("status"),user.get().getEmail(),userRepository.getAllAdmin());
                    return  CafeUtils.getResponseEntity("User updates successfully",HttpStatus.OK);
                }else{
                    return  CafeUtils.getResponseEntity("User id not exist",HttpStatus.OK);
                }
            }else {
                return  CafeUtils.getResponseEntity(CafeConstant.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToke() {
        try
        {

        }catch (Exception exception){

        }
        return CafeUtils.getResponseEntity("true",HttpStatus.OK);

    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try
        {
            User user = userRepository.findByEmailId(jwtFilter.getCurrentUser());
            if(user!=null){
                if(user.getPassword().equalsIgnoreCase(requestMap.get("oldPassword"))){
                    user.setPassword(requestMap.get("newPassword"));
                    userRepository.save(user);
                    return CafeUtils.getResponseEntity("Password updates successfully",HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return CafeUtils.getResponseEntity("Incorrect old password",HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception exception){

        }
        return CafeUtils.getResponseEntity(SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try
        {
            User user = userRepository.findByEmailId(jwtFilter.getCurrentUser());
            if(user!=null && !StringUtils.isEmpty(user.getEmail())){
                if(user.getPassword().equalsIgnoreCase(requestMap.get("oldPassword"))){
                    return CafeUtils.getResponseEntity("Check your email for credential",HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return CafeUtils.getResponseEntity("Incorrect old password",HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception exception){

        }
        return CafeUtils.getResponseEntity(SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendEmailToAll(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status!=null && status.equalsIgnoreCase("true")){
            emailUtil.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Approve","USER- "+user+"\n approve by admin"+jwtFilter.getCurrentUser(),allAdmin);
        }else{
            emailUtil.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Approve","USER- "+user+"\n disable by admin"+jwtFilter.getCurrentUser(),allAdmin);
        }
    }
}

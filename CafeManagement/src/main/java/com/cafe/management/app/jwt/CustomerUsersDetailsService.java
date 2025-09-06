package com.cafe.management.app.jwt;

import com.cafe.management.app.pojo.User;
import com.cafe.management.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomerUsersDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    private User user;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      log.info("Inside CustomerUsersDetailsService.loadUserByUsername()");
        user=userRepository.findByEmailId(username);
        if(!Objects.isNull(user)){
            return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),new ArrayList<>());
        }else{
            throw new UsernameNotFoundException("User not found.");
        }
    }

    public User getUserDetail() {
        return user;
    }
}

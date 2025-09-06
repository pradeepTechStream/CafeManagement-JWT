package com.cafe.management.app.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
public class EmailUtil {

    @Autowired
    JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text, List<String> ccList){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("pradeepmaurya560@gmail.com");
        message.setSubject(subject);
        message.setTo(to);
        message.setText(text);
        if(!CollectionUtils.isEmpty(ccList)){
            message.setCc(getCCList(ccList));
        }
        emailSender.send(message);

    }

    private String[] getCCList(List<String> ccList){
        String[] cc=new String[ccList.size()];
        for(int i=0;i<ccList.size();i++){
            cc[i]=ccList.get(i);
        }
        return  cc;
    }
}

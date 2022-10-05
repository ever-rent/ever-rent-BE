package com.finalproject.everrent_be.domain.auth.emailverified.service;

import com.nimbusds.oauth2.sdk.auth.Secret;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterMail {


    private final JavaMailSender emailsender;

    private String ePw; //인증번호 생성, createKey를 바로 넣어주면 런 타임시 ePw의 키를 생성해버리기 때문에 따로 뺌
    private MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = emailsender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to); //보내는 대상

        message.setSubject("Everrent 인증 코드입니다.");
        message.setText("이메일 인증코드: "+ePw);

        message.setFrom(new InternetAddress("bsj9254@naver.com","Everrent_Admin"));

        return  message;
    }

    public String createKey()
    {
        StringBuffer key=new StringBuffer();
        Random rnd=new Random();

        for(int i=0;i<8;i++)
        {
            int index= rnd.nextInt(3); //rnd에 따라 switch문 실행
            switch(index)
            {
                case 0:
                    key.append((char)((int)(rnd.nextInt(26))+97));//영어 소문자

                    //a-z 1+97='b'
                    break;
                case 1:
                    key.append((char)((int)(rnd.nextInt(26))+65));//영어 대문자
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));//숫자 랜덤
                    break;
            }

        }

        return key.toString();
    }

    public String sendSimpleMessage(String to)  throws Exception {
        ePw=createKey();
        MimeMessage message=createMessage(to);

        try{
            emailsender.send(message);
        }catch (MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw;
    }

}

package ssm.service;

import org.springframework.stereotype.Service;


public interface CommonsService {
    
    public void sendActivateMail(String toMail, String emailMsg);
}

package ssm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssm.dao.UserMapper;
import ssm.entity.User;
import ssm.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    UserMapper mapper;
    
    //注册
    @Override
    public boolean register(User user) {
        int row = 0;
        row = mapper.register(user);
        return row>0;
    }
    
    //激活
    @Override
    public void mailActivate(String activeCode) {
        
        mapper.mailActivate(activeCode);
    }

    //检测用户姓名是否已存在
    @Override
    public boolean checkUserName(String username) {
        Long isExist = 0L;
        isExist = mapper.checkUserName(username);
        return isExist>0;
    }

    @Override
    public User login(String username, String password) {
        return mapper.login(username, password);
    }
}

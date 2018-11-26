package ssm.service;

import ssm.entity.User;

public interface UserService {
    boolean register(User user);

    void mailActivate(String activeCode);

    boolean checkUserName(String username);

    User login(String username, String password);
}

package ssm.dao;

        import org.apache.ibatis.annotations.Param;
        import ssm.entity.User;

public interface UserMapper {
    int register(User user);

    void mailActivate(@Param("activeCode") String activeCode);

    Long checkUserName(@Param("username") String username);

    User login(@Param("username") String username,@Param("password") String password);
}

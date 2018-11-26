package ssm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ssm.entity.User;
import ssm.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequestMapping("/user")
@Controller
public class UserController {
    
    @Autowired
    UserService userService;
    
    //登录
    @RequestMapping(value="/login")
    public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        //如何对密码加密？ MD5算法

        User user = userService.login(username, password);

        //判断用户是否登录成功 user是否是null
        if(user!=null){
            //登录成功
            // 判断用户是否勾选了自动登录
            String autoLogin = request.getParameter("autoLogin");
            if("true".equals(autoLogin)){
                //需要自动登录，使用cookie
                Cookie cookie_username = new Cookie("cookie_username", user.getUsername());
                Cookie cookie_password = new Cookie("cookie_password", user.getPassword());
                cookie_password.setMaxAge(10*60);
                
                response.addCookie(cookie_username);
                response.addCookie(cookie_password);
            }
            session.setAttribute("user", user);
            return "redirect:"+ request.getContextPath() +"/index.jsp";
        } else {
            request.setAttribute("loginError", "用户名或密码错误");
            return "redirect:/login.jsp";
        }
    }
    
    @RequestMapping(value = "/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/index";
    }
}

package ssm.controller;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ssm.entity.User;
import ssm.service.CommonsService;
import ssm.service.UserService;
import ssm.utils.CommonsUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RequestMapping("/register")
@Controller
public class RegisterController {
    @Autowired
    UserService service;

    @Autowired
    CommonsService sendMailService;
    
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String doRegister(HttpServletRequest request) throws UnsupportedEncodingException {
        
        request.setCharacterEncoding("UTF-8");
        
        //获取表单信息
        Map<String, String[]> properties = request.getParameterMap();
        User user = new User();
        try {
            // 作为类型转换器(string--->date)
            ConvertUtils.register(new Converter() {
                @Override
                public Object convert(Class aClass, Object o) {
                    
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date parse = null;
                    try {
                        parse = format.parse(o.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return parse;
                }
            }, Date.class);
            BeanUtils.populate(user,properties);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } 
        
        //uid
        user.setUid(CommonsUtils.getUUID());
        //TELEPHONE
        user.setTelephone(null);
        //  int state 
        user.setState(0);
        // code 激活码
        String activeCode = CommonsUtils.getUUID();
        user.setCode(activeCode);
        
        //将user传递到service层
        boolean isRegisterSuccess = service.register(user);
        
        //是否注册成功
        if(isRegisterSuccess){
            //发送激活邮件
            String emailMsg = "恭喜您注册成功！请点击下面的链接激活您的账户" 
                    + "<a href='http://localhost:8080/register/active?activeCode=" + activeCode + "'>"
                    + "http://localhost:8080/active?activeCode=" + activeCode + "</a>";
            sendMailService.sendActivateMail(user.getEmail(),emailMsg);
            //跳转到注册成功页面
            return "redirect:"+ request.getContextPath() + "/registerSuccess.jsp";
        } else {
            //注册失败
            return "redirect:"+ request.getContextPath() + "/registerFail.jsp";
        }
    }
    
    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public String doActivate(HttpServletRequest request){

        //获取激活码
        String activeCode = request.getParameter("activeCode");
        
        service.mailActivate(activeCode);
        
        return "redirect:"+ request.getContextPath() + "/login.jsp";
    }
    
    @ResponseBody         //返回json，需要此注解
    @RequestMapping("/checkUsername")
    public String doCheckUserName(HttpServletRequest request){
        
        String userName = request.getParameter("username");
        boolean isExist =  service.checkUserName(userName);
        String json = "{\"isExist\":" + isExist + "}";
        
        return json;
    }
    
}

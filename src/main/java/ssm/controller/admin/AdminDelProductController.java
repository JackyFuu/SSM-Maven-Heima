package ssm.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ssm.service.AdminProductService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdminDelProductController {
    
    @Autowired
    AdminProductService service;
    
    @RequestMapping(value = "/adminDelProduct", method = RequestMethod.GET)
    public String delProduct(HttpServletRequest request){
        String pid = request.getParameter("pid");
        service.delProductById(pid);
        return "redirect:/adminProductList";
    }
}

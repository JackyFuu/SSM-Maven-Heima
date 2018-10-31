package ssm.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ssm.entity.Category;
import ssm.entity.Product;
import ssm.service.AdminProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class AdminUpdateProductUIController {
    
    @Autowired
    AdminProductService service;
    
    @RequestMapping(value = "/adminUpdateProductUI", method = RequestMethod.GET)
    public String updateProductUI(HttpServletRequest request, HttpServletResponse response){
        String pid = request.getParameter("pid");
        Product product = service.findProductById(pid);
        request.setAttribute("product", product);
        
        List<Category> categoryList = service.findAllCategory();
        request.setAttribute("categoryList", categoryList);
        return "/admin/product/edit";
    }
}

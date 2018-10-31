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
public class AdminProductListController {
    
    @Autowired
    AdminProductService service;
    
    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    private String getHome(){
        return "/admin/home";
    }
    
    @RequestMapping(value = "/adminProductList", method = RequestMethod.GET)
    protected String doGet(HttpServletRequest request, HttpServletResponse response){
        List<Product> productList = service.findAllProduct();
        List<Category> categoryList = service.findAllCategory();
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("productList", productList);
        return "/admin/product/list";
    }
}

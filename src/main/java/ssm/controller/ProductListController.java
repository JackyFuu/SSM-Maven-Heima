package ssm.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ssm.service.ProductService;
import ssm.vo.PageBean;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProductListController {
    
    @Autowired
    ProductService service;
    
    // 实现分页获取商品列表
    @RequestMapping(value = "/productList", method = RequestMethod.GET)
    public String getMainPage(HttpServletRequest request){
        
        //获得当前页  从request域中取出的数据都是string
        String currentPageStr = request.getParameter("currentPage");
            // 如果为首次访问，则默认显示第一页
        if(currentPageStr==null) currentPageStr="1";    
        int currentPage = Integer.parseInt(currentPageStr);
//        int currentPage = 1;
        // 认为每页显示12条
        int currentCount = 12;
        PageBean pageBean = service.findPageBean(currentPage, currentCount);
        request.setAttribute("pageBean", pageBean);
        return "/product_list";
    }
}

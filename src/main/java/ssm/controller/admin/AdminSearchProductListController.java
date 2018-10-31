package ssm.controller.admin;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ssm.entity.Category;
import ssm.entity.Product;
import ssm.service.AdminProductService;
import ssm.vo.ConditionVo;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Controller
public class AdminSearchProductListController {

    @Autowired
    AdminProductService service;

    @RequestMapping(value = "/adminSearchProductList", method = RequestMethod.POST)
    public String getSearchProductList(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");

        //1、收集表单数据
        Map<String, String[]> properties = request.getParameterMap();
        //2、将散装的查询数据封装到一个VO实体中
        ConditionVo conditionVo = new ConditionVo();
        try {
            BeanUtils.populate(conditionVo, properties);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        //准备商品类别
        //获得所有的商品的类别数据
        List<Category> categoryList = service.findAllCategory();
        
        //3、将实体类传递到service层
        List<Product> productList = service.findProductListByConditionVo(conditionVo);
        request.setAttribute("condition", conditionVo);
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("productList", productList);
        return "/admin/product/list";
    }
}    

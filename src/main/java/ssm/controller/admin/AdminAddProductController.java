package ssm.controller.admin;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ssm.entity.Product;
import ssm.service.AdminProductService;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Controller
public class AdminAddProductController {
    @Autowired
    AdminProductService service;

    @RequestMapping(value = "/adminAddProduct", method = RequestMethod.POST)
    public String doAddProduct(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        //1获取数据
        Map<String, String[]> properties = request.getParameterMap();
        //2封装数据
        Product product = new Product();
        try {
            BeanUtils.populate(product, properties);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
            //此位置product已经封装完毕，将表单的数据封装完毕
            //手动设置表单中没有的数据
            //1、pid
            product.setPid(UUID.randomUUID().toString());
            //2、pImage  ---已写死
            product.setPimage("products/1/c_0033.jpg");
            //3、pDate //上架日期
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String pDate = format.format(new Date());
            product.setPdate(pDate);
            //4、pFlag // 商品是否下架 ---已写死
            product.setPflag(0);
            //3传递数据给service层
            service.addProduct(product);

            return "redirect:/adminProductList"; 
    }
}

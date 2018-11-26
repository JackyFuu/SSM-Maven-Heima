package ssm.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ssm.cache.RedisCache;
import ssm.entity.Category;
import ssm.service.ProductService;

import java.util.List;

@Controller
public class CategoryListController {
    
    @Autowired
    ProductService service;
    @Autowired
    private RedisCache cache;
    
    //把数据缓存到内存当中
    @ResponseBody  //返回Json
    @RequestMapping(value = "/categoryList",
            produces = {"text/html;charset=UTF-8"})
    public String getCategoryList(){
//        //先从缓存当中查询，如果有，直接用，否者从数据库查询。
//        String categoryListJson = (String) cache.get("categoryListJson");
//        //判断categoryListJson是否为Null
//        if(categoryListJson==null){
//            System.out.println("缓存中没有数据 查询数据库.....");
            List<Category> categoryList = service.findAllCategory();
            Gson gson = new Gson();
            String categoryListJson = gson.toJson(categoryList);
//            cache.set("categoryListJson", categoryListJson);
//        } else {
//            System.out.println("查询缓存中.....");
//        }
        return categoryListJson;
    }
}

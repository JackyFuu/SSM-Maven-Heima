package ssm.controller.admin;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ssm.entity.Product;
import ssm.service.AdminProductService;
import ssm.utils.CommonsUtils;
import ssm.vo.ProductVo;
import sun.reflect.FieldInfo;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AdminAddProductController {
    @Autowired
    AdminProductService service;

    //添加商品
    @RequestMapping(value = "/adminAddProduct", method = RequestMethod.POST)
    public String addProduct(HttpServletRequest request) throws UnsupportedEncodingException {
        // 目的：实现带文件上传的添加商品功能。
        Product product = new Product();
        //收集数据的容器
        Map<String, Object> map = new HashMap<>();
        try {
            //1、创建磁盘文件工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //2、创建文件上传核心对象
            ServletFileUpload upload = new ServletFileUpload(factory);
            //3、通过文件上传核心对象 upload 解析request域中的文件项集合
            List<FileItem> parseRequest = upload.parseRequest(request);
            //4、遍历List中的每一个表单项
            for(FileItem fileItem : parseRequest){
                if(fileItem.isFormField()){
                    //普通表单项，把他封装到product中
                    String fieldName = fileItem.getFieldName();
                    // 以UTF-8的编码格式从浏览器获得普通表单项内容（主要是为了防止中文乱码）。
                    String fieldValue = fileItem.getString("UTF-8");
                    //--做到这里，我们如何将这个表单项封装到product中呢？
                    //--如果一个一个去找可能会很麻烦，不如我们把上面两个字段以Map的形式存起来,
                    //--然后通过BeanUtils这个jar包（需要引入）将Map直接转成Product。
                    
                    map.put(fieldName, fieldValue);
                }else{
                    //文本上传项，获取文本名字，获取文件内容
                    String fileName = fileItem.getName();
                    String path = request.getSession().getServletContext().getRealPath("upload");
                    InputStream in = fileItem.getInputStream();
                    OutputStream out = new FileOutputStream(path +"/"+ fileName);
                    IOUtils.copy(in, out);
                    in.close();
                    out.close();
                    fileItem.delete();
                    
                    map.put("pimage","upload/"+fileName);
                }
            }
            // 通过BeanUtils将Map直接转成Product。
            BeanUtils.populate(product, map);
            //到此，product并没有封装完成
            //private String pid;
            product.setPid(CommonsUtils.getUUID());
            //private String pdate;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String pDate = format.format(new Date());
            product.setPdate(pDate);
            //private int pflag;
            product.setPflag(0);
            //到此，product封装完毕！！！

            service.addProduct(product);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/adminProductList";

        /** 没有文件上传时的添加商品代码表示
         * 
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
         */
    }
    
}

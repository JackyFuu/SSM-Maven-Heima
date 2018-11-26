package ssm.controller.admin;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ssm.entity.Order;
import ssm.entity.OrderItem;
import ssm.service.AdminProductService;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AdminOrderController {
    @Autowired
    AdminProductService service;

    /**
     *获得所有订单
     * @param request
     * @return
     */
    @RequestMapping(value = "/findAllOrders")
    public String findAllOrders(HttpServletRequest request){
        List<Order> orderList = service.findAllOrders();
        request.setAttribute("orderList", orderList);
        return "admin/order/list";
    }
    
    //根据订单id查询订单项和商品信息
    @ResponseBody
    @RequestMapping(value = "/findOrderInfoByOid",
        produces = {"text/html;charset=UTF-8"})
    public String findOrderInfoByOid(HttpServletRequest request){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String oid = request.getParameter("oid");
        List<OrderItem> orderItemList = service.finOrderInfoByOid(oid);
        Gson gson = new Gson();
        System.out.println(gson.toJson(orderItemList));
        return gson.toJson(orderItemList);
    }
}

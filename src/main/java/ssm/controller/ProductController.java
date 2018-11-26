package ssm.controller;


import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ssm.entity.Order;
import ssm.entity.OrderItem;
import ssm.entity.Product;
import ssm.entity.User;
import ssm.service.ProductService;
import ssm.utils.CommonsUtils;
import ssm.vo.Cart;
import ssm.vo.CartItem;
import ssm.vo.PageBean;
import ssm.vo.ProductVo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


@Controller
public class ProductController {
    
    @Autowired
    ProductService service;
    
    // 实现分页获取商品列表
    @RequestMapping(value = "/productList", method = RequestMethod.GET)
    public String getProductList(HttpServletRequest request){
        
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
    
    // 加载有热门商品和最新商品的页面
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getIndex(HttpServletRequest request){
        // 准备热门商品
        List<ProductVo> hotProductList = service.findHotProductList();
        //准备最新商品
        List<ProductVo> newProductList = service.findNewProductList();
        
        // 此处能不能够准备分类数据呢？
        // 此处能不能使用切面编程？
        //准备分类数据
        //List<Category> categoryList = service.findAllCategory();
        //request.setAttribute("categoryList", categoryList);
        
        request.setAttribute("hotProductList", hotProductList);
        request.setAttribute("newProductList", newProductList);
        return "/index";
    }
    
    //获取分类后的分页数据
    @RequestMapping(value = "/productListByCid")
    public String getProductListByCid(HttpServletRequest request){
        //获取Cid
        String cid = request.getParameter("cid");
        //当前页
        String currentPageStr = request.getParameter("currentPage");
        if(currentPageStr == null) currentPageStr = "1";
        int currentPage = Integer.parseInt(currentPageStr);
        //每页显示数
        int currentCount = 12;
        
        //通过service得到pageBean
        PageBean pageBean = service.findPageBeanByCid(cid, currentPage, currentCount);
        
        request.setAttribute("pageBean", pageBean);
        request.setAttribute("cid", cid);
        
        //定义一个根据cookie记录历史商品记录的集合
        List<Product> historyProductList = new ArrayList<>();
        //从客户端获得名为pids的cookie
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if("pids".equals(cookie.getName())){
                    String pids = cookie.getValue(); //3-2-1
                    String[] split = pids.split("-");
                    for(String pid : split){
                        Product product = service.findProductByPid(pid);
                        historyProductList.add(product);
                    }
                }
            }
        }
        //将历史记录的集合放到域中
        request.setAttribute("historyProductList", historyProductList);
        
        return "/product_list";
    }

    /**
     * 根据 pid 查看商品的详细信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/productInfo")
    public String getProductInfo(HttpServletRequest request, HttpServletResponse response){
        //获得商品id
        String pid = request.getParameter("pid");
        //由于要返回上一页，需要参数cid&currentPage
        String cid = request.getParameter("cid");
        String currentPage = request.getParameter("currentPage");
        
        //根据pid得到商品信息
        Product product = service.findProductByPid(pid);
        
        request.setAttribute("product", product);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("cid", cid);

        //接下来是将获得客户端带来的cookie,将当前的pid加入到cookie中，
        //产生新的cookies,再发送到客户端。
        //1、pids为cookie
        String pids= pid;
        //2、从request域中获得Cookies.
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies){
                if("pids".equals(cookie.getName())){
                    pids = cookie.getValue();
                    //1-3-2 本次访问商品pid是8----->8-1-3-2
                    //1-3-2 本次访问商品pid是3----->3-1-2
                    //1-3-2 本次访问商品pid是2----->2-1-3
                    //将pids拆成一个数组
                    String[] split = pids.split("-"); //{3,1,2}
                    List<String> asList = Arrays.asList(split);  //[3,1,2]
                    LinkedList<String> list = new LinkedList<String>(asList); //[3,1,2]
                    //判断集合中是否存在当前pid,如果存在，则删除它，然后再首部再添加pid
                    if(list.contains(pid)){
                        list.remove(pid);
                    }
                    list.addFirst(pid);
                    //再将[3,1,2]转成3-1-2的字符串
                    StringBuffer sb = new StringBuffer();
                    for(int i=0; i<list.size() && i<7; i++){
                        sb.append(list.get(i));
                        sb.append("-");
                    }
                    //去掉3-1-2-后的 -
                    pids = sb.substring(0, sb.length() - 1);
                }
            }
        }
        Cookie cookie_pids = new Cookie("pids", pids);
        response.addCookie(cookie_pids);
        return "/product_info";
    }
    
    //添加商品到购物车中
    @RequestMapping(value = "/addProductToCart")
    public String addProductToCart(HttpServletRequest request, HttpSession session){
        //获取商品的 pid 和 需要购买的数量
        String pid = request.getParameter("pid");
        int buyNum = Integer.parseInt(request.getParameter("buyNum"));

        // 封装CartItem
        //--根据pid查询商品
        ProductVo productVo = service.findProductVoByPid(pid);
        //--计算小计 subTotal
        double subTotal = productVo.getShop_price() * buyNum;
        //--开始封装
        CartItem item = new CartItem();
        item.setProductVo(productVo);
        item.setBuyNum(buyNum);
        item.setSubTotal(subTotal);
        
        //获取购物车，首先判断session中有没有购物车
        Cart cart = (Cart) session.getAttribute("cart");
        if(cart==null){
            cart = new Cart();
        }
        //将购物项放到车中---key是pid
        //先判断购物车中是否已将包含此购物项了 ----- 判断key是否已经存在
        //如果购物车中已经存在该商品----将现在买的数量与原有的数量进行相加操作
        Map<String, CartItem> cartItems = cart.getCartItems();
        
        double newSubtotal = 0.0;
        if(cartItems.containsKey(pid)){
            //取出购买数量，再将新增的数量加到原来的购买数量中
            int oldBuyNum = cartItems.get(pid).getBuyNum();
            oldBuyNum = oldBuyNum + buyNum;
            cartItems.get(pid).setBuyNum(oldBuyNum);
            //修改小计
            double oldSubtotal = cartItems.get(pid).getSubTotal();
            newSubtotal = buyNum * productVo.getShop_price();
            cartItems.get(pid).setSubTotal(oldSubtotal + newSubtotal);
        }else{
            // cart中没有该商品
            cart.getCartItems().put(productVo.getPid(), item);
            newSubtotal = buyNum * productVo.getShop_price() + newSubtotal;
        }
        //计算总计
        double total = cart.getTotal() + newSubtotal;
        cart.setTotal(total);
        
        session.setAttribute("cart", cart);
        return "redirect:/cart.jsp";
    }
    
    //从购物车删除单个商品订单。
    @RequestMapping(value = "/delProductFromCart")
    public String delProductFromCart(HttpServletRequest request, HttpSession session){
        //获取要删除的商品的pid
        String pid = request.getParameter("pid");
        //获取session中的购物车
        Cart cart = (Cart) session.getAttribute("cart");
        if(cart!=null){
            Map<String, CartItem> cartItems = cart.getCartItems();
            //改变购物车总价
            double total = cart.getTotal() - cart.getCartItems().get(pid).getSubTotal();
            cart.setTotal(total);
            //删除cartItems中的指定商品pid的商品。
            cartItems.remove(pid);
            cart.setCartItems(cartItems);
        }
        session.setAttribute("cart", cart);
        return "redirect:"+ request.getContextPath() +"/cart.jsp";
    }
    
    //清空购物车
    @RequestMapping(value = "/clearCart")
    public String clearCart(HttpServletRequest request, HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:" + request.getContextPath() + "/cart.jsp";
    }
    
    
    //提交订单
    @RequestMapping(value = "/submitOrder")
    public String submitOrder(HttpServletRequest request, HttpSession session){
        // 判断用户是否已经登录，没有登录，订单不能够提交
        User user = (User) session.getAttribute("user");
        if(user == null){
            return "redirect:/login.jsp";
        }
        // 封装一个Order对象传给Service层
        Order order = new Order();
        //1、private String oid;  //订单号
        order.setOid(CommonsUtils.getUUID());
        //2、private Date orderTime;  // 下单时间
        order.setOrderTime(new Date());
        //3、private double total;  // 订单总金额
        Cart cart = (Cart) session.getAttribute("cart");
        order.setTotal(cart.getTotal());
        //4、private int state;  //是否已付款，1:已付款  0：未付款
        order.setState(0);
        //5、private String address;  // 收货地址
        order.setAddress(null);
        //6、private String name;  // 收货人姓名
        order.setName(null);
        //7、private String telephone;  // 收货人联系电话
        order.setTelephone(null);
        //8、private User user; //订单属于哪个用户
        order.setUser(user);
        //9、 订单的订单项 List<OrderItem> orderItems = new ArrayList<>();
        Map<String, CartItem> cartItems = cart.getCartItems();
        for(Map.Entry<String, CartItem> entry : cartItems.entrySet()){
            //取出购物项
            CartItem cartItem = entry.getValue();
            //创建订单项
            OrderItem orderItem = new OrderItem();
            //1)private String itemId;
            orderItem.setItemId(CommonsUtils.getUUID());
            //2)private int count;
            orderItem.setCount(cartItem.getBuyNum());
            //3)private double subtotal;
            orderItem.setSubtotal(cartItem.getSubTotal());
            //4)private Product product;
            orderItem.setProduct(cartItem.getProductVo());
            //5)private Order order;
            orderItem.setOrder(order);
            
            //将订单项添加到订单的订单项集合中
            order.getOrderItems().add(orderItem);
        }
        //order对象封装完毕
        //传递数据到service层
        service.submitOrder(order);
        
        session.setAttribute("order", order);
        return "redirect:"+ request.getContextPath() +"/order_info.jsp";
    }
    
    //暂时无法实现 2018.11.20
//    //确认订单---更新收获人信息+在线支付
//    public String confirmOrder(HttpServletRequest request, HttpServletResponse response){
//        //1、更新收货人信息
//        Map<String, String[]> properties = request.getParameterMap();
//        Order order = new Order();
//        try {
//            BeanUtils.populate(order, properties);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//
//        service.updateOrderAddr(order);
//    }
    
    @RequestMapping(value = "/myOrders")
    public String myOrders(HttpServletRequest request, HttpSession session){
        User user = (User) session.getAttribute("user");
        //如果没有登录，不能够查看我的订单
        if(user==null){
            return "redirect:/login.jsp";
        }
        //查询用户的所有订单集合（单表查询orders表）
        List<Order> orderList = service.findAllOrders(user.getUid());
        //但是这样查询，Order对象中的数据是不完整的，缺少List<OrderItem> orderItems数据
        
        //对订单集合进行遍历，查询每个订单下的所有订单项，为每个订单填充订单项的信息
        if(orderList!=null){
            for(Order order : orderList){
                //获得每个订单的oid，根据oid去数据库中查询得到订单项信息
                String oid = order.getOid();
                List<OrderItem> orderItemList = service.findAllOrderItemByOid(oid);
                for(OrderItem orderItem : orderItemList){
                    order.getOrderItems().add(orderItem);
                }
            }
        }
        request.setAttribute("orderList", orderList);
        return "/order_list";
    }
}






























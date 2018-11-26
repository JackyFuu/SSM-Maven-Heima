package ssm.service;


import ssm.entity.Category;
import ssm.entity.Order;
import ssm.entity.OrderItem;
import ssm.entity.Product;
import ssm.vo.PageBean;
import ssm.vo.ProductVo;

import java.util.List;


public interface ProductService {
    public List<Product> findAllProduct();

    public PageBean findPageBean(int currentPage, int currentCount);

    List<ProductVo> findHotProductList();

    List<ProductVo> findNewProductList();

    List<Category> findAllCategory();

    PageBean findPageBeanByCid(String cid, int currentPage, int currentCount);

    Product findProductByPid(String pid);

    ProductVo findProductVoByPid(String pid);

    void submitOrder(Order order);

    void updateOrderAddr(Order order);

    List<Order> findAllOrders(String uid);

    List<OrderItem> findAllOrderItemByOid(String oid);
}

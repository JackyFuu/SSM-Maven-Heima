package ssm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssm.dao.ProductMapper;
import ssm.entity.Category;
import ssm.entity.Order;
import ssm.entity.OrderItem;
import ssm.entity.Product;
import ssm.service.ProductService;
import ssm.vo.PageBean;
import ssm.vo.ProductVo;

import java.beans.Transient;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper mapper;
    @Override
    public List<Product> findAllProduct() {

        return mapper.findAllProduct();
    }
    
    @Override
    public PageBean findPageBean(int currentPage, int currentCount) {
        // 目的就是想方法封装一个PageBean 并返回
        PageBean pageBean = new PageBean();
        // 1、当前页 int currentPage
        pageBean.setCurrentPage(currentPage);
        // 2、当前页显示的条数 int currentCount 
        pageBean.setCurrentCount(currentCount);
        // 3、总条数 private int totalPage;
        int totalCount = mapper.getTotalCount();
        pageBean.setTotalCount(totalCount);
        // 4、总页数private int totalPage;
        /*
         * 总条数		当前页显示的条数	总页数
         * 10		4				3
         * 11		4				3
         * 12		4				3
         * 13		4				4
         *
         * 公式：总页数=Math.ceil(总条数/当前显示的条数) //向上取整
         *
         */
        int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
        pageBean.setTotalPage(totalPage);
        //5、每页显示的数据private List<T> productList = new ArrayList<T>();
        /*
         * 页数与limit起始索引的关系
         * 例如 每页显示4条
         * 页数		起始索引		每页显示条数
         * 1		0			4
         * 2		4			4
         * 3		8			4
         * 4		12			4
         *
         * 索引index = (当前页数-1)*每页显示的条数
         *
         */
        int index = (currentPage - 1) * currentCount;
        List<Product> productList = mapper.findProductListForPageBean(index, currentCount);
        pageBean.setProductList(productList);
        return pageBean;
    }

    //找到最热商品列表
    @Override
    public List<ProductVo> findHotProductList() {
        return mapper.findHotProductList();
    }

    //找到最新商品
    @Override
    public List<ProductVo> findNewProductList() {
        return mapper.findNewProductList();
    }

    @Override
    public List<Category> findAllCategory() {
        return mapper.findAllCategory();
    }

    /**
     * 封装PageBean以此实现分页function.
     * @param cid 商品类别id
     * @param currentPage  当前页
     * @param currentCount  当前页个体个数
     * @return 封装好的PageBean
     */
    @Override
    public PageBean findPageBeanByCid(String cid, int currentPage, int currentCount) {
        //初始化一个PageBean
        PageBean<Product> pageBean = new PageBean<Product>();
        
        //1、封装当前页
        pageBean.setCurrentPage(currentPage);
        //2、每页个数
        pageBean.setCurrentCount(currentCount);
        //3、总条数
        int totalCount = mapper.getTotalCountByCid(cid);
        pageBean.setTotalCount(totalCount);
        //4、总页数
        int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
        pageBean.setTotalPage(totalPage);
        //5、当前页显示的数据
        int index = (currentPage - 1) * currentCount;
        List<Product> productList = mapper.findProductListForPageBeanByCid(cid, index, currentCount);
        pageBean.setProductList(productList);
        return pageBean;
    }

    @Override
    public Product findProductByPid(String pid) {
        return mapper.findProductByPid(pid);
    }

    @Override
    public ProductVo findProductVoByPid(String pid) {
        return mapper.findProductVoByPid(pid);
    }

    //使用事务操作
    @Override
    @Transactional
    public void submitOrder(Order order) {
        mapper.addOrders(order);
        List<OrderItem> items = order.getOrderItems();
        for(OrderItem orderItem : items){
            mapper.addOrderItem(orderItem);
        }
    }

    //更新用户地址
    @Override
    public void updateOrderAddr(Order order) {
        mapper.updateOrderAddr(order);
    }
    
    
    @Override
    public List<Order> findAllOrders(String uid) {
        return mapper.findAllOrdersByUid(uid);
    }

    @Override
    public List<OrderItem> findAllOrderItemByOid(String oid) {
        return mapper.findAllOrderItemByOid(oid);
    }
}

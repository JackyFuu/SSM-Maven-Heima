package ssm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssm.dao.AdminProductMapper;
import ssm.entity.Category;
import ssm.entity.Order;
import ssm.entity.OrderItem;
import ssm.entity.Product;
import ssm.service.AdminProductService;
import ssm.vo.ConditionVo;

import java.util.List;

@Service
public class AdminProductServiceImpl implements AdminProductService {
    @Autowired
    AdminProductMapper mapper;
    
    @Override
    public List<Product> findAllProduct() {
        return mapper.findAllProduct();
    }

    @Override
    public List<Category> findAllCategory() {
        return mapper.findAllCategory();
    }

    @Override
    public void addProduct(Product product) {
        mapper.addProduct(product);
    }

    @Override
    public void delProductById(String pid) {
        mapper.delProductById(pid);
    }

    @Override
    public Product findProductById(String pid) {
        return mapper.findProductById(pid);
    }

    @Override
    public void updateProduct(Product product) {
        mapper.updateProduct(product);
    }

    @Override
    public List<Product> findProductListByConditionVo(ConditionVo conditionVo) {
        return mapper.findProductListByConditionVo(conditionVo);
    }

    @Override
    public List<Order> findAllOrders() {
        return mapper.findAllOrders();
    }

    @Override
    public List<OrderItem> finOrderInfoByOid(String oid) {
        return mapper.finOrderInfoByOid(oid);
    }
}

package ssm.service;


import ssm.entity.Product;
import ssm.vo.PageBean;

import java.util.List;


public interface ProductService {
    public List<Product> findAllProduct();

    public PageBean findPageBean(int currentPage, int currentCount);
}

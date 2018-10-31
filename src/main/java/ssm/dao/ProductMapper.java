package ssm.dao;

import org.apache.ibatis.annotations.Param;
import ssm.entity.Product;

import java.util.List;

public interface ProductMapper {
    List<Product> findAllProduct();

    int getTotalCount();

    List<Product> findProductListForPageBean(@Param("offset") int index,@Param("limit") int currentCount);
}

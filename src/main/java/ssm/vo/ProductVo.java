package ssm.vo;

import ssm.entity.Category;
import ssm.entity.Product;

public class ProductVo extends Product {
    
    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

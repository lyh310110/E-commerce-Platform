package edu.fdzc.mallproduct.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.fdzc.mallproduct.entity.Product;
import edu.fdzc.mallproduct.entity.ProductComment;
import edu.fdzc.mallproduct.mapper.ProductMapper;
import edu.fdzc.mallproduct.mapper.ProductCommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import edu.fdzc.mallcommon.constant.OrderConstant;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    private final ProductMapper productMapper;
    private final ProductCommentMapper productCommentMapper;

    public List<Product> getAllProducts(String sort){
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", OrderConstant.PRODUCT_STATUS_ON_SALE);
        
        // 根据排序参数设置排序方式
        if (sort != null) {
            switch (sort) {
                case "price_asc":
                    queryWrapper.orderByAsc("price");
                    break;
                case "price_desc":
                    queryWrapper.orderByDesc("price");
                    break;
                case "sales_desc":
                    queryWrapper.orderByDesc("sale_count");
                    break;
                case "newest":
                    queryWrapper.orderByDesc("create_time");
                    break;
                default:
                    // 默认按创建时间降序排列
                    queryWrapper.orderByDesc("create_time");
                    break;
            }
        } else {
            // 默认按创建时间降序排列
            queryWrapper.orderByDesc("create_time");
        }
        
        return productMapper.selectList(queryWrapper);
    }

    public Product getProductById(Long id){
        return productMapper.selectById(id);
    }

    /**
     * 获取商品总数
     */
    public long getProductCount(){
        return baseMapper.selectCount(null);
    }
    /**
     * 卖家发布商品
     */
    public boolean publishProduct(Product product) {
        product.setStatus(OrderConstant.PRODUCT_STATUS_ON_SALE);
        product.setAuditStatus(OrderConstant.PRODUCT_AUDIT_PENDING);
        product.setSaleCount(0);
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());

        return baseMapper.insert(product) > 0;
    }

    /**
     * 卖家更新商品
     */
    public boolean updateProduct(Product product) {
        if (product.getId() == null) {
            return false;
        }

        product.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(product) > 0;
    }

    /**
     * 卖家下架商品
     */
    public boolean offShelfProduct(Long id, Long shopId) {
        Product product = baseMapper.selectById(id);
        if (product == null || !product.getShopId().equals(shopId)) {
            return false;
        }

        product.setStatus(OrderConstant.PRODUCT_STATUS_OFF_SALE);
        product.setUpdateTime(LocalDateTime.now());

        return baseMapper.updateById(product) > 0;
    }

    /**
     * 获取店铺商品列表
     */
    public List<Product> getShopProducts(Long shopId, Integer status) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id", shopId);

        if (status != null) {
            queryWrapper.eq("status", status);
        }

        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 获取商品评论列表
     */
    public List<ProductComment> getProductComments(Long productId) {
        return productCommentMapper.getCommentsByProductId(productId);
    }
}

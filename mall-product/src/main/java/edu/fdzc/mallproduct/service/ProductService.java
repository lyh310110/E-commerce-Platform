package edu.fdzc.mallproduct.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.fdzc.mallproduct.entity.Product;
import edu.fdzc.mallproduct.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import edu.fdzc.mallcommon.constant.OrderConstant;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    private final ProductMapper productMapper;

    public List<Product> getAllProducts(){
        return productMapper.selectList(null);
    }

    public Product getProductById(Long id){
        return productMapper.selectById(id);
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
}

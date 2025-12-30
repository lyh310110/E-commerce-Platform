package edu.fdzc.malluser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.fdzc.malluser.entity.User;
import edu.fdzc.malluser.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import edu.fdzc.malluser.entity.Shop;
import edu.fdzc.malluser.mapper.ShopMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final ShopMapper shopMapper;

    public User login(String username, String password){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username)
                .eq("password",password);
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 1. 用户新增
     */
    public User addUser(User user) {
        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User existingUser = userMapper.selectOne(queryWrapper);
        if (existingUser != null) {
            return null; // 用户名已存在
        }

        int result = userMapper.insert(user);
        return result > 0 ? user : null;
    }

    /**
     * 2. 用户密码修改
     */
    public boolean updatePassword(Long id, String oldPassword, String newPassword) {
        User user = userMapper.selectById(id);
        if (user == null || !user.getPassword().equals(oldPassword)) {
            return false; // 用户不存在或旧密码错误
        }

        user.setPassword(newPassword);
        int result = userMapper.updateById(user);
        return result > 0;
    }

    /**
     * 3. 用户信息修改
     */
    public User updateUser(User user) {
        if (user.getId() == null) {
            return null; // ID不能为空
        }

        // 检查用户名是否被其他用户使用
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername())
                .ne("id", user.getId());
        User existingUser = userMapper.selectOne(queryWrapper);
        if (existingUser != null) {
            return null; // 用户名已被其他用户使用
        }

        int result = userMapper.updateById(user);
        return result > 0 ? userMapper.selectById(user.getId()) : null;
    }

    /**
     * 4. 用户列表（分页查询，条件查询）
     */
    public List<User> getUserList(Integer pageNum, Integer pageSize, String keyword) {
        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // 条件查询：用户名或者昵称
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like("username", keyword)
                    .or()
                    .like("nickname", keyword);
        }

        // 按ID升序排列
        queryWrapper.orderByAsc("id");

        Page<User> userPage = userMapper.selectPage(page, queryWrapper);
        return userPage.getRecords();
    }

    /**
     * 根据ID获取用户信息
     */
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    /**
     * 删除用户
     */
    public boolean deleteUser(Long id) {
        int result = userMapper.deleteById(id);
        return result > 0;
    }

    /**
     * 申请开店
     */
    public boolean applyShop(Shop shop) {
        // 检查用户是否已经是卖家
        Shop existingShop = getShopByUserId(shop.getUserId());
        if (existingShop != null) {
            return false;
        }

        // 检查店铺名是否重复
        Shop shopByName = getShopByName(shop.getShopName());
        if (shopByName != null) {
            return false;
        }

        shop.setStatus(0); // 待审核状态
        shop.setCreateTime(LocalDateTime.now());
        shop.setUpdateTime(LocalDateTime.now());

        return shopMapper.insert(shop) > 0;
    }

    /**
     * 获取用户店铺
     */
    public Shop getShopByUserId(Long userId) {
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return shopMapper.selectOne(queryWrapper);
    }

    /**
     * 根据店铺名查询
     */
    public Shop getShopByName(String shopName) {
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_name", shopName);
        return shopMapper.selectOne(queryWrapper);
    }

    /**
     * 更新店铺信息
     */
    public boolean updateShop(Shop shop) {
        if (shop.getId() == null) {
            return false;
        }
        shop.setUpdateTime(LocalDateTime.now());
        return shopMapper.updateById(shop) > 0;
    }
}
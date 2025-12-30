package edu.fdzc.mallauth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.fdzc.mallauth.entity.User;
import edu.fdzc.mallauth.entity.UserRole;
import edu.fdzc.mallauth.mapper.UserMapper;
import edu.fdzc.mallauth.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    /**
     * 用户注册
     */
    public User register(User user, Integer roleType) {
        // 检查用户名是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        if (userMapper.selectOne(queryWrapper) != null) {
            return null;
        }

        // 设置默认值
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 保存用户
        int result = userMapper.insert(user);
        if (result <= 0) {
            return null;
        }

        // 创建用户角色
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleType(roleType);
        userRole.setStatus(1);
        userRole.setCreateTime(LocalDateTime.now());
        userRole.setUpdateTime(LocalDateTime.now());
        userRoleMapper.insert(userRole);

        return user;
    }

    /**
     * 用户登录
     */
    public User login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                .eq("password", password)
                .eq("status", 1);

        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            // 更新最后登录时间
            user.setLastLoginTime(LocalDateTime.now());
            userMapper.updateById(user);
        }

        return user;
    }

    /**
     * 获取用户角色
     */
    public List<UserRole> getUserRoles(Long userId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("status", 1);
        return userRoleMapper.selectList(queryWrapper);
    }

    /**
     * 根据ID获取用户
     */
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    /**
     * 更新用户信息
     */
    public boolean updateUser(User user) {
        if (user.getId() == null) {
            return false;
        }
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.updateById(user) > 0;
    }
}
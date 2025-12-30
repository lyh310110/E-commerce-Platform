package edu.fdzc.mallauth.service;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.mallauth.entity.User;
import edu.fdzc.mallauth.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;

    /**
     * 用户登录
     */
    public Result<Map<String, Object>> login(String username, String password) {
        User user = userService.login(username, password);
        if (user == null) {
            return Result.error("用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            return Result.error("用户已被禁用");
        }

        // 获取用户角色
        List<UserRole> userRoles = userService.getUserRoles(user.getId());
        if (userRoles.isEmpty()) {
            return Result.error("用户未分配角色");
        }

        // 取第一个有效角色
        UserRole activeRole = null;
        for (UserRole role : userRoles) {
            if (role.getStatus() != null && role.getStatus() == 1) {
                activeRole = role;
                break;
            }
        }

        if (activeRole == null) {
            return Result.error("用户没有可用角色");
        }

        // 生成token，包含角色信息
        String token = jwtService.generateToken(
                user.getId(),
                user.getUsername(),
                activeRole.getRoleType()
        );

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        result.put("roleType", activeRole.getRoleType());

        return Result.success(result);
    }

    /**
     * 用户注册
     */
    public Result<User> register(String username, String password, String nickname, Integer roleType) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);

        User registeredUser = userService.register(user, roleType);
        if (registeredUser == null) {
            return Result.error("注册失败，用户名可能已存在");
        }

        return Result.success(registeredUser);
    }

    /**
     * 验证token
     */
    public Result<Map<String, Object>> verifyToken(String token) {
        if (!jwtService.validateToken(token)) {
            return Result.error("Token无效或已过期");
        }

        Long userId = jwtService.getUserIdFromToken(token);
        String username = jwtService.getUsernameFromToken(token);
        Integer roleType = jwtService.getRoleTypeFromToken(token);

        User user = userService.getUserById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("username", username);
        result.put("roleType", roleType);
        result.put("user", user);

        return Result.success(result);
    }
}
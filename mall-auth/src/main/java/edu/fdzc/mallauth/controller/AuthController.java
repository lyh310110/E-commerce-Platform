package edu.fdzc.mallauth.controller;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.mallauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        return authService.login(username, password);
    }

    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String nickname = params.get("nickname");

        // 默认为买家角色
        Result result = authService.register(username, password, nickname, 1);
        if (result.getCode() == 200) {
            // 注册成功后自动登录
            return authService.login(username, password);
        }
        return result;
    }

    @PostMapping("/register/seller")
    public Result<Map<String, Object>> registerSeller(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String nickname = params.get("nickname");

        // 卖家角色注册
        Result result = authService.register(username, password, nickname, 2);
        if (result.getCode() == 200) {
            return authService.login(username, password);
        }
        return result;
    }

    @PostMapping("/verify-token")
    public Result<Map<String, Object>> verifyToken(@RequestBody Map<String, String> params) {
        String token = params.get("token");
        return authService.verifyToken(token);
    }
}
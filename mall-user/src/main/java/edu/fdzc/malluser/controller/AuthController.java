package edu.fdzc.malluser.controller;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.malluser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginParams){
        String username = loginParams.get("username");
        String password = loginParams.get("password");
        Map<String, Object> resultMap = userService.login(username, password);
        return resultMap == null ? Result.error("用户名或者密码错误！") : Result.success(resultMap);
    }
}
package edu.fdzc.malluser.controller;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.malluser.entity.User;
import edu.fdzc.malluser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 1. 用户新增
     */
    @PostMapping("/add")
    public Result<User> addUser(@RequestBody User user) {
        User savedUser = userService.addUser(user);
        return savedUser != null ? Result.success(savedUser) : Result.error("用户新增失败！");
    }

    /**
     * 2. 用户密码修改
     */
    @PutMapping("/updatePassword")
    public Result<String> updatePassword(@RequestParam Long id,
                                         @RequestParam String oldPassword,
                                         @RequestParam String newPassword) {
        boolean success = userService.updatePassword(id, oldPassword, newPassword);
        return success ? Result.success("密码修改成功！") : Result.error("密码修改失败！");
    }

    /**
     * 3. 用户信息修改
     */
    @PutMapping("/update")
    public Result<User> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        return updatedUser != null ? Result.success(updatedUser) : Result.error("用户信息修改失败！");
    }

    /**
     * 4. 用户列表（分页查询，条件查询）
     */
    @GetMapping("/list")
    public Result<List<User>> getUserList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        List<User> userList = userService.getUserList(pageNum, pageSize, keyword);
        return Result.success(userList);
    }

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? Result.success(user) : Result.error("用户不存在！");
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        boolean success = userService.deleteUser(id);
        return success ? Result.success("用户删除成功！") : Result.error("用户删除失败！");
    }
}
package com.lsp.aicodemother.controller;

import cn.hutool.core.bean.BeanUtil;
import com.lsp.aicodemother.annotation.AuthCheck;
import com.lsp.aicodemother.common.BaseResponse;
import com.lsp.aicodemother.common.DeleteRequest;
import com.lsp.aicodemother.common.ResultUtils;
import com.lsp.aicodemother.constant.UserConstant;
import com.lsp.aicodemother.exception.ErrorCode;
import com.lsp.aicodemother.exception.ThrowUtils;
import com.lsp.aicodemother.model.dto.user.*;
import com.lsp.aicodemother.model.vo.LoginUserVO;
import com.lsp.aicodemother.model.vo.UserVO;
import com.mybatisflex.core.paginate.Page;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.lsp.aicodemother.model.entity.User;
import com.lsp.aicodemother.service.UserService;

import java.util.List;

/**
 * 用户 控制层。
 *
 * @author lsp
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 保存用户。
     *
     * @param user 用户
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody User user) {
        return userService.save(user);
    }

    /**
     * 根据主键删除用户。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Long id) {
        return userService.removeById(id);
    }

    /**
     * 根据主键更新用户。
     *
     * @param user 用户
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody User user) {
        return userService.updateById(user);
    }

    /**
     * 查询所有用户。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<User> list() {
        return userService.list();
    }

    /**
     * 根据主键获取用户。
     *
     * @param id 用户主键
     * @return 用户详情
     */
    @GetMapping("getInfo/{id}")
    public User getInfo(@PathVariable Long id) {
        return userService.getById(id);
    }

    /**
     * 分页查询用户。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<User> page(Page<User> page) {
        return userService.page(page);
    }


    /**
     * 用户注册
     * @param userRegisterRequest
     * @param request
     * @return
     */
    @PostMapping("register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userRegisterRequest==null, ErrorCode.PARAMS_ERROR);
        String userAccount=userRegisterRequest.getUserAccount();
        String userPassword=userRegisterRequest.getUserPassword();
        String checkPassword=userRegisterRequest.getCheckPassword();
        Long result=userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest==null, ErrorCode.PARAMS_ERROR);
        String userAccount=userLoginRequest.getUserAccount();
        String userPassword=userLoginRequest.getUserPassword();
        LoginUserVO result=userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    @GetMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request==null,ErrorCode.PARAMS_ERROR);
        boolean result=userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 添加用户（仅管理员可用）
     * @param userAddRequest
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest){
        ThrowUtils.throwIf(userAddRequest==null, ErrorCode.PARAMS_ERROR);
        User user=new User();
        BeanUtil.copyProperties(userAddRequest, user);
        //默认密码12345678
        final String DEFAULT_PASSWORD="12345678";
        String encryptPassword=userService.getEncryptPassword(DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        boolean result=userService.save(user);
        ThrowUtils.throwIf(result,ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }


    /**
     * 根据id获取用户信息（仅管理员可用）
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(@RequestParam long id){
        ThrowUtils.throwIf(id<=0, ErrorCode.PARAMS_ERROR);
        User user=userService.getById(id);
        ThrowUtils.throwIf(user==null, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据id获取用户信息视图
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id){
        BaseResponse<User> response=getUserById(id);
        User user=response.getData();
        UserVO userVO=userService.getUserVO(user);
        return ResultUtils.success(userVO);
    }

    /**
     * 根据id删除用户（仅管理员可用）
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest){
        ThrowUtils.throwIf(deleteRequest==null||deleteRequest.getId()<=0, ErrorCode.PARAMS_ERROR);
        boolean result=userService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }


    /**
     * 更新用户信息（仅管理员可用）
     * @param userUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest){
        ThrowUtils.throwIf(userUpdateRequest==null||userUpdateRequest.getId()==null, ErrorCode.PARAMS_ERROR);
        User user=new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        boolean result=userService.updateById(user);
        ThrowUtils.throwIf(result,ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取用户信息视图列表（仅管理员可用）
     * @param userQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> listUserVOBYPage(@RequestBody UserQueryRequest userQueryRequest){
        ThrowUtils.throwIf(userQueryRequest==null, ErrorCode.PARAMS_ERROR);
        long pageNum=userQueryRequest.getPageNum();
        long pageSize=userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(Page.of(pageNum, pageSize), userService.getQueryWrapper(userQueryRequest));

        //数据脱敏
        Page<UserVO> userVOPage = new Page<>(pageNum, pageSize, userPage.getTotalRow());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }



}

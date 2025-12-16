package com.lsp.aicodemother.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.lsp.aicodemother.exception.BusinessException;
import com.lsp.aicodemother.exception.ErrorCode;
import com.lsp.aicodemother.exception.ThrowUtils;
import com.lsp.aicodemother.model.dto.user.UserQueryRequest;
import com.lsp.aicodemother.model.vo.LoginUserVO;
import com.lsp.aicodemother.model.enums.UserRoleEnum;
import com.lsp.aicodemother.model.vo.UserVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.lsp.aicodemother.model.entity.User;
import com.lsp.aicodemother.mapper.UserMapper;
import com.lsp.aicodemother.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lsp.aicodemother.constant.UserConstant.USER_LOGIN_STATE;


/**
 * 用户 服务层实现。
 *
 * @author lsp
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService {
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null)
            return null;
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度过短");
        }
        // 2. 加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 查询用户是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.mapper.selectOneByQuery(queryWrapper);
        // 用户不存在
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        // 4. 获得脱敏后的用户信息
        return this.getLoginUserVO(user);
    }

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 2. 检查是否重复
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.mapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 3. 加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 4. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("无名");
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user.getId();
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        //判断是否已经登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        ThrowUtils.throwIf(currentUser == null || currentUser.getId() == null, ErrorCode.NOT_LOGIN_ERROR);
        //从数据库查询（防止用户信息被修改）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        ThrowUtils.throwIf(currentUser == null, ErrorCode.NOT_LOGIN_ERROR);
        return currentUser;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        //判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;

    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        return QueryWrapper.create()
                .eq("id", id)
                .eq("userRole", userRole)
                .like("userAccount", userAccount)
                .like("userName", userName)
                .like("userProfile", userProfile)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }

    private static final String SALT="lsp_ai_code_mother_salt_2025";

    @Override
    public String getEncryptPassword(String userPassword) {
        try {
            String raw=SALT+userPassword;
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb=new StringBuilder();
            //防止显示乱码
            for(byte b:bytes){
                //8位到32位，防止负数自动补一，所以先0xff过滤，保证前面补零,再转16进制,才能获得原来的值
                //因为byte是-128-127，如果为全1，那么转成int就是-1，转16进制就是ffffffff，所以要和0xff与运算
                String hex = Integer.toHexString(0xff & b);
                if(hex.length()==1)sb.append('0');
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}


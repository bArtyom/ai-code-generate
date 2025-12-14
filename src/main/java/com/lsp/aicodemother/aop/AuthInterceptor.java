package com.lsp.aicodemother.aop;


import com.lsp.aicodemother.annotation.AuthCheck;
import com.lsp.aicodemother.exception.BusinessException;
import com.lsp.aicodemother.exception.ErrorCode;
import com.lsp.aicodemother.model.entity.User;
import com.lsp.aicodemother.model.enums.UserRoleEnum;
import com.lsp.aicodemother.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthInterceptor {


    @Resource
    private UserService userService;


    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole=authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        //当前登录用户
        User loginUser=userService.getLoginUser(request);
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);

        //不需要权限，放行
        if(mustRoleEnum==null){
            return joinPoint.proceed();
        }

        //需要权限校验
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());

        //没有权限，拒绝
        if(userRoleEnum==null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        //必须有管理员权限
        if(UserRoleEnum.ADMIN.equals(mustRoleEnum)&&!UserRoleEnum.ADMIN.equals(userRoleEnum)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //通过权限校验
        return joinPoint.proceed();
    }

}

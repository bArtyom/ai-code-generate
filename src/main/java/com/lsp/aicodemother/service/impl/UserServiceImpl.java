package com.lsp.aicodemother.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.lsp.aicodemother.model.entity.User;
import com.lsp.aicodemother.mapper.UserMapper;
import com.lsp.aicodemother.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户 服务层实现。
 *
 * @author lsp
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService{

}

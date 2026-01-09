package com.lsp.aicodemother.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.lsp.aicodemother.model.entity.AppMember;
import com.lsp.aicodemother.mapper.AppMemberMapper;
import com.lsp.aicodemother.service.AppMemberService;
import org.springframework.stereotype.Service;

/**
 * 应用成员协作表 服务层实现。
 *
 * @author lsp
 */
@Service
public class AppMemberServiceImpl extends ServiceImpl<AppMemberMapper, AppMember>  implements AppMemberService{

}

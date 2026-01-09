package com.lsp.aicodemother.service;

import com.lsp.aicodemother.model.dto.app.AppQueryRequest;
import com.lsp.aicodemother.model.entity.App;
import com.lsp.aicodemother.model.entity.User;
import com.lsp.aicodemother.model.vo.AppVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import reactor.core.publisher.Flux;

import java.io.Serializable;
import java.util.List;

/**
 * 应用 服务层。
 *
 * @author lsp
 */
public interface AppService extends IService<App> {

    /**
     * 获取应用视图
     *
     * @param app 应用实体
     * @return 应用视图
     */
    AppVO getAppVO(App app);

    /**
     * 获取应用视图列表
     *
     * @param appList 应用列表
     * @return 应用视图列表
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 构建查询包装器
     *
     * @param appQueryRequest 查询请求
     * @return 查询包装器
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    /**
     * 校验应用参数
     *
     * @param app 应用实体
     * @param add 是否为创建操作
     */
    void validApp(App app, boolean add);

    /**
     * 聊天生成代码
     * @param appId
     * @param message
     * @param loginUser
     * @return
     */
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    /**
     * 部署应用
     * @param appId
     * @param loginUser
     * @return
     */
    String deployApp(Long appId, User loginUser);

    boolean removeById(Serializable id);

    int incrementConversationCount(Long appId);

    int getConversationCount(Long appId);

    List<App> getMemberApp(Long userId);
}


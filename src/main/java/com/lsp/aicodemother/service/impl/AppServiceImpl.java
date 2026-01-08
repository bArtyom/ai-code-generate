package com.lsp.aicodemother.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.lsp.aicodemother.constant.AppConstant;
import com.lsp.aicodemother.core.AiCodeGeneratorFacade;
import com.lsp.aicodemother.exception.BusinessException;
import com.lsp.aicodemother.exception.ErrorCode;
import com.lsp.aicodemother.exception.ThrowUtils;
import com.lsp.aicodemother.mapper.AppMapper;
import com.lsp.aicodemother.model.dto.app.AppQueryRequest;
import com.lsp.aicodemother.model.entity.App;
import com.lsp.aicodemother.model.entity.User;
import com.lsp.aicodemother.model.enums.ChatHistoryMessageTypeEnum;
import com.lsp.aicodemother.model.enums.CodeGenTypeEnum;
import com.lsp.aicodemother.model.vo.AppVO;
import com.lsp.aicodemother.model.vo.UserVO;
import com.lsp.aicodemother.service.UserService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.lsp.aicodemother.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 应用 服务层实现。
 *
 * @author lsp
 */
@Service
@Slf4j
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Autowired
    private UserService userService;
    @Autowired
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;
    @Autowired
    private ChatHistoryServiceImpl chatHistoryService;
    @Autowired
    private AppMapper appMapper;

    @Override
    public AppVO getAppVO(App app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = new AppVO();
        BeanUtil.copyProperties(app, appVO);
        
        // 关联查询用户信息
        Long userId = app.getUserId();
        if (userId != null) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            appVO.setUser(userVO);
        }
        
        return appVO;
    }

    @Override
    public List<AppVO> getAppVOList(List<App> appList) {
        if (CollectionUtils.isEmpty(appList)) {
            return new ArrayList<>();
        }
        
        // 关联查询用户信息
        Set<Long> userIdSet = appList.stream()
                .map(App::getUserId)
                .filter(userId -> userId != null && userId > 0)
                .collect(Collectors.toSet());
        
        Map<Long,UserVO> userVOMap=userService.listByIds(userIdSet).stream()
                .collect(Collectors.toMap(User::getId,userService::getUserVO));
        
        // 填充信息
        return appList.stream().map(app -> {
           AppVO appVO=getAppVO(app);
           UserVO userVO=userVOMap.get(app.getUserId());
           appVO.setUser(userVO);
           return appVO;
        }).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest) {
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String cover = appQueryRequest.getCover();
        String initPrompt = appQueryRequest.getInitPrompt();
        String codeGenType = appQueryRequest.getCodeGenType();
        String deployKey = appQueryRequest.getDeployKey();
        Integer priority = appQueryRequest.getPriority();
        Long userId = appQueryRequest.getUserId();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();
        return QueryWrapper.create()
                .eq("id", id)
                .like("appName", appName)
                .like("cover", cover)
                .like("initPrompt", initPrompt)
                .eq("codeGenType", codeGenType)
                .eq("deployKey", deployKey)
                .eq("priority", priority)
                .eq("userId", userId)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }

    @Override
    public void validApp(App app, boolean add) {
        if (app == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        String appName = app.getAppName();
        String codeGenType = app.getCodeGenType();
        
        // 创建时，参数不能为空
        if (add) {
            if (StrUtil.isBlank(appName)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用名称不能为空");
            }
            if (StrUtil.isBlank(codeGenType)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "代码生成类型不能为空");
            }
        }
        
        // 校验参数
        if (StrUtil.isNotBlank(appName) && appName.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用名称过长");
        }
        if (StrUtil.isNotBlank(app.getInitPrompt()) && app.getInitPrompt().length() > 5000) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "初始化 prompt 过长");
        }
    }


    @Override
    public Flux<String> chatToGenCode(Long appId, String message, User loginUser) {
        //1、参数校验
        ThrowUtils.throwIf(appId==null||appId<=0, ErrorCode.PARAMS_ERROR, "应用ID不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(message),ErrorCode.PARAMS_ERROR,"用户消息不为空");
        //2、查询应用信息
        App app=this.getById(appId);
        ThrowUtils.throwIf(app==null,ErrorCode.PARAMS_ERROR,"应用不存在");
        //3、验证用户是否有权限访问该应用，仅本人可以生成代码
        ThrowUtils.throwIf(!app.getUserId().equals(loginUser.getId()),ErrorCode.NO_AUTH_ERROR,"无权限访问该应用");
        //4、获取应用的代码生成类型
        // 4. 获取应用的代码生成类型
        String codeGenTypeStr = app.getCodeGenType();
        CodeGenTypeEnum codeGenTypeEnum = CodeGenTypeEnum.getEnumByValue(codeGenTypeStr);
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型");
        }

        // 5. 通过检验后, 添加用户消息到对话历史
        chatHistoryService.addChatMessage(appId, message, ChatHistoryMessageTypeEnum.USER.getValue(), loginUser.getId());

        // 6. 调用AI 生成代码（流式）
        Flux<String> contentFlux = aiCodeGeneratorFacade.generateAndSaveCodeStream(message, codeGenTypeEnum, appId);

        // 7. 收集AI响应内容并在完成后记录到对话历史
        StringBuilder aiResponseBuilder = new StringBuilder();
        return contentFlux
                .map(chunk -> {
                    // 收集AI响应内容
                    aiResponseBuilder.append(chunk);
                    return chunk;
                })
                .doOnComplete(() -> {
                    // 流式响应完成后, 添加AI消息到对话历史
                    String aiResponse = aiResponseBuilder.toString();
                    if (StrUtil.isNotBlank(aiResponse)) {
                        chatHistoryService.addChatMessage(appId, aiResponse, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
                    }
                })
                .doOnError(error -> {
                    // 如果AI回复失败, 也要记录错误消息
                    String errorMessage = "AI回复失败: " + error.getMessage();
                    chatHistoryService.addChatMessage(appId, errorMessage, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
                });
    }

    @Override
    public String deployApp(Long appId, User loginUser) {
        //参数校验
        ThrowUtils.throwIf(appId==null||appId<=0, ErrorCode.PARAMS_ERROR, "应用ID不能为空");
        ThrowUtils.throwIf(loginUser==null,ErrorCode.NOT_LOGIN_ERROR,"用户未登录");
        //查询应用信息
        App app=this.getById(appId);
        ThrowUtils.throwIf(app==null,ErrorCode.PARAMS_ERROR,"应用不存在");
        //验证用户是否有权限访问该应用，仅本人可以部署应用
        ThrowUtils.throwIf(!app.getUserId().equals(loginUser.getId()),ErrorCode.NO_AUTH_ERROR,"无权限访问该应用");
        //检查是否已经有deployKey
        String deployKey=app.getDeployKey();
        if(StrUtil.isBlank(deployKey)){
           deployKey= RandomUtil.randomString(6);
        }
        //获取代码生成类型，构建源目录路径
        String codeGenType=app.getCodeGenType();
        String sourceDirName=codeGenType+"_"+appId;
        String sourceDirPath= AppConstant.CODE_OUTPUT_ROOT_DIR+ File.separator+sourceDirName;
        //检查源目录是否存在
        File sourceDir=new File(sourceDirPath);
        ThrowUtils.throwIf(!sourceDir.exists()||!sourceDir.isDirectory(),ErrorCode.OPERATION_ERROR,"源代码目录不存在，无法部署应用");
        //复制文件到部署目录
        String deployDirPath=AppConstant.CODE_DEPLOY_ROOT_DIR+File.separator+deployKey;
        try {
            FileUtil.copyContent(sourceDir,new File(deployDirPath),true);
        } catch (IORuntimeException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"部署应用失败，文件操作异常:"+e.getMessage());
        }
        //更新应用的deployKey和部署时间
        App updateApp=new App();
        updateApp.setId(appId);
        updateApp.setDeployKey(deployKey);
        updateApp.setDeployedTime(LocalDateTime.now());
        boolean result=this.updateById(updateApp);
        ThrowUtils.throwIf(!result,ErrorCode.SYSTEM_ERROR,"部署应用失败，更新应用信息异常");
        return String.format("%s/%s/",AppConstant.CODE_DEPLOY_HOST,deployKey);
    }

    /**
     * 删除应用时关联删除对话历史
     *
     * @param id 应用ID
     * @return 是否成功
     */
    @Override
    public boolean removeById(Serializable id) {
        if (id == null) {
            return false;
        }
        // 转换为 Long 类型
        Long appId = Long.valueOf(id.toString());
        if (appId <= 0) {
            return false;
        }
        // 先删除关联的对话历史
        try {
            chatHistoryService.deleteByAppId(appId);
        } catch (Exception e) {
            // 记录日志但不阻止应用删除
            log.error("删除应用关联对话历史失败: {}", e.getMessage());
        }
        // 删除应用
        return super.removeById(id);
    }

    @Override
    public int incrementConversationCount(Long appId) {
        return appMapper.incrementConversationCount(appId);
    }

    @Override
    public int getConversationCount(Long appId) {
        return appMapper.getConversationCount(appId);
    }
}


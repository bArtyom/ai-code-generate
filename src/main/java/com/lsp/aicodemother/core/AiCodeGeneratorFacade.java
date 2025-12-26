package com.lsp.aicodemother.core;


import com.lsp.aicodemother.ai.AiCodeGeneratorService;
import com.lsp.aicodemother.ai.model.HtmlCodeResult;
import com.lsp.aicodemother.ai.model.MultiFileCodeResult;
import com.lsp.aicodemother.core.parser.CodeParserExecutor;
import com.lsp.aicodemother.core.saver.CodeFileSaverExecutor;
import com.lsp.aicodemother.exception.BusinessException;
import com.lsp.aicodemother.exception.ErrorCode;
import com.lsp.aicodemother.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultLifecycleProcessor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * AI 代码生成外观类，组合生成和保存功能
 */
@Slf4j
@Service
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    /**
     * 统一入口：根据类型生成并保存代码
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return 保存的目录
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum,Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
       return switch(codeGenTypeEnum){
            case HTML->{
                HtmlCodeResult result= aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result,codeGenTypeEnum,appId);
            }
           case MULTI_FILE -> {
                MultiFileCodeResult result= aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result,codeGenTypeEnum,appId);
           }
           default->{
                String errorMessage="不支持的生成类型："+codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,errorMessage);
           }
       };
    }

    /**
     * 统一入口：根据类型生成并保存代码（流式）
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum,Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        return switch (codeGenTypeEnum) {
            case HTML -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.HTML,appId);
            }
            case MULTI_FILE -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.MULTI_FILE,appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

     private Flux<String> processCodeStream(Flux<String> codeStream,CodeGenTypeEnum codeGenTypeEnum,Long appId){
        StringBuilder codeBuilder=new StringBuilder();
        return codeStream.doOnNext(chunk->{
            codeBuilder.append(chunk);
        }).doOnComplete(()->{
            try {
                String completeCode=codeBuilder.toString();
                Object parsedResult= CodeParserExecutor.executeParser(completeCode,codeGenTypeEnum);
                File savedDir= CodeFileSaverExecutor.executeSaver(parsedResult,codeGenTypeEnum,appId);
                log.info("保存成功，路径为："+savedDir.getAbsolutePath());
            } catch (Exception e) {
                log.error("保存失败: {}", e.getMessage());
            }
        });
     }

}


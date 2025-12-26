package com.lsp.aicodemother.core.saver;

import com.lsp.aicodemother.ai.model.HtmlCodeResult;
import com.lsp.aicodemother.ai.model.MultiFileCodeResult;
import com.lsp.aicodemother.exception.BusinessException;
import com.lsp.aicodemother.exception.ErrorCode;
import com.lsp.aicodemother.model.enums.CodeGenTypeEnum;

import java.io.File;

public class CodeFileSaverExecutor {
    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaverTemplate=new HtmlCodeFileSaverTemplate();

    private static final MultiFileCodeFileSaverTemplate multiFileCodeFileSaverTemplate=new MultiFileCodeFileSaverTemplate();

    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenTypeEnum,Long appId){
        return switch (codeGenTypeEnum){
            case HTML -> htmlCodeFileSaverTemplate.saveCode((HtmlCodeResult) codeResult,appId);
            case MULTI_FILE -> multiFileCodeFileSaverTemplate.saveCode((MultiFileCodeResult) codeResult,appId);
            default-> throw new BusinessException(ErrorCode.SYSTEM_ERROR,"不支持的保存类型："+codeGenTypeEnum.getValue());
        };
    }
}

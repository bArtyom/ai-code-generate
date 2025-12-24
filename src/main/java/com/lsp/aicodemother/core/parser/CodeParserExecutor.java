package com.lsp.aicodemother.core.parser;

import com.lsp.aicodemother.exception.BusinessException;
import com.lsp.aicodemother.exception.ErrorCode;
import com.lsp.aicodemother.model.enums.CodeGenTypeEnum;

public class CodeParserExecutor{
    private static final HtmlCodeParser htmlCodeParser=new HtmlCodeParser();

    private static final MultiFileCodeParser multiFileCodeParser=new MultiFileCodeParser();

    public static Object executeParser(String codeContent, CodeGenTypeEnum codeGenTypeEnum){
        return switch (codeGenTypeEnum){
            case HTML -> htmlCodeParser.parseCode(codeContent);
            case MULTI_FILE -> multiFileCodeParser.parseCode(codeContent);
            default ->throw new BusinessException(ErrorCode.SYSTEM_ERROR,"不支持的解析类型："+codeGenTypeEnum.getValue());
        };
    }

}

package com.lsp.aicodemother.core.saver;

import com.lsp.aicodemother.ai.model.HtmlCodeResult;
import com.lsp.aicodemother.exception.BusinessException;
import com.lsp.aicodemother.exception.ErrorCode;
import com.lsp.aicodemother.model.enums.CodeGenTypeEnum;

public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult>{
    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        //保存html文件
        writeToFile(baseDirPath,"index.html",result.getHtmlCode());
    }

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        if(result.getHtmlCode()==null||result.getHtmlCode().trim().isEmpty()){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"HTML代码内容不能为空");
        }
    }
}

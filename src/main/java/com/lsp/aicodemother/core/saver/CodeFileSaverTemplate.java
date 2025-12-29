package com.lsp.aicodemother.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.lsp.aicodemother.constant.AppConstant;
import com.lsp.aicodemother.exception.BusinessException;
import com.lsp.aicodemother.exception.ErrorCode;
import com.lsp.aicodemother.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

public abstract class CodeFileSaverTemplate<T> {

    //文件保存目录
    protected static final String FILE_SAVE_PATH = AppConstant.CODE_OUTPUT_ROOT_DIR;

    public final File saveCode(T result,long appId){
        //1、验证输入
        validateInput(result);
        //2、构建基于appID的唯一目录
        String baseDirPath=buildUniqueDir(appId);
        //3、保存文件
        saveFiles(result,baseDirPath);
        //4、返回保存目录
        return new File(baseDirPath);
    }

    protected  abstract void saveFiles(T result, String baseDirPath);

    protected void validateInput(T result) {
        if(result==null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"保存结果为空");
        }
    }

    protected final String buildUniqueDir(Long appId){
        String codeType=getCodeType().getValue();
        String uniqueDirName= StrUtil.format("{}_{}",codeType, appId);
        String dirPath=FILE_SAVE_PATH+File.separator+uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    protected abstract CodeGenTypeEnum getCodeType();


    protected final void writeToFile(String dirPath,String filename,String content){
        if(StrUtil.isNotBlank(content)){
            String filePath=dirPath+File.separator+filename;
            FileUtil.writeString(content,filePath, StandardCharsets.UTF_8);
        }
    }
}

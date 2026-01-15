package com.lsp.aicodemother.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Data;
import lombok.Getter;
import org.apache.ibatis.javassist.compiler.CodeGen;


@Getter
public enum CodeGenTypeEnum {
    //枚举值列举之间是逗号分隔
    HTML("原生HTML模式","html"),
    MULTI_FILE("多文件模式","multi_file"),
    VUE_PROJECT("Vue工程模式", "vue_project");

    private final String text;
    private final String value;


    CodeGenTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static CodeGenTypeEnum getEnumByValue(String value){
        if(ObjUtil.isEmpty(value)){
            return null;
        }
        for(CodeGenTypeEnum anEnum: CodeGenTypeEnum.values()){
            if(anEnum.value.equals(value)){
                return anEnum;
            }
        }
        return null;
    }
}

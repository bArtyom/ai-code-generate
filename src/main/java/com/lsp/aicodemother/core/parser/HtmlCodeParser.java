package com.lsp.aicodemother.core.parser;

import com.lsp.aicodemother.ai.model.HtmlCodeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlCodeParser implements CodeParser<HtmlCodeResult>{

    private static final Pattern HTML_CODE_PATTERN = Pattern.compile("<!DOCTYPE html>[\\s\\S]*?</html>", Pattern.CASE_INSENSITIVE);
    @Override
    public HtmlCodeResult parseCode(String codeContent) {
        HtmlCodeResult result=new HtmlCodeResult();
        //提出html代码
        String htmlCode = extractHtmlCode(codeContent);
        if(htmlCode!=null&&!htmlCode.trim().isEmpty()){
            result.setHtmlCode(htmlCode.trim());
        }else {
            //如果没有找到代码块，将整个内容作为HTML
            result.setHtmlCode(codeContent.trim());
        }
        return result;
    }

    private String extractHtmlCode(String content){
        Matcher matcher = HTML_CODE_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }
}

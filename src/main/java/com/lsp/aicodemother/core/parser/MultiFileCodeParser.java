package com.lsp.aicodemother.core.parser;

import com.lsp.aicodemother.ai.model.MultiFileCodeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiFileCodeParser implements CodeParser<MultiFileCodeResult> {

    private static final Pattern HTML_CODE_PATTERN = Pattern.compile("<!DOCTYPE html>[\\s\\S]*?</html>", Pattern.CASE_INSENSITIVE);

    private static final Pattern CSS_CODE_PATTERN = Pattern.compile("<style[\\s\\S]*?>([\\s\\S]*?)</style>", Pattern.CASE_INSENSITIVE);

    private static final Pattern JS_CODE_PATTERN = Pattern.compile("<script[\\s\\S]*?>([\\s\\S]*?)</script>", Pattern.CASE_INSENSITIVE);

    @Override
    public MultiFileCodeResult parseCode(String codeContent) {
        MultiFileCodeResult result = new MultiFileCodeResult();

        //提取各类代码
        String htmlCode = extractCodeByPattern(codeContent, HTML_CODE_PATTERN);
        String cssCode = extractCodeByPattern(codeContent, CSS_CODE_PATTERN);
        String jsCode = extractCodeByPattern(codeContent, JS_CODE_PATTERN);

        //设置HTML代码
        if (htmlCode != null && !htmlCode.trim().isEmpty()) {
            result.setHtmlCode(htmlCode.trim());
        }
        //设置CSS代码
        if (cssCode != null && !cssCode.trim().isEmpty()) {
            result.setCssCode(cssCode.trim());
        }
        //设置JS代码
        if (jsCode != null && !jsCode.trim().isEmpty()) {
            result.setJsCode(jsCode.trim());
        }
        return result;
    }

    /**
     * 通过正则表达式提取代码
     * @param content
     * @param pattern
     * @return
     */
    private String extractCodeByPattern(String content, Pattern pattern){
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }


}

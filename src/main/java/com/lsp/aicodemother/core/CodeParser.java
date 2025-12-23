package com.lsp.aicodemother.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsp.aicodemother.ai.model.HtmlCodeResult;
import com.lsp.aicodemother.ai.model.MultiFileCodeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 代码解析器
 * 提供静态方法解析不同类型的代码内容
 *
 * @author yupi
 */
public class CodeParser {

    //private static final Pattern HTML_CODE_PATTERN = Pattern.compile("```html\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);
    private static final Pattern HTML_CODE_PATTERN = Pattern.compile("<!DOCTYPE html>[\\s\\S]*?</html>", Pattern.CASE_INSENSITIVE);
    private static final Pattern CSS_CODE_PATTERN = Pattern.compile("<style[\\s\\S]*?>([\\s\\S]*?)</style>", Pattern.CASE_INSENSITIVE);
    private static final Pattern JS_CODE_PATTERN = Pattern.compile("<script[\\s\\S]*?>([\\s\\S]*?)</script>", Pattern.CASE_INSENSITIVE);

    /**
     * 解析 HTML 单文件代码
     */
    public static HtmlCodeResult parseHtmlCode(String codeContent) {
        HtmlCodeResult result = new HtmlCodeResult();
        // 提取 HTML 代码
        String htmlCode = extractHtmlCode(codeContent);
        if (htmlCode != null && !htmlCode.trim().isEmpty()) {
            result.setHtmlCode(htmlCode.trim());
        } else {
            // 如果没有找到代码块，将整个内容作为HTML
            result.setHtmlCode(codeContent.trim());
        }
        return result;
    }

    /**
     * 解析多文件代码（HTML + CSS + JS）
     */
    public static MultiFileCodeResult parseMultiFileCode(String codeContent)  {
        MultiFileCodeResult result = new MultiFileCodeResult();
        // 提取各类代码
//        String htmlCode = extractHtmlCode(codeContent);
//        String cssCode = extractCssCode(codeContent,CSS_CODE_PATTERN);
//        String jsCode = extractCssCode(codeContent,JS_CODE_PATTERN);

        //正则提取会导致转义字符无法正确的读取，改为json读取
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(codeContent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // 直接获取内容，Jackson 会自动处理反转义
        String htmlCode = node.path("htmlCode").asText();
        String cssCode = node.path("cssCode").asText();
        String jsCode = node.path("jsCode").asText();
        String desc = node.path("description").asText();
        // 设置HTML代码
        if (htmlCode != null && !htmlCode.trim().isEmpty()) {
            result.setHtmlCode(htmlCode.trim());
        }
        // 设置CSS代码
        if (cssCode != null && !cssCode.trim().isEmpty()) {
            result.setCssCode(cssCode.trim());
        }
        // 设置JS代码
        if (jsCode != null && !jsCode.trim().isEmpty()) {
            result.setJsCode(jsCode.trim());
        }
        return result;
    }

    /**
     * 提取HTML代码内容
     *
     * @param content 原始内容
     * @return HTML代码
     */
    private static String extractHtmlCode(String content) {
        Matcher matcher = HTML_CODE_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    /**
     * 根据正则模式提取代码
     *
     * @param content 原始内容
     * @return 提取的代码
     */
    private static String extractCssCode(String content,Pattern pattern) {
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }


}


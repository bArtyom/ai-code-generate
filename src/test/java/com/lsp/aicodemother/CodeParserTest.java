package com.lsp.aicodemother;

import com.lsp.aicodemother.ai.model.HtmlCodeResult;
import com.lsp.aicodemother.ai.model.MultiFileCodeResult;
import com.lsp.aicodemother.core.parser.HtmlCodeParser;
import com.lsp.aicodemother.core.parser.MultiFileCodeParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CodeParserTest {

    @Test
    void parseHtmlCode() {
        String codeContent = """
                创建一个完整的网页：
                
                       ```html
                       <!DOCTYPE html>
                       <html>
                       ...
                       </html>
                       ```
                """;
        HtmlCodeParser parser = new HtmlCodeParser();
        HtmlCodeResult result = parser.parseCode(codeContent);
        System.out.println(result.getHtmlCode());
        assertNotNull(result);
        assertNotNull(result.getHtmlCode());
    }

    @Test
    void parseMultiFileCode() {
        String codeContent = """
                html 格式
                <!DOCTYPE html>
                <html>
                <head>
                    <title>多文件示例</title>
                    <link rel="stylesheet" href="style.css">
                </head>
                <body>
                    <h1>欢迎使用</h1>
                    <script src="script.js"></script>
                </body>
                </html>

                css 格式
                h1 {
                    color: blue;
                    text-align: center;
                }
                ```
                ```js
                console.log('页面加载完成');

                文件创建完成！
                """;

        MultiFileCodeParser parser = new MultiFileCodeParser();
        MultiFileCodeResult result = parser.parseCode(codeContent);
        assertNotNull(result);
        assertNotNull(result.getHtmlCode());
        assertNotNull(result.getCssCode());
        assertNotNull(result.getJsCode());
    }
}

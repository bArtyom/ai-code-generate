package com.lsp.aicodemother.ai;

import com.lsp.aicodemother.ai.model.HtmlCodeResult;
import com.lsp.aicodemother.ai.model.MultiFileCodeResult;
import com.lsp.aicodemother.model.entity.ChatHistory;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import reactor.core.publisher.Flux;

import java.util.List;

public interface AiCodeGeneratorService {

    String generateCode(String userMessage);

    /**
     * 生成 HTML 代码
     *
     * @param userMessage 用户消息
     * @return 生成的代码结果
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    HtmlCodeResult generateHtmlCode(/*@MemoryId int memory,*/ @V("userMessage")String userMessage);

    /**
     * 生成多文件代码
     *
     * @param userMessage 用户消息
     * @return 生成的代码结果
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    MultiFileCodeResult generateMultiFileCode(/*@MemoryId int memory,*/@V("userMessage")String userMessage);


    /**
     * 生成 HTML 代码（流式）
     *
     * @param userMessage 用户消息
     * @return 生成的代码结果
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    Flux<String> generateHtmlCodeStream(/*@MemoryId int memory,*/@V("userMessage")String userMessage);

    /**
     * 生成多文件代码（流式）
     *
     * @param userMessage 用户消息
     * @return 生成的代码结果
     */

    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    Flux<String> generateMultiFileCodeStream(/*@MemoryId int memory,*/@V("userMessage")String userMessage);


    @SystemMessage(fromResource = "prompt/codegen-chat-history-system-prompt.txt")
    String generateCodeChatHistory(@V("historyList") String historyList);
}

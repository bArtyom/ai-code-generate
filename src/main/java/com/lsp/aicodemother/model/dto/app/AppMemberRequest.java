package com.lsp.aicodemother.model.dto.app;

import com.lsp.aicodemother.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppMemberRequest extends PageRequest {

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 成员用户id
     */
    private Long memberUserId;

    private static final long serialVersionUID = 1L;

}

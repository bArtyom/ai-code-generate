package com.lsp.aicodemother.constant;

public interface AppConstant {

    /**
     * 优质应用优先级
     */
    Integer GOOD_APP_PRIORITY=99;

    /**
     * 默认应用优先级
     */
    Integer DEFAULT_APP_PRIORITY=0;

    /**
     * 代码输出根目录
     */
    String CODE_OUTPUT_ROOT_DIR=System.getProperty("user.dir")+"/tmp/code_output";

    /**
     * 代码部署根目录
     */
    String CODE_DEPLOY_ROOT_DIR=System.getProperty("user.dir")+"/tmp/code_deploy";

    /**
     * 代码部署服务地址
     */
    String CODE_DEPLOY_HOST="http://localhost";

}

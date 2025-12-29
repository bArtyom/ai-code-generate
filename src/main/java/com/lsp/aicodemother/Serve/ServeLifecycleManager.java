package com.lsp.aicodemother.Serve;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

//@Component
public class ServeLifecycleManager {

    //@Autowired
    private ServeDeployService serveDeployService;


    /**
     * Springboot应用启动完成后，启动serve服务
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(){
        serveDeployService.startServeService();
    }

    /**
     * Springboot应用关闭前，停止serve服务
     */
    @PreDestroy
    public void onApplicationShutdown(){
        System.out.println("Application is shutting down, stopping serve service...");
        serveDeployService.stopServeService();
    }

}

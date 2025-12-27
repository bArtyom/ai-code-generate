package com.lsp.aicodemother.Serve;

import com.lsp.aicodemother.service.ServeDeployService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;



@Component
public class ServeDeployServiceImpl implements ServeDeployService {

    private static final String CODE_BASE_DIR="/tmp/deploy";
    private static final int SERVE_PORT=3000;
    private static Process serverProcess;

    /**
     * 启动serve服务
     */
    @Override
    public void startServeService() {
        if(serverProcess==null|| !serverProcess.isAlive()){
            try {
                ProcessBuilder pb=new ProcessBuilder("npx","serve",CODE_BASE_DIR,"-p",String.valueOf(SERVE_PORT));
                pb.redirectErrorStream(true);
                serverProcess=pb.start();
                System.out.println("Serve service started on port "+SERVE_PORT);
            } catch (IOException e) {
                throw new RuntimeException("Failed to start serve service", e);
            }
        }
    }

    /**
     * 停止serve服务
     */
    @Override
    public void stopServeService() {
        if(serverProcess!=null && serverProcess.isAlive()){
            serverProcess.destroy();
            try {
                serverProcess.waitFor(5, TimeUnit.SECONDS);
                System.out.println("Serve service stopped.");
            } catch (InterruptedException e) {
                serverProcess.destroyForcibly();
                System.out.println("Serve service forcibly stopped.");
            }
        }
    }
}

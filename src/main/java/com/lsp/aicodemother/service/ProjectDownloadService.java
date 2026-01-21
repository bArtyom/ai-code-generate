package com.lsp.aicodemother.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 项目下载服务接口
 * 定义项目打包、下载相关的核心功能
 *
 * @author 自定义（可补充你的作者信息）
 * @date 2026-01-20
 */
public interface ProjectDownloadService {

    /**
     * 打包项目为ZIP压缩包
     * 核心功能：过滤指定的文件/目录/扩展名，将项目目录打包为ZIP文件
     *
     * @param projectRootPath 项目根目录的绝对路径（例如：/Users/xxx/projects/my-app）
     * @param outputZipPath   生成的ZIP压缩包输出路径（例如：/Users/xxx/downloads/my-app.zip）
     * @throws IllegalArgumentException 当项目根目录不存在/不是有效目录时抛出
     * @throws IOException              打包过程中出现IO异常（如文件读写失败）时抛出
     */
    void downloadProjectAsZip(String projectRootPath, String outputZipPath, HttpServletResponse response) ;

//    /**
//     * 获取项目ZIP包的字节数组
//     * 适用于Web场景：将ZIP包转换为字节数组，前端可直接下载该字节流（无需落地到磁盘）
//     *
//     * @param projectRootPath 项目根目录的绝对路径
//     * @return ZIP压缩包的字节数组
//     * @throws IllegalArgumentException 当项目根目录不存在/不是有效目录时抛出
//     * @throws IOException              打包或读取字节流时出现IO异常时抛出
//     */
//    byte[] getProjectZipAsBytes(String projectRootPath) throws IOException;
}
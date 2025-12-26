package com.lsp.aicodemother.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;

import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户 实体类。
 *
 * @author lsp
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id(keyType = KeyType.Generator,value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 账号
     */
    @Column("userAccount")
    private String userAccount;

    /**
     * 密码
     */
    @Column("userPassword")
    private String userPassword;

    /**
     * 用户昵称
     */
    @Column("userName")
    private String userName;

    /**
     * 用户头像
     */
    @Column("userAvatar")
    private String userAvatar;

    /**
     * 用户简介
     */
    @Column("userProfile")
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    @Column("userRole")
    private String userRole;

    /**
     * 编辑时间
     */
    @Column("editTime")
    private LocalDateTime editTime;

    /**
     * 创建时间
     */
    @Column("createTime")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column("updateTime")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @Column(value = "isDelete", isLogicDelete = true)
    private Integer isDelete;

    /**
     * 应用 实体类。
     *
     * @author lsp
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Table("app")
    public static class App implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * id
         */
        @Id(keyType = KeyType.Auto)
        private Long id;

        /**
         * 应用名称
         */
        @Column("appName")
        private String appName;

        /**
         * 应用封面
         */
        private String cover;

        /**
         * 应用初始化的 prompt
         */
        @Column("initPrompt")
        private String initPrompt;

        /**
         * 代码生成类型（枚举）
         */
        @Column("codeGenType")
        private String codeGenType;

        /**
         * 部署标识
         */
        @Column("deployKey")
        private String deployKey;

        /**
         * 部署时间
         */
        @Column("deployedTime")
        private LocalDateTime deployedTime;

        /**
         * 优先级
         */
        private Integer priority;

        /**
         * 创建用户id
         */
        @Column("userId")
        private Long userId;

        /**
         * 编辑时间
         */
        @Column("editTime")
        private LocalDateTime editTime;

        /**
         * 创建时间
         */
        @Column("createTime")
        private LocalDateTime createTime;

        /**
         * 更新时间
         */
        @Column("updateTime")
        private LocalDateTime updateTime;

        /**
         * 是否删除
         */
        @Column(value = "isDelete", isLogicDelete = true)
        private Integer isDelete;

    }
}

package com.lsp.aicodemother.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 应用成员协作表 实体类。
 *
 * @author lsp
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("app_member")
public class AppMember implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 应用id
     */
    @Column("appId")
    private Long appId;

    /**
     * 成员id
     */
    @Column("userId")
    private Long userId;

    /**
     * 角色：admin/member
     */
    private String role;

    @Column("createTime")
    private LocalDateTime createTime;

}

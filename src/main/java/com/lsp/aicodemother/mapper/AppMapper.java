package com.lsp.aicodemother.mapper;

import com.lsp.aicodemother.model.entity.App;
import com.lsp.aicodemother.model.entity.User;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AppMapper extends BaseMapper<App> {

    @Update("UPDATE app SET conversation_count = conversation_count + 1 WHERE id = #{appId}")
    int incrementConversationCount(@Param("appId") Long appId);

    @Select("SELECT conversation_count FROM app WHERE id = #{appId}")
    int getConversationCount(@Param("appId") Long appId);

    Page<App> getMemberApp(Page<App> page,@Param("userId")Long userId);
}

package com.lsp.aicodemother.controller;

import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.lsp.aicodemother.model.entity.AppMember;
import com.lsp.aicodemother.service.AppMemberService;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 应用成员协作表 控制层。
 *
 * @author lsp
 */
@RestController
@RequestMapping("/appMember")
public class                                                                                                                                        AppMemberController {

    @Autowired
    private AppMemberService appMemberService;

    /**
     * 保存应用成员协作表。
     *
     * @param appMember 应用成员协作表
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AppMember appMember) {
        return appMemberService.save(appMember);
    }

    /**
     * 根据主键删除应用成员协作表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Long id) {
        return appMemberService.removeById(id);
    }

    /**
     * 根据主键更新应用成员协作表。
     *
     * @param appMember 应用成员协作表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AppMember appMember) {
        return appMemberService.updateById(appMember);
    }

    /**
     * 查询所有应用成员协作表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AppMember> list() {
        return appMemberService.list();
    }

    /**
     * 根据主键获取应用成员协作表。
     *
     * @param id 应用成员协作表主键
     * @return 应用成员协作表详情
     */
    @GetMapping("getInfo/{id}")
    public AppMember getInfo(@PathVariable Long id) {
        return appMemberService.getById(id);
    }

    /**
     * 分页查询应用成员协作表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AppMember> page(Page<AppMember> page) {
        return appMemberService.page(page);
    }




}

package com.ruoyi.web.controller.system;

import java.util.List;

import com.ruoyi.system.domain.SysNoticeLite;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;

/**
 * 公告 信息操作处理
 */
@Api(tags = "公告信息操作处理")
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {

    @Autowired
    private ISysNoticeService noticeService;

    /**
     * 获取首页通知列表
     * @return
     */
    @ApiOperation("获取首页通知列表")
    @GetMapping("/notices")
    public AjaxResult getNotices() {
        startPage();
        SysNoticeLite noticeQuery = new SysNoticeLite();
        noticeQuery.setNoticeType("1");
        List<SysNoticeLite> notices = noticeService.selectAnnouncementLiteList();
        return AjaxResult.success(notices);
    }

    /**
     * 获取首页新闻中心列表
     * @return
     */
    @ApiOperation("获取首页新闻中心列表")
    @GetMapping("/news")
    public AjaxResult getNews() {
        startPage();
        SysNoticeLite noticeQuery = new SysNoticeLite();
        noticeQuery.setNoticeType("2");
        List<SysNoticeLite> announcementList = noticeService.selectNewsCenterLiteList();
        return AjaxResult.success(announcementList);
    }

    /**
     * 获取通知公告列表
     */
    @ApiOperation("获取通知公告列表")
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(@ApiParam("通知公告查询条件") SysNotice notice) {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @ApiOperation("根据通知公告编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@ApiParam("通知公告编号") @PathVariable Long noticeId) {
        return success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @ApiOperation("新增通知公告")
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@ApiParam("通知公告信息") @Validated @RequestBody SysNotice notice) {
        notice.setCreateBy(getUsername());
        return toAjax(noticeService.insertNotice(notice));
    }

    /**
     * 修改通知公告
     */
    @ApiOperation("修改通知公告")
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@ApiParam("通知公告信息") @Validated @RequestBody SysNotice notice) {
        notice.setUpdateBy(getUsername());
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     */
    @ApiOperation("删除通知公告")
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@ApiParam("通知公告编号数组") @PathVariable Long[] noticeIds) {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
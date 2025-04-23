package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.SysNoticeLite;

/**
 * 公告 服务层
 */
public interface ISysNoticeService {

    /**
     * 查询公告信息
     * 
     * @param noticeId 公告ID
     * @return 公告信息
     */
    public SysNotice selectNoticeById(Long noticeId);

    /**
     * 查询公告列表
     * 
     * @param notice 公告信息
     * @return 公告集合
     */
    public List<SysNotice> selectNoticeList(SysNotice notice);

    /**
     * 新增公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    public int insertNotice(SysNotice notice);

    /**
     * 修改公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    public int updateNotice(SysNotice notice);

    /**
     * 删除公告信息
     * 
     * @param noticeId 公告ID
     * @return 结果
     */
    public int deleteNoticeById(Long noticeId);
    
    /**
     * 批量删除公告信息
     * 
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    public int deleteNoticeByIds(Long[] noticeIds);

    /**
     * 查询通知（noticeType = '1'）的精简信息列表
     * @return 通知精简信息列表
     */
    List<SysNoticeLite> selectAnnouncementLiteList();

    /**
     * 查询新闻中心（noticeType = '2'）的精简信息列表
     * @return 新闻中心（公告）精简信息列表
     */
    List<SysNoticeLite> selectNewsCenterLiteList();

}

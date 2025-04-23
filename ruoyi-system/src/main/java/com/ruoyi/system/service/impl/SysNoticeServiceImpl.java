package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.system.domain.SysNoticeLite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.mapper.SysNoticeMapper;
import com.ruoyi.system.service.ISysNoticeService;

/**
 * 公告 服务层实现
 */
@Service
@Slf4j
public class SysNoticeServiceImpl implements ISysNoticeService {

    @Autowired
    private SysNoticeMapper noticeMapper;

    /**
     * 查询公告信息
     * 
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice selectNoticeById(Long noticeId)
    {
        return noticeMapper.selectNoticeById(noticeId);
    }

    /**
     * 查询公告列表
     * 
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice)
    {
        return noticeMapper.selectNoticeList(notice);
    }

    /**
     * 新增公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(SysNotice notice)
    {
        return noticeMapper.insertNotice(notice);
    }

    /**
     * 修改公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(SysNotice notice)
    {
        return noticeMapper.updateNotice(notice);
    }

    /**
     * 删除公告对象
     * 
     * @param noticeId 公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeById(Long noticeId)
    {
        return noticeMapper.deleteNoticeById(noticeId);
    }

    /**
     * 批量删除公告信息
     * 
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(Long[] noticeIds)
    {
        return noticeMapper.deleteNoticeByIds(noticeIds);
    }

    /**
     * 查询通知（noticeType = '1'）的精简信息列表
     *
     * @return 通知精简信息列表
     */
    @Override
    public List<SysNoticeLite> selectAnnouncementLiteList() {
        try {
            SysNotice notice = new SysNotice();
            notice.setNoticeType("1"); // 1 表示通知
            log.info("查询通知精简信息列表，noticeType: {}", notice.getNoticeType());
            List<SysNoticeLite> result = noticeMapper.selectNoticeLiteList(notice);
            for (SysNoticeLite noticeLite : result) {
                if (noticeLite != null) {
                    log.info("查询结果: noticeTitle={}, createTime={}, noticeContent={}", noticeLite.getNoticeTitle(), noticeLite.getCreateTime(), noticeLite.getNoticeContent());
                } else {
                    log.info("查询结果: null");
                }
            }
            return result;
        } catch (Exception e) {
            // 记录日志
            log.error("查询通知精简信息列表失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 查询新闻中心（noticeType = '2'）的精简信息列表
     *
     * @return 新闻中心（公告）精简信息列表
     */
    @Override
    public List<SysNoticeLite> selectNewsCenterLiteList() {
        SysNotice notice = new SysNotice();
        notice.setNoticeType("2"); // 2 表示新闻中心（公告）
        return noticeMapper.selectNoticeLiteList(notice);
    }


}

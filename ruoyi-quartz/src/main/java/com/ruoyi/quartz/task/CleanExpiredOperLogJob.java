package com.ruoyi.quartz.task;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.service.ISysOperLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("cleanLog")
public class CleanExpiredOperLogJob {

    private static final Logger log = LoggerFactory.getLogger(CleanExpiredOperLogJob.class);

    public void cleanExpiredOperLog() {
        try {
            // 获取操作日志服务
            ISysOperLogService operLogService = SpringUtils.getBean(ISysOperLogService.class);

            // 调用清理方法
            int deletedCount = operLogService.cleanExpiredOperLog();
            log.info("成功清理 {} 条过期操作记录", deletedCount);
        } catch (Exception e) {
            log.error("清理过期操作记录任务执行失败", e);
        }
    }
}

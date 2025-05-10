package com.ruoyi.quartz.task;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.system.service.ISysLogininforService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("cleanJob")
public class CleanExpiredDataJob {

    private static final Logger log = LoggerFactory.getLogger(CleanExpiredDataJob.class);

    public void clean() {
        try {
            // 获取登录信息服务
            ISysLogininforService logininforService = SpringUtils.getBean(ISysLogininforService.class);
            // 调用清理过期登录信息的方法
            int deletedCount = logininforService.cleanExpiredLoginInfo();
            log.info("成功清理 {} 条过期登录信息", deletedCount);
        } catch (Exception e) {
            log.error("清理过期登录信息任务执行失败", e);
        }
    }
}

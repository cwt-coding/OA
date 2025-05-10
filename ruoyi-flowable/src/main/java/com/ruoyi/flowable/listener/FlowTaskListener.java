package com.ruoyi.flowable.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

/**
 * 任务监听器
 *
 * create（创建）:在任务被创建且所有的任务属性设置完成后才触发
 * assignment（指派）：在任务被分配给某个办理人之后触发
 * complete（完成）：在配置了监听器的上一个任务完成时触发
 * delete（删除）：在任务即将被删除前触发。请注意任务由completeTask正常完成时也会触发
 */
@Slf4j
@Component
public class FlowTaskListener implements TaskListener{

    @Override
    public void notify(DelegateTask delegateTask) {

        String eventName = delegateTask.getEventName();
        log.info("任务监听器触发: 事件类型={}, 任务ID={}, 任务名称={}",
                eventName, delegateTask.getId(), delegateTask.getName());

        try {
            // 根据事件类型执行不同的处理逻辑
            switch (eventName) {
                case TaskListener.EVENTNAME_CREATE:
                    handleTaskCreate(delegateTask);
                    break;
                case TaskListener.EVENTNAME_ASSIGNMENT:
                    handleTaskAssignment(delegateTask);
                    break;
                case TaskListener.EVENTNAME_COMPLETE:
                    handleTaskComplete(delegateTask);
                    break;
                case TaskListener.EVENTNAME_DELETE:
                    handleTaskDelete(delegateTask);
                    break;
                default:
                    log.warn("未知的任务事件类型: {}", eventName);
            }
        } catch (Exception e) {
            log.error("处理任务事件时发生错误: {}", e.getMessage(), e);
        }
    }

    private void handleTaskCreate(DelegateTask delegateTask) {
        log.info("任务创建事件: 任务ID={}, 办理人={}",
                delegateTask.getId(), delegateTask.getAssignee());
        // 可以添加任务创建后的处理逻辑，如发送创建通知
        sendNotification(delegateTask, "任务已创建");
    }

    private void handleTaskAssignment(DelegateTask delegateTask) {
        log.info("任务指派事件: 任务ID={}, 新办理人={}",
                delegateTask.getId(), delegateTask.getAssignee());
        // 可以添加任务指派后的处理逻辑，如发送指派通知
        sendNotification(delegateTask, "任务已指派给您");
    }

    private void handleTaskComplete(DelegateTask delegateTask) {
        log.info("任务完成事件: 任务ID={}, 办理人={}",
                delegateTask.getId(), delegateTask.getAssignee());
        // 可以添加任务完成后的处理逻辑，如发送完成通知
        sendNotification(delegateTask, "任务已完成");
    }

    private void handleTaskDelete(DelegateTask delegateTask) {
        log.info("任务删除事件: 任务ID={}, 办理人={}",
                delegateTask.getId(), delegateTask.getAssignee());
        // 可以添加任务删除后的处理逻辑
    }

    private void sendNotification(DelegateTask delegateTask, String message) {
        // 实际项目中可以调用消息服务发送通知
        // 这里仅打印日志作为示例
        String assignee = delegateTask.getAssignee();
        if (assignee != null) {
            log.info("发送通知给 {}: {}", assignee, message);
            // 示例：调用消息服务
            // messageService.sendTaskNotification(assignee, delegateTask.getId(), message);
        }
    }

}

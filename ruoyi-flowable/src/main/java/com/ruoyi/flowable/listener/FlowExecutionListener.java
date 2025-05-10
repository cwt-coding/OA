package com.ruoyi.flowable.listener;

import com.ruoyi.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 执行监听器
 *
 * 执行监听器允许在执行过程中执行Java代码。
 * 执行监听器可以捕获事件的类型：
 * 流程实例启动，结束
 * 输出流捕获
 * 获取启动，结束
 * 路由开始，结束
 * 中间事件开始，结束
 * 触发开始事件，触发结束事件
 */
@Slf4j
@Component
public class FlowExecutionListener implements ExecutionListener {
    /**
     * 流程设计器添加的参数
     */
    private Expression param;

    // 事件处理类型常量
    private static final String EVENTTYPE_START = "start";
    private static final String EVENTTYPE_END = "end";
    private static final String EVENTTYPE_TAKE = "take";

    @Override
    public void notify(DelegateExecution execution) {
        try {
            String eventType = execution.getEventName();
            log.info("执行监听器触发: 事件类型={}, 执行ID={}, 流程实例ID={}",
                    eventType, execution.getId(), execution.getProcessInstanceId());

            // 解析注入的参数
            Object paramValue = param != null ? param.getValue(execution) : null;
            log.debug("监听器参数值: {}", paramValue);

            // 根据事件类型执行不同处理逻辑
            switch (eventType) {
                case EVENTTYPE_START:
                    // 处理开始事件
                    org.flowable.bpmn.model.FlowElement currentElement = execution.getCurrentFlowElement();
                    if (currentElement instanceof FlowNode) {
                        FlowNode flowNode = (FlowNode) currentElement;
                        log.info("开始事件处理: 活动ID={}, 活动名称={}",
                                flowNode.getId(), flowNode.getName());
                    }

                    // 记录流程开始时间
                    String startTimeStr = DateUtils.parseDateToStr(
                            DateUtils.YYYY_MM_DD_HH_MM_SS,
                            new Date() // 直接使用new Date()获取当前时间
                    );
                    execution.setVariable("startTime", startTimeStr);
                    log.info("开始时间：{}", startTimeStr);
                    break;

                case EVENTTYPE_END:
                    // 处理结束事件
                    org.flowable.bpmn.model.FlowElement endElement = execution.getCurrentFlowElement();
                    if (endElement instanceof FlowNode) {
                        FlowNode endFlowNode = (FlowNode) endElement;
                        log.info("结束事件处理: 活动ID={}, 活动名称={}",
                                endFlowNode.getId(), endFlowNode.getName());
                    }

                    // 记录流程结束时间
                    String endTimeStr = DateUtils.parseDateToStr(
                            DateUtils.YYYY_MM_DD_HH_MM_SS,
                            new Date(System.currentTimeMillis())
                    );
                    execution.setVariable("endTime", endTimeStr);
                    log.info("结束时间：{}", endTimeStr);
                    break;

                case EVENTTYPE_TAKE:
                    // 处理连线事件
                    org.flowable.bpmn.model.FlowElement transitionElement = execution.getCurrentFlowElement();
                    if (transitionElement instanceof SequenceFlow) {
                        SequenceFlow sequenceFlow = (SequenceFlow) transitionElement;
                        log.info("连线事件处理: 源活动={} -> 目标活动={}",
                                sequenceFlow.getSourceRef(), sequenceFlow.getTargetRef());

                    }
                    break;

                default:
                    log.warn("未知事件类型: {}", eventType);
            }
        } catch (Exception e) {
            log.error("事件处理失败: {}", e.getMessage(), e);
        }
    }
}

package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.FlowProcDefDto;

import java.util.List;

/**
 * 流程定义查询
 **/
public interface FlowDeployMapper {

    /**
     * 流程定义列表
     * @param name
     * @return
     */
    List<FlowProcDefDto> selectDeployList(String name);
}

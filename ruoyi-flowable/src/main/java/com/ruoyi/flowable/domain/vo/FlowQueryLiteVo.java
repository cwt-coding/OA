package com.ruoyi.flowable.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("工作流任务相关--请求参数")
public class FlowQueryLiteVo {

    @ApiModelProperty("当前页码")
    private Integer pageNum;

    @ApiModelProperty("每页条数")
    private Integer pageSize;

}

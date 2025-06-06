package com.ruoyi.flowable.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>可退回节点<p>
 */
@Data
@ApiModel("可退回节点")
public class ReturnTaskNodeVo {

    @ApiModelProperty("任务Id")
    private String id;

    @ApiModelProperty("用户Id")
    private String name;

}

package com.capol.component.biz.param;

import com.capol.component.framework.pojo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 分页查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QueryDatasetPageParam extends PageParam {
    /**
     * 业务主题Id
     */
    private Long subjectId;
    /**
     * 主表名
     */
    private String tableName;
    /**
     * 数据集名称
     */
    private String datasetName;
}

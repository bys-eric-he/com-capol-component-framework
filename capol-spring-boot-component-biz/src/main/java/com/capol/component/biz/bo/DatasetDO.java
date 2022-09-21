package com.capol.component.biz.bo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.capol.component.framework.object.BaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据集表
 * @TableName t_dataset
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="t_dataset")
@Data
public class DatasetDO extends BaseObject {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 父主键Id
     */
    private Long parentId;

    /**
     * 企业Id
     */
    private Long enterpriseId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 业务主题key
     */
    private String subjectKey;

    /**
     * 业务主题Id
     */
    private Long subjectId;

    /**
     * 主表名
     */
    private Long tableId;

    /**
     * 主表名
     */
    private String tableName;

    /**
     * 表来源类型(1宽表;2用户自定义表)
     */
    private Integer tableSourceType;

    /**
     * 数据集名称
     */
    private String datasetName;

    /**
     * 层级(1分类;2数据集)
     */
    private Integer level;

    /**
     * 数据结果集SQL
     */
    private String datasetSql;

    /**
     * 是否设置高级配置
     */
    private Integer advanceSetting;

    /**
     * 序号
     */
    private Integer orderNo;

}
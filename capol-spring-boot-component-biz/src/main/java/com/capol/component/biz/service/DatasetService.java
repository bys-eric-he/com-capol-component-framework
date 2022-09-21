package com.capol.component.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.capol.component.biz.bo.DatasetDO;
import com.capol.component.biz.param.QueryDatasetPageParam;
import com.capol.component.framework.pojo.PageResult;

public interface DatasetService extends IService<DatasetDO> {
    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    PageResult<DatasetDO> getDatasetByPage(QueryDatasetPageParam pageParam);
}

package com.capol.component.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.capol.component.biz.bo.DatasetDO;
import com.capol.component.biz.mapper.DatasetMapper;
import com.capol.component.biz.param.QueryDatasetPageParam;
import com.capol.component.biz.service.DatasetService;
import com.capol.component.framework.pojo.PageResult;
import com.capol.component.framework.query.LambdaQueryWrapperX;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DatasetServiceImpl extends ServiceImpl<DatasetMapper, DatasetDO> implements DatasetService {
    @Autowired
    private DatasetMapper datasetMapper;

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<DatasetDO> getDatasetByPage(QueryDatasetPageParam pageParam) {
        LambdaQueryWrapperX<DatasetDO> queryWrapperX = new LambdaQueryWrapperX<>();
        if (StringUtils.isNotBlank(pageParam.getDatasetName())) {
            queryWrapperX.eqIfPresent(DatasetDO::getDatasetName, pageParam.getDatasetName());
        }
        if (StringUtils.isNotBlank(pageParam.getTableName())) {
            queryWrapperX.eqIfPresent(DatasetDO::getTableName, pageParam.getTableName());
        }
        if (pageParam.getSubjectId() != null && pageParam.getSubjectId() > 0L) {
            queryWrapperX.eqIfPresent(DatasetDO::getSubjectId, pageParam.getSubjectId());
        }
        return datasetMapper.selectPage(pageParam, queryWrapperX);
    }
}

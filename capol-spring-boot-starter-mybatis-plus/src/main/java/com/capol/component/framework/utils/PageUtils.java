package com.capol.component.framework.utils;

import com.capol.component.framework.pojo.PageParam;

public class PageUtils {
    public static int getStart(PageParam pageParam) {
        return (pageParam.getPageNo() - 1) * pageParam.getPageSize();
    }
}

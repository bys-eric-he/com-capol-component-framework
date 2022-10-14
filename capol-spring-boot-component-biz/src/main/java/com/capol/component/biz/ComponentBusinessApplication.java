package com.capol.component.biz;

import com.alibaba.fastjson.JSON;
import com.capol.component.biz.bo.DatasetDO;
import com.capol.component.biz.context.SpringContextHolder;
import com.capol.component.biz.param.QueryDatasetPageParam;
import com.capol.component.biz.service.DatasetService;
import com.capol.component.framework.annotation.EnableRedis;
import com.capol.component.framework.config.CapolRedisTemplate;
import com.capol.component.framework.constants.RedisModeConstants;
import com.capol.component.framework.core.CapolRabbitMQTemplate;
import com.capol.component.framework.pojo.PageResult;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableRedis(value = RedisModeConstants.REDIS_SENTINEL)
@EnableAspectJAutoProxy
@MapperScan(basePackages = {"com.capol.component.biz.mapper"})
@ComponentScan(basePackages = {"com.capol.component.framework.*", "com.capol.component.biz.*"})
public class ComponentBusinessApplication {
    private static final String CAPOL_REDIS_COMPONENT = "CAPOL-REDIS-COMPONENT";

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ComponentBusinessApplication.class, args);
        CapolRabbitMQTemplate capolRabbitMQTemplate = SpringContextHolder.getBean(CapolRabbitMQTemplate.class);
        CapolRedisTemplate capolRedisTemplate = SpringContextHolder.getBean(CapolRedisTemplate.class);
        capolRedisTemplate.set(CAPOL_REDIS_COMPONENT, "这是Capol Redis组件写入的数据!!!", 6000);

        for (int i = 0; i < 3; i++) {
            Thread.sleep(1000L);
            capolRabbitMQTemplate.send(String.valueOf(2022090001 + i), String.format("这是第 %d 条MQ消息!!!!", i));
        }
        Object value = capolRedisTemplate.get(CAPOL_REDIS_COMPONENT);
        System.out.println("获取Redis缓存值-> Key: " + CAPOL_REDIS_COMPONENT + "Value: " + value);

        DatasetService datasetService = SpringContextHolder.getBean(DatasetService.class);
        QueryDatasetPageParam pageParam = new QueryDatasetPageParam();
        //pageParam.setSubjectId(1658850545999556L);
        pageParam.setPageNo(1);
        pageParam.setPageSize(10);
        PageResult<DatasetDO> result = datasetService.getDatasetByPage(pageParam);

        System.out.println("分页查询结果：" + JSON.toJSONString(result));

    }
}

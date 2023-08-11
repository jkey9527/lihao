package com.cattle.lihao.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.cattle.lihao.bean.*;
import com.cattle.lihao.enums.LogModelEnum;
import com.cattle.lihao.enums.LogTypeEnum;
import com.cattle.lihao.mapper.ProductMapper;
import com.cattle.lihao.mapper.RecordMapper;
import com.cattle.lihao.service.RecordService;
import com.cattle.lihao.service.SystemService;
import com.cattle.lihao.util.UuIdUtil;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 费用服务类
 *
 * @author niujie
 * @date 2023/4/21 22:40
 */
@Service
@AllArgsConstructor
public class RecordServiceImpl implements RecordService {
    private static final Logger LOGGER = Logger.getLogger(RecordServiceImpl.class);

    private ProductMapper productMapper;
    private RecordMapper recordMapper;
    private SystemService systemService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveRecord(RecordBean record) throws Exception {
        try {
            List<ProductDetailBean> productDetailBeans = record.getProductDetailBeans();
            if(CollUtil.isEmpty(productDetailBeans)){
                throw new Exception("未填写任何货品明细信息！");
            }
            record.setR_id(UuIdUtil.getUUID());
            int count = productDetailBeans.stream().mapToInt(ProductDetailBean::getPro_det_num).sum();
            record.setR_sell_num(count);
            Date date = new Date();
            int week = DateUtil.weekOfMonth(date);
            int month = DateUtil.month(date) + 1;
            int year = DateUtil.year(date);
            record.setR_date(date);
            record.setR_week(week);
            record.setR_month(month);
            record.setR_year(year);
            for (ProductDetailBean productDetailBean : productDetailBeans) {
                productMapper.subProductDetail(productDetailBean);
            }
            recordMapper.saveRecord(record);
            systemService.saveOptLog(LogModelEnum.record.getValue(), LogTypeEnum.save.getValue(), JSONUtil.toJsonStr(record));
        }catch (Exception e){
            LOGGER.error("操作异常",e);
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public RecordResult getRecord(RecordParam recordParam) throws Exception {
        try {
            RecordResult recordResult = new RecordResult();
            List<RecordBean> recordBeanList = recordMapper.getRecord(recordParam);
            if(CollUtil.isEmpty(recordBeanList)){
                return recordResult;
            }
            Map<String, List<RecordBean>> typeGroup =
                    recordBeanList.stream().collect(Collectors.groupingBy(RecordBean::getR_type));
            Map moneyMap = new HashMap<>(3);
            Map countMap = new HashMap<>(7);
            for (Map.Entry<String, List<RecordBean>> entry : typeGroup.entrySet()) {
                List<RecordBean> recordBeans = entry.getValue();
                BigDecimal totalMoney = recordBeans.stream().map(RecordBean::getR_sell_price).reduce(BigDecimal.ZERO, BigDecimal::add);
                moneyMap.put(entry.getKey(),totalMoney);
            }
            BigDecimal totalMoney = recordBeanList.stream().map(RecordBean::getR_sell_price).reduce(BigDecimal.ZERO, BigDecimal::add);
            moneyMap.put("totalMoney",totalMoney);

            List<String> proIds = recordBeanList.stream().map(RecordBean::getR_pro_id).distinct().collect(Collectors.toList());
            List<ProductBean> productBeans = productMapper.getProductListByProIds(proIds);
            Map<String, ProductBean> productMap = productBeans.stream().collect(Collectors.toMap(ProductBean::getPro_id, Function.identity()));
            for (RecordBean recordBean : recordBeanList) {
                String proId = recordBean.getR_pro_id();
                ProductBean productBean = productMap.get(proId);
                recordBean.setR_pro_type(productBean.getPro_type());
            }
            Map<String, List<RecordBean>> proTypeGroup =
                    recordBeanList.stream().collect(Collectors.groupingBy(RecordBean::getR_pro_type));
            for (Map.Entry<String, List<RecordBean>> entry : proTypeGroup.entrySet()) {
                List<RecordBean> recordBeans = entry.getValue();
                int count = recordBeans.stream().mapToInt(RecordBean::getR_sell_num).sum();
                countMap.put(entry.getKey(),count);
            }
            int totalCount = recordBeanList.stream().mapToInt(RecordBean::getR_sell_num).sum();
            countMap.put("totalCount",totalCount);
            recordResult.setMoneyMap(moneyMap);
            recordResult.setCountMap(countMap);
            return recordResult;
        }catch (Exception e){
            LOGGER.error("操作异常",e);
            throw new Exception(e.getMessage());
        }
    }
}

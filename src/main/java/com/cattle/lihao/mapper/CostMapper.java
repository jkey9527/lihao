package com.cattle.lihao.mapper;

import com.cattle.lihao.bean.CostBean;
import com.cattle.lihao.bean.SettlementBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 费用映射类
 *
 * @author niujie
 * @date 2023/4/21 22:35
 */
@Mapper
public interface CostMapper {

    /**
     * 费用
     * @param cost cost
     * @return void
     * @author niujie
     * @date 2023/8/6
     */
    void saveCost(CostBean cost);

    /**
     * 查询费用信息
     * @param cost cost
     * @return java.util.List<com.cattle.lihao.bean.CostBean>
     * @author niujie
     * @date 2023/8/6
     */
    List<CostBean> getCostList(CostBean cost);

    /**
     * 保存结算
     * @param settlement settlement
     * @return void
     * @author niujie
     * @date 2023/8/6
     */
    void saveSettlement(SettlementBean settlement);

    /**
     * 查询结算
     * @param settlement settlement
     * @return java.util.List<com.cattle.lihao.bean.SettlementBean>
     * @author niujie
     * @date 2023/8/6
     */
    List<SettlementBean> getSettlementList(SettlementBean settlement);
}

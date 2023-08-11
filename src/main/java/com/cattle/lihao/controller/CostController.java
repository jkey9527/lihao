package com.cattle.lihao.controller;

import com.cattle.lihao.bean.CostBean;
import com.cattle.lihao.bean.SettlementBean;
import com.cattle.lihao.response.Result;
import com.cattle.lihao.service.CostService;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 费用
 *
 * @author niujie
 * @date 2023/4/21 22:43
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "cattle/lihao/cost", method = RequestMethod.POST)
@CrossOrigin(origins = "*")
public class CostController {
    private static final Logger LOGGER = Logger.getLogger(CostController.class);

    private CostService costService;

    /**
     * 费用
     * @param cost cost
     * @return java.lang.String
     * @author niujie
     * @date 2023/8/6
     */
    @RequestMapping("/saveCost")
    public String saveCost(@RequestBody CostBean cost) {
        try {
            costService.saveCost(cost);
            return Result.success("操作成功！");
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 查询费用信息
     * @param cost cost
     * @return java.lang.String
     * @author niujie
     * @date 2023/8/6
     */
    @RequestMapping("/getCostList")
    public String getCostList(@RequestBody CostBean cost) {
        try {
            List<CostBean> costList = costService.getCostList(cost);
            return Result.success("操作成功！",costList);
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 结算
     * @return java.lang.String
     * @author niujie
     * @date 2023/8/6
     */
    @RequestMapping("/getSettlement")
    public String getSettlement() {
        try {
            SettlementBean settlement = costService.getSettlement();
            return Result.success("操作成功！",settlement);
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 保存结算
     * @param settlement settlement
     * @return java.lang.String
     * @author niujie
     * @date 2023/8/6
     */
    @RequestMapping("/saveSettlement")
    public String saveSettlement(@RequestBody SettlementBean settlement) {
        try {
            costService.saveSettlement(settlement);
            return Result.success("操作成功！");
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 查询结算
     * @param settlement settlement
     * @return java.lang.String
     * @author niujie
     * @date 2023/8/6
     */
    @RequestMapping("/getSettlementList")
    public String getSettlementList(@RequestBody SettlementBean settlement) {
        try {
            List<SettlementBean> settlements = costService.getSettlementList(settlement);
            return Result.success("操作成功！", settlements);
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }


}

package com.cattle.lihao.mapper;

import com.cattle.lihao.bean.OptLog;
import com.cattle.lihao.bean.SystemBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *  系统参数映射
 * @author niujie
 * @date 2023/6/1 16:56
 */
@Mapper
public interface SystemMapper {

    /**
     * 查询系统参数
     * @return java.util.List<com.cattle.lihao.bean.SystemBean>
     * @author niujie
     * @date 2023/6/1
     */
    List<SystemBean> getSystem();

    /**
     * 保存系统参数
     * @param systemBean systemBean
     * @return void
     * @author niujie
     * @date 2023/6/1
     */
    void saveSystem(SystemBean systemBean);

    /**
     * 保存系统日志
     * @param optLog optLog
     * @return void
     * @author niujie
     * @date 2023/8/6
     */
    void saveOptLog(OptLog optLog);

    /**
     * 查询日志
     * @param log log
     * @return java.util.List<com.cattle.lihao.bean.OptLog>
     * @author niujie
     * @date 2023/8/6
     */
    List<OptLog> getLogs(OptLog log);
}

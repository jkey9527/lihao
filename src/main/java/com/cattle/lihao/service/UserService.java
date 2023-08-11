package com.cattle.lihao.service;

import com.cattle.lihao.bean.UserBean;

/**
 * 用户服务接口
 *
 * @author niujie
 * @date 2023/4/21 22:38
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param userBean userBean
     * @return com.cattle.lihao.bean.UserBean
     * @author niujie
     * @date 2023/4/22
     */
    UserBean loginIn(UserBean userBean) throws Exception;

    /**
     * 注销登录
     *
     * @param user user
     * @return void
     * @author niujie
     * @date 2023/5/14
     */
    void loginOut(UserBean user);
}

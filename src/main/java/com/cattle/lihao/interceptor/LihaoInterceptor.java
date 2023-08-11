package com.cattle.lihao.interceptor;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.cattle.lihao.bean.ProfileBean;
import com.cattle.lihao.bean.UserBean;
import com.cattle.lihao.response.Result;
import com.cattle.lihao.service.TokenService;
import com.cattle.lihao.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.util.Date;

/**
 * 拦截器
 * 用于判断token与用户登录token
 *
 * @author niujie
 * @date 2023/4/29 11:05
 */
public class LihaoInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = Logger.getLogger(LihaoInterceptor.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProfileBean profileBean;

    public LihaoInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOGGER.info(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN) + "--system url：" + request.getRequestURL());
        boolean development = Convert.toBool(profileBean.isDevelopment(), false);
        LOGGER.info("development is " + development);
        if (development) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }
        String token = request.getHeader("token");
        LOGGER.info("======>token：" + token);
        String msg = tokenService.checkToken(token);
        LOGGER.info("======>token-error-msg：" + msg);
        if (StrUtil.isBlank(msg)) {
            String userId = request.getHeader("userid");
            LOGGER.info("======>userId：" + userId);
            boolean userLogin = redisUtil.hasKey(userId);
            LOGGER.info("======>userLogin：" + userLogin);
            if (!userLogin) {
                msg = "登录信息已过期，请重新登录！";
            }
            UserBean userBean = new UserBean();
            userBean.setUser_id(userId);
            UserContext.set(userBean);
        }
        if (StrUtil.isNotBlank(msg)) {
            LOGGER.info(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN) + "--错误信息：" + msg);
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "*");

            PrintWriter writer = response.getWriter();
            writer.write(Result.failToLogin(msg));
            writer.flush();
            writer.close();
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}

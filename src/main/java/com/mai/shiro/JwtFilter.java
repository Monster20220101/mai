package com.mai.shiro;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mai.common.lang.Result;
import com.mai.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 继承shiro内置的AuthenticatingFilter，一个内置了可以自动登录方法的过滤器
 */
@Component
public class JwtFilter extends AuthenticatingFilter {
    /*
    点开源码：AuthenticatingFilter -> executeLogin
    可以看到shiro的subject.login(token)
    这里将登录委托给UserReam处理

    这里面：用户被锁定的话 抛出异常，进入onLoginFailure方法里面。
    为了返回Result给前端，重写onLoginFailure方法
     */
    @Autowired
    JwtUtils jwtUtils;

    /**
     * 实现登录，我们需要生成我们自定义支持的JwtToken
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 获取 token
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if (StringUtils.isBlank(jwt)) {
            return null; // 跳过登录
        }
        return new JwtToken(jwt);
    }

    /**
     * 拦截校验，当头部没有Authorization的时候，我们直接通过，不需要自动登录（游客状态）；
     * 当带有的时候，首先我们校验jwt的有效性，没问题我们就直接执行executeLogin方法实现自动登录
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            return true;
        } else {
            // 判断是否已过期
            Claims claim = jwtUtils.getClaimByToken(token);
            // claim == null：校验出错
            if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {

                throw new ExpiredCredentialsException("token已失效，请重新登录！");
            }
        }
        // 执行自动登录
        return executeLogin(servletRequest, servletResponse);
    }

    /**
     * 登录异常时候进入的方法，我们直接把异常信息封装然后抛出
     *
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            //处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            Result r = Result.fail(throwable.getMessage());
            String json = JSONUtil.toJsonStr(r);
            httpResponse.getWriter().print(json);
        } catch (IOException e1) {
        }
        return false;
    }

    /**
     * 对跨域提供支持 <br/>
     * 拦截器的前置拦截，因为我们是前后端分离项目，项目中除了需要跨域全局配置之外，我们在拦截器中也需要提供跨域支持。
     * 这样，拦截器才不会在进入Controller之前就被限制了。
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}

package com.example.springboot.filter;

import com.example.springboot.services.RedisService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenFilter implements Filter {

    private final RedisService redisService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String token = req.getHeader("Authorization");
        if (token != null && redisService.hasKey(token)) {
            // 用户在线，刷新过期时间
            redisService.expire(token, 1800); // 30分钟
            chain.doFilter(request, response);
        } else {
            // 设置响应的编码，确保输出的字符流为 UTF-8
            res.setContentType("application/json;charset=UTF-8");

            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // 返回错误信息
            res.getWriter().write("{ \"error\": \"Token无效或已过期，请重新登录！\" }");
        }
    }
}

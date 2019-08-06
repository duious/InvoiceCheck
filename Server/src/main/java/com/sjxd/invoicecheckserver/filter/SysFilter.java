package com.sjxd.invoicecheckserver.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.sjxd.invoicecheckserver.filter.BaseFilter.printAjaxObj;

/**
 * @author zyl
 * @date 2019/7/31 10:51
 */
@Component
@WebFilter(urlPatterns = {"*.do"}, filterName = "sysFilter", asyncSupported = true, initParams = {@WebInitParam(name = "excludedUri", value = "/")})
@Order(2)
public class SysFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String url = String.valueOf(request.getRequestURI());
        if (url.endsWith(".do")) {
            request.getRequestDispatcher(request.getRequestURI().replace(".do", "")).forward(request, response);
            // chain.doFilter(request, response);
            return;
        } else {
            printAjaxObj(request, response, "未授权请求！！！");
        }
    }

    @Override
    public void destroy() {

    }

}

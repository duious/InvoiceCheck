package com.sjxd.invoicecheckserver.filter;

import com.sjxd.invoicecheckserver.util.Result;
import com.sjxd.invoicecheckserver.util.Util;
import com.sjxd.invoicecheckserver.util.Validate;
import com.sjxd.invoicecheckserver.util.token.Token;
import com.sjxd.invoicecheckserver.util.token.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 过滤器
 *
 * @author zhangyl
 * <p>
 * 2017年6月15日
 */
@Component
@WebFilter(urlPatterns = "/*", filterName = "baseFilter")
@Order(1)
public class BaseFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(BaseFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();

        String tokenId = request.getParameter("token");
        if (!Validate.isNull(tokenId)) {
            int type = Integer.parseInt(request.getParameter("tokenType"));
            Result result = Token.checkToken(tokenId, type);
            if (!result.getCode().equals(Result.CODE_SUCCESS)) {
                logger.info("\tip:" + Util.getIp(request) + "\tvisit:" + request.getRequestURI() + "\thas no " +
                        "token, blocked");
                printAjaxObj(request, response, result.getMsg());
                return;
            }
            Map<String, Object> map = Token.getTokenAttribute(tokenId, type);
            if (null != map && !map.isEmpty()) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    session.setAttribute(entry.getKey(), entry.getValue());
                }
            }
            chain.doFilter(request, response);
            return;
        }
        String url = String.valueOf(request.getRequestURI());
        if ("/".equalsIgnoreCase(request.getRequestURI())) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=utf-8");
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            // pc浏览
            if (!"2345".contains(TokenType.getTokenType(request))) {
                chain.doFilter(request, response);
            } else {
                request.getRequestDispatcher("/").forward(request, response);
            }
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    /**
     * 将数据用json格式打印到页面
     */
    static void printAjaxObj(HttpServletRequest request, HttpServletResponse response, String msg)
            throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        // 跨域问题 开发时用
//        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "0");
//        response.setHeader("Access-Control-Allow-Headers",
//                "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, " +
//                        "Expires, Content-Type, X-E4M-With,userId,token");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("XDomainRequestAllowed", "1");
//         String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
//                 + request.getContextPath() + "/";
        PrintWriter out = response.getWriter();
        if ("POST".equalsIgnoreCase(String.valueOf(request.getMethod()))) {
            Result result = new Result();
            result.setMsg("未授权请求！！！");
            out.write(Util.convJSONObject(result));
            out.flush();
            out.close();
            return;
        }
        response.reset();
        out.flush();
        out.close();
    }
}

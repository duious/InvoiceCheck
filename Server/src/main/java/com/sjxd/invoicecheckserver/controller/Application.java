package com.sjxd.invoicecheckserver.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;

/**
 * @author zyl
 * @date 2019/7/31 9:58
 */
@ComponentScan({"com.sjxd.invoicecheckserver.controller", "com.sjxd.invoicecheckserver.server",
        "com.sjxd.invoicecheckserver.model", "com.sjxd.invoicecheckserver.util",
        "com.sjxd.invoicecheckserver.filter"})
@EntityScan({"com.sjxd.gconfs.*.model"})
@ServletComponentScan
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 设置匹配.do后缀的请求
     *
     * @param dispatcherServlet
     * @return
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean bean = new ServletRegistrationBean(dispatcherServlet);
        bean.addUrlMappings("*.do");
        bean.addUrlMappings("*.html");
        bean.addUrlMappings("/*");
        bean.setLoadOnStartup(1);
        bean.setMultipartConfig(new MultipartConfigElement("", 10240, 10485760, 0));
        bean.setAsyncSupported(true);
        bean.setName("dispatcherServlet");
        bean.setOrder(1);
        return bean;
    }

    /***
     * 打包
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }

}

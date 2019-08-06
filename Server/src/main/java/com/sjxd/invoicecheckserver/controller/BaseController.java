package com.sjxd.invoicecheckserver.controller;

import com.sjxd.invoicecheckserver.model.InvoiceInfo;
import com.sjxd.invoicecheckserver.server.IndexServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyl
 * @date 2019/7/31 10:00
 */
@RestController
public class BaseController {

    private final IndexServer indexServer;

    @Autowired
    public BaseController(IndexServer indexServer) {
        this.indexServer = indexServer;
    }

    @RequestMapping("/index")
    public Object index() {
        return indexServer.index();

    }

    @RequestMapping("/list")
    public Object list(String type, String pageNum, String pageSize) {
        return indexServer.list(type, pageNum, pageSize);

    }

    @RequestMapping("/save")
    public Object save(InvoiceInfo invoiceInfo) {
        return indexServer.save(invoiceInfo);

    }

    @RequestMapping("/del")
    public Object del(String no) {
        return indexServer.del(no);

    }

    @RequestMapping("/check")
    public Object check(String no) {
        return indexServer.check(no);
    }

    /**
     * @param file base64 img String
     * @return
     */
    @RequestMapping("/recognition")
    public Object recognition(String file) {
        return indexServer.recognition(file);
    }
}

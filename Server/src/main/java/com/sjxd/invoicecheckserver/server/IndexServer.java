package com.sjxd.invoicecheckserver.server;

import com.sjxd.invoicecheckserver.model.InvoiceInfo;
import com.sjxd.invoicecheckserver.util.Result;

/**
 * @author zyl
 * @date 2019/7/31 10:06
 */
public interface IndexServer {
    /**
     * @return
     */
    Result index();

    /**
     * @return
     */
    Result save(InvoiceInfo invoiceInfo);

    /**
     * @return
     */
    Result del(String no);

    /**
     * @return
     */
    Result list(String type,String pageNum,String pageSize);

    /**
     * @return
     */
    Result check(String no);

    /**
     * @return
     */
    Result recognition(String file);
}

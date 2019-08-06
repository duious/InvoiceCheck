package com.sjxd.invoicecheckserver.server.impl;

import com.sjxd.invoicecheckserver.model.InvoiceInfo;
import com.sjxd.invoicecheckserver.server.IndexServer;
import com.sjxd.invoicecheckserver.util.Result;
import com.sjxd.invoicecheckserver.util.Util;
import com.sjxd.invoicecheckserver.util.UtilBaiDu;
import com.sjxd.invoicecheckserver.util.Validate;
import com.sjxd.invoicecheckserver.util.cache.UtilRedis;
import net.sf.json.JSONArray;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyl
 * @date 2019/7/31 10:07
 */
@Service
public class IndexServerImpl implements IndexServer {
    private UtilRedis utilRedis = UtilRedis.getInstance();

    @Override
    public Result index() {
        Result result = new Result();
        long len = utilRedis.lists().llen(UtilRedis.BASE_KEY + "list");
        result.setData(len);
        result.setMsg(Result.MSG_SUCCESS);
        result.setCode(Result.CODE_SUCCESS);
        return result;
    }

    @Override
    public Result save(InvoiceInfo invoiceInfo) {
        Result result = new Result();
        if (0 == utilRedis.sets().sadd(UtilRedis.BASE_KEY + "set:" + invoiceInfo.getNumber(), invoiceInfo.getNumber())) {
            result.setCode(Result.CODE_ERROR_EXIST);
            result.setMsg(Result.MSG_EXIST);
            return result;
        }
        int oldLen = new Long(utilRedis.lists().llen(UtilRedis.BASE_KEY + "list")).intValue();
        int newLen = new Long(utilRedis.lists().rpush(UtilRedis.BASE_KEY + "list", Util.convJSONObject(invoiceInfo))).intValue();
        // 异常保存处理
        if (newLen - oldLen != 1) {
            utilRedis.lists().lrem(UtilRedis.BASE_KEY + "list", 1, Util.convJSONObject(invoiceInfo));
        }
        String res = utilRedis.lists().lindex(UtilRedis.BASE_KEY + "list", --newLen);
        Map<String, Object> invoiceInfoMap = Util.convJSONToMap(res);
        if (MapUtils.isEmpty(invoiceInfoMap) || !invoiceInfoMap.get("number").equals(invoiceInfo.getNumber())) {
            // 保存失败 清除存储记录
            if (1 != utilRedis.sets().srem(UtilRedis.BASE_KEY + "set:" + invoiceInfo.getNumber(), invoiceInfo.getNumber())) {
                result.setCode(Result.CODE_ERROR);
                result.setMsg(Result.MSG_SERVER_ERROR);
                return result;
            }
            result.setCode(Result.CODE_ERROR);
            result.setMsg(Result.MSG_SAVE_ERROR);
            return result;
        }
        result.setMsg(Result.MSG_SAVE_SUCCESS);
        result.setCode(Result.CODE_SUCCESS);
        return result;
    }

    @Override
    public Result del(String number) {
        Result result = new Result();
        if (!utilRedis.sets().sismember(UtilRedis.BASE_KEY + "set:" + number, number)) {
            result.setCode(Result.CODE_ERROR_NULL);
            result.setMsg(Result.MSG_NOT_EXIST);
            return result;
        }
        List<String> invoiceInfoStringList = utilRedis.lists().lrange(UtilRedis.BASE_KEY + "list", 0, utilRedis.lists().llen(UtilRedis.BASE_KEY + "list"));
        for (int i = 0; i < invoiceInfoStringList.size(); i++) {
            if (!Validate.isNull(invoiceInfoStringList.get(i))) {
                if (MapUtils.isNotEmpty(Util.convJSONToMap(invoiceInfoStringList.get(i)))) {
                    if (number.equals(Util.convJSONToMap(invoiceInfoStringList.get(i)).get("number"))) {
                        long size = utilRedis.lists().lrem(UtilRedis.BASE_KEY + "list", 1, invoiceInfoStringList.get(i));
                        if (1 != size) {
                            result.setCode(Result.CODE_ERROR);
                            result.setMsg(Result.MSG_DEL_ERROR);
                            return result;
                        }
                        size = utilRedis.sets().srem(UtilRedis.BASE_KEY + "set:" + number, number);
                        if (1 != size) {
                            result.setCode(Result.CODE_ERROR);
                            result.setMsg(Result.MSG_DEL_ERROR);
                            return result;
                        }
                        break;
                    }
                }
            }
        }
        result.setMsg(Result.MSG_DEL_SUCCESS);
        result.setCode(Result.CODE_SUCCESS);
        return result;
    }

    @Override
    public Result list(String type, String pageNum, String pageSize) {
        Result result = new Result();
        Map<String, Object> map = new HashMap<>(8);
        if (Validate.isNull(pageNum, pageSize)) {
            result.setCode(Result.CODE_ERROR);
            result.setMsg(Result.MSG_PARAM_NULL);
            return result;
        }
        long len = utilRedis.lists().llen(UtilRedis.BASE_KEY + "list");
        if (len - Long.parseLong(Util.getPageNum(pageNum, pageSize)) <= 0 ||
                (len - Long.parseLong(Util.getPageNum(pageNum, pageSize)) + Long.valueOf(pageSize)) <= 0) {
            result.setCode(Result.CODE_ERROR);
            result.setMsg(Result.MSG_PARAM_ERROR);
            return result;
        }
        List<String> invoiceInfoStringList = utilRedis.lists().lrange(UtilRedis.BASE_KEY + "list",
                Long.parseLong(Util.getPageNum(pageNum, pageSize)), Long.valueOf(Util.getPageNum(pageNum, pageSize)) + Long.valueOf(pageSize));
        List<InvoiceInfo> invoiceInfoList = JSONArray.toList(JSONArray.fromObject(invoiceInfoStringList), InvoiceInfo.class);
        for (int i = 0; i < invoiceInfoList.size(); i++) {
            if (null != invoiceInfoList.get(i)) {
                invoiceInfoList.get(i).setDate(Util.TimeFormat(invoiceInfoList.get(i).getDate(), 4));
            }
        }
        map.put("listData", invoiceInfoList);
        map.put("total", len);
        result.setData(map);
        result.setMsg(Result.MSG_SUCCESS);
        result.setCode(Result.CODE_SUCCESS);
        return result;
    }

    @Override
    public Result check(String no) {
        Result result = new Result();
        if (utilRedis.sets().sismember(UtilRedis.BASE_KEY + "set:" + no, no)) {
            result.setCode(Result.CODE_ERROR_EXIST);
            result.setMsg(Result.MSG_EXIST);
            return result;
        }
        result.setMsg(Result.MSG_NOT_EXIST);
        result.setCode(Result.CODE_SUCCESS);
        return result;
    }

    @Override
    public Result recognition(String file) {
        Result result = new Result();
        file = file.replace("{{", "").replace("}}", "");
        Result baiduResult = UtilBaiDu.simpleRecognition(file);
        if (!baiduResult.getCode().equals(Result.CODE_SUCCESS)) {
            result.setMsg(Result.MSG_REC_ERROR);
            result.setCode(Result.CODE_ERROR);
            return result;
        }
        Map<String, Object> invoiceInfoMap;
        InvoiceInfo invoiceInfo = new InvoiceInfo();
        for (Object obj : JSONArray.fromObject(String.valueOf(baiduResult.getData()))) {
            invoiceInfoMap = Util.convJSONToMap(String.valueOf(obj));
            if (String.valueOf(invoiceInfoMap.get("words")).contains("年")) {
                invoiceInfo.setDate(String.valueOf(invoiceInfoMap.get("words")));
            }
            if ((String.valueOf(invoiceInfoMap.get("words")).contains("号") ||
                    String.valueOf(invoiceInfoMap.get("words")).contains("码")) &&
                    String.valueOf(invoiceInfoMap.get("words")).length() <= 12) {
                invoiceInfo.setNumber(String.valueOf(invoiceInfoMap.get("words")));
            }
        }
        result.setData(invoiceInfo);
        result.setMsg(Result.MSG_REC_SUCCESS);
        result.setCode(Result.CODE_SUCCESS);
        return result;
    }

}

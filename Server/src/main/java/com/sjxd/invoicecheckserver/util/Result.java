package com.sjxd.invoicecheckserver.util;

/**
 * @author zyl
 * @date 2019/7/31 10:07
 */
public class Result {
    public static final String CODE_SUCCESS = "200";
    public static final String CODE_ERROR = "500";
    public static final String CODE_ERROR_NULL = "501";
    public static final String CODE_ERROR_EXIST = "502";

    public static final String MSG_NULL = "暂无数据！";
    public static final String MSG_SUCCESS = "操作成功！";
    public static final String MSG_EXIST = "数据已存在！";
    public static final String MSG_NOT_EXIST = "数据不存在！";
    public static final String MSG_SAVE_SUCCESS = "保存成功！";
    public static final String MSG_SAVE_ERROR = "保存失败！";
    public static final String MSG_DEL_SUCCESS = "删除成功！";
    public static final String MSG_DEL_ERROR = "删除失败！";
    public static final String MSG_SERVER_ERROR = "服务异常！";
    public static final String MSG_PARAM_NULL = "参数不能为空！";
    public static final String MSG_PARAM_ERROR = "参数错误！";
    public static final String MSG_REC_SUCCESS = "识别成功！";
    public static final String MSG_REC_ERROR = "识别失败！";

    private String code;
    private String msg;
    private Object data;

    public Result() {
        this.code = CODE_ERROR_NULL;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

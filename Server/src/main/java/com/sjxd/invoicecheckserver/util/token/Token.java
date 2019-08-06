package com.sjxd.invoicecheckserver.util.token;

import com.sjxd.invoicecheckserver.util.Result;
import com.sjxd.invoicecheckserver.util.Util;
import com.sjxd.invoicecheckserver.util.Validate;
import com.sjxd.invoicecheckserver.util.cache.UtilRedis;

import java.util.HashMap;
import java.util.Map;

/**
 * token管理工具
 *
 * @author zhangyl
 * <p>
 * 17-11-7
 */
public class Token {
    private static String token = "token";
    private static String app = "app";
    private static String apple = "apple";
    private static String wechat = "wechat";
    private static String pc = "pc";
    private static String other = "other";
    private static int expireTime = 3600;
    private static UtilRedis utilRedis = UtilRedis.getInstance();

    /**
     * 获取token
     *
     * @param type 渠道
     * @return token
     */
    public static String getToken(int type) {
        String tokenId = Util.getUUID();
        String key = getKey(tokenId, type);
        utilRedis.strings().set(key, "");
        utilRedis.keys().expire(key, expireTime);
        return tokenId;
    }

    /**
     * 检查token
     *
     * @param tokenId tokenid
     * @param type    渠道
     * @return 结果
     */
    public static Result checkToken(String tokenId, int type) {
        Result result = new Result();
        result.setCode(utilRedis.keys().exists(getKey(tokenId, type)) == true ? "200" : "100");
        if (!result.getCode().equals(Result.CODE_SUCCESS)) {
            result.setMsg("令牌错误");
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            if (utilRedis.keys().exists(getKey(tokenId, type))) {
                String str = utilRedis.strings().get(getKey(tokenId, type));
                if (!Validate.isNull(str)) {
                    map = Util.convJSONToMap(str);
                }
            }
            map.put("utime", Util.getTime());
            utilRedis.strings().set(getKey(tokenId, type), Util.convJSONObject(map));
            utilRedis.keys().expire(getKey(tokenId, type), expireTime);
        }
        return result;
    }

    /**
     * 获取存储值
     *
     * @param tokenId tokenid
     * @param type    渠道
     * @return 存储值map
     */
    public static Map<String, Object> getTokenAttribute(String tokenId, int type) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (utilRedis.keys().exists(getKey(tokenId, type))) {
            String str = utilRedis.strings().get(getKey(tokenId, type));
            if (!Validate.isNull(str)) {
                map = Util.convJSONToMap(str);
            }
        }
        return map;
    }

    /**
     * 增加存储值
     *
     * @param tokenId tokenid
     * @param type    渠道
     * @param addKey  key
     * @param value   value
     * @return 存储值map
     */
    public static void setTokenAttribute(String tokenId, int type, String addKey, String value) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (utilRedis.keys().exists(getKey(tokenId, type))) {
            String str = utilRedis.strings().get(getKey(tokenId, type));
            if (!Validate.isNull(str)) {
                map = Util.convJSONToMap(str);
            }
        }
        map.put(addKey, value);
        utilRedis.strings().set(getKey(tokenId, type), Util.convJSONObject(map));
        utilRedis.keys().expire(getKey(tokenId, type), expireTime);
    }

    /**
     * 获取存储key
     *
     * @param tokenId 客户端tokenid
     * @param type    token类型
     * @return key
     */
    private static String getKey(String tokenId, int type) {
        String key;
        switch (type) {
            case 1:
                key = token + ":" + pc + ":" + tokenId;
                break;
            case 2:
                key = token + ":" + wechat + ":" + tokenId;
                break;
            case 3:
                key = token + ":" + app + ":" + tokenId;
                break;
            case 4:
                key = token + ":" + apple + ":" + tokenId;
                break;
            default:
                key = token + ":" + other + ":" + tokenId;
                break;
        }
        return key;
    }
}

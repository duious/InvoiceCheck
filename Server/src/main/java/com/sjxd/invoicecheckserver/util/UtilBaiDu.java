package com.sjxd.invoicecheckserver.util;

import com.baidu.aip.ocr.AipOcr;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 百度SDK工具类
 *
 * @author zyl
 * @date 2019/8/1 11:35
 */
public class UtilBaiDu {
    private static final String filePath = "F:/IdeaProjects/invoicecheckserver/target/";
    //设置APPID/AK/SK
    private static final String APP_ID = "16931201";
    private static final String API_KEY = "ORfGuD3jM2UTyaw9QuQaLgkE";
    private static final String SECRET_KEY = "o9EUr1MbV7Y4ELgwrbfylWzs0H7b2I8d";
    private static AipOcr client;

    private UtilBaiDu() {
    }

    /**
     * 通用文字识别
     *
     * @param imgFile 图片
     */
    public static Result simpleRecognition(String imgFile) {
        Result result = new Result();
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<>(8);
        options.put("language_type", "CHN_ENG");
        options.put("detect_direction", "true");
        options.put("detect_language", "true");
        options.put("probability", "false");

        JSONObject res = null;
        try {
            res = getInstance().basicGeneral(Base64.decodeBase64(imgFile.replaceFirst("data:(.+?);base64,", "")), options);
            // System.out.println(res.toString(2));
            result.setCode(Result.CODE_SUCCESS);
            result.setData(res.getJSONArray("words_result"));
        } catch (Exception e) {
            result.setCode(Result.CODE_ERROR);
        }
        return result;
    }

    private static AipOcr getInstance() {
        if (null == client) {
            client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        }
        return client;
    }


}

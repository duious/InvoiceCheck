package com.sjxd.invoicecheckserver.util;

/**
 * 统一资源标识 包括：<br>
 * 报名表<br>
 * 站内信表<br>
 * 短信表<br>
 * 邮件表<br>
 * 座次表<br>
 * 资源表<br>
 * 日程表<br>
 * 日程人员表<br>
 *
 * @author zhangyl
 * <p>
 * 2017年6月9日
 */
public class Res {
    // ----------------------------------------------------基本资源类型---------------------------------------------------
    /**
     * 系统用户
     */
    public static String TYPE_SYS = "999";
    /**
     * 一般用户
     */
    public static String TYPE_USER = "1";
    /**
     * 嘉宾
     */
    public static String TYPE_EXPERT = "2";
    /**
     * 论文评审专家
     */
    public static String TYPE_JUDGE = "3";
    /**
     * 分销渠道
     */
    public static String TYPE_CHANNEL = "4";
    /**
     * 超级管理员
     */
    public static String TYPE_SUPERADMIN = "6";
    /**
     * 管理员
     */
    public static String TYPE_ADMIN = "7";
    /**
     * 工作人员
     */
    public static String TYPE_OFFICEHOLDER = "8";
    /**
     * 单位
     */
    public static String TYPE_ORG = "9";
    /**
     * 活动
     */
    public static String TYPE_ACTIVITY = "10";
    /**
     * 活动主会场
     */
    public static String TYPE_VENUE = "11";
    /**
     * 活动分会场
     */
    public static String TYPE_DEPUTY_VENUE = "12";
    /**
     * 新闻
     */
    public static String TYPE_NEWS = "18";
    /**
     * 公告
     */
    public static String TYPE_NOTICES = "19";
    /**
     * 邮件
     */
    public static String TYPE_EMAIL = "20";
    /**
     * 议题
     */
    public static String TYPE_ISSUE = "21";
    /**
     * 旅游产品
     */
    public static String TYPE_TOURISM = "22";
    // -------------------------------------------------------文件资源类型------------------------------------------------
    /**
     * 图片
     */
    public static int TYPE_IMAGE = 1;
    /**
     * 文件
     */
    public static int TYPE_FILE = 2;
    /**
     * 文字
     */
    public static int TYPE_TEXT = 3;
    // ----------------------------------------------------报名来源------------------------------------------------------
    /**
     * 报名方式:pc
     */
    public static String ATTEND_WAY_PC = "1";
    /**
     * 报名方式:微信
     */
    public static String ATTEND_WAY_WX = "2";
    /**
     * 报名方式:安卓
     */
    public static String ATTEND_WAY_AZ = "3";
    /**
     * 报名方式:苹果
     */
    public static String ATTEND_WAY_PG = "4";
    /**
     * 报名方式:导入
     */
    public static String ATTEND_WAY_IMPORT = "5";
    // -------------------------------------------------------访问限制---------------------------------------------------
    /**
     * 访问限制：公开
     */
    public static String LIMIT_PUBLIC = "2";
    /**
     * 访问限制：私有
     */
    public static String LIMIT_PRIVATE = "1";
    /**
     * 访问限制：活动内
     */
    public static String LIMIT_ACTIVITY = "3";
    // --------------------------------------------------------新闻公告--------------------------------------------------
    /**
     * 新闻
     */
    public static String MSG_NEWS = "1";
    /**
     * 公告
     */
    public static String MSG_NOTICE = "2";
    // --------------------------------------------------------酒店-----------------------------------------------------
    /**
     * 同程
     */
    public static String HOTEL_LY = "1";
    /**
     * 自定义
     */
    public static String HOTEL_DIY = "2";
    // --------------------------------------------------------活动规模--------------------------------------------------
    /**
     * 活动规模：小型
     */
    public static String ACTIVITY_SMALL = "1";
    /**
     * 活动规模：中型
     */
    public static String ACTIVITY_MEDIUM = "2";
    /**
     * 活动规模：大型
     */
    public static String ACTIVITY_BIG = "3";
    /**
     * 活动种类：普通
     */
    public static String ACTIVITY_NORMAL = "0";
    /**
     * 活动种类：学术
     */
    public static String ACTIVITY_ACADEMIC = "1";
    // ----------------------------------------------------签到方式------------------------------------------------------
    /**
     * 签到方式:微信
     */
    public static String SIGN_WAY_WX = "1";
    /**
     * 签到方式:app
     */
    public static String SIGN_WAY_APP = "2";
    /**
     * 签到方式:智能卡
     */
    public static String SIGN_WAY_SMARTCARD = "3";
    /**
     * 签到方式:地毯
     */
    public static String SIGN_WAY_CARPET = "4";
    /**
     * 签到方式:其它
     */
    public static String SIGN_WAY_OTHER = "5";
    /**
     * 签到方式:扫码枪
     */
    public static String SIGN_WAY_SCAN_GUN = "6";
    // --------------------------------------------------用户消息类型----------------------------------------------------
    /**
     * 用户消息类型：短信
     */
    public static String MSG_TYPE_MSG = "1";
    /**
     * 用户消息类型：邮件
     */
    public static String MSG_TYPE_EMAIL = "2";
    /**
     * 用户消息类型：站内信
     */
    public static String MSG_TYPE_UMSG = "3";
    //---------------------------------------------------支付方式-------------------------------------------------------
    /**
     * 用户支付类型：银行转账
     */
    public static String PAY_TYPE_BANK_TRANSFER = "1";
    /**
     * 用户支付类型：微信
     */
    public static String PAY_TYPE_WEIXIN = "2";
    //----------------------------------------------------商品类型------------------------------------------------------
    /**
     * 商品类型：活动相关费用
     */
    public static String GOOD_TYPE_ACTIVITY_CONFIG_FEE = "1";
}
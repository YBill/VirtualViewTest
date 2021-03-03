package com.bill.virtualviewtest.util;

/**
 * author : Bill
 * date : 2021/3/3
 * description :
 */
public class HttpUtil {

    public static String getHostIp() {
        return "10.0.2.83";
    }

    public static String getHostUrl() {
        return "http://" + HttpUtil.getHostIp() + ":7788/";
    }

}

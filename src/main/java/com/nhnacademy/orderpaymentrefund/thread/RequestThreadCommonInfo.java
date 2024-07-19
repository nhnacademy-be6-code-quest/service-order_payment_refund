package com.nhnacademy.orderpaymentrefund.thread;

public class RequestThreadCommonInfo {
    public static ThreadLocal<Long> clientId = new ThreadLocal<>();
    public static ThreadLocal<String> role = new ThreadLocal<>();
}

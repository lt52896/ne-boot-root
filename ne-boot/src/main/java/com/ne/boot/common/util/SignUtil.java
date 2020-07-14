package com.ne.boot.common.util;

import com.ne.boot.common.ThreadContext;

/**
 * Created by dogchen on 2017/6/16.
 */
public class SignUtil {

    public static String getAppId() {
        return ThreadContext.get("appId");
    }
}

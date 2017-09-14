package com.jusenr.opensource;

import android.content.Context;
import android.widget.Toast;

/**
 * Description:
 * Copyright  : Copyright (c) 2017
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2017/09/14
 * Time       : 19:44
 * Project    ：OpenSource.
 */
public class TipsUtils {

    public static void apply(Context context, int code) {
        apply(context, code, "Unknow");
    }

    public static void apply(Context context, int code, String msg) {
        switch (code) {
            case 400:
                msg = "（错误请求）服务器不理解请求的语法。";
                break;
            case 401:
                msg = "（未授权）请求要求身份验证。对于需要token的接口，服务器可能返回此响应。";
                break;
            case 403:
                msg = "（禁止）服务器拒绝请求。对于群组 / 聊天室服务，表示本次调用不符合群组 / 聊天室操作的正确逻辑，例如调用添加成员接口，添加已经在群组里的用户， 或者移除聊天室中不存在的成员等操作。";
                break;
            case 404:
                msg = "（未找到）服务器找不到请求的接口。";
                break;
            case 408:
                msg = "（请求超时）服务器等候请求时发生超时。";
                break;
            case 413:
                msg = "（请求体过大）请求体超过了5kb，拆成更小的请求体重试即可。";
                break;
            case 429:
                msg = "（服务不可用）请求接口超过调用频率限制，即接口被限流。";
                break;
            case 500:
                msg = "（服务器内部错误）服务器遇到错误，无法完成请求。";
                break;
            case 501:
                msg = "（尚未实施）服务器不具备完成请求的功能。例如，服务器无法识别请求方法时可能会返回此代码。";
                break;
            case 502:
                msg = "（错误网关）服务器作为网关或代理，从上游服务器收到无效响应。";
                break;
            case 503:
                msg = "（服务不可用）请求接口超过调用频率限制，即接口被限流。";
                break;
            case 504:
                msg = "（网关超时）服务器作为网关或代理，但是没有及时从上游服务器收到请求。";
                break;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}

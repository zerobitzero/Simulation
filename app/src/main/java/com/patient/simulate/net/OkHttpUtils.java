package com.patient.simulate.net;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author zs
 */
public final class OkHttpUtils {

    private static OkHttpClient sClient;

    private String url;

    static {
        sClient = new OkHttpClient();
        sClient.setConnectTimeout(30, TimeUnit.SECONDS);
        sClient.setWriteTimeout(30, TimeUnit.SECONDS);
    }

    private OkHttpUtils() {
    }

    public static void get(final String url, final ICallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            final Response response = sClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                if (null != callback) {
                    callback.onSuccess(result);
                }
            } else {
                if (null != callback) {
                    callback.onFailure(response.code(), response.message());
                }
            }

            response.body().close();
        } catch (IOException e1) {
            e1.printStackTrace();
            if (null != callback) {
                callback.onFailure(-1, "IO异常");
            }
        }
    }

    public static void getAsync(final String url, final ICallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();

        sClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (null != callback) {
                    callback.onFailure(-1, "IO异常");
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    if (null != callback) {
                        callback.onSuccess(result);
                    }
                } else {
                    if (null != callback) {
                        callback.onFailure(response.code(), response.message());
                    }
                }

                response.body().close();
            }
        });
    }

    public static interface ICallback {

        void onSuccess(final String result);

        void onFailure(final int errorCode, final String errorDetail);
    }
}

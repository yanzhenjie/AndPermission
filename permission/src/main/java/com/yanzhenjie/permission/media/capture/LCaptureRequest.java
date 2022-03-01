package com.yanzhenjie.permission.media.capture;

import android.content.Intent;

import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.bridge.BridgeRequest;
import com.yanzhenjie.permission.bridge.RequestManager;
import com.yanzhenjie.permission.source.Source;

/**
 * Created by Khanh Bui on 2022/2/22.
 */
public class LCaptureRequest extends BaseRequest implements RequestExecutor, BridgeRequest.Callback {

    public LCaptureRequest(Source source) {
        super(source);
    }

    @Override
    public void start() {
        execute();
    }

    @Override
    public void execute() {
        BridgeRequest request = new BridgeRequest(mSource);
        request.setType(BridgeRequest.TYPE_MEDIA_CAPTURE);
        request.setCallback(this);
        RequestManager.get().add(request);
    }

    @Override
    public void cancel() {
        callbackFailed();
    }

    @Override
    public void onCallback(Intent intent) {
        if (intent != null) {
            callbackSucceed(intent);
        } else {
            callbackFailed();
        }
    }
}

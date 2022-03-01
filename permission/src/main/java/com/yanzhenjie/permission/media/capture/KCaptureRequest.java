package com.yanzhenjie.permission.media.capture;

import com.yanzhenjie.permission.source.Source;

/**
 * Created by Khanh Bui on 2022/2/22.
 */
public class KCaptureRequest extends BaseRequest {

    public KCaptureRequest(Source source) {
        super(source);
    }

    @Override
    public void start() {
        callbackFailed();
    }
}

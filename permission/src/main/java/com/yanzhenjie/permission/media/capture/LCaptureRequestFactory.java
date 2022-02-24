package com.yanzhenjie.permission.media.capture;

import com.yanzhenjie.permission.media.Media;
import com.yanzhenjie.permission.source.Source;

/**
 * Created by Khanh Bui on 2022/2/22.
 */
public class LCaptureRequestFactory implements Media.MediaRequestFactory {

    @Override
    public CaptureRequest create(Source source) {
        return new LCaptureRequest(source);
    }
}

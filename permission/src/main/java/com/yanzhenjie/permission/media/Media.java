package com.yanzhenjie.permission.media;

import android.os.Build;

import com.yanzhenjie.permission.media.capture.LCaptureRequestFactory;
import com.yanzhenjie.permission.media.capture.KCaptureRequestFactory;
import com.yanzhenjie.permission.media.capture.CaptureRequest;
import com.yanzhenjie.permission.source.Source;

/**
 * Created by Khanh Bui on 2022/2/22.
 */
public class Media {

    private static final MediaRequestFactory MEDIA_CAPTURE_REQUEST_FACTORY;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MEDIA_CAPTURE_REQUEST_FACTORY = new LCaptureRequestFactory();
        } else {
            MEDIA_CAPTURE_REQUEST_FACTORY = new KCaptureRequestFactory();
        }
    }

    public interface MediaRequestFactory {

        CaptureRequest create(Source source);
    }

    private Source mSource;

    public Media(Source source) {
        this.mSource = source;
    }

    /**
     * Permission media capture.
     */
    public CaptureRequest capture() {
        return MEDIA_CAPTURE_REQUEST_FACTORY.create(mSource);
    }
}

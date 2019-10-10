/*
 * Copyright © Zhenjie Yan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.permission.checker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Zhenjie Yan on 2018/1/14.
 */
class RecordAudioTest implements PermissionTest {

    private static final int[] RATES = new int[]{8000, 11025, 22050, 44100};

    private Context mContext;

    RecordAudioTest(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean test() throws Throwable {
        AudioRecord audioRecord = null;
        File file = null;
        FileOutputStream fos = null;
        try {
            int[] params = findAudioParameters();
            if (params == null) return !existMicrophone(mContext);

            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, params[0], params[1], params[2], params[3]);
            int state = audioRecord.getState();
            if (state != AudioRecord.STATE_INITIALIZED) return !existMicrophone(mContext);

            int recordState = audioRecord.getRecordingState();
            if (recordState != AudioRecord.RECORDSTATE_STOPPED) return true;

            audioRecord.startRecording();
            if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) return true;

            File cacheDir = new File(mContext.getCacheDir(), "_andpermission_audio_record_test_");
            cacheDir.mkdirs();

            file = new File(cacheDir, Long.toString(System.currentTimeMillis()));
            if (file.exists()) file.createNewFile();

            fos = new FileOutputStream(file);
            byte[] buffer = new byte[params[3]];

            int len = audioRecord.read(buffer, 0, params[3]);
            fos.write(buffer, 0, len);
            fos.flush();
        } catch (Throwable e) {
            return !existMicrophone(mContext);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ignored) {
                }
            }
            if (file != null && file.exists()) {
                file.delete();
            }
            if (audioRecord != null) {
                audioRecord.release();
            }
        }
        return true;
    }

    public static boolean existMicrophone(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    public static int[] findAudioParameters() {
        for (int rate : RATES) {
            for (int channel : new int[]{AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO}) {
                for (int format : new int[]{AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT}) {
                    int buffer = AudioRecord.getMinBufferSize(rate, channel, format);
                    if (buffer != AudioRecord.ERROR_BAD_VALUE) {
                        return new int[]{rate, channel, format, buffer};
                    }
                }
            }
        }
        return null;
    }

}
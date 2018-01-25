/*
 * Copyright © Yan Zhenjie
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

import android.media.MediaRecorder;

import java.io.File;

/**
 * Created by YanZhenjie on 2018/1/14.
 */
class RecordAudioTest implements PermissionTest {

    private File mTempFile = null;
    private MediaRecorder mRecorder = null;

    RecordAudioTest() {
        mRecorder = new MediaRecorder();
    }

    @Override
    public boolean test() throws Throwable {
        try {
            mTempFile = File.createTempFile("permission", "test");

            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(mTempFile.getAbsolutePath());
            mRecorder.prepare();
            mRecorder.start();
            return true;
        } finally {
            stop();
        }
    }

    private void stop() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
            } catch (Exception ignored) {
            }
            try {
                mRecorder.release();
            } catch (Exception ignored) {
            }
        }

        if (mTempFile != null && mTempFile.exists()) {
            // noinspection ResultOfMethodCallIgnored
            mTempFile.delete();
        }
    }
}
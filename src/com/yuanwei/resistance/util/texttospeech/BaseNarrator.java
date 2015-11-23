package com.yuanwei.resistance.util.texttospeech;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;

/**
 * Created by chenyuanwei on 15/10/23.
 */
public abstract class BaseNarrator {

    public static final String UTTERANCE_SIGNAL = "signal";

    // Switch this on to speed up phrases and skip the pauses
    protected static final boolean TEST_MODE = false;

    private static final int SHORT_PAUSE = 1500;

    private static final int LONG_PAUSE = 5000;

    protected TextToSpeech mTTS;

    protected Context mContext;

    protected HashMap<String, String> params;

    public BaseNarrator(Context context, TextToSpeech tts) {
        mContext = context;
        mTTS = tts;
        params = new HashMap<>(1);
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UTTERANCE_SIGNAL);
    }

    public abstract void saySpeech();

    protected void speak(int resId) {
        mTTS.speak(mContext.getString(resId), TextToSpeech.QUEUE_ADD, null);
    }

    protected void shortPause() {
        if (!TEST_MODE) {
            mTTS.playSilence(SHORT_PAUSE, TextToSpeech.QUEUE_ADD, null);
        }
    }

    protected void longPause() {
        if (!TEST_MODE) {
            mTTS.playSilence(LONG_PAUSE, TextToSpeech.QUEUE_ADD, null);
        }
    }

}

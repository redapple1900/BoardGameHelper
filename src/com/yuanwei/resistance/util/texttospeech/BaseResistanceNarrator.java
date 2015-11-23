package com.yuanwei.resistance.util.texttospeech;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.model.protocol.BaseConfig;

/**
 * Created by chenyuanwei on 15/10/20.
 */
public abstract class BaseResistanceNarrator extends BaseNarrator{


    public BaseResistanceNarrator(Context context, TextToSpeech tts) {
        super(context, tts);
    }

    public void saySpeech() {
        mTTS.speak(mContext.getString(R.string.script_close_eyes), TextToSpeech.QUEUE_ADD, null);
        saySpeechWithConfig();
        mTTS.speak(mContext.getString(R.string.script_open_eyes), TextToSpeech.QUEUE_ADD, params);
    }

    protected abstract void saySpeechWithConfig();

    public abstract <T extends BaseConfig>  void setConfig(T config);
}

package com.yuanwei.resistance.partygame.avalon.texttospeech;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.util.texttospeech.BaseNarrator;

/**
 * Created by chenyuanwei on 15/11/29.
 */
public class LancelotNarrator extends BaseNarrator {

    public LancelotNarrator(Context context, TextToSpeech tts) {
        super(context, tts);
    }

    @Override
    public void saySpeech() {
        speak(R.string.lancelot_swap);
    }
}

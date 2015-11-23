package com.yuanwei.resistance.partygame.origin.texttospeech;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.model.protocol.BaseConfig;
import com.yuanwei.resistance.util.texttospeech.BaseResistanceNarrator;

/**
 * Created by chenyuanwei on 15/10/20.
 */
public class Narrator extends BaseResistanceNarrator {

    public Narrator(Context context, TextToSpeech tts) {
        super(context, tts);
    }

    @Override
    protected void saySpeechWithConfig() {
        say();
    }

    @Override
    public <T extends BaseConfig> void setConfig(T config) {

    }

    private void say() {
        shortPause();
        speak(R.string.script_spies_find_each_other);
        longPause();
        speak(R.string.script_spies_close_eyes);
        shortPause();
    }
}

package com.yuanwei.resistance.partygame.avalon.texttospeech;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.model.protocol.BaseConfig;
import com.yuanwei.resistance.partygame.avalon.Config;
import com.yuanwei.resistance.partygame.avalon.model.Avalon;
import com.yuanwei.resistance.util.texttospeech.BaseResistanceNarrator;

/**
 * Created by chenyuanwei on 15/10/21.
 */
public class Narrator extends BaseResistanceNarrator {

    private Config mConfig;

    public Narrator(Context context, TextToSpeech tts) {
        super(context, tts);
    }

    @Override
    protected void saySpeechWithConfig() {
        say(mConfig);
    }

    @Override
    public <T extends BaseConfig> void setConfig(T config) {
        mConfig = (Config) config;
    }


    private void say(Config config) {

        if (TEST_MODE) {
            mTTS.setSpeechRate(2.5f);
        }

        boolean allDisabled = true;

        for (Avalon.Role role : Avalon.getInstance().getSpecialRoles()) {
            if (config.isRoleEnabled(role)) {
                allDisabled = false;
            }
        }

        // If all are disabled, use the basic Resistance terminology
        if (allDisabled) {
            speak(R.string.script_close_eyes);
            shortPause();
            speak(R.string.script_spies_find_each_other);
            longPause();
            speak(R.string.script_spies_close_eyes);
            shortPause();
        }

        // If any are enabled, use Avalon terminology
        else {
            boolean merlinEnabled = config.isRoleEnabled(Avalon.Role.MERLIN);

            if (merlinEnabled) speak(R.string.script_extend_fists);
            shortPause();

            speak(config.isRoleEnabled(Avalon.Role.OBERON) ? R.string.script_evil_find_each_other_except_oberon
                    : R.string.script_evil_find_each_other);
            longPause();
            speak(R.string.script_evil_close_eyes);
            shortPause();

            if (merlinEnabled) {
                speak(config.isRoleEnabled(Avalon.Role.MORDRED) ? R.string.script_evil_be_known_except_mordred
                        : R.string.script_evil_be_known);
                shortPause();
                speak(R.string.script_merlin_know_evil);
                longPause();
                speak(R.string.script_evil_hide);
                speak(R.string.script_merlin_close_eyes);
                shortPause();

                if (config.isRoleEnabled(Avalon.Role.PERCIVAL)) {
                    boolean morganaEnabled = config.isRoleEnabled(Avalon.Role.MORGANA);
                    speak(morganaEnabled ? R.string.script_merlin_morgana_be_known : R.string.script_merlin_be_known);
                    shortPause();
                    speak(morganaEnabled ? R.string.script_percival_know_merlin_morgana
                            : R.string.script_percival_know_merlin);
                    longPause();
                    speak(morganaEnabled ? R.string.script_merlin_morgana_hide : R.string.script_merlin_hide);
                    speak(R.string.script_percival_close_eyes);
                    shortPause();
                }
            }

            if (config.isRoleEnabled(Avalon.Role.LANCELOT_ARTHUR)
                    && config.isRoleEnabled(Avalon.Role.LANCELOT_MORDRED)) {
                speak(R.string.script_lancelot_know_each_other);
                longPause();
                speak(R.string.script_lancelot_close_eyes);
                shortPause();
            }
        }
    }
}

package com.yuanwei.resistance.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.util.texttospeech.BaseNarrator;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseTextToSpeechFragment extends Fragment
        implements TextToSpeech.OnInitListener{

    protected TextToSpeech mTTS;

    private BaseNarrator mNarrator;

    private boolean mSpeakable;

    public BaseTextToSpeechFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mTTS = new TextToSpeech(activity, this);
        String language = Locale.getDefault().getLanguage();
        mSpeakable = language.equals("zh") || language.equals("en");
    }

    @Override
    public void onDestroyView() {
        if(mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroyView();
    }

    // Begin implementing TextToSpeech.onInitListener
    @Override
    public void onInit(int status) {
        switch (status) {
            case TextToSpeech.SUCCESS:
                if (Build.VERSION.SDK_INT >= 15) {
                    mTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String s) {
                        }

                        @Override
                        public void onDone(String s) {
                            onSpeakDone();
                        }

                        @Override
                        public void onError(String s) {

                        }
                    });
                } else {
                    mTTS.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                        @Override
                        public void onUtteranceCompleted(String s) {
                            onSpeakDone();
                        }
                    });
                }
                break;
            case TextToSpeech.ERROR:
                break;
        }
    }
    // END implementing TextToSpeech.onInitListener

    protected  void setNarrator(BaseNarrator narrator) {
        this.mNarrator = narrator;
    }

    protected abstract void onSpeakDone();

    protected abstract void onSpeakStart();

    protected abstract void onSpeakError();
    // More methods may be added here

    protected void playSound() {
        if (!mSpeakable) {
            Toast.makeText(getActivity(), getString(R.string.language_not_available), Toast.LENGTH_LONG).show();
        }
        if (getVolume() < 6) {
            onSpeakError();
            return;
        }
        onSpeakStart();
        mNarrator.saySpeech();
    }

    private int getVolume() {
        return ((AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE))
                .getStreamVolume(AudioManager.STREAM_MUSIC);
    }
}

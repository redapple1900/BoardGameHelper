package com.yuanwei.resistance.moderator.protocol;

/**
 * Created by chenyuanwei on 15/10/28.
 */
public interface OnAnnounceListener extends OnActListener {
        /**
         * Do some setting before one round of introduction starts
         */
        void onIntroduceStart();

        /**
         * What to introduce?
         */
        void onIntroduce();

        /**
         * Do some thing after one round of introduction is done
         */
        void onIntroduceDone();
}

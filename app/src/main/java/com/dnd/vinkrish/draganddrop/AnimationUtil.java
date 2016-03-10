package com.dnd.vinkrish.draganddrop;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by vinkrish on 10/03/16.
 */
public class AnimationUtil {

    public static void alphaTranslate(View view, Context context) {
        view.setVisibility(View.VISIBLE);
        AnimationSet animationSet = new AnimationSet(context , null);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, view.getHeight(), 0);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(700);
        view.startAnimation(animationSet);
    }

    public static void alphaScaleInOut (final View view, Context context) {
        AnimationSet animationSet = new AnimationSet(context , null);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f,1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(500);
        view.startAnimation(animationSet);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AlphaAnimation fade = new AlphaAnimation(1f, 0f);
                fade.setDuration(500);
                view.startAnimation(fade);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }



}

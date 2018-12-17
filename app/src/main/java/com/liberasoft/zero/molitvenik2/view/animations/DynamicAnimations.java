package com.liberasoft.zero.molitvenik2.view.animations;

import android.app.Activity;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageButton;

public class DynamicAnimations {

  private static SpringAnimation springAnimation;

  public static void playAnimation(ImageButton view) {
    springAnimation = new SpringAnimation(view, SpringAnimation.SCALE_X);
    SpringForce spring = new SpringForce();
    spring.setFinalPosition(0.7f);
    spring.setStiffness(SpringForce.STIFFNESS_LOW);
    spring.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
    springAnimation.setSpring(spring);
    springAnimation.start();

    springAnimation = new SpringAnimation(view, SpringAnimation.SCALE_Y);
    spring.setFinalPosition(0.7f);
    spring.setStiffness(SpringForce.STIFFNESS_LOW);
    spring.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
    springAnimation.setSpring(spring);
    springAnimation.start();
  }

  public static void reverseAnim(View view) {
    springAnimation = new SpringAnimation(view, SpringAnimation.SCALE_X);
    SpringForce spring = new SpringForce();
    spring.setFinalPosition(1f);
    spring.setStiffness(SpringForce.STIFFNESS_LOW);
    spring.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
    springAnimation.setSpring(spring);
    springAnimation.start();

    springAnimation = new SpringAnimation(view, SpringAnimation.SCALE_Y);
    spring.setFinalPosition(1f);
    spring.setStiffness(SpringForce.STIFFNESS_LOW);
    spring.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
    springAnimation.setSpring(spring);
    springAnimation.start();
  }

  public static <T> void changeColor(Activity activity, T t, int color) {
    ImageButton view = (ImageButton) t;
    DrawableCompat.setTint(view.getBackground(), ContextCompat.getColor(activity, color));
  }
}

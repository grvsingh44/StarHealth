package com.bimahelpline.starhealth.custom;

import android.view.animation.Interpolator;

/**
 * Created by gauravsingh on 17/04/17.
 */

public class SpringInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        /*
                sin(x * pi) e⁻ˣ
         */
        return  (float)(-Math.sin(Math.PI * (8*input))*Math.pow(Math.PI, -(2*input))*1.2);

    }
}

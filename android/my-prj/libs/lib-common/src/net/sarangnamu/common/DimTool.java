/*
 * DimTool.java
 * Copyright 2013 Burke Choi All rights reserved.
 *             http://www.sarangnamu.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sarangnamu.common;

import android.content.Context;

/**
 * <pre>
 * {@code
    DimTool.dpTpPixel(context, 100);
 * }
 * </pre>
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class DimTool {
    public static float pixelToDp(Context context, float pixel) {
        return pixel / context.getResources().getDisplayMetrics().density;
    }

    public static float dpToPixel(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPixelInt(Context context, float dp) {
        return (int)(dp * context.getResources().getDisplayMetrics().density);
    }

    public static float[] dpToPixel(Context context, float dp1, float dp2, float dp3, float dp4) {
        float[] pixels = new float[4];
        pixels[0] = dpToPixel(context, dp1);
        pixels[1] = dpToPixel(context, dp2);
        pixels[2] = dpToPixel(context, dp3);
        pixels[3] = dpToPixel(context, dp4);

        return pixels;
    }

    public static int[] dpToPixelInt(Context context, int dp1, int dp2, int dp3, int dp4) {
        int[] pixels = new int[4];
        pixels[0] = dpToPixelInt(context, dp1);
        pixels[1] = dpToPixelInt(context, dp2);
        pixels[2] = dpToPixelInt(context, dp3);
        pixels[3] = dpToPixelInt(context, dp4);

        return pixels;
    }

    public static float[] dpToPixel(Context context, float[] dp) {
        float[] pixels = new float[4];
        pixels[0] = dpToPixel(context, dp[0]);
        pixels[1] = dpToPixel(context, dp[1]);
        pixels[2] = dpToPixel(context, dp[2]);
        pixels[3] = dpToPixel(context, dp[3]);

        return pixels;
    }

    public static int[] dpToPixelInt(Context context, int[] dp) {
        int[] pixels = new int[4];
        pixels[0] = dpToPixelInt(context, dp[0]);
        pixels[1] = dpToPixelInt(context, dp[1]);
        pixels[2] = dpToPixelInt(context, dp[2]);
        pixels[3] = dpToPixelInt(context, dp[3]);

        return pixels;
    }
}

package net.sarangnamu.utils;

import android.content.Context;

/**
 * @see http://theeye.pe.kr/entry/display-utility-for-convert-dip-to-pixel-on-android
 *
 */
public class DisplayUnit {
	private static final float DEFAULT_HDIP_DENSITY_SCALE = 1.5f;

	/**
	 * 픽셀단위를 현재 디스플레이 화면에 비례한 크기로 반환합니다.
	 * 
	 * @param pixel 픽셀
	 * @return 변환된 값 (DP)
	 */
	public static int DPFromPixel(Context context, float pixel)
	{
	    float scale = context.getResources().getDisplayMetrics().density;
	    
	    return (int)(pixel / DEFAULT_HDIP_DENSITY_SCALE * scale);
	}
	  
	/**
	 * 현재 디스플레이 화면에 비례한 DP단위를 픽셀 크기로 반환합니다.
	 * 
	 * @param  DP 픽셀
	 * @return 변환된 값 (pixel)
	 */
	public static int PixelFromDP(Context context, float DP)
	{
	    float scale = context.getResources().getDisplayMetrics().density;
	    
	    return (int)(DP / scale * DEFAULT_HDIP_DENSITY_SCALE);
	}
}

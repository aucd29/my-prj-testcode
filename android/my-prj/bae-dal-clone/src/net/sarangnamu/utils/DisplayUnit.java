package net.sarangnamu.utils;

import android.content.Context;

/**
 * @see http://theeye.pe.kr/entry/display-utility-for-convert-dip-to-pixel-on-android
 *
 */
public class DisplayUnit {
	private static final float DEFAULT_HDIP_DENSITY_SCALE = 1.5f;

	/**
	 * �ȼ������� ���� ���÷��� ȭ�鿡 ����� ũ��� ��ȯ�մϴ�.
	 * 
	 * @param pixel �ȼ�
	 * @return ��ȯ�� �� (DP)
	 */
	public static int DPFromPixel(Context context, float pixel)
	{
	    float scale = context.getResources().getDisplayMetrics().density;
	    
	    return (int)(pixel / DEFAULT_HDIP_DENSITY_SCALE * scale);
	}
	  
	/**
	 * ���� ���÷��� ȭ�鿡 ����� DP������ �ȼ� ũ��� ��ȯ�մϴ�.
	 * 
	 * @param  DP �ȼ�
	 * @return ��ȯ�� �� (pixel)
	 */
	public static int PixelFromDP(Context context, float DP)
	{
	    float scale = context.getResources().getDisplayMetrics().density;
	    
	    return (int)(DP / scale * DEFAULT_HDIP_DENSITY_SCALE);
	}
}

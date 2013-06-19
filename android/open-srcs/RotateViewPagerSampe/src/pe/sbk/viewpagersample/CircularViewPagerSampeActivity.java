package pe.sbk.viewpagersample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 10���� �������� �ִµ� 1������ 10������ 10������ �ٽ� 1������ ��ȭ�ϴ� �� ������
 * @author SeolBK
 */
public class CircularViewPagerSampeActivity extends Activity{
	private final int COUNT=10;					//������ ����
	private int mPrevPosition;						//������ ���õǾ��� ������ ��

	private ViewPager mPager;					//�� ������
	private LinearLayout mPageMark;			//���� �� ������ ���� ��Ÿ���� ��

	//�������� ���ȭ�� ����. �������� �����ϱ� ����.
	private int[] mColorArray = {Color.YELLOW, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY,
										Color.GREEN, Color.LTGRAY, Color.MAGENTA, Color.RED, Color.WHITE}; 

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other);
		((View)findViewById(R.id.prev).getParent()).setVisibility(View.GONE);

		mPageMark = (LinearLayout)findViewById(R.id.page_mark);			//����� ���� ������ ��Ÿ���� ��

		mPager = (ViewPager)findViewById(R.id.pager);						//�� ������
		mPager.setAdapter(new BkPagerAdapter(getApplicationContext()));//PagerAdapter�� ����
		mPager.setCurrentItem(COUNT);			//���� ��ũ�� �ϱ� ���ؼ��� �������� 3��� ������ �ְ� �� �� ��� ������ �����۸� ���̰� �Ѵ�
		mPager.setOnPageChangeListener(new OnPageChangeListener() {	//�������� ����Ǹ�, gallery�� listview�� onItemSelectedListener�� ���
			@Override public void onPageSelected(int position) {
				if(position < COUNT)												//3���� �������� �����̸� ���������� �������� ��� ������ �̵���Ų��
					mPager.setCurrentItem(position+COUNT, false);
				else if(position >= COUNT*2)										//3���� ������ �� �����̸� ���������� �������� ��� ������ �̵���Ų�� 
					mPager.setCurrentItem(position - COUNT, false);
				else {																	//��� �����̸� ����� ������ ǥ�ø� �����Ѵ�
					position -= COUNT;
					//�������� ������ �Ǿ�����
					mPageMark.getChildAt(mPrevPosition).setBackgroundResource(R.drawable.page_not);	//���� �������� �ش��ϴ� ������ ǥ�� �̹��� ����
					mPageMark.getChildAt(position).setBackgroundResource(R.drawable.page_select);		//���� �������� �ش��ϴ� ������ ǥ�� �̹��� ����
					mPrevPosition = position;				//���� ������ ���� ����� ����
				}
			}
			@Override public void onPageScrolled(int position, float positionOffest, int positionOffsetPixels) {}
			@Override public void onPageScrollStateChanged(int state) {}
		});

		initPageMark();	//���� ������ ǥ���ϴ� �� �ʱ�ȭ
	}

	//����� ���� ������ ǥ���ϴ� �� �ʱ�ȭ
	private void initPageMark(){
		for(int i=0; i<COUNT; i++)
		{
			ImageView iv = new ImageView(getApplicationContext());	//������ ǥ�� �̹��� �� ����
			iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			//ù ������ ǥ�� �̹��� �̸� ���õ� �̹�����
			if(i==0)
				iv.setBackgroundResource(R.drawable.page_select);
			else	//�������� ���þȵ� �̹�����
				iv.setBackgroundResource(R.drawable.page_not);

			//LinearLayout�� �߰�
			mPageMark.addView(iv);
		}
		mPrevPosition = 0;	//���� ������ �� �ʱ�ȭ
	}

	//Pager �ƴ��� ����
	private class BkPagerAdapter extends PagerAdapter{
		private Context mContext;
		public BkPagerAdapter( Context con) { super(); mContext = con; }

		@Override public int getCount() { return COUNT * 3; }

		//������������ ����� �䰴ü ����/���
		@Override public Object instantiateItem(View pager, int position) 
		{
			position %= COUNT;
			TextView tv = new TextView(mContext);					//�ؽ�Ʈ��
			tv.setBackgroundColor(mColorArray[position]);			//���� ����
			tv.setText("ViewPager Item"+(position+1));				//��������
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);		//���� ũ�� 24sp
			tv.setTextColor(mColorArray[mColorArray.length - (position+1)]);	//���� ������ ���� �ٸ� ������
			((ViewPager)pager).addView(tv, 0);		//�� �������� �߰�

			return tv;
		}

		//�� ��ü ����.
		@Override public void destroyItem(View pager, int position, Object view) {
			((ViewPager)pager).removeView((View)view);
		}

		// instantiateItem�޼ҵ忡�� ������ ��ü�� �̿��� ������
		@Override public boolean isViewFromObject(View view, Object obj) { return view == obj; }

		@Override public void finishUpdate(View arg0) {}
		@Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
		@Override public Parcelable saveState() { return null; }
		@Override public void startUpdate(View arg0) {}
	}
}
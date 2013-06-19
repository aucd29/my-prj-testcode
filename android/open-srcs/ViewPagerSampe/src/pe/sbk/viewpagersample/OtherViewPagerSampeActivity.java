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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class OtherViewPagerSampeActivity extends Activity implements OnClickListener {
	private final int COUNT=10;					//������ ����
	private int mPrevPosition;						//������ ���õǾ��� ������ ��
	
	private ViewPager mPager;					//�� ������
	private LinearLayout mPageMark;			//���� �� ������ ���� ��Ÿ���� ��
	private Button mPrev, mNext;					//������ư, ������ư
	private RadioGroup mOption;				//������ �̵��� �ִϸ��̼� ȿ�� ��뿩�� ���� �ɼ�
	private boolean isAnimated;					//�ִϸ��̼� ȿ�� ��� ����
	
	//�������� ���ȭ�� ����. �������� �����ϱ� ����.
	private int[] mColorArray = {Color.YELLOW, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY,
										Color.GREEN, Color.LTGRAY, Color.MAGENTA, Color.RED, Color.WHITE}; 
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other);

		mPageMark = (LinearLayout)findViewById(R.id.page_mark);			//����� ���� ������ ��Ÿ���� ��

		mPager = (ViewPager)findViewById(R.id.pager);						//�� ������
		mPager.setAdapter(new BkPagerAdapter(getApplicationContext()));//PagerAdapter�� ����
		mPager.setOnPageChangeListener(new OnPageChangeListener() {	//�������� ����Ǹ�, gallery�� listview�� onItemSelectedListener�� ���
			//�������� ������ �Ǿ�����
			@Override public void onPageSelected(int position) {
				mPageMark.getChildAt(mPrevPosition).setBackgroundResource(R.drawable.page_not);	//���� �������� �ش��ϴ� ������ ǥ�� �̹��� ����
				mPageMark.getChildAt(position).setBackgroundResource(R.drawable.page_select);		//���� �������� �ش��ϴ� ������ ǥ�� �̹��� ����
				mPrevPosition = position;				//���� ������ ���� ����� ����
			}
			@Override public void onPageScrolled(int position, float positionOffest, int positionOffsetPixels) {}
			@Override public void onPageScrollStateChanged(int state) {}
		});

		initPageMark();	//���� ������ ǥ���ϴ� �� �ʱ�ȭ
		
		mPrev = (Button)findViewById(R.id.prev);		//���� ���������� �̵� ��ư
		mPrev.setOnClickListener(this);
		
		mNext = (Button)findViewById(R.id.next);		//���� ���������� �̵� ��ư
		mNext.setOnClickListener(this);
		
		mOption = (RadioGroup)findViewById(R.id.option);	//�ɼ� ���� �׷�
		mOption.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				isAnimated = checkedId == R.id.enable;		//�ɼ��� ����Ǹ� ���� ���� �����Ѵ�.
			}
		});
		isAnimated = true;		//�⺻������ �ִϸ��̼� ���.
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

		@Override public int getCount() { return COUNT; }	//���⼭�� 2���� �� ���̴�.

		//������������ ����� �䰴ü ����/���
		@Override public Object instantiateItem(View pager, int position) {
			
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

	@Override
	public void onClick(View v) {
		int view = v.getId();
		if(view == R.id.prev){		//���� ��ư
			int cur = mPager.getCurrentItem();	//���� ������ ������
			if(cur > 0)				//ù �������� �ƴϸ�
				mPager.setCurrentItem(cur-1, isAnimated);	//���� �������� �̵�
			else						//ù ������ �̸�
				Toast.makeText(getApplicationContext(), "�� ó�� ������ �Դϴ�.", Toast.LENGTH_SHORT).show();	//�޽��� ���
		}
		else if(view == R.id.next){	//���� ��ư
			int cur = mPager.getCurrentItem();	//���� ������ ������
			if(cur < COUNT-1)		//������ �������� �ƴϸ�
				mPager.setCurrentItem(cur+1, isAnimated);	//���� �������� �̵�
			else						//������ ������ �̸�
				Toast.makeText(getApplicationContext(), "�� ������ ������ �Դϴ�.", Toast.LENGTH_SHORT).show();	//�޽��� ���
		}
	}
}
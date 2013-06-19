package net.sarangnamu.testpager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PagerAdapterSampeActivity extends Activity {
	private final int COUNT=2;
	private ViewPager mPager;	//�� ������

	private OnClickListener mButtonClick = new OnClickListener() {		//Ŭ�� �̺�Ʈ ��ü
		public void onClick(View v) {
			String text = ((Button)v).getText().toString();		//��ư���� �̺�Ʈ�� �����. ��ư ���� ������.
			Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();	//�佺Ʈ�� ���
		}
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(new BkPagerAdapter(getApplicationContext()));
	}

	//Pager �ƴ��� ����
	private class BkPagerAdapter extends PagerAdapter{
		private LayoutInflater mInflater;

		public BkPagerAdapter( Context con) {
			super();
			mInflater = LayoutInflater.from(con);
		}

		@Override public int getCount() { return COUNT; }	//���⼭�� 2���� �� ���̴�.

		//�������� ����� �䰴ü ��/���
		@Override public Object instantiateItem(View pager, int position) {
			View v = null;
			if(position==0){
				v = mInflater.inflate(R.layout.layout1, null);
				v.findViewById(R.id.btn1).setOnClickListener(mButtonClick);
				v.findViewById(R.id.btn2).setOnClickListener(mButtonClick);
			}
			else{
				v = mInflater.inflate(R.layout.layout2, null);
				v.findViewById(R.id.btn3).setOnClickListener(mButtonClick);
				v.findViewById(R.id.btn4).setOnClickListener(mButtonClick);
			}

			((ViewPager)pager).addView(v, 0);
			return v; 
		}

		//�� ��ü ����.
		@Override public void destroyItem(View pager, int position, Object view) {
			((ViewPager)pager).removeView((View)view);
		}

		// instantiateItem�޼ҵ忡�� ���� ��ü�� �̿��� ������
		@Override public boolean isViewFromObject(View view, Object obj) { return view == obj; }

		@Override public void finishUpdate(View arg0) {}
		@Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
		@Override public Parcelable saveState() { return null; }
		@Override public void startUpdate(View arg0) {}
	}
}
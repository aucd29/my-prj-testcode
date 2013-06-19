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
	private final int COUNT=10;					//아이템 갯수
	private int mPrevPosition;						//이전에 선택되었던 포지션 값
	
	private ViewPager mPager;					//뷰 페이저
	private LinearLayout mPageMark;			//현재 몇 페이지 인지 나타내는 뷰
	private Button mPrev, mNext;					//이전버튼, 다음버튼
	private RadioGroup mOption;				//아이템 이동시 애니메이션 효과 사용여부 선택 옵션
	private boolean isAnimated;					//애니메이션 효과 사용 여부
	
	//아이템의 배경화면 색상. 아이템을 구분하기 위해.
	private int[] mColorArray = {Color.YELLOW, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY,
										Color.GREEN, Color.LTGRAY, Color.MAGENTA, Color.RED, Color.WHITE}; 
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other);

		mPageMark = (LinearLayout)findViewById(R.id.page_mark);			//상단의 현재 페이지 나타내는 뷰

		mPager = (ViewPager)findViewById(R.id.pager);						//뷰 페이저
		mPager.setAdapter(new BkPagerAdapter(getApplicationContext()));//PagerAdapter로 설정
		mPager.setOnPageChangeListener(new OnPageChangeListener() {	//아이템이 변경되면, gallery나 listview의 onItemSelectedListener와 비슷
			//아이템이 선택이 되었으면
			@Override public void onPageSelected(int position) {
				mPageMark.getChildAt(mPrevPosition).setBackgroundResource(R.drawable.page_not);	//이전 페이지에 해당하는 페이지 표시 이미지 변경
				mPageMark.getChildAt(position).setBackgroundResource(R.drawable.page_select);		//현재 페이지에 해당하는 페이지 표시 이미지 변경
				mPrevPosition = position;				//이전 포지션 값을 현재로 변경
			}
			@Override public void onPageScrolled(int position, float positionOffest, int positionOffsetPixels) {}
			@Override public void onPageScrollStateChanged(int state) {}
		});

		initPageMark();	//현재 페이지 표시하는 뷰 초기화
		
		mPrev = (Button)findViewById(R.id.prev);		//이전 아이템으로 이동 버튼
		mPrev.setOnClickListener(this);
		
		mNext = (Button)findViewById(R.id.next);		//다음 아이템으로 이동 버튼
		mNext.setOnClickListener(this);
		
		mOption = (RadioGroup)findViewById(R.id.option);	//옵션 라디오 그룹
		mOption.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				isAnimated = checkedId == R.id.enable;		//옵션이 변경되면 값도 같이 변경한다.
			}
		});
		isAnimated = true;		//기본적으로 애니메이션 사용.
	}

	//상단의 현재 페이지 표시하는 뷰 초기화
	private void initPageMark(){
		for(int i=0; i<COUNT; i++)
		{
			ImageView iv = new ImageView(getApplicationContext());	//페이지 표시 이미지 뷰 생성
			iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			//첫 페이지 표시 이미지 이면 선택된 이미지로
			if(i==0)
				iv.setBackgroundResource(R.drawable.page_select);
			else	//나머지는 선택안된 이미지로
				iv.setBackgroundResource(R.drawable.page_not);

			//LinearLayout에 추가
			mPageMark.addView(iv);
		}
		mPrevPosition = 0;	//이전 포지션 값 초기화
	}

	//Pager 아답터 구현
	private class BkPagerAdapter extends PagerAdapter{
		private Context mContext;
		public BkPagerAdapter( Context con) { super(); mContext = con; }

		@Override public int getCount() { return COUNT; }	//여기서는 2개만 할 것이다.

		//뷰페이저에서 사용할 뷰객체 생성/등록
		@Override public Object instantiateItem(View pager, int position) {
			
			TextView tv = new TextView(mContext);					//텍스트뷰
			tv.setBackgroundColor(mColorArray[position]);			//배경색 지정
			tv.setText("ViewPager Item"+(position+1));				//글자지정
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);		//글자 크기 24sp
			tv.setTextColor(mColorArray[mColorArray.length - (position+1)]);	//글자 색상은 배경과 다른 색으로
			
			((ViewPager)pager).addView(tv, 0);		//뷰 페이저에 추가

			return tv; 
		}

		//뷰 객체 삭제.
		@Override public void destroyItem(View pager, int position, Object view) {
			((ViewPager)pager).removeView((View)view);
		}

		// instantiateItem메소드에서 생성한 객체를 이용할 것인지
		@Override public boolean isViewFromObject(View view, Object obj) { return view == obj; }

		@Override public void finishUpdate(View arg0) {}
		@Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
		@Override public Parcelable saveState() { return null; }
		@Override public void startUpdate(View arg0) {}
	}

	@Override
	public void onClick(View v) {
		int view = v.getId();
		if(view == R.id.prev){		//이전 버튼
			int cur = mPager.getCurrentItem();	//현재 아이템 포지션
			if(cur > 0)				//첫 페이지가 아니면
				mPager.setCurrentItem(cur-1, isAnimated);	//이전 페이지로 이동
			else						//첫 페이지 이면
				Toast.makeText(getApplicationContext(), "맨 처음 페이지 입니다.", Toast.LENGTH_SHORT).show();	//메시지 출력
		}
		else if(view == R.id.next){	//다음 버튼
			int cur = mPager.getCurrentItem();	//현재 아이템 포지션
			if(cur < COUNT-1)		//마지막 페이지가 아니면
				mPager.setCurrentItem(cur+1, isAnimated);	//다음 페이지로 이동
			else						//마지막 페이지 이면
				Toast.makeText(getApplicationContext(), "맨 마지막 페이지 입니다.", Toast.LENGTH_SHORT).show();	//메시지 출력
		}
	}
}
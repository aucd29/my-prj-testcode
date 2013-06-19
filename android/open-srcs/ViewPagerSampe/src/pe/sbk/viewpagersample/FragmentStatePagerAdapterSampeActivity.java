package pe.sbk.viewpagersample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

//support package에 있는 FragmentActivity를 상속 받는다.
public class FragmentStatePagerAdapterSampeActivity extends FragmentActivity {
	private final int COUNT=2;
	private ViewPager mPager;	//뷰 페이저

	private static OnClickListener mButtonClick = new OnClickListener() {		//클릭 이벤트 객체
		public void onClick(View v) {
			String text = ((Button)v).getText().toString();		//버튼에만 이벤트를 등록함. 버튼 글자 가져옴.
			Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();	//토스트로 출력
		}
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(new BkFragmentStateAdapter(getSupportFragmentManager()));
		
	}

	//FragmentPager 구현
	private class BkFragmentStateAdapter extends FragmentStatePagerAdapter{
		public BkFragmentStateAdapter(FragmentManager fm) { super(fm); }
		
		/**
		 * 실제 뷰페이저에서 보여질 fragment를 반환.
		 * 일반 아답터(갤러리, 리스트뷰 등)의 getView와 같은 역할
		 * @param position - 뷰페이저에서 보여저야할 페이지 값( 0부터 )
		 * @return 보여질 fragment
		 */
		@Override public Fragment getItem(int position) {
			return ArrayFragment.newInstance(position);
		}

		//뷰페이저에서 보여질 총 페이지 수
		@Override public int getCount() { return COUNT; }
	}

	//뷰 페이저의 페이지에 맞는 fragment를 생성하는 객체
	private static class ArrayFragment extends Fragment {
		int mPosition;	//뷰 페이저의 페이지 값

		//fragment 생성하는 static 메소드 뷰페이저의 position을 값을 받는다.
		static ArrayFragment newInstance(int position) {
			ArrayFragment f = new ArrayFragment();	//객체 생성
			Bundle args = new Bundle();					//해당 fragment에서 사용될 정보 담을 번들 객체
			args.putInt("position", position);				//포지션 값을 저장
			f.setArguments(args);							//fragment에 정보 전달.
			return f;											//fragment 반환
		}

		//fragment가 만들어질 때
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mPosition = getArguments() != null ? getArguments().getInt("position") : 0;	// 뷰페이저의 position값을  넘겨 받음
		}

		//fragment의 UI 생성
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.layout1+mPosition, container, false);	//미리 알고 있는 레이아웃을 inflate 한다.

			if(mPosition==0){
				v.findViewById(R.id.btn1).setOnClickListener(mButtonClick);
				v.findViewById(R.id.btn2).setOnClickListener(mButtonClick);
			}
			else{
				v.findViewById(R.id.btn3).setOnClickListener(mButtonClick);
				v.findViewById(R.id.btn4).setOnClickListener(mButtonClick);
			}
			return v;
		}
	}
}
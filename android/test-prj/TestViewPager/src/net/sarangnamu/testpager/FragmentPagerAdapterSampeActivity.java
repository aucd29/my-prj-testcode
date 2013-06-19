package net.sarangnamu.testpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

//support package�� �ִ� FragmentActivity�� ��� �޴´�.
public class FragmentPagerAdapterSampeActivity extends FragmentActivity {
	private final int COUNT=2;
	private ViewPager mPager;	//�� ������

	private static OnClickListener mButtonClick = new OnClickListener() {		//Ŭ�� �̺�Ʈ ��ü
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
		mPager.setAdapter(new BkFragmentAdapter(getSupportFragmentManager()));
	}

	//FragmentPager ����
	private class BkFragmentAdapter extends FragmentPagerAdapter{
		//����
		public BkFragmentAdapter(FragmentManager fm) {super(fm);}

		/**
		 * ���� �������� ������ fragment�� ��ȯ.
		 * �Ϲ� �ƴ���(������, ����Ʈ�� ��)�� getView�� ���� ����
		 * @param position - �������� ��������� ������ ��( 0���� )
		 * @return ������ fragment
		 */
		@Override public Fragment getItem(int position) {
			return ArrayFragment.newInstance(position);
		}

		//�������� ������ �� ������ ��
		@Override public int getCount() { return COUNT; }
	}

	//�� �������� �������� �´� fragment�� ���ϴ� ��ü
	private static class ArrayFragment extends Fragment {
		int mPosition;	//�� �������� ������ ��

		//fragment ���ϴ� static �޼ҵ� ���������� position�� ���� �޴´�.
		static ArrayFragment newInstance(int position) {
			ArrayFragment f = new ArrayFragment();	//��ü ��
			Bundle args = new Bundle();					//�ش� fragment���� ���� ���� ���� ��� ��ü
			args.putInt("position", position);				//������ ���� ����
			f.setArguments(args);							//fragment�� ���� ���.
			return f;											//fragment ��ȯ
		}

		//fragment�� ������� ��
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mPosition = getArguments() != null ? getArguments().getInt("position") : 0;	// ���������� position����  �Ѱ� ����
		}

		//fragment�� UI ��
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.layout1+mPosition, container, false);	//�̸� �˰� �ִ� ���̾ƿ��� inflate �Ѵ�.

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
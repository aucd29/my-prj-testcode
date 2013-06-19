package net.sarangnamu.activity;

import net.sarangnamu.baedal.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageActivity extends Activity {
	public static final String TITLE_IMAGE = "title";
	public static final String MAIN_IMAGE = "image";
	public static final String BACKGROUND = "background";
	public static final String TARGET_URI = "uri";
	private String targetUri;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		if (intent != null) {
			Log.d("", "error!!!!!!!!!!!!!!!!!!!!!!!");
			finish();
			return ;
		}
		
		int imageResId = intent.getIntExtra(MAIN_IMAGE, -1);
		targetUri = intent.getStringExtra(TARGET_URI);
		
		if (imageResId == -1 || (targetUri == null || targetUri.length() == 0)) {
			Log.d("", "error!!!!!!!!!!!!!!!!!!!!!!!");
			finish();
			return ;
		}
		
		int background = intent.getIntExtra(BACKGROUND, -1);		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		if(background > -1) {
			layout.setBackgroundResource(background);
		}
				
		ImageView imageView = new ImageView(this);
		imageView.setImageResource(imageResId);
		layout.addView(imageView);
		
		setContentView(layout);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetUri));
		startActivity(intent);
		
		finish();
		return super.onTouchEvent(event);
	}
}

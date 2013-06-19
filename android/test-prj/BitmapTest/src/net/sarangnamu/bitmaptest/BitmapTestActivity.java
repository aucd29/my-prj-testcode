package net.sarangnamu.bitmaptest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class BitmapTestActivity extends Activity {
    private Bitmap bmp, base;
    private Button btn;
    private ImageView img, img2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btn = (Button) findViewById(R.id.btn);
        img = (ImageView) findViewById(R.id.image);
        img2 = (ImageView) findViewById(R.id.image2);

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bmp = Bitmap.createBitmap(100, 100, Config.ARGB_8888);

        Canvas cv = new Canvas();
        Paint ptBg = new Paint();
        ptBg.setColor(Color.RED);
        ptBg.setStyle(Paint.Style.FILL);

        cv.setBitmap(bmp);
        cv.drawRect(10, 10, 90, 90, ptBg);

        // copy bitmap
        base = bmp.copy(bmp.getConfig(), true);
        img.setImageBitmap(bmp);
        img2.setImageBitmap(base);
    }
}
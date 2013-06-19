package net.sarangnamu.testinvoke;

import java.lang.reflect.Method;

import net.sarangnamu.testinvoke.test.TestCode;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestInvokeActivity extends Activity {
    private static final String TAG = "TestInvokeActivity";

    private Button btn;

    private TestCode xxx = new TestCode();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btn = (Button)findViewById(R.id.button1);

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Class cls;
                try {
                    cls = Class.forName("net.sarangnamu.testinvoke.test.TestCode");
                    Method[] methods = cls.getMethods();
                    for(Method m : methods )
                    {
                        System.out.println( "Found a method: " + m );
                    }

                    //                    Method method = cls.getMethod("testMethod", new Class[] {int.class});
                    //                    method.invoke(cls.getInterfaces(), new Object[] {1234});

                    final String P = "net.sarangnamu.testinvoke.test.TestCode";
                    Method md = Class.forName(P).getMethod("testMethod2");

                    if (md == null) {
                        Log.d(TAG, "===================================================================");
                        Log.d(TAG, "md is null");
                        Log.d(TAG, "===================================================================");
                    } else {
                        Log.d(TAG, "===================================================================");
                        Log.d(TAG, "md okay");
                        Log.d(TAG, "===================================================================");
                    }

                    Object ccc = cls.newInstance();
                    md.invoke(ccc);

                    //                    md.invoke(cls.getInterfaces(), new Object[] {2222});

                    Log.d(TAG, "===================================================================");
                    Log.d(TAG, "okay");
                    Log.d(TAG, "===================================================================");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
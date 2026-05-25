import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private WindowManager mWindowManager;
    private View mFloatView;
    private WindowManager.LayoutParams mParams;

    private float mTouchX;
    private float mTouchY;
    private int mViewX;
    private int mViewY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFloatWindow();
    }

    private void initFloatWindow() {
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mFloatView = LayoutInflater.from(this).inflate(R.layout.float_view, null);

        mParams = new WindowManager.LayoutParams();
        mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.x = 100;
        mParams.y = 200;

        // 悬浮窗拖动
        mFloatView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchX = event.getRawX();
                        mTouchY = event.getRawY();
                        mViewX = mParams.x;
                        mViewY = mParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = event.getRawX() - mTouchX;
                        float dy = event.getRawY() - mTouchY;
                        mParams.x = (int) (mViewX + dx);
                        mParams.y = (int) (mViewY + dy);
                        mWindowManager.updateViewLayout(mFloatView, mParams);
                        break;
                }
                return false;
            }
        });

        mWindowManager.addView(mFloatView, mParams);
        Toast.makeText(this, "悬浮窗已开启", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFloatView != null && mFloatView.isShown()) {
            mWindowManager.removeView(mFloatView);
        }
    }
}

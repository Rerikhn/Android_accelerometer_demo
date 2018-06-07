package magtu.com.example.circlesrecursive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.*;
import android.view.*;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public static DrawThread gameThread;

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        // turn off thread
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // if cant turn off, then continue
            }
        }
    }

    /**
     * Support function to call all default
     *
     * @param context by application
     */
    private void init(Context context) {
        getHolder().addCallback(this);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        gameThread = new DrawThread(getHolder(), metrics);
        setFocusable(true);
    }

    /*@SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Do some stuff
                break;
            case MotionEvent.ACTION_MOVE:
                // this too
                gameThread.setPX(event.getX());
                gameThread.setPY(event.getY());
        }
        return true;
    }*/

}

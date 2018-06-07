package magtu.com.example.circlesrecursive;

import android.annotation.SuppressLint;
import android.graphics.*;
import android.hardware.SensorEventListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Random;

/**
 * Main class of all graphics with thread
 */
public class DrawThread extends Thread {

    private DisplayMetrics metrics;
    private final SurfaceHolder surfaceHolder;
    private long prevTime;
    private Paint paint;
    private boolean runFlag;
    private float pX, pY;

    @SuppressLint("ClickableViewAccessibility")
    DrawThread(SurfaceHolder surfaceHolder, DisplayMetrics metrics) {
        this.surfaceHolder = surfaceHolder;
        this.metrics = metrics;
        // Paint
        paint = new Paint();
        paint.setAntiAlias(true);
        // load components for graphics

        // save current time
        prevTime = System.currentTimeMillis();
    }

    void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (!runFlag) {
            try {
                surfaceHolder.wait();
            } catch (InterruptedException ignored) {
            }
        }
        while (runFlag) {
            // get current time and calculate difference with older time
            long now = System.currentTimeMillis();
            long elapsedTime = now - prevTime;
            // 1 - second = 1000 milliseconds
            //  60 frames per second = (1/60s) * 1000ms ~ 16.6ms
            if (elapsedTime > 16) {
                //if time > N milliseconds, save current time
                prevTime = now;
                //updateFrame(); // picture update with 60 fps (only for moving objects)
            }
            canvas = null;
            try {
                // get Canvas and create drawings
                canvas = surfaceHolder.lockCanvas(null);
                if (canvas != null) synchronized (surfaceHolder) {
                    // graphics
                    canvas.drawColor(Color.BLACK);
                    circles(canvas, paint, 300, pX, pY, 255);
                }
            } finally {
                if (canvas != null) {
                    // if graphics done, set it on display
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void updateFrame() {
        // supporting method for graphics update
        // only for moving objects
    }

    private void circles(Canvas canvas, Paint paint, float radius, float pX, float pY, int alpha) {
        paint.setColor(Color.rgb(
                new Random().nextInt(255),
                new Random().nextInt(255),
                new Random().nextInt(255))
        );
        //paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setAlpha(alpha);
        paint.setStrokeWidth(5);

        canvas.drawCircle(metrics.widthPixels / 2 + pX,
                metrics.heightPixels / 2 + pY, radius, paint);

        radius *= 0.9f;
        pX *= 0.9f;
        pY *= 0.9f;
        alpha *= 0.93f;
        if (radius >= 1) circles(canvas, paint, radius, pX, pY, alpha);
    }

    /**
     * This function is remapping line value
     * Example:
     * 0 from 10
     * remap(4 from 0  to 10) = 40 from 0 to 100
     *
     * @param s  value to find
     * @param a1 start value 1
     * @param a2 stop value 1
     * @param b1 start value 2
     * @param b2 start value 2
     * @return returns a float value
     */
    private float reMap(float s, float a1, float a2, float b1, float b2) {
        return b1 + (s - a1) * (b2 - b1) / (a2 - a1);
    }

    public void setPX(float plusX) {
        /*this.pX = reMap(plusX, 0, metrics.widthPixels,
                -metrics.widthPixels, metrics.widthPixels);*/
        this.pX = -plusX * 100;
    }

    public void setPY(float plusY) {
        /*this.pY = reMap(plusY, 0, metrics.heightPixels,
                -metrics.heightPixels, metrics.heightPixels);*/
        this.pY = plusY * 100;
    }
}



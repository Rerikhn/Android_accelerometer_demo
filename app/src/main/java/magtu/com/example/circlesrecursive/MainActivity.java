package magtu.com.example.circlesrecursive;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float ALPHA = 0.1f;
    private float[] accelValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        assert mSensorManager != null;
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_FASTEST);
        hideUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideUI();
        mSensorManager.unregisterListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideUI();
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Function that turn on immersive mode
     */
    public void hideUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelValues = lowPass(sensorEvent.values.clone(), accelValues);
            GameView.gameThread.setPX(accelValues[0]);
            GameView.gameThread.setPY(accelValues[1]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // Low-pass smooth filtering for accelerometer
    protected float[] lowPass(float[] input, float[] output) {
        if (output == null) return input;

        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }
}

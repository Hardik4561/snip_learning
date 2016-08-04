package com.hardik.fakewalk;

import android.content.Context;
import android.graphics.PixelFormat;
import android.location.Location;
import android.location.LocationManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;

/**
 * Creates the head layer view which is displayed directly on window manager.
 * It means that the view is above every application's view on your phone -
 * until another application does the same.
 */
public class HeadLayer extends View {

    private Context mContext;
    private FrameLayout mFrameLayout;
    private WindowManager mWindowManager;

    LocationManager locationManager;

    public HeadLayer(Context context) {
        super(context);

        mContext = context;
        mFrameLayout = new FrameLayout(mContext);

        addToWindowManager();
    }

    private void addToWindowManager() {

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.LEFT;

        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(mFrameLayout, params);

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Here is the place where you can inject whatever layout you want.
        layoutInflater.inflate(R.layout.head, mFrameLayout);

        // Support dragging the image view
        final Joystick joystick = (Joystick) mFrameLayout.findViewById(R.id.joystick);

        joystick.setJoystickListener(new JoystickListener() {

            @Override
            public void onDown() {

            }

            @Override
            public void onDrag(float degrees, float offset) {

            }

            @Override
            public void onUp() {

            }
        });

    }

    /**
     * Removes the view from window manager.
     */
    public void destroy() {
        mWindowManager.removeView(mFrameLayout);
    }
}

package com.atom.traningandroid.activity;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.atom.traningandroid.utils.AppUtils;

public class SimpleGestureFilter extends GestureDetector.SimpleOnGestureListener {
    // Swipe gestures type
    public final static int SWIPE_UP = 1;
    public final static int SWIPE_DOWN = 2;
    public final static int SWIPE_LEFT = 3;
    public final static int SWIPE_RIGHT = 4;

    public final static int MODE_TRANSPARENT = 0;
    public final static int MODE_SOLID = 1;
    public final static int MODE_DYNAMIC = 2;

    private final static int ACTION_FAKE = -13; // just an unlikely number

    // Swipe distances
    private int swipe_Min_Distance = 50;
    private int swipe_Max_Distance = 5350;
    private int swipe_Min_Velocity = 0;

    private int mode = MODE_DYNAMIC;
    private boolean running = true;
    private boolean tapIndicator = false;

    private Activity context;
    private GestureDetector detector;
    private SimpleGestureListener listener;

    public SimpleGestureFilter(Activity context,
                               SimpleGestureListener simpleGestureListener) {

        this.context = context;
        this.detector = new GestureDetector(context, this);
        this.listener = simpleGestureListener;
    }

    public void onTouchEvent(MotionEvent event) {

        if (!this.running) {
            System.out.println("running");
            return;
        }

        boolean result = this.detector.onTouchEvent(event);
        // Get the gesture
        if (this.mode == MODE_SOLID)
            event.setAction(MotionEvent.ACTION_CANCEL);
        else if (this.mode == MODE_DYNAMIC) {

            if (event.getAction() == ACTION_FAKE)
                event.setAction(MotionEvent.ACTION_UP);
            else if (result)
                event.setAction(MotionEvent.ACTION_CANCEL);
            else if (this.tapIndicator) {
                event.setAction(MotionEvent.ACTION_DOWN);
                this.tapIndicator = false;
            }

        } else {
            System.out.println(".........any thing");
        }
// else just do nothing, it's Transparent
    }

    public void setMode(int m) {
        this.mode = m;
    }

    public int getMode() {
        return this.mode;
    }

    public void setEnabled(boolean status) {
        this.running = status;
    }

    public void setSwipeMaxDistance(int distance) {
        this.swipe_Max_Distance = distance;
    }

    public void setSwipeMinDistance(int distance) {
        this.swipe_Min_Distance = distance;
    }

    public void setSwipeMinVelocity(int distance) {
        this.swipe_Min_Velocity = distance;
    }

    public int getSwipeMaxDistance() {
        return this.swipe_Max_Distance;
    }

    public int getSwipeMinDistance() {
        return this.swipe_Min_Distance;
    }

    public int getSwipeMinVelocity() {
        return this.swipe_Min_Velocity;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        System.out.println("...Fling...");
        final float xDistance = Math.abs(e1.getX() - e2.getX());
        final float yDistance = Math.abs(e1.getY() - e2.getY());
        System.out.println("xD: " + xDistance);
        System.out.println("yD: " + yDistance);
        System.out.println("e1x: " + e1.getX());
        System.out.println("e1y: " + e1.getY());
        System.out.println("e2x: " + e2.getX());
        System.out.println("e2y: " + e2.getY());
        if (xDistance > this.swipe_Max_Distance
                || yDistance > this.swipe_Max_Distance)
            return false;

        velocityX = Math.abs(velocityX);
        velocityY = Math.abs(velocityY);
//        boolean result = false;

        if (xDistance >= yDistance) {
            if (e1.getX() > e2.getX()) {
                this.listener.onSwipe(SWIPE_LEFT);
            } else {
                this.listener.onSwipe(SWIPE_RIGHT);
            }
        } else if (yDistance >= swipe_Min_Distance) {
            if (e1.getY() > e2.getY()) { // bottom to up
                this.listener.onSwipe(SWIPE_UP);
            } else {
                this.listener.onSwipe(SWIPE_DOWN);
            }
        }
//        if (velocityX > this.swipe_Min_Velocity
//                && xDistance > this.swipe_Min_Distance) {
//            if (e1.getX() > e2.getX()) // right to left
//                this.listener.onSwipe(SWIPE_LEFT);
//            else
//                this.listener.onSwipe(SWIPE_RIGHT);
//
//            result = true;
//        } else if (velocityY > this.swipe_Min_Velocity
//                && yDistance > this.swipe_Min_Distance) {
//            if (e1.getY() > e2.getY()) // bottom to up
//                this.listener.onSwipe(SWIPE_UP);
//            else
//                this.listener.onSwipe(SWIPE_DOWN);
//
//            result = true;
//        }

        return true;
    }

    static interface SimpleGestureListener {
        void onSwipe(int direction);
    }
}

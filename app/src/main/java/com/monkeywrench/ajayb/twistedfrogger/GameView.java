package com.monkeywrench.ajayb.twistedfrogger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

class Frog{
    public Rect frog_rect;
    public int current_lane = 0;
    public int current_col = 3;
    public Paint paint;
    private static Frog frogInstance = null;

    private Frog(){
        current_lane = 1;
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
    }

    public static Frog makeFrog(){
        if (frogInstance == null){
            frogInstance = new Frog();
        }

        return frogInstance;
    }
}

public class GameView extends SurfaceView implements Runnable, View.OnTouchListener {
    private static final String TAG = "GameView";

    private Context context;
    private SurfaceHolder surfaceHolder;
    private Paint paint, frogPaint;
    private volatile boolean isRunning = true;
    private Thread gameThread;
    private Terrain terrain;
    private AssetsHelper assetsHelper;
    private Frog frog;
    private int screenWidth, screenHeight;

    public GameView(Context context, int width, int height) {
        super(context);
        init(context, width, height);
        setOnTouchListener(this);
    }

    private void init(Context context, int width, int height) {
        this.screenHeight = height;
        this.screenWidth = width;

        this.context = context;
        this.surfaceHolder = getHolder();
        this.paint = new Paint();
        this.paint.setColor(Color.GRAY);
        this.frogPaint = new Paint();
        this.frogPaint.setStyle(Paint.Style.STROKE);
        this.frogPaint.setStrokeWidth(5);
        this.frogPaint.setColor(Color.GREEN);
        terrain = new Terrain(width, height);
        Log.i(TAG, Integer.toString(terrain.BLOCK_X_SIZE) + " " + Integer.toString(terrain.BLOCK_Y_SIZE));
        assetsHelper = AssetsHelper.getAssetsHelper(context, terrain.BLOCK_X_SIZE, terrain.BLOCK_Y_SIZE);
        frog = Frog.makeFrog();
        frog.frog_rect = terrain.getGRID()[3][terrain.V_BLOCKS - 1];
    }

    public void pause() {
        this.isRunning = false;
        try {
            while (true) {
                gameThread.join();
                gameThread = null;
                break;
            }
        } catch (InterruptedException e) {

        }
    }

    public void resume() {
        gameThread = new Thread(this);
        gameThread.start();
        this.isRunning = true;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (this.isRunning) {
            if (this.surfaceHolder.getSurface().isValid()) {
                long thenTime = System.nanoTime();
                canvas = this.surfaceHolder.lockCanvas();
                canvas.save();
                canvas.drawARGB(255, 127, 127, 127);
//                terrain.drawGRID(canvas);
                terrain.drawRoad(canvas, assetsHelper.roadtile);
                terrain.drawCorners(canvas, assetsHelper.cornertile);
                frog.frog_rect = terrain.getGRID()[frog.current_col][frog.current_lane];
                canvas.drawRect(frog.frog_rect, frog.paint);
//                terrain.drawScale(canvas);
                canvas.restore();
                this.surfaceHolder.unlockCanvasAndPost(canvas);
                long nowTime = System.nanoTime();

                long diffInMillis = (nowTime - thenTime) / 1000000;
                if (diffInMillis < 0.17) {
                    long toWait = (long) 0.17 - diffInMillis;
                    try {
                        gameThread.wait(toWait);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            if ((x > frog.frog_rect.left) && (x < frog.frog_rect.right)){
                boolean touchedUp =(y > 0) && (y < frog.frog_rect.top);
                boolean touchedDown = (y < screenHeight) && (y > frog.frog_rect.bottom);
                if (touchedUp && (frog.current_lane < Terrain.V_BLOCKS)){
                    frog.current_lane--;
                }else if(touchedDown && (frog.current_lane >= 0)){
                    frog.current_lane++;
                }
            }

            if((y > frog.frog_rect.top) && (y < frog.frog_rect.bottom)){
                boolean touchedLeft = (x > 0) && (x < frog.frog_rect.left);
                boolean touchedRight = (x < screenWidth) && (x > frog.frog_rect.right);
                if (touchedRight && (frog.current_col >= 0)){
                    frog.current_col ++;
                }else if(touchedLeft && (frog.current_col < Terrain.H_BLOCKS)){
                    frog.current_col --;
                }
            }
        }
        return true;
    }
}
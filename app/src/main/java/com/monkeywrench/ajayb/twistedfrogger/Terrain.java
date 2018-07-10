package com.monkeywrench.ajayb.twistedfrogger;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Terrain {
    public static final int H_BLOCKS = 10;
    public static final int V_BLOCKS = 6;
    private Paint paint;
    private Rect[][] GRID;
    public int BLOCK_X_SIZE, BLOCK_Y_SIZE;

    public Terrain(int width, int height){
        paint = new Paint();
        GRID = new Rect[H_BLOCKS][V_BLOCKS];

        BLOCK_X_SIZE = width / H_BLOCKS;
        BLOCK_Y_SIZE = height / V_BLOCKS;

        for(int i = 0; i < H_BLOCKS; i++){
            int x = i * BLOCK_X_SIZE;
            for(int j = 0; j < V_BLOCKS; j++){
                int y = j * BLOCK_Y_SIZE;
                GRID[i][j] = new Rect(x, y, x + BLOCK_X_SIZE, y + BLOCK_Y_SIZE);
            }
        }
    }

    private void drawCorners(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);

        canvas.drawRect(GRID[0][0], paint);
        canvas.drawRect(GRID[H_BLOCKS - 1][0], paint);
        canvas.drawRect(GRID[H_BLOCKS - 1][V_BLOCKS - 1], paint);
        canvas.drawRect(GRID[0][V_BLOCKS - 1], paint);

        paint.setStrokeWidth(1);
    }

    public void drawGRID(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);

        for (int i = 0; i < H_BLOCKS; i++){
            for (int j = 0; j < V_BLOCKS; j++){
                canvas.drawRect(GRID[i][j], paint);
            }
        }
    }

    public void drawRoad(Canvas canvas, Bitmap tile){
        for( int j = 1; j <= 4; j++ ){
            for( int i = 0; i < H_BLOCKS; i++){
                canvas.drawBitmap(tile, i * BLOCK_X_SIZE, j * BLOCK_Y_SIZE,null);
            }
        }
    }

    public void drawCorners(Canvas canvas, Bitmap tile){
        canvas.drawBitmap(tile, GRID[0][0].left, GRID[0][0].top, null);
        canvas.drawBitmap(tile, GRID[H_BLOCKS - 1][0].left, GRID[H_BLOCKS - 1][0].top, null);
        canvas.drawBitmap(tile, GRID[H_BLOCKS - 1][V_BLOCKS - 1].left, GRID[H_BLOCKS - 1][V_BLOCKS - 1].top, null);
        canvas.drawBitmap(tile, GRID[0][V_BLOCKS - 1].left, GRID[0][V_BLOCKS - 1].top, null);
    }

    public void drawScale(Canvas canvas){
        paint.setTextSize(100);
        canvas.drawText(Integer.toString(BLOCK_X_SIZE), 100, 100, paint);
        canvas.drawText(Integer.toString(BLOCK_Y_SIZE), 100, 200, paint);
    }

    public Rect[][] getGRID(){
        return GRID;
    }
}

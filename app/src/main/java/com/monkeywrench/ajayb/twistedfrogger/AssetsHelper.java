package com.monkeywrench.ajayb.twistedfrogger;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
 import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class AssetsHelper {

    private static final String TAG = "AssetsHelper";
    private static AssetsHelper assetsHelper = null;

    public Bitmap roadtile, cornertile;

    private static final int X_WIDTH = 90;
    private static final int Y_WIDTH = 90;

    private static int X_BLOCK_SIZE, Y_BLOCK_SIZE;

    private AssetsHelper(Context context, int x_block_size, int y_block_size){

        X_BLOCK_SIZE = x_block_size; Y_BLOCK_SIZE = y_block_size;

        AssetManager manager = context.getAssets();
        try{
            InputStream is = manager.open("art/roadtiles.png");
            Bitmap master = BitmapFactory.decodeStream(is);

            roadtile = prepareTile(master,150, 150);
            cornertile = prepareTile(master, 390, 510);

            Log.i(TAG, Integer.toString(x_block_size)+ " " + Integer.toString(y_block_size));
        }
        catch(IOException ioe){
            Log.e(TAG, "Loading asset failed \n" + ioe.getMessage(), null);
        }
    }

    public static AssetsHelper getAssetsHelper(Context context, int x_block_size, int y_block_size){
        if ( assetsHelper == null ){
            assetsHelper = new AssetsHelper(context, x_block_size, y_block_size);
        }

        return assetsHelper;
    }

    private Bitmap prepareTile(Bitmap master,int left, int top){
        Bitmap tile = Bitmap.createBitmap(master, left, top , X_WIDTH, Y_WIDTH);
        tile = Bitmap.createScaledBitmap(tile, X_BLOCK_SIZE, Y_BLOCK_SIZE, false);

        return tile;
    }

}

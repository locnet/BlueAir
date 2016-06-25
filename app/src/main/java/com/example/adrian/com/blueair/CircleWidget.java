package com.example.adrian.com.blueair;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;


/**
 * Custom circular view, thanks to StrackOverflow.
 */
public class CircleWidget extends ShapeDrawable {
    protected int position = 0;
    private  ShapeDrawable mPaint;
    Context mContext;

    // public constructor
    public CircleWidget (Context context, int r, double price, int p){
        mContext = context;
        position = p;
        int x = 10;
        int y = 10;
        OvalShape mCircle = new OvalShape();
        mPaint = new ShapeDrawable(mCircle);
        mPaint.getPaint().setColor(getCircleColor(price));
        mPaint.setBounds(x, y, x + r, y + r);
    }

    @Override
    public void draw(Canvas canvas) {
        mPaint.draw(canvas);
    }

    public ShapeDrawable getCircle(){
        return mPaint;
    }

    /**
     * returneaza o culoare in functie un ranking calculat in ResultsArrayAdapter.java
     * @param price pretul biletului
     * @return int corespunzator culorii
     */
    public int getCircleColor (double price) {
        int c = 0;
        // daca pretul este mai mic ca 1 inseamna ca biletele sunt vandute
        if (price < 1) {
            c = mContext.getResources().getColor(R.color.lightGrey);
        } else if (position == 0) {
            c = mContext.getResources().getColor(R.color.ultraLightGreen);
        } else if (position == 1) {
            c = mContext.getResources().getColor(R.color.green);
        } else if (position == 2) {
            c = mContext.getResources().getColor(R.color.darkGreen);
        }  else if (position == 3) {
            c =  mContext.getResources().getColor(R.color.lightOrange);
        } else if (position == 4) {
            c = mContext.getResources().getColor(R.color.lightRed);
        } else if (position > 4) {
            c = Color.RED;
        }
        return c;
    }
}

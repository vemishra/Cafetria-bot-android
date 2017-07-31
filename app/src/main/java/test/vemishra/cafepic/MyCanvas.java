package test.vemishra.cafepic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

import static android.R.attr.name;
import static java.lang.Math.min;

/**
 * Created by vemishra on 7/25/2017.
 */

public class MyCanvas extends View{
        private String name1,des1,cal1,price1,quantity1,CurrencyI;
        private Integer breakpoint, firstheight,checkpoint,startpoint;
    public MyCanvas(Context context,String name,String des,String quan, String cal,String price,String CurrencyIcon) {
        super(context);
        name1  = name;
        des1  = des;
        cal1 = cal;
        price1 = price;
        quantity1 = quan;
        CurrencyI = CurrencyIcon;
        firstheight = 350;
        checkpoint = 0;
        startpoint = 0;


    }
        @Override
        protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        //Paint pBackground = new Paint();
        //pBackground.setColor(Color.YELLOW);
        //canvas.drawRect(0, 0, 500, 500, pBackground);

        Paint pText = new Paint();
        pText.setColor(Color.BLACK);
        pText.setTextSize(14);
        pText.setAntiAlias(true);
        pText.setTypeface(Typeface.create("Arial", Typeface.NORMAL));
        Paint hText = new Paint();
        hText.setColor(Color.BLACK);
        hText.setTextSize(24);
        hText.setAntiAlias(true);
        hText.setTypeface(Typeface.create("Arial", Typeface.NORMAL));
        Paint hbText = new Paint();
        hbText.setColor(Color.BLACK);
        hbText.setTextSize(24);
        hbText.setAntiAlias(true);
        hbText.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        Paint gText = new Paint();
        gText.setColor(Color.GREEN);
        gText.setTextSize(34);
        gText.setAntiAlias(true);
        gText.setTypeface(Typeface.create("Arial", Typeface.NORMAL));
        Paint grayText = new Paint();
        grayText.setColor(Color.GRAY);
        grayText.setTextSize(14);
        grayText.setAntiAlias(true);
        grayText.setTypeface(Typeface.create("Arial", Typeface.NORMAL));
        //  canvas.drawText(name1, 20, firstheight, hText);

      //  canvas.drawText(name1,0,25, 20, firstheight, hText);
            checkpoint = 25;
            startpoint = 0;
        while((name1.length()-startpoint)>25){
                breakpoint =  name1.lastIndexOf(' ', checkpoint);
                //    Toast.makeText(getContext(),""+breakpoint,Toast.LENGTH_LONG).show();
                canvas.drawText(name1,startpoint,breakpoint,20,firstheight,hText);
                checkpoint = min(breakpoint+26,name1.length());
                startpoint = breakpoint+1;
                firstheight+=24;
        }
            if(startpoint < name1.length())
                canvas.drawText(name1,startpoint,name1.length(),20,firstheight,hText);
            checkpoint = 45;
        startpoint = 0;
        breakpoint = 0;
        firstheight+=34;
        while((des1.length()-startpoint)>45){
            breakpoint = des1.lastIndexOf(' ', checkpoint);
            //    Toast.makeText(getContext(),""+breakpoint,Toast.LENGTH_LONG).show();
            canvas.drawText(des1,startpoint,breakpoint,20,firstheight,pText);
            checkpoint = min(breakpoint+46,des1.length());
            startpoint = breakpoint+1;
            firstheight+=14;
        }
        if(startpoint < des1.length())
            canvas.drawText(des1,startpoint,des1.length(),20,firstheight,pText);
        firstheight+=25;
        Bitmap icon = getResizedBitmap(BitmapFactory.decodeResource(getContext().getResources(),R.mipmap.icon_amt_no),40,40);
        canvas.drawBitmap(icon,20,firstheight,null);
        Bitmap icon1 = getResizedBitmap(BitmapFactory.decodeResource(getContext().getResources(),R.mipmap.icon_cal_no),30,30);
        canvas.drawBitmap(icon1,100,firstheight,null);

        firstheight+=55;
        canvas.drawText(quantity1,20,firstheight,hbText);
        canvas.drawText(cal1,100,firstheight,hbText);
        //  canvas.drawText(CurrencyI,210,firstheight,gText);
        //  gText.setTextSize(24);
        Integer endP = price1.length();
        gText.setTextSize(24);
        canvas.drawText(price1,0,1,215,firstheight,gText);
        gText.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        gText.setTextSize(34);
        canvas.drawText(price1,1,endP,230,firstheight,gText);
        firstheight+=22;
        canvas.drawText("Quantity",20,firstheight,grayText);
        canvas.drawText("Calories",100,firstheight,grayText);
    }
    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }
}

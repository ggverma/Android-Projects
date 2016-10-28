package com.example.runcarrun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;


@SuppressLint("HandlerLeak")
public class Drag3  extends View{

	Display display;
	int w, h;
	Paint paint;
	Bitmap car1, car2, car3;
	int carW, carH;
	double car1X = 10, car2X = 10, car3X = 10;
	
	double distance;
	
	double finishLine;
	
	double car1Time, car2Time, car3Time, winCarTime;
	
	CountDown cd;
	long cdCount = 3;
	boolean cdRunning = true;
		
	Race race;
	
	String winner="";
	
	boolean raceIsOn = false;
	
	//String engineCond = "";
	
	double raceTime ;
	
	double acc1, acc2, acc3;
	String carStat = "";
	String er = "";
	public Drag3(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		h = displayMetrics.heightPixels;
		w = displayMetrics.widthPixels;
		
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		paint.setTextSize(50);
		
		car1 = BitmapFactory.decodeResource(getResources(), R.drawable.smallcar);
		car2 = BitmapFactory.decodeResource(getResources(), R.drawable.smallcar);
		car3 = BitmapFactory.decodeResource(getResources(), R.drawable.smallcar);
		
		carW = car1.getWidth();
		carH = car1.getHeight();
		
		carTimeGenerator();
		
		finishLine = w - carW - 10; 
		
		raceTime = 0;
		
		distance = w - carW;
		
		cd = new CountDown();
		cd.start();
		
	}
	
	public void carTimeGenerator(){
		car1Time = (long)(Math.random()*2500 + 2500);
		car2Time = (long)(Math.random()*2500 + 2500);
		car3Time = (long)(Math.random()*2500 + 2500);
		
		if(car1Time==car2Time || car2Time==car3Time || car1Time==car3Time)
			carTimeGenerator();
		
		if(Math.abs(car1Time-car2Time) < 500 || Math.abs(car3Time-car2Time) < 500  || Math.abs(car1Time-car3Time) < 500)
			carTimeGenerator();
		
		acc1 = Math.random();
		acc2 = Math.random();
		acc3 = Math.random();
		
		
		if(car1Time < car2Time){
			if(car1Time<car3Time){
				winCarTime = car1Time;
				
			}
			else {
				winCarTime = car3Time;
			}
		}
		else if(car2Time<car3Time){
			winCarTime = car2Time;
		}
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		if(!cdRunning && raceIsOn){
			
			for(int i=-2 ; i<3 ; i++){
				canvas.drawLine(0, h/3 + i, w, h/3 + i, paint);
				canvas.drawLine(0, 2*h/3 + i, w, 2*h/3 + i, paint);
			}
			
			for(int i = -2 ; i < 3 ; i++)
				canvas.drawLine(w/8 + i, 0, w/8 + i, h, paint);
			
			for(int i = -2 ; i < 3 ; i++)
				canvas.drawLine(w + i - carW - 10, 0, w + i - carW - 10, h, paint);
			
			//canvas.drawText(car1Time+" "+car2Time+" "+car3Time, 10, 100, paint);
			
			paint.setTextSize(100);
			paint.setAlpha(100);
			
			canvas.drawText("Player 1", w/4, h/6, paint);
			canvas.drawText("Player 2", w/4, h/2, paint);
			canvas.drawText("Player 3", w/4, 5*h/6, paint);
			
			paint.setAlpha(255);
			
			
			canvas.drawBitmap(car1, (int)car1X, h/6, paint);
			canvas.drawBitmap(car2, (int)car2X, h/2, paint);
			canvas.drawBitmap(car3, (int)car3X, 5*h/6, paint);
		}
		else if(cdRunning){
			canvas.drawCircle(w/2, h/2, 200, paint);
			paint.setColor(Color.WHITE);
			
			canvas.drawCircle(w/2, h/2, 190, paint);
			paint.setColor(Color.BLACK);
			
			paint.setTextSize(120);
			canvas.drawText(cdCount + "", w/2 - 20, h/2 + 60, paint);
			
			
		}
		else if(!raceIsOn){
			
			canvas.drawText(winner + " wins!!!", w/2 - 100, h/2 +60, paint);
			
		}
	}
		
	class CountDown extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(cdRunning){
				try{
					Thread.sleep(1000);
					cdCount--;
					
				}
				catch(Exception e){}
				h1.sendMessage(h1.obtainMessage());
				if(cdCount == 0){
					raceIsOn = true;
										
					race = new Race();
					race.start();
					cdRunning = false;
				}
			}
			
		}
		Handler h1 = new Handler(){
			public void handleMessage(android.os.Message msg) {
				invalidate();
				
			};
		};
	}
	
	class Race  extends Thread{
		
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(raceIsOn){
				
				while(raceTime <= winCarTime){
					try{
						Thread.sleep((long)(winCarTime/distance));
						
					}
					catch(Exception e){
						er = "error";
					}
					h2.sendMessage(h2.obtainMessage());
					raceTime += 1;
				}
			}
		}
	}
		Handler h2 = new Handler(){
			public void handleMessage(android.os.Message msg) {
				if(raceTime <= winCarTime/3){
					car1X += (double)distance/car1Time;
					car2X += (double)distance/car2Time;
					car3X += (double)distance/car3Time;
				}
				else if(raceTime <= winCarTime){
					car1X += (double)distance/car1Time + acc1;
					car2X += (double)distance/car2Time + acc2;
					car3X += (double)distance/car3Time + acc3;
					
				}
				
				if(car1X >= finishLine){
					winner = "Player1";
					carStat = "car1";
					raceIsOn = false;
					raceTime = winCarTime;
				}
				else if(car2X >= finishLine){
					winner = "Player2";
					carStat = "car2";
					raceTime = winCarTime;
					raceIsOn = false;
				}
				else if(car3X >= finishLine){
					winner = "Player3";
					raceTime = winCarTime;
					carStat = "car3";
					raceIsOn = false;
				}
				invalidate();
			};
		};
	
}

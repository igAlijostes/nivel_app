package com.example.nivel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    getSupportActionBar().hide();
    setContentView(new MiVista(this));
    }

    public  class MiVista extends View implements SensorEventListener {

        private Bitmap bolita;
        private Bitmap fondo;
        private int ALTO,ANCHO;
        private float topeX,topeY;
        private PointF posicion=new PointF();
        private SensorManager manager;
        private Sensor acelerometro;

        public MiVista(Context context){
            super(context);
            manager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            if(manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() !=0) {
                acelerometro=manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
                if(manager.registerListener(this,acelerometro,SensorManager.SENSOR_DELAY_GAME)){
                    Toast.makeText(getApplicationContext(),"ERROR AL ASCEDER AL SENSOR",Toast.LENGTH_LONG).show();
                }
            }

            bolita= BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.precision);
            bolita=Bitmap.createScaledBitmap(bolita,200,200,true);
            DisplayMetrics metricts= new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metricts);

            ALTO=metricts.heightPixels;
            int centroY= ALTO/2;
            topeY=ALTO-bolita.getHeight()/2;

            ANCHO=metricts.widthPixels;
            int centroX= ANCHO/2;
            topeX=ANCHO-bolita.getWidth()/2;

            posicion.x=centroX;
            posicion.y=centroY;

            Bitmap.Config config=Bitmap.Config.ARGB_8888;
            fondo= Bitmap.createBitmap(ANCHO,ALTO,config);

            Canvas diana=new Canvas(fondo);
            Paint miPincel=new Paint();
            miPincel.setColor(Color.RED);
            miPincel.setStrokeWidth(5);
            miPincel.setStyle(Paint.Style.STROKE);

            diana.drawCircle(centroX,centroY,150,miPincel);
            diana.drawCircle(centroX,centroY,50,miPincel);
            diana.drawLine(centroX,0,centroX,ALTO,miPincel);
            diana.drawLine(0,centroY,ANCHO,centroY,miPincel);



        }

        protected void onDraw (Canvas canvas){
        canvas.drawBitmap(fondo,0,0,null);
        float xBall= posicion.x - (bolita.getWidth()/2);
        float yBall= posicion.y - (bolita.getHeight()/2);
        canvas.drawBitmap(bolita,xBall,yBall,null);

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
        posicion.x +=event.values[1];

        if(posicion.x < 0) {
            posicion.x=0;
        } else if (posicion.x > topeX) {
          posicion.x=topeX;
        }

            posicion.x +=event.values[0];

        if(posicion.y < 0) {
                posicion.y=0;
            } else if (posicion.y > topeY) {
                posicion.y=topeY;
            }

        invalidate();

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }






}
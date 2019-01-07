package com.example.administrator.myapplication1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Logger;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {
    TextView userTextview;
    Button logoutBtn;
    SharedPreferences sp;
    long exitTime=0;
    /*ImageView imageView;
    //记录指南针图片转过的角度
    float currentDegree = 0f;
    //定义sensor管理器
    SensorManager sensorManager;
    SharedPreferences sp;*/
    /*private Paint paint;
    private float gravity[] = new float[3];
    private float linear_accelerometer[] = new float[3];
*/
    //修改
    private static final String TAG = CompassActivity.class.getSimpleName();
    private CompassView compassView;
    private Sensor sensor;
    private SensorManager sensorManager;
    private float currentDegree = 0f;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_compass);
        compassView = (CompassView) findViewById(R.id.compass_view);
        initSensor();


        sp = this.getSharedPreferences("userinfo",MODE_PRIVATE);
        String username = sp.getString("USERNAME","");
        userTextview = findViewById(R.id.id_username);
        logoutBtn = findViewById(R.id.id_logout);
        userTextview.setText(username);
        logoutBtn = findViewById(R.id.id_logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

       /* imageView = findViewById(R.id.id_imageview);
        userTextview = findViewById(R.id.id_username);
        logoutBtn = findViewById(R.id.id_logout);
        //获取传感器管理服务
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

       *//* Intent intentUser = getIntent();
        String username = intentUser.getStringExtra("username");*//*
        userTextview.setText(username);

        logoutBtn = findViewById(R.id.id_logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
        }
        });*/
    }

    private void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CompassActivity.this);
        builder.setMessage("确认退出吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("退出登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("AUTO_ISCHECK",false);
                editor.commit();
                Intent intent = new Intent(CompassActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("退出应用", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("AUTO_ISCHECK",true);
                editor.commit();
                finish();

            }
        });
        builder.show();

    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        /*//获取重力传感器
        Sensor acceler = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,acceler,SensorManager.SENSOR_DELAY_GAME);

        //为系统的方向传感器注册监听器
        @SuppressWarnings("deprecation")
        Sensor orient = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this,orient,SensorManager.SENSOR_DELAY_GAME);*/
    }
    @Override
    protected  void onPause(){
        //取消注册
        sensorManager.unregisterListener(this);
        super.onPause();
    }
    @Override
    protected  void onStop(){
        //取消注册
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    private void initSensor(){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        switch (sensorType) {
            case Sensor.TYPE_ORIENTATION:
                float degree = event.values[0];
                if (Math.abs(currentDegree - degree) > 1) {
                    compassView.setRotate(degree);
                    currentDegree = degree;

                    //Logger.d(TAG, "========setRotate=======");
                }
                break;

            default:
                break;
        }

        /*boolean Switch = true;
        //获取触发event的传感器类型
        int sensorType = event.sensor.getType();
        switch (sensorType){
            case Sensor.TYPE_ORIENTATION:
                //获取绕Z轴转过的角度
                float degree = event.values[0];
                //创建旋转动画（反向转过degree度）
                RotateAnimation rotateAnimation = new RotateAnimation(currentDegree,-degree,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                //设置动画持续时间
                rotateAnimation.setDuration(200);
                //运行动画
                imageView.startAnimation(rotateAnimation);
                currentDegree = -degree;
                break;

        }*/

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        //  按两次返回键退出应用程序
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 判断间隔时间 大于2秒就退出应用
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                // 应用名
                String applicationName = getResources().getString(
                        R.string.app_name);
                String msg = "再按一次返回键退出" + applicationName;
                Toast.makeText(CompassActivity.this, msg,Toast.LENGTH_SHORT).show();
                // 计算两次返回键按下的时间差
                exitTime = System.currentTimeMillis();
            } else {
                // 关闭应用程序
                finish();
                // 返回桌面操作
                // Intent home = new Intent(Intent.ACTION_MAIN);
                // home.addCategory(Intent.CATEGORY_HOME);
                // startActivity(home);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}

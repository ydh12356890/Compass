package com.example.administrator.myapplication1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Logining extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView textView;

    long exitTime=0;
    //设置延迟时间
    private final int SKIP_DELAY_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_logining);
        progressBar = findViewById(R.id.id_progress);
        Timer time = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //跳转界面
                Intent intent = new Intent(Logining.this,CompassActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        };
        time.schedule(task,SKIP_DELAY_TIME);


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
                //String msg1 = "再按一次返回键回到桌面";
                Toast.makeText(Logining.this, msg,Toast.LENGTH_SHORT).show();
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

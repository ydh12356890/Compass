package com.example.administrator.myapplication1;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.SyncStatusObserver;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.InputType;
        import android.util.Log;
        import android.widget.Button;
        import android.view.*;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.Toast;

        import java.sql.Time;
        import java.util.Timer;
        import java.util.TimerTask;

public class MainActivity extends AppCompatActivity  {
    Button loginBtn;
    Button registerBtn;
    EditText etusername;
    EditText etpassword;
    CheckBox CBRemember;
    CheckBox CBAutoLogin;
    long exitTime=0;
    private String username;
    private String password;
    SharedPreferences sp;
    String spusername;
    String sppassword;
    ImageView iv_see_password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        sp = this.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        initView();
    }
    private void initView(){
        etusername = findViewById(R.id.edit1);
        etpassword = findViewById(R.id.edit2);
        CBRemember = findViewById(R.id.id_checkbox1);
        CBAutoLogin = findViewById(R.id.id_checkbox2);
        loginBtn = findViewById(R.id.btnid_login);
        registerBtn = findViewById(R.id.btnid_register);
        iv_see_password = findViewById(R.id.iv_see_password);
        spusername = sp.getString("USERNAME","");
        sppassword = sp.getString("PASSWORD","");

        //判断记住密码多选框的状态
        if(sp.getBoolean("ISCHECK",false)){
            //设置默认是记住密码状态
            CBRemember.setChecked(true);
            etusername.setText(spusername);
            etpassword.setText(sppassword);
            //判断自动登录多选框状态
            if(sp.getBoolean("AUTO_ISCHECK",false)){
                //设置默认是自动登录状态
                CBAutoLogin.setChecked(true);
                //跳转界面
                Intent intent  = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(MainActivity.this, Logining.class);
                startActivity(intent);
            }
        }
        iv_see_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPasswordVisibility();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 username = etusername.getText().toString();
                 password = etpassword.getText().toString();
                if(username.equals("")){
                    Toast.makeText(MainActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                }else if(password.equals("")){
                    Toast.makeText(MainActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                } else if(username.equals(spusername)&&password.equals(sppassword)) {
                    //登录成功和记住密码框为选中状态才保存用户信息
                    if(CBRemember.isChecked()){
                        //记住用户名和密码
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("USERNAME",username);
                        editor.putString("PASSWORD",password);
                        editor.commit();
                    }
                    Intent intent = new Intent();
//                    intent.putExtra("username",username);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(MainActivity.this, Logining.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, ",用户名或密码错误，请重新登录！", Toast.LENGTH_LONG).show();
                }

            }
        });
        CBRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(CBRemember.isChecked()){
                    System.out.println("记住密码已选中");
                    sp.edit().putBoolean("ISCHECK",true).commit();
                }
                else{
                    System.out.println("记住密码没有选中");
                    sp.edit().putBoolean("ISCHECK",false).commit();
                }

            }
        });

        CBAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(CBAutoLogin.isChecked()){
                    System.out.println("自动登录已选中");
                    sp.edit().putBoolean("AUTO_ISCHECK",true).commit();
                }else {
                    System.out.println("自动登录没有选中");
                    sp.edit().putBoolean("AUTO_ISCHECK",false).commit();
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


    }

    private void setPasswordVisibility() {
        if (iv_see_password.isSelected()) {
            iv_see_password.setSelected(false);
            //密码不可见
            etpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            iv_see_password.setSelected(true);
            //密码可见
            etpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

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
                Toast.makeText(MainActivity.this, msg,Toast.LENGTH_SHORT).show();
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


  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myOpenHelper = new MyOpenHelper(this,"mydb.db",null,1);
        initView();
    }
    private void initView(){
        etusername = findViewById(R.id.edit1);
        etpassword = findViewById(R.id.edit2);
        CBRemember = findViewById(R.id.id_checkbox1);
        CBAutoLogin = findViewById(R.id.id_checkbox2);
        loginBtn = findViewById(R.id.btnid_login);
        registerBtn = findViewById(R.id.btnid_register);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etusername.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                }else if(etpassword.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                } else if(login(etusername.getText().toString(),etpassword.getText().toString())) {
                    Toast.makeText(MainActivity.this, "登陆成功！", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.putExtra("username",etusername.getText().toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(MainActivity.this, CompassActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                }

            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


    }
    public boolean login(String username,String password){

        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        String sql="select * from userlist where username = '"+ username +"'and password ='" + password +"'";
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.getCount()>0){ //用户名和密码匹配
            cursor.close();
            return true;
        }
        return false;
    }*/

}

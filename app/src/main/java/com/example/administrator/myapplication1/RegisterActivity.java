package com.example.administrator.myapplication1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    Button registerbtn;
    Button backtologinbtn;
    EditText RETusername;
    EditText RETpassword;
    EditText RETSpassword;
    long exitTime=0;
    private String username;
    private  String password;
    private  String password2;
    SharedPreferences sp;
    ImageView iv_see_password;
    ImageView iv_see_password2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        sp = this.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        initView();
    }
    private void initView(){
        registerbtn = findViewById(R.id.btnid_registerui);
        backtologinbtn = findViewById(R.id.btnid_backtologin);
        RETusername = findViewById(R.id.edit1_r);
        RETpassword = findViewById(R.id.edit2_r);
        iv_see_password = findViewById(R.id.iv_see_password);
        iv_see_password2 = findViewById(R.id.iv_see_password2);
        iv_see_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPasswordVisibility();
            }
        });
        iv_see_password2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPasswordVisibility2();
            }
        });

        RETSpassword = findViewById(R.id.edit3_r);
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = RETusername.getText().toString();
                password = RETpassword.getText().toString();
                password2 = RETSpassword.getText().toString();
                if(username.equals("")) {
                    Toast.makeText(RegisterActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                }else if(password.equals("")){
                    Toast.makeText(RegisterActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                }else if(password2.equals("")){
                    Toast.makeText(RegisterActivity.this, "确认密码不能为空！", Toast.LENGTH_SHORT).show();
                }else if(!password.equals(password2)) {
                    Toast.makeText(RegisterActivity.this,"两次输入的密码不一致，请重新输入！",Toast.LENGTH_LONG).show();
                }else {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("USERNAME",username);
                    editor.putString("PASSWORD",password);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_LONG).show();
                }

            }
        });
        backtologinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }
    private void setPasswordVisibility() {
        if (iv_see_password.isSelected()) {
            iv_see_password.setSelected(false);
            //密码不可见
            RETpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            iv_see_password.setSelected(true);
            //密码可见
            RETpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

    }
    private void setPasswordVisibility2() {
        if (iv_see_password2.isSelected()) {
            iv_see_password2.setSelected(false);
            //密码不可见
            RETSpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            iv_see_password2.setSelected(true);
            //密码可见
            RETSpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
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
                Toast.makeText(RegisterActivity.this, msg,Toast.LENGTH_SHORT).show();
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

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myOpenHelper = new MyOpenHelper(this,"mydb.db",null,1);
        initView();
    }
    private void initView(){
        registerbtn = findViewById(R.id.btnid_registerui);
        backtologinbtn = findViewById(R.id.btnid_backtologin);
        RETusername = findViewById(R.id.edit1_r);
        RETpassword = findViewById(R.id.edit2_r);
        RETSpassword = findViewById(R.id.edit3_r);
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RETusername.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                }else if(RETpassword.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                }else if(RETSpassword.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "确认密码不能为空！", Toast.LENGTH_SHORT).show();
                }else  if(passwordCorrect()) {
                    if (CheckUserExists(RETusername.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "该用户已存在，请重新选择用户名！", Toast.LENGTH_LONG).show();
                    } else {
                        insertData(RETusername.getText().toString(), RETpassword.getText().toString());
                        Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this,"两次输入的密码不一致，请重新输入！",Toast.LENGTH_LONG).show();
                }

            }
        });
        backtologinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }
    //检查用户是否存在
    public boolean CheckUserExists(String username){
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        String query = "select * from userlist where username='"+username+"'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }
        return false;
    }
    //插入新用户
    public void insertData(String username,String password){
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username",username);
        values.put("password",password);
        db.insert("userlist",null,values);
        db.close();



    }
    public boolean passwordCorrect(){
        if(RETpassword.getText().toString().equals(RETSpassword.getText().toString())){
            return true;
        }
        return false;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }*/

}

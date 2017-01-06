package com.example.kirill.placelocator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kirill.placelocator.APIUtil.MD5Crypt;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String PREFERENCES_AUTHORISATION = "authorisation";
    public static final String PREFERENCE_LOGIN = "login";
    public static final String PREFERENCE_HASH = "hash";
    private Button buttonEnter;
    private TextView textViewLogin;
    private TextView textViewPassword;
    private TextView textViewSignIn;
    private TextView textViewFail;
    private SharedPreferences preferences;

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.arg1 == 1)
                Toast.makeText(MainActivity.this,"Incorrect login/password", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.initComponents();
    }

    private void initComponents() {
        this.preferences = getSharedPreferences(PREFERENCES_AUTHORISATION, Context.MODE_PRIVATE);
        this.initButtonEnter();
        this.initTextViews();
    }

    private void initTextViews() {
        this.textViewLogin = (TextView) this.findViewById(R.id.content_authorisation_login);
        this.textViewPassword = (TextView) this.findViewById(R.id.content_authorisation_password);
        this.textViewSignIn = (TextView) this.findViewById(R.id.content_authorisation_sign_in);
        this.textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
        this.textViewFail = (TextView) this.findViewById(R.id.content_authorisation_fail);
    }

    private void initButtonEnter() {
        this.buttonEnter = (Button) this.findViewById(R.id.content_authorisation_button_enter);
        this.buttonEnter.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit...");
        builder.setMessage("Do you really want to quit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    @Override
    public void onClick(View view) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String login = null;
                HttpURLConnection conn = null;
                try {
                    Log.i("chat", "+ FoneService --------------- ОТКРОЕМ СОЕДИНЕНИЕ");

                    login  = textViewLogin.getText().toString();
                    String password  = textViewPassword.getText().toString();
                    password = MD5Crypt.md5(password);

                    String server_name = "http://vhost8260.cpsite.ru";
                    String lnk = server_name + "/?action=auth&login=" + login + "&password=" + password;
                    conn = (HttpURLConnection) new URL(lnk)
                            .openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                    conn.setDoInput(true);
                    conn.connect();

                } catch (Exception e) {
                    Log.i("chat", "+ FoneService ошибка: " + e.getMessage());
                }

                // получаем ответ ---------------------------------->
                try {
                    InputStream is = conn.getInputStream();
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(is, "UTF-8"));

                    String ans = br.readLine();
                    String anHash = br.readLine();

                    Log.i("chat", "+ FoneService - полный ответ сервера:\n"
                            + ans + anHash);
                    SharedPreferences.Editor editor = preferences.edit();
                    if(ans != null && ans.contains("true")) {
                        Intent intent = new Intent(MainActivity.this, StartActivity.class);
                        startActivity(intent);
                        editor.putString(PREFERENCE_LOGIN, login);
                        editor.putString(PREFERENCE_HASH, ans.split("\\+")[1]);
                        editor.apply();
                        finish();
                    }
                    else{
                        Message msg = handler.obtainMessage();
                        msg.arg1 = 1;
                        handler.sendMessage(msg);
                    }

                    is.close(); // закроем поток
                    br.close(); // закроем буфер

                } catch (Exception e) {
                    Log.i("chat", "+ FoneService ошибка: " + e.getMessage());
                } finally {
                    conn.disconnect();
                    Log.i("chat", "+ FoneService --------------- ЗАКРОЕМ СОЕДИНЕНИЕ");
                }
            }
        });
        thread.start();
    }
}

package com.example.kirill.placelocator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kirill.placelocator.APIUtil.MD5Crypt;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView textViewLogin;
    private TextView textViewPassword;
    private Button buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initComponents();
    }

    private void initComponents() {
        initToolbar();
        initTextView();
        this.buttonSignIn  = (Button) this.findViewById(R.id.content_authorisation_button_sign_in);
        this.buttonSignIn.setOnClickListener(this);
    }

    private void initTextView() {
        this.textViewLogin = (TextView) this.findViewById(R.id.content_sign_in_login);
        this.textViewPassword = (TextView) this.findViewById(R.id.content_sign_in_password);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View view) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    Log.i("chat", "+ FoneService --------------- ОТКРОЕМ СОЕДИНЕНИЕ");

                    String login  = textViewLogin.getText().toString();
                    String password  = textViewPassword.getText().toString();
                    password = MD5Crypt.md5(password);

                    String server_name = "http://vhost8260.cpsite.ru";
                    String lnk = server_name + "/?action=reg&login=" + login + "&password=" + password;
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
                    if(ans.contains("true")) {
                        finish();
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

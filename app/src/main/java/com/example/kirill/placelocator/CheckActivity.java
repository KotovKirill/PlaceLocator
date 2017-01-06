package com.example.kirill.placelocator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.kirill.placelocator.APIUtil.Network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!Network.hasConnection(this)){
            Toast.makeText(this, "Network is not available", Toast.LENGTH_SHORT).show();
            return;
        }
        final SharedPreferences preferences = getSharedPreferences(MainActivity.PREFERENCES_AUTHORISATION, Context.MODE_PRIVATE);
        if(preferences.contains(MainActivity.PREFERENCE_HASH)){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection conn = null;
                    try {
                        Log.i("chat", "+ FoneService --------------- ОТКРОЕМ СОЕДИНЕНИЕ");

                        String login  = preferences.getString(MainActivity.PREFERENCE_LOGIN, "");
                        String hash_str = preferences.getString(MainActivity.PREFERENCE_HASH, "");
                        Log.i("chat", hash_str);
                        String server_name = "http://vhost8260.cpsite.ru";
                        String lnk = server_name + "/?action=auth_hash&login=" + login + "&hash_str=" + hash_str;
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
                            Intent intent = new Intent(CheckActivity.this, StartActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Intent intent = new Intent(CheckActivity.this, MainActivity.class);
                            startActivity(intent);
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
        else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

package com.example.kirill.placelocator.SettingsActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kirill.placelocator.CheckActivity;
import com.example.kirill.placelocator.MainActivity;
import com.example.kirill.placelocator.R;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences mSettings;
    private TextView textViewUserName;
    private TextView textViewUserSecName;
    private CircleImageView imageViewAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initComponents();
    }

    private void initComponents() {
        initToolbar();
        this.mSettings = getSharedPreferences(EditAccountSettingsActivity.ACCOUNT_PREFERENCES,
                Context.MODE_PRIVATE);
        this.initTextView();
        this.imageViewAvatar = (CircleImageView) this.findViewById(R.id.profile_image);
    }

    private void initTextView() {
        this.textViewUserName = (TextView) this.findViewById(R.id.content_settings_edit_text_user_name);
        this.textViewUserSecName = (TextView) this.findViewById(R.id.content_settings_edit_text_user_sec_name);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void settingsRefresh(String fileName){
        SharedPreferences preferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public void buttonsListener(View view) {
        int id = view.getId(); //Опредеяем идентификатор кнопки нажатой в SettingsActivity
        Intent intent;
        switch(id){
            /*case R.id.content_settings_button_common:
                intent = new Intent(SettingsActivity.this, CommonSettingsActivity.class);
                startActivity(intent);
                break;*/
            case R.id.content_settings_edit_information:
                intent = new Intent(SettingsActivity.this, EditAccountSettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.content_settings_button_account:
                intent = new Intent(SettingsActivity.this, EditAccountSettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.content_settings_button_reset:
                settingsRefresh(EditAccountSettingsActivity.ACCOUNT_PREFERENCES);
                Toast.makeText(this, "Settings reset", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.content_settings_button_about_us:
                intent = new Intent(SettingsActivity.this,AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.content_settings_button_contact_author:
                ShareCompat.IntentBuilder.from(SettingsActivity.this).setType("message/rfc822")
                        .addEmailTo(getString(R.string.main_activity_navigation_item_feedback_email))
                        .setSubject(getString(R.string.app_name))
                        .setText(getString(R.string.main_activity_navigation_item_feedback_message))
                        .setChooserTitle(getString(R.string.main_activity_navigation_item_feedback_title))
                        .startChooser();
                break;
            case R.id.content_settings_exit:
                buttonClickExit();
                break;
        }
    }

    private void buttonClickExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.content_settings_alert_dialog_title);
        builder.setMessage(R.string.content_settings_alert_dialog_message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences preferences = getSharedPreferences(MainActivity.PREFERENCES_AUTHORISATION, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(SettingsActivity.this, CheckActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSettings.contains(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_NAME)
                && mSettings.contains(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_SEC_NAME)) {
            this.textViewUserName.setText(mSettings.getString(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_NAME, "Не выбрано"));
            this.textViewUserSecName.setText(mSettings.getString(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_SEC_NAME, "Не выбрано"));
        }
        if(mSettings.contains(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_AVATAR)){
            try {
                this.imageViewAvatar.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(),
                        Uri.parse(mSettings.getString(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_AVATAR, ""))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

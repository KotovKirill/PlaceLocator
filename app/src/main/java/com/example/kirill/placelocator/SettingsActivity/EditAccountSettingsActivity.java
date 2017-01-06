package com.example.kirill.placelocator.SettingsActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.kirill.placelocator.R;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditAccountSettingsActivity extends AppCompatActivity {
    public static final String ACCOUNT_PREFERENCES = "account";
    public static final String ACCOUNT_PREFERENCES_USER_NAME = "username";
    public static final String ACCOUNT_PREFERENCES_USER_SEC_NAME = "usersecname";
    public static final String ACCOUNT_PREFERENCES_USER_POL = "pol";
    public static final String ACCOUNT_PREFERENCES_ADDRESS = "address";
    public static final String ACCOUNT_PREFERENCES_EMAIL = "email";
    public static final String ACCOUNT_PREFERENCES_DATE_BORN_YEAR = "datebornyear";
    public static final String ACCOUNT_PREFERENCES_DATE_BORN_MONTH = "datebornmonth";
    public static final String ACCOUNT_PREFERENCES_DATE_BORN_DAY = "datebornday";
    public static final String ACCOUNT_PREFERENCES_USER_AVATAR = "useravatar";
    private static final int REQUEST_AVATAR = 1;

    private EditText textViewUserName;
    private EditText textViewUserSecName;
    private TextView textViewDateBorn;
    private TextView textViewAddress;
    private TextView textViewEMail;
    private RadioGroup radioGroupUserPol;
    private CircleImageView viewCircleImage;

    private SharedPreferences mSettings;
    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account_settings);
        initComponents();
    }

    private void initComponents() {
        this.mSettings = getSharedPreferences(ACCOUNT_PREFERENCES, Context.MODE_PRIVATE);
        initToolbar();
        viewCircleImage = (CircleImageView) this.findViewById(R.id.profile_image);
        viewCircleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_AVATAR);
            }
        });
        initInputForms();
    }

    private void initInputForms() {
        initEditText();
        textViewAddress = (TextView) this.findViewById(R.id.text_view_settings_address);
        textViewEMail = (TextView) this.findViewById(R.id.text_view_settings_mail);
        textViewAddress.setText(mSettings.getString(ACCOUNT_PREFERENCES_ADDRESS,
                getResources().getString(R.string.content_settings_text_no_changed)));
        textViewEMail.setText(mSettings.getString(ACCOUNT_PREFERENCES_EMAIL,
                getResources().getString(R.string.content_settings_text_no_changed)));
        radioGroupUserPol = (RadioGroup) findViewById(R.id.content_settings_radio_group);
        radioGroupUserPol.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putInt(ACCOUNT_PREFERENCES_USER_POL, checkedId);
                editor.apply();
            }
        });
        textViewUserName.setText(mSettings.getString(ACCOUNT_PREFERENCES_USER_NAME, ""));
        textViewUserSecName.setText(mSettings.getString(ACCOUNT_PREFERENCES_USER_SEC_NAME, ""));
        mYear = mSettings.getInt(ACCOUNT_PREFERENCES_DATE_BORN_YEAR, 1989);
        mMonth = mSettings.getInt(ACCOUNT_PREFERENCES_DATE_BORN_MONTH, 8);
        mDay = mSettings.getInt(ACCOUNT_PREFERENCES_DATE_BORN_DAY, 26);
        textViewDateBorn = (TextView) this.findViewById(R.id.text_view_settings_date_born);
        textViewDateBorn.setText(String.format("%d.%d.%d", mDay, mMonth, mYear));
        if(mSettings.contains(ACCOUNT_PREFERENCES_USER_POL))
            radioGroupUserPol.check(mSettings.getInt(ACCOUNT_PREFERENCES_USER_POL, 0));
        if(mSettings.contains(ACCOUNT_PREFERENCES_USER_AVATAR)) {
            try {
                Uri uri = Uri.parse(mSettings.getString(ACCOUNT_PREFERENCES_USER_AVATAR,""));
                viewCircleImage.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initEditText() {
        textViewUserName = (EditText) this.findViewById(R.id.content_settings_edit_text_user_name);
        textViewUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(ACCOUNT_PREFERENCES_USER_NAME,
                        textViewUserName.getText().toString());
                editor.apply();
            }
        });
        textViewUserSecName = (EditText) this.findViewById(R.id.content_settings_edit_text_user_secname);
        textViewUserSecName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(ACCOUNT_PREFERENCES_USER_SEC_NAME,
                        textViewUserSecName.getText().toString());
                editor.apply();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void LinearLayoutListener(View view) {
        int id = view.getId();
        if(id == R.id.content_settings_ll_date_born) {
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear + 1;
                    mDay = dayOfMonth;
                    textViewDateBorn.setText(String.format("%d.%d.%d", mDay, mMonth, mYear));
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putInt(ACCOUNT_PREFERENCES_DATE_BORN_YEAR, mYear);
                    editor.putInt(ACCOUNT_PREFERENCES_DATE_BORN_MONTH, mMonth);
                    editor.putInt(ACCOUNT_PREFERENCES_DATE_BORN_DAY, mDay);
                    editor.apply();
                }
            }, mYear, mMonth-1, mDay);
            dialog.show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int title = 0;
        int message = 0;
        switch (id){
            case R.id.content_settings_ll_address:
                title = R.string.content_settings_text_address;
                message = R.string.content_settings_text_input_address;
                break;
            case R.id.content_settings_ll_mail:
                title = R.string.content_settings_button_mail;
                message = R.string.content_settings_text_input_mail;
                break;
        }
        builder.setTitle(title);
        builder.setMessage(message);
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.content_settings_alert_dialog_layout, null);
        builder.setView(linearLayout);
        final TextView textView = (TextView) linearLayout
                .findViewById(R.id.content_settings_alert_dialog_text_view);
        final TextView textViewIndicator = (TextView) linearLayout
                .findViewById(R.id.content_settings_alert_dialog_text_indicator);
        textViewIndicator.setText(id + "");
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int idLl = Integer.valueOf(textViewIndicator.getText().toString());
                SharedPreferences.Editor editor = mSettings.edit();
                String str = textView.getText().toString();
                if(str.equals(""))
                    return;
                switch (idLl){
                    case R.id.content_settings_ll_address:
                        textViewAddress.setText(str);
                        editor.putString(ACCOUNT_PREFERENCES_ADDRESS, textViewAddress.getText().toString());
                        break;
                    case R.id.content_settings_ll_mail:
                        textViewEMail.setText(str);
                        editor.putString(ACCOUNT_PREFERENCES_EMAIL, textViewEMail.getText().toString());
                        break;
                }
                editor.apply();
            }
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
            if(requestCode == REQUEST_AVATAR){
                Uri uri = data.getData();
                SharedPreferences.Editor editor = mSettings.edit();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    viewCircleImage.setImageBitmap(bitmap);
                    editor.putString(ACCOUNT_PREFERENCES_USER_AVATAR, uri.toString());
                    editor.apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}

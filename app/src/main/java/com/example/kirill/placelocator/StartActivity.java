package com.example.kirill.placelocator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kirill.placelocator.LocationActivity.LocationActivity;
import com.example.kirill.placelocator.SettingsActivity.AboutUsActivity;
import com.example.kirill.placelocator.SettingsActivity.EditAccountSettingsActivity;
import com.example.kirill.placelocator.SettingsActivity.HelpActivity;
import com.example.kirill.placelocator.SettingsActivity.SettingsActivity;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class StartActivity extends AppCompatActivity{
    private Toolbar toolbar; //Поле характеризующее тулбар(панель хранящая заголовок, тогл, меню)
    private DrawerLayout drawerLayout; //Поле характеризующее ящик
    private Button buttonPlay; //Поле характеризующее баттон
    private View headerNavigationView;
    private CircleImageView imageViewAvatar;
    private TextView textViewUserSecName;
    private TextView textViewUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start); //Метод связи activity_main.xml с кодом Java
        initComponents(); //Метод инициализации компонентов
    }

    private void initComponents() {
        initToolbar(); //Метод инициализации Тулбара
        initNavigationView();//Инициализация меню навигации
        initButtonPlay();//Инициализация кнопки play
    }

    private void initButtonPlay() {
        /*
        Метод findViewById осуществляет поиск эллемента по id
        Возвращает общий объект View, поэтому необходимо произвести операцию приведения типов
        Тем самым прировняв к переменной кнопки объект кнопку
         */
        buttonPlay = (Button) findViewById(R.id.content_main_button_play);
        /*
        Метод setOnClickListener устанавливает для данной кнопки
        обработчик события нажатия на эту кнопку.
        В этот метод мы передаем анонимный объект анонимного внутреннего класса
         */
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Заводим объект Intent. Он предназначается для того,
                чтобы запустить новое Activity. В его конструктор
                мы передаем контекст- MainActivity.this и класс того
                Activity, которое зотим запустить.
                 */
                Intent intent = new Intent(StartActivity.this,LocationActivity.class);
                startActivity(intent); //Метод запуска Activity по объекту intent
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);//Нашли тулбар по id
        setSupportActionBar(toolbar);//Установили програмно найденный тулбар в качестве основного.
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*
        Завели объект ActionBarDrawerToggle (три полосы для выдвижения меню навигации)
        В конструктор передаем контекст- данную activity, drawerLayout, Toolbar, строку открыть,
        строку закрыть.
         */
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.action_bar_drawer_toggle_open,R.string.action_bar_drawer_toggle_close) ;
        /*
        добавление для DrawerLayout обработчика выдвижения
        ящика (три полоски начинаю вращаться по часовой стрелке при выдвижения ящика)
        */
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState(); // синхронизация с NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        /*
        Установка обработчика нажатия на эллемент NavigationView
        В методе OnNavigationItemSelected в зависимости от того,
        какой пункт меню был выбран (определяется по идентификатору пункта меню)
        выполняется то или иное действие.

        Установка обработчика осуществояется в том случае, если метод FindViewById
        смог найти NavigationView по id и соответсыенно NavigationView не равно null
         */
        if (navigationView != null) {
            headerNavigationView = navigationView.getHeaderView(0);
            imageViewAvatar = (CircleImageView) headerNavigationView.findViewById(R.id.profile_image);
            textViewUserSecName = (TextView) headerNavigationView.findViewById(R.id.content_settings_edit_text_user_sec_name);
            textViewUserName = (TextView) headerNavigationView.findViewById(R.id.content_settings_edit_text_user_name);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) { //обрабатывающий метод (выполняется тогда, когда пользователь выбрал эллемент меню)
                    drawerLayout.closeDrawers(); // закрывает полку
                    int id = item.getItemId();
                    switch (id){
                        case R.id.sub_menu_navigation_view_about_us:
                            Intent intent = new Intent(StartActivity.this,AboutUsActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.menu_navigation_view_help:
                            Intent intent2 = new Intent(StartActivity.this, HelpActivity.class);
                            startActivity(intent2);
                            break;
                        case R.id.menu_navigation_view_feedback:
                            /*
                            Запуск activity для отправки mail
                             */
                            ShareCompat.IntentBuilder.from(StartActivity.this).setType("message/rfc822")
                                    .addEmailTo(getString(R.string.main_activity_navigation_item_feedback_email))
                                    .setSubject(getString(R.string.app_name))
                                    .setText(getString(R.string.main_activity_navigation_item_feedback_message))
                                    .setChooserTitle(getString(R.string.main_activity_navigation_item_feedback_title))
                                    .startChooser();
                            break;
                        case R.id.menu_navigation_view_settings:
                            Intent intent1 = new Intent(StartActivity.this, SettingsActivity.class);
                            startActivity(intent1);
                            break;
                    }
                    return true;
                }
            });
        }
    }

    /*
    Привязка menu_main.xml к коду
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
    Обработка пунктов меню в зависимости от id
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    protected void onResume() {
        super.onResume();
        if (headerNavigationView != null) {
            SharedPreferences mSettings = getSharedPreferences(EditAccountSettingsActivity.ACCOUNT_PREFERENCES, Context.MODE_PRIVATE);
            if (mSettings.contains(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_NAME)
                    && mSettings.contains(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_SEC_NAME)) {
                textViewUserName.setText(mSettings.getString(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_NAME, "Не выбрано"));
                textViewUserSecName.setText(mSettings.getString(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_SEC_NAME, "Не выбрано"));
            }
            if (mSettings.contains(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_AVATAR)) {
                try {
                    imageViewAvatar.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(),
                            Uri.parse(mSettings.getString(EditAccountSettingsActivity.ACCOUNT_PREFERENCES_USER_AVATAR, ""))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
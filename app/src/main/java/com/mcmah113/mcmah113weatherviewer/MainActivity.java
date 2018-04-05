package com.mcmah113.mcmah113weatherviewer;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements SettingsDialog.OnCompleteListener {
    private final String unitTemperatureList[] = {"Celsius", "Fahrenheit", "Kelvin"};
    private String temperatureUnit = unitTemperatureList[0];
    private String timeUnit = "12h";

    private static AssetManager assetManager;
    private Toolbar toolbarCustom;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assetManager = getAssets();

        toolbarCustom = findViewById(R.id.toolbarCustom);
        setSupportActionBar(toolbarCustom);

        final ListView listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(view);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged (AbsListView view, int scrollState) {
                hideKeyboard(view);
            }

            public void onScroll (AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //hideKeyboard(view);
            }
        });

        final EditText editTextLocation = findViewById(R.id.editTextLocation);
        editTextLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                hideKeyboard(view);
                weatherSearch(editTextLocation.getText().toString(), listView);
                return true;
            }
        });
        editTextLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });

        final FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                hideKeyboard(view);
                weatherSearch(editTextLocation.getText().toString(), listView);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_extras, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        hideKeyboard(toolbarCustom);
        switch (item.getItemId()) {
            case R.id.menuExit:
                Intent myIntent = new Intent(Intent.ACTION_MAIN);
                myIntent.addCategory(Intent.CATEGORY_HOME);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);
                System.exit(0);
                return true;
            case R.id.menuSettings:
                Bundle bundle = new Bundle();
                bundle.putStringArray("unitTemperatureList", unitTemperatureList);
                bundle.putString("currentUnit", temperatureUnit);
                bundle.putString("currentTime", timeUnit);

                DialogFragment settingsDialog = new SettingsDialog();

                settingsDialog.setArguments(bundle);
                settingsDialog.show(getFragmentManager(), "Settings Dialog");
                return true;
            default:
                return false;
        }
    }

    public void onComplete(Bundle bundle) {
        temperatureUnit = bundle.getString("currentUnit");
        timeUnit = bundle.getString("currentTime");
        Toast.makeText(this, "Temp: " + temperatureUnit + " Time: " + timeUnit, Toast.LENGTH_SHORT).show();
    }

    public void weatherSearch(String cityLocation, ListView listView) {
        WeatherData[] weatherData = null;
        try {
            weatherData = new OpenWeather().execute(cityLocation, temperatureUnit, timeUnit).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateListView(weatherData, listView);
    }

    public void updateListView(WeatherData[] weatherData, ListView listView) {
        if(weatherData != null) {
            //parse weather data array
            final WeatherDataAdapter adapter = new WeatherDataAdapter(this, Arrays.asList(weatherData));
            listView.setAdapter(adapter);
        }
        else {
            Toast.makeText(MainActivity.this, "Could not find any results", Toast.LENGTH_SHORT).show();
            listView.setAdapter(null);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if(inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static Bitmap getImage(String weatherDescription, int time24) {
        try {
            InputStream inputStream;
            Bitmap weatherIcon;

            if(6 <= time24 && time24 <= 18) {
                //day
                switch (weatherDescription) {
                    case "light rain":
                        inputStream = assetManager.open("white/chancerain.png");
                    break;
                    case "few clouds":
                        inputStream = assetManager.open("white/partlycloudy.png");
                    break;
                    case "scattered clouds":
                        inputStream = assetManager.open("white/partlycloudy.png");
                    break;
                    case "overcast clouds":
                        inputStream = assetManager.open("white/cloudy.png");
                    break;
                    case "clear sky":
                        inputStream = assetManager.open("white/sunny.png");
                    break;
                    case "moderate rain":
                        inputStream = assetManager.open("white/rain.png");
                    break;
                    case "broken clouds":
                        inputStream = assetManager.open("white/partlycloudy.png");
                    break;
                    case "light snow":
                        inputStream = assetManager.open("white/chancesnow.png");
                    break;
                    default:
                        inputStream = assetManager.open("white/unknown.png");
                }
            }
            else {
                //night
                switch (weatherDescription) {
                    case "light rain":
                        inputStream = assetManager.open("white/nt_chancerain.png");
                        break;
                    case "few clouds":
                        inputStream = assetManager.open("white/nt_cloudy.png");
                        break;
                    case "scattered clouds":
                        inputStream = assetManager.open("white/nt_partlycloudy.png");
                        break;
                    case "overcast clouds":
                        inputStream = assetManager.open("white/nt_cloudy.png");
                    break;
                    case "clear sky":
                        inputStream = assetManager.open("white/nt_clear.png");
                        break;
                    case "moderate rain":
                        inputStream = assetManager.open("white/nt_rain.png");
                        break;
                    case "broken clouds":
                        inputStream = assetManager.open("white/nt_partlycloudy.png");
                        break;
                    case "light snow":
                        inputStream = assetManager.open("white/nt_chancesnow.png");
                        break;
                    default:
                        inputStream = assetManager.open("white/nt_unknown.png");
                }
            }

            weatherIcon = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            return weatherIcon;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

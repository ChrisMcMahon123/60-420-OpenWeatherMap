package com.mcmah113.mcmah113weatherviewer;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenWeather extends AsyncTask<String, Void, WeatherData[]> {
    @SuppressWarnings("All")
    public OpenWeather() {

    }

    protected WeatherData[] doInBackground(String... params) {
        String httpURL = "http://api.openweathermap.org/data/2.5/forecast?";
        final String apiKey = "APPID=9988a22e8bf24c3fe0751c9d68ccb696";

        String cityName = params[0];
        String temperatureUnit = params[1];
        String timeFlag = params[2];

        httpURL += apiKey + "&q=" + cityName + getUnitURL(temperatureUnit);

        try {
            URL url = new URL(httpURL);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String inputLine;
            StringBuilder data = new StringBuilder();

            while((inputLine = bufferedReader.readLine()) != null) {
                data.append(inputLine).append("\n");
            }

            bufferedReader.close();
            inputStreamReader.close();
            urlConnection.disconnect();

            Log.d("HTTP REQUEST","SUCCESSFULLY COMMUNICATED WITH SERVER");

            return getResult(data.toString(), temperatureUnit, timeFlag);
        }
        catch(Exception e) {
            Log.d("HTTP REQUEST","FAILED TO RETRIEVE DATA FROM OPEN WEATHER");
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(WeatherData result[]) {

    }

    @SuppressWarnings("All")
    public WeatherData[] getResult(String dataJSON, String temperatureUnit, String timeFlag) {
        if(dataJSON != null) {
            Log.d("HTTP REQUEST", dataJSON);
            Log.d("HTTP REQUEST", "SUCCESSFULLY RETRIEVED DATA FROM OPEN WEATHER ");

            try {
                JSONArray jsonRootArray = new JSONObject(dataJSON).getJSONArray("list");
                JSONObject jsonObject;
                JSONObject jsonObjectMain;
                JSONArray jsonArrayWeather;

                WeatherData weatherData[] = new WeatherData[jsonRootArray.length()];

                for(int i = 0; i < jsonRootArray.length(); i ++) {
                    jsonObject = jsonRootArray.getJSONObject(i);
                    jsonObjectMain = jsonObject.getJSONObject("main");
                    jsonArrayWeather = jsonObject.getJSONArray("weather");

                    weatherData[i] = new WeatherData(
                                        jsonObject.getString("dt_txt"),
                                        jsonArrayWeather.getJSONObject(0).getString("description"),
                                        jsonObjectMain.getString("humidity"),
                                        jsonObjectMain.getString("temp"),
                                        jsonObjectMain.getString("temp_max"),
                                        jsonObjectMain.getString("temp_min"),
                                        temperatureUnit,
                                        timeFlag);
                }

                Log.d("JSON PARSE","SUCCESSFULLY PARSED JSON");

                return weatherData;
            }
            catch(Exception e) {
                Log.d("JSON PARSE","FAILED TO PARSE JSON");
                e.printStackTrace();
                return null;
            }
        }
        else {
            Log.d("GET RESULT","COULD NOT GET RESULT, VARIABLE IS <<NULL>>");
            return null;
        }
    }

    private String getUnitURL(String unit) {
        switch (unit) {
            case "Celsius":
                return "&units=metric";
            case "Fahrenheit":
                return "&units=imperial";
            case "Kelvin":
                return "";
            default:
                return "";
        }
    }
}

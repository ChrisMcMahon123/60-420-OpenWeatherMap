package com.mcmah113.mcmah113weatherviewer;

import android.graphics.Bitmap;
import java.util.StringTokenizer;

public class WeatherData {
    private boolean timeFlag;//12 = true, 24 = false
    private Bitmap weatherIcon;
    private String dateRaw;
    private String dateString;
    private String time12;
    private String time24;
    private String weatherDescription;
    private String humidity;
    private String temperature;
    private String temperatureHigh;
    private String temperatureLow;
    private String unit;

    private final String monthArray[][] = {
                            {"January","Jan"},
                            {"February","Feb"},
                            {"March","Mar"},
                            {"April","Apr"},
                            {"May","May"},
                            {"June","Jun"},
                            {"July","Jul"},
                            {"August","Aug"},
                            {"September","Sept"},
                            {"October","Oct"},
                            {"November","Nov"},
                            {"December","Dec"}
                        };

    @SuppressWarnings("All")
    public WeatherData(String date, String weatherDescription, String humidity, String temperature, String temperatureHigh, String temperatureLow, String unit, String timeUnit) {
        this.dateRaw = date;
        this.weatherDescription = weatherDescription;
        this.humidity = humidity;
        this.temperature = temperature;
        this.temperatureHigh = temperatureHigh;
        this.temperatureLow = temperatureLow;
        this.unit = unit;

        dateString = setDateString();
        time12 = setTime12();
        time24 = setTime24();
        timeFlag = setTimeUnit(timeUnit);
        weatherIcon = setWeatherIcon();
    }

    @SuppressWarnings("All")
    public Bitmap getWeatherIcon() { return weatherIcon; }

    @SuppressWarnings("All")
    public String getDateString() { return dateString; }

    @SuppressWarnings("All")
    public String getTime() {
        if(timeFlag) {
            return time12;
        }
        else {
            return time24  + "h";
        }
    }

    @SuppressWarnings("All")
    public String getWeatherDescription() { return weatherDescription; }

    @SuppressWarnings("All")
    public String getHumidity() { return humidity + "% Humidity"; }

    @SuppressWarnings("All")
    public String getTemperature() { return  temperature + getUnitSymbol(); }

    @SuppressWarnings("All")
    public String getTemperatureHigh() { return temperatureHigh + getUnitSymbol() + " High"; }

    @SuppressWarnings("All")
    public String getTemperatureLow() { return temperatureLow + getUnitSymbol() + " Low"; }

    private char getUnitSymbol() {
        switch (unit) {
            case "Celsius":
                return '\u2103';
            case "Fahrenheit":
                return '\u2109';
            case "Kelvin":
                return '\u212A';
            default:
                return ' ';
        }
    }
    //24h to 12h time
    private String setTime12() {
        String hour = dateRaw.substring(dateRaw.indexOf(' ')+1,dateRaw.indexOf(':'));

        int hourInt = Integer.parseInt(hour);

        if(hourInt == 0 || hourInt == 24) {
            return "12 am";
        }
        else if(hourInt > 12) {
            return Integer.toString(hourInt -12) + "pm";
        }
        else if(hourInt == 12) {
            return "12 pm";
        }
        else {
            return Integer.toString(hourInt) + "am";
        }
    }

    //clean up 24 hour time, removing minute and second, which are 0 anyways
    private String setTime24() {
        return dateRaw.substring(dateRaw.indexOf(' ')+1,dateRaw.indexOf(':'));
    }

    //change year-month-day to Short day year. Ex 2017-01-20 == Jan 20,2017
    private String setDateString() {
        StringTokenizer stringTokenizer = new StringTokenizer(dateRaw,"-");

        String year = stringTokenizer.nextToken();
        String month = stringTokenizer.nextToken();
        String day = stringTokenizer.nextToken();
        day = day.substring(0,day.indexOf(' '));

        return monthArray[Integer.parseInt(month) -1][1] + " " + day;
    }

    private Bitmap setWeatherIcon() {
        return MainActivity.getImage(weatherDescription, Integer.parseInt(time24));
    }

    private boolean setTimeUnit(String timeUnit) {
        switch (timeUnit) {
            case "12h":
                return true;
            case "24h":
                return false;
            default:
                return false;
        }
    }
}
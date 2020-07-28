package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityName ;
    TextView outputView;
    String temp;
    public void findWeather(View view){
        //Log.i("city Name",cityName.getText().toString());
        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        DownloadTask task = new DownloadTask();
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);
        //task.execute("api.openweathermap.org/data/2.5/weather?q=" + cityName.getText().toString());
        task.execute("https://samples.openweathermap.org/data/2.5/weather?q=" +cityName.getText().toString()+"&appid=439d4b804bc8187953eb36d2a8c26a02");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (EditText)findViewById(R.id.cityName);
        outputView = (TextView)findViewById(R.id.outputView);
    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                    char current = (char)data;
                    result += current;
                    data = reader.read();

                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");
                //Log.i("Website Content",weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);

                for (int i=0;i<arr.length();i++){

                    JSONObject tempJSON = arr.getJSONObject(i);
                    temp = "";

                    String main = tempJSON.getString("main");
                    String description = tempJSON.getString("description");
                    if(main!="" && description!=""){
                        temp += main + " : " + description+"\r\n";
                    }
                    Log.i("Temp Content",temp);

                }
                if(temp!=""){
                    outputView.setText(temp);
                    outputView.setVisibility(View.VISIBLE);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
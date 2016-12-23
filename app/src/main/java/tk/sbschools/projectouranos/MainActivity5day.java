package tk.sbschools.projectouranos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity5day extends AppCompatActivity {
    ArrayList<String> forecast,time;
    ArrayList<Integer> high,low,imageRes;
    ArrayList<Double> temp;
    ListView display;
    //ImageView background;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity5day);

        prefs = this.getSharedPreferences("tk.sbschools.projectouranos",Context.MODE_PRIVATE);
        editor = prefs.edit();

        forecast = new ArrayList<>();
        temp = new ArrayList<>();
        high = new ArrayList<>();
        low = new ArrayList<>();
        time = new ArrayList<>();
        imageRes = new ArrayList<>();

        /*background = (ImageView) findViewById(R.id.ImageView_background);
        if(prefs.getString("theme", "") != ""){
            if(prefs.getString("theme", "").equals("rwby")){
                switch((int)(Math.random()*8)+1){
                    case 1:background.setImageResource(R.drawable.rwby1);break;
                    case 2:background.setImageResource(R.drawable.rwby2);break;
                    case 3:background.setImageResource(R.drawable.rwby3);break;
                    case 4:background.setImageResource(R.drawable.rwby4);break;
                    case 5:background.setImageResource(R.drawable.rwby5);break;
                    case 6:background.setImageResource(R.drawable.rwby6);break;
                    case 7:background.setImageResource(R.drawable.rwby7);break;
                    case 8:background.setImageResource(R.drawable.rwby8);break;
                }
            }else{
                switch((int)(Math.random()*8)+1){
                    //case 1:background.setImageResource(R.drawable.code1);break;
                    case 2:background.setImageResource(R.drawable.code2);break;
                    case 3:background.setImageResource(R.drawable.code3);break;
                    //case 4:background.setImageResource(R.drawable.code4);break;
                    case 5:background.setImageResource(R.drawable.code5);break;
                    case 6:background.setImageResource(R.drawable.code6);break;
                    case 7:background.setImageResource(R.drawable.code7);break;
                    case 8:background.setImageResource(R.drawable.code8);break;
                    default:background.setImageResource(R.drawable.code7);break;
                }
            }
        }else{
            prefs.edit().putString("theme", "code").apply();;
        }*/



        WeatherThread retrieveWeatherData = new WeatherThread(this,forecast,time,high,low,imageRes,temp);
        retrieveWeatherData.execute("zip=08852,us");

    }

    public class WeatherThread extends AsyncTask<String, Void, JSONObject> {

        Context main;
        ArrayList<String> forecast, time;
        ArrayList<Integer>  high, low, imageRes;
        ArrayList<Double> temp;

        public WeatherThread( Context mainThread, ArrayList<String> fcast, ArrayList<String> time, ArrayList<Integer> high,
        ArrayList<Integer> low, ArrayList<Integer> imageRes, ArrayList<Double> temp) {
            this.main = mainThread;
            this.forecast = fcast;
            this.time = time;
            this.high = high;
            this.low = low;
            this.imageRes = imageRes;
            this.temp = temp;

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String inputString = params[0];
            JSONObject retrievedData = new JSONObject();
            try {
                //InputStream in = new java.net.URL("http://query.yahooapis.com/v1/public/yql?q=" + Uri.encode("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text='" + inputString + "')") + "&format=json").openStream();
                InputStream in = new java.net.URL("http://api.openweathermap.org/data/2.5/forecast/daily?" + inputString + "&appid=5db23354f636c5fb73b6cd20611a4c6f").openStream();
                BufferedReader webData = new BufferedReader(new InputStreamReader(in));
                String dataString = webData.readLine();
                retrievedData = new JSONObject(dataString);
            } catch (Exception e) {
                System.err.println("WeatherThread.doInBackground ERROR: Something up there dun fucked up");
                e.printStackTrace();
                //Toast.makeText(MainActivity.this, "Error in Retrieving Weather Data", Toast.LENGTH_SHORT).show();
            }
            return retrievedData;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            System.out.println("5Day" + result.toString());

            //super.onPostExecute(result);
            try {
                for (int i = 0; i < 5; i++) { //
                    JSONObject hourlyRes = result.getJSONArray("list").getJSONObject(i);
                    this.forecast.add(hourlyRes.getJSONArray("weather").getJSONObject(0).get("main").toString());
                    this.temp.add(((9 / 5) * ((double) (hourlyRes.getJSONObject("temp").get("day")) - 273.15) + 32));
                    this.high.add((int) ((9 / 5) * ((double) (hourlyRes.getJSONObject("temp").get("max")) - 273.15) + 32));
                    this.low.add((int) ((9 / 5) * ((double) (hourlyRes.getJSONObject("temp").get("min")) - 273.15) + 32));
                    this.time.add(hourlyRes.get("dt").toString());
                    switch ((int) hourlyRes.getJSONArray("weather").getJSONObject(0).get("id")) {
                        //ThunderStorm
                        case 200:case 201:case 202:case 210:case 211:case 212:case 221:case 230:case 231:case 232:
                            this.imageRes.add(R.drawable.storm);
                            break;
                        //Drizzle
                        case 300:
                        case 301:
                        case 302:
                        case 310:
                        case 311:
                        case 312:
                        case 313:
                        case 314:
                        case 321:
                            this.imageRes.add(R.drawable.rain_thin);
                            break;
                        //rain
                        case 500:
                        case 501:
                        case 502:
                        case 503:
                        case 504:
                            this.imageRes.add(R.drawable.rain);
                            break;
                        //FreezingRain
                        case 511:
                            this.imageRes.add(R.drawable.snow_thin);
                            break;
                        //Shower
                        case 520:
                        case 521:
                        case 522:
                        case 531:
                            this.imageRes.add(R.drawable.rain_thin);
                            break;
                        //Snow
                        case 600:
                        case 601:
                        case 602:
                        case 611:
                        case 612:
                        case 615:
                        case 616:
                        case 620:
                        case 621:
                        case 622:
                            this.imageRes.add(R.drawable.snow);
                            break;
                        //Atmosphere Light
                        case 701:
                        case 711:
                        case 721:
                            this.imageRes.add(R.drawable.haze_thin);
                            break;
                        //Atmosphere Dark (Emergency)
                        case 731:
                        case 741:
                        case 751:
                        case 761:
                        case 762:
                        case 771:
                            this.imageRes.add(R.drawable.haze);
                            break;
                        //Tornado Watch
                        case 781:
                            this.imageRes.add(R.drawable.tornado);
                            break;
                        //Clear
                        case 800:
                            this.imageRes.add(R.drawable.sun);
                            break;
                        //Clouds
                        case 801:
                        case 802:
                            this.imageRes.add(R.drawable.cloud_thin);
                            break;
                        case 803:
                        case 804:
                            this.imageRes.add(R.drawable.cloud);
                            break;
                        default:
                            this.imageRes.add(R.drawable.danger);
                            break;
                    }
                }
                /*this.fcast.setText(result.getJSONArray("weather").getJSONObject(0).get("main").toString());
                this.loc.setText(result.get("name").toString());

                double recivTemp = (9 / 5) * ((double) (result.getJSONObject("main").get("temp")) - 273.15) + 32;
                double recivTempMax = (9 / 5) * ((double) (result.getJSONObject("main").get("temp_max")) - 273.15) + 32;
                double recivTempMin = (9 / 5) * ((double) (result.getJSONObject("main").get("temp_min")) - 273.15) + 32;
                DecimalFormat numberFormat = new DecimalFormat("#.0");

                this.temp.setText(numberFormat.format(recivTemp) + "°");
                this.tempHigh.setText(numberFormat.format(recivTempMax) + "°");
                this.tempLow.setText(numberFormat.format(recivTempMin) + "°");*/

            } catch (JSONException e) {
                e.printStackTrace();
            }
            DecimalFormat numberFormat = new DecimalFormat("#.0");
            //Next Day
            TextView forecastView = (TextView) findViewById(R.id.textView_forecast0);
            TextView tempView = (TextView) findViewById(R.id.textView_temp0);
            TextView tempHighView = (TextView) findViewById(R.id.textView_tempHigh0);
            TextView tempLowView = (TextView) findViewById(R.id.textView_tempLow0);
            TextView timeView = (TextView) findViewById(R.id.textView_time0);
            ImageView displayView = (ImageView) findViewById(R.id.imageView_display0);
            forecastView.setText(forecast.get(0).toString());
            tempView.setText(numberFormat.format(temp.get(0)) + "°");
            tempHighView.setText(high.get(0).toString() + "°");
            tempLowView.setText(low.get(0).toString() + "°");
            Date t = new Date((long)Integer.parseInt(time.get(0))*1000);
            timeView.setText(t.toString());
            displayView.setImageResource((int)imageRes.get(0));
            //---------------------------------
            forecastView = (TextView) findViewById(R.id.textView_forecast1);
            tempView = (TextView) findViewById(R.id.textView_temp1);
            tempHighView = (TextView) findViewById(R.id.textView_tempHigh1);
            tempLowView = (TextView) findViewById(R.id.textView_tempLow1);
            timeView = (TextView) findViewById(R.id.textView_time1);
            displayView = (ImageView) findViewById(R.id.imageView_display1);
            forecastView.setText(forecast.get(1).toString());
            tempView.setText(numberFormat.format(temp.get(1)) + "°");
            tempHighView.setText(high.get(1).toString() + "°");
            tempLowView.setText(low.get(1).toString() + "°");
            t = new Date((long)Integer.parseInt(time.get(1))*1000);
            timeView.setText(t.toString());
            displayView.setImageResource((int)imageRes.get(1));
            //---------------------------------
            forecastView = (TextView) findViewById(R.id.textView_forecast2);
            tempView = (TextView) findViewById(R.id.textView_temp2);
            tempHighView = (TextView) findViewById(R.id.textView_tempHigh2);
            tempLowView = (TextView) findViewById(R.id.textView_tempLow2);
            timeView = (TextView) findViewById(R.id.textView_time2);
            displayView = (ImageView) findViewById(R.id.imageView_display2);
            forecastView.setText(forecast.get(2).toString());
            tempView.setText(numberFormat.format(temp.get(2)) + "°");
            tempHighView.setText(high.get(2).toString() + "°");
            tempLowView.setText(low.get(2).toString() + "°");
            t = new Date((long)Integer.parseInt(time.get(2))*1000);
            timeView.setText(t.toString());
            displayView.setImageResource((int)imageRes.get(2));
            //----------------------------------
            forecastView = (TextView) findViewById(R.id.textView_forecast3);
            tempView = (TextView) findViewById(R.id.textView_temp3);
            tempHighView = (TextView) findViewById(R.id.textView_tempHigh3);
            tempLowView = (TextView) findViewById(R.id.textView_tempLow3);
            timeView = (TextView) findViewById(R.id.textView_time3);
            displayView = (ImageView) findViewById(R.id.imageView_display3);
            forecastView.setText(forecast.get(3).toString());
            tempView.setText(numberFormat.format(temp.get(3)) + "°");
            tempHighView.setText(high.get(3).toString() + "°");
            tempLowView.setText(low.get(3).toString() + "°");
            t = new Date((long)Integer.parseInt(time.get(3))*1000);
            timeView.setText(t.toString());
            displayView.setImageResource((int)imageRes.get(3));
            //-----------------------------------
            forecastView = (TextView) findViewById(R.id.textView_forecast4);
            tempView = (TextView) findViewById(R.id.textView_temp4);
            tempHighView = (TextView) findViewById(R.id.textView_tempHigh4);
            tempLowView = (TextView) findViewById(R.id.textView_tempLow4);
            timeView = (TextView) findViewById(R.id.textView_time4);
            displayView = (ImageView) findViewById(R.id.imageView_display4);
            forecastView.setText(forecast.get(4).toString());
            tempView.setText(numberFormat.format(temp.get(4)) + "°");
            tempHighView.setText(high.get(4).toString() + "°");
            tempLowView.setText(low.get(4).toString() + "°");
            t = new Date((long)Integer.parseInt(time.get(4))*1000);
            timeView.setText(t.toString());
            displayView.setImageResource((int)imageRes.get(4));
        }
    }
    public void gotoForecast(View view){
        Intent i = new Intent(this, MainActivity.class);
        //i.putExtra(NAMELIST, nameList);
        startActivityForResult(i, 1);
        finish();
    }
    public void gotoOptions(View view){
        Intent i = new Intent(this, Options.class);
        //i.putExtra(NAMELIST, nameList);
        startActivityForResult(i, 1);
        finish();
    }
}

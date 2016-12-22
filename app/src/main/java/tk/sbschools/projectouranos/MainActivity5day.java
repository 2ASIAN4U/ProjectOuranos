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
import java.util.List;

public class MainActivity5day extends AppCompatActivity {
    ArrayList<String> forecast,time;
    ArrayList<Integer> temp,high,low,imageRes;
    ImageView background;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity5day);

        prefs = this.getPreferences(Context.MODE_PRIVATE);
        editor = prefs.edit();

        background = (ImageView) findViewById(R.id.ImageView_background);
        background.setImageResource(R.drawable.rwby6);

        WeatherThread retrieveWeatherData = new WeatherThread(this);
        retrieveWeatherData.execute("zip=08852,us");
    }

    public class CustomAdapter extends ArrayAdapter<String> {
        List forecast,time;
        List temp,high,low,imageRes;
        Context mainContext;

        public CustomAdapter(Context context, int resource, List<String> forecast, List<Double> temp, List<String> time, List<Integer> high, List<Integer> low, List<Integer> imageRes) {
            super(context, resource, forecast);

            mainContext = context;
            this.forecast = forecast;
            this.temp = temp;
            this.time = time;
            this.high = high;
            this.low = low;
            this.imageRes = imageRes;
            //imageList = images;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mainContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View layoutView = inflater.inflate(R.layout.listforecast, null);

            TextView forecastView = (TextView) layoutView.findViewById(R.id.textView_forecast);
            TextView tempView = (TextView) layoutView.findViewById(R.id.textView_temp);
            TextView tempHighView = (TextView) layoutView.findViewById(R.id.textView_tempHigh);
            TextView tempLowView = (TextView) layoutView.findViewById(R.id.textView_tempLow);
            TextView timeView = (TextView) layoutView.findViewById(R.id.textView_time);
            ImageView displayView = (ImageView) layoutView.findViewById(R.id.imageView_display);

            forecastView.setText(forecast.get(position).toString());

            DecimalFormat numberFormat = new DecimalFormat("#.0");
            tempView.setText(numberFormat.format(temp.get(position)) + "°");

            tempHighView.setText(high.get(position).toString() + "°");
            tempLowView.setText(low.get(position).toString() + "°");
            timeView.setText(time.get(position).toString());
            displayView.setImageResource((int) imageRes.get(position));
            //if(!imageList.isEmpty() && imageList.size()-1 > position){
            //    imageView.setImageBitmap((Bitmap)imageList.get(position));
            //}else {
            //imagecache.add(position,((BitmapDrawable)imageView.getDrawable()).getBitmap());
            //}


            return layoutView;
        }
    }

    public class WeatherThread extends AsyncTask<String, Void, JSONObject> {
        Context main;
        ArrayList<String> forecast, time;
        ArrayList<Integer>  high, low, imageRes;
        ArrayList<Double> temp;

        public WeatherThread(Context mainThread) {
            this.main = mainThread;
            forecast = new ArrayList<>();
            temp = new ArrayList<>();
            high = new ArrayList<>();
            low = new ArrayList<>();
            time = new ArrayList<>();
            imageRes = new ArrayList<>();
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
                for (int i = 0; i < result.getJSONArray("list").length(); i++) { //
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
            CustomAdapter myAdapter = new CustomAdapter(main, R.layout.listforecast, forecast, temp, time, high, low, imageRes);

            ((ListView) findViewById(R.id.listView_5day)).setAdapter(myAdapter);
        }
    }
    public void gotoForecast(View view){
        Intent i = new Intent(this, MainActivity.class);
        //i.putExtra(NAMELIST, nameList);
        startActivityForResult(i, 1);
    }
}

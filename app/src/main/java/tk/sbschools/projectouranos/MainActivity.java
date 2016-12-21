package tk.sbschools.projectouranos;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView forecast, location, temp, tempHigh, tempLow;
    ImageView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         //https://query.yahooapis.com/v1/public/yql?q=select * from weather.forecast where woeid in (select woeid from geo.places(1) where text='08852')&format=json

        forecast = (TextView)findViewById(R.id.textView_forcast);
        location = (TextView)findViewById(R.id.textView_location);
        temp = (TextView)findViewById(R.id.textView_temp);
        tempHigh = (TextView)findViewById(R.id.textView_tempHigh);
        tempLow = (TextView)findViewById(R.id.textView_tempLow);
        display = (ImageView)findViewById(R.id.imageView_display);

        WeatherThread retrieveWeatherData = new WeatherThread(forecast,location,temp,tempHigh,tempLow,display);
        retrieveWeatherData.execute("zip=08852,us");
    }

    public class WeatherThread extends AsyncTask<String,Void,JSONObject>{
        TextView fcast, loc, temp, tempHigh, tempLow;
        ImageView dispIcon;

        public WeatherThread(TextView fcast, TextView loc, TextView temp, TextView tempHigh, TextView tempLow, ImageView disp){
            this.fcast = fcast;
            this.loc = loc;
            this.temp = temp;
            this.tempHigh = tempHigh;
            this.tempLow = tempLow;
            this.dispIcon = disp;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String inputString = params[0];
            JSONObject retrievedData = new JSONObject();
            try{
                //InputStream in = new java.net.URL("http://query.yahooapis.com/v1/public/yql?q=" + Uri.encode("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text='" + inputString + "')") + "&format=json").openStream();
                InputStream in = new java.net.URL("http://api.openweathermap.org/data/2.5/weather?" + inputString + "&appid=5db23354f636c5fb73b6cd20611a4c6f").openStream();
                BufferedReader webData = new BufferedReader(new InputStreamReader(in));
                String dataString = webData.readLine();
                retrievedData = new JSONObject(dataString);
            }catch (Exception e){
                System.err.println("WeatherThread.doInBackground ERROR: Something up there dun fucked up");
                e.printStackTrace();
                //Toast.makeText(MainActivity.this, "Error in Retrieving Weather Data", Toast.LENGTH_SHORT).show();
            }
            return retrievedData;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            System.out.println(result.toString());

            //super.onPostExecute(result);
            try{
                //------------------------------------Former Yahoo Weather API-------------------------------------------------
                /*if(!(result.getJSONObject("query").getInt("count") == 0)) {
                    JSONObject main = result.getJSONObject("query").getJSONObject("results").getJSONObject("channel");
                    this.fcast.setText(main.getJSONObject("item").getJSONObject("condition").get("text").toString());
                    this.loc.setText(main.getJSONObject("location").get("city").toString() + ", " + main.getJSONObject("location").get("region").toString());
                    this.temp.setText(main.getJSONObject("item").getJSONObject("condition").get("temp").toString() + "°");
                    this.tempHigh.setText(main.getJSONObject("item").getJSONObject("forecast").getJSONObject("0").get("high").toString() + "°");
                    this.tempLow.setText(main.getJSONObject("item").getJSONObject("forecast").getJSONObject("0").get("low").toString() + "°");
                }*/
                this.fcast.setText(result.getJSONArray("weather").getJSONObject(0).get("main").toString());
                this.loc.setText(result.get("name").toString());

                double recivTemp = (9/5)*((double)(result.getJSONObject("main").get("temp"))-273.15)+32;
                double recivTempMax = (9/5)*((double)(result.getJSONObject("main").get("temp_max"))-273.15)+32;
                double recivTempMin = (9/5)*((double)(result.getJSONObject("main").get("temp_min"))-273.15)+32;
                DecimalFormat numberFormat = new DecimalFormat("#.0");

                this.temp.setText(numberFormat.format(recivTemp) + "°");
                this.tempHigh.setText(numberFormat.format(recivTempMax) + "°");
                this.tempLow.setText(numberFormat.format(recivTempMin) + "°");

                switch ((int)result.getJSONArray("weather").getJSONObject(0).get("id")){
                    //ThunderStorm
                    case 200:case 201:case 202:case 210:case 211:case 212:case 221:case 230:case 231:case 232:
                        dispIcon.setImageResource(R.drawable.storm);
                        break;
                    //Drizzle
                    case 300:case 301:case 302:case 310:case 311:case 312:case 313:case 314:case 321:
                        dispIcon.setImageResource(R.drawable.rain_thin);
                        break;
                    //rain
                    case 500:case 501:case 502:case 503:case 504:
                        dispIcon.setImageResource(R.drawable.rain);
                        break;
                    //FreezingRain
                    case 511: dispIcon.setImageResource(R.drawable.snow_thin); break;
                    //Shower
                    case 520:case 521:case 522:case 531:
                        dispIcon.setImageResource(R.drawable.rain_thin);
                        break;
                    //Snow
                    case 600:case 601:case 602:case 611:case 612:case 615:case 616:case 620:case 621:case 622:
                        dispIcon.setImageResource(R.drawable.snow);
                        break;
                    //Atmosphere Light
                    case 701:case 711:case 721:
                        dispIcon.setImageResource(R.drawable.haze_thin);
                        break;
                    //Atmosphere Dark (Emergency)
                    case 731:case 741:case 751:case 761:case 762:case 771:
                        dispIcon.setImageResource(R.drawable.haze);
                        break;
                    //Tornado Watch
                    case 781: dispIcon.setImageResource(R.drawable.tornado); break;
                    //Clear
                    case 800: dispIcon.setImageResource(R.drawable.sun);break;
                    //Clouds
                    case 801:case 802:
                        dispIcon.setImageResource(R.drawable.cloud_thin);
                        break;
                    case 803:case 804:
                        dispIcon.setImageResource(R.drawable.cloud);
                        break;
                    default: dispIcon.setImageResource(R.drawable.danger); break;
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
    public void goto5Day(View view){
        Intent i = new Intent(this, MainActivity5day.class);
        //i.putExtra(NAMELIST, nameList);
        startActivityForResult(i, 1);
    }
}

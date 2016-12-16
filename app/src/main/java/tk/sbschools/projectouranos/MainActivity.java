package tk.sbschools.projectouranos;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    TextView forecast, location, temp, tempHigh, tempLow;

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

        WeatherThread retrieveWeatherData = new WeatherThread(forecast,location,temp,tempHigh,tempLow);
        retrieveWeatherData.execute("08852");
    }

    public class WeatherThread extends AsyncTask<String,Void,JSONObject>{
        TextView fcast, loc, temp, tempHigh, tempLow;

        public WeatherThread(TextView fcast, TextView loc, TextView temp, TextView tempHigh, TextView tempLow){
            this.fcast = fcast;
            this.loc = loc;
            this.temp = temp;
            this.tempHigh = tempHigh;
            this.tempLow = tempLow;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String inputString = params[0];
            JSONObject retrievedData = new JSONObject();
            try{
                InputStream in = new java.net.URL("http://query.yahooapis.com/v1/public/yql?q=" + Uri.encode("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text='" + inputString + "')") + "&format=json").openStream();
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
            /*try {
                result = new JSONObject("{\"query\":{\"count\":1,\"created\":\"2016-12-14T12:46:48Z\",\"lang\":\"en-US\",\"results\":{\"channel\":{\"units\":{\"distance\":\"mi\",\"pressure\":\"in\",\"speed\":\"mph\",\"temperature\":\"F\"},\"title\":\"Yahoo! Weather - South Brunswick, NJ, US\",\"link\":\"http:\\/\\/us.rd.yahoo.com\\/dailynews\\/rss\\/weather\\/Country__Country\\/*https:\\/\\/weather.yahoo.com\\/country\\/state\\/city-12761289\\/\",\"description\":\"Yahoo! Weather for South Brunswick, NJ, US\",\"language\":\"en-us\",\"lastBuildDate\":\"Wed, 14 Dec 2016 07:46 AM EST\",\"ttl\":\"60\",\"location\":{\"city\":\"South Brunswick\",\"country\":\"United States\",\"region\":\" NJ\"},\"wind\":{\"chill\":\"25\",\"direction\":\"300\",\"speed\":\"11\"},\"atmosphere\":{\"humidity\":\"83\",\"pressure\":\"1011.0\",\"rising\":\"0\",\"visibility\":\"16.1\"},\"astronomy\":{\"sunrise\":\"7:14 am\",\"sunset\":\"4:33 pm\"},\"image\":{\"title\":\"Yahoo! Weather\",\"width\":\"142\",\"height\":\"18\",\"link\":\"http:\\/\\/weather.yahoo.com\",\"url\":\"http:\\/\\/l.yimg.com\\/a\\/i\\/brand\\/purplelogo\\/\\/uh\\/us\\/news-wea.gif\"},\"item\":{\"title\":\"Conditions for South Brunswick, NJ, US at 07:00 AM EST\",\"lat\":\"40.389568\",\"long\":\"-74.539688\",\"link\":\"http:\\/\\/us.rd.yahoo.com\\/dailynews\\/rss\\/weather\\/Country__Country\\/*https:\\/\\/weather.yahoo.com\\/country\\/state\\/city-12761289\\/\",\"pubDate\":\"Wed, 14 Dec 2016 07:00 AM EST\",\"condition\":{\"code\":\"31\",\"date\":\"Wed, 14 Dec 2016 07:00 AM EST\",\"temp\":\"31\",\"text\":\"Clear\"},\"forecast\":[{\"code\":\"30\",\"date\":\"14 Dec 2016\",\"day\":\"Wed\",\"high\":\"40\",\"low\":\"30\",\"text\":\"Partly Cloudy\"},{\"code\":\"30\",\"date\":\"15 Dec 2016\",\"day\":\"Thu\",\"high\":\"29\",\"low\":\"20\",\"text\":\"Partly Cloudy\"},{\"code\":\"30\",\"date\":\"16 Dec 2016\",\"day\":\"Fri\",\"high\":\"28\",\"low\":\"18\",\"text\":\"Partly Cloudy\"},{\"code\":\"5\",\"date\":\"17 Dec 2016\",\"day\":\"Sat\",\"high\":\"47\",\"low\":\"24\",\"text\":\"Rain And Snow\"},{\"code\":\"39\",\"date\":\"18 Dec 2016\",\"day\":\"Sun\",\"high\":\"56\",\"low\":\"32\",\"text\":\"Scattered Showers\"},{\"code\":\"28\",\"date\":\"19 Dec 2016\",\"day\":\"Mon\",\"high\":\"31\",\"low\":\"23\",\"text\":\"Mostly Cloudy\"},{\"code\":\"28\",\"date\":\"20 Dec 2016\",\"day\":\"Tue\",\"high\":\"31\",\"low\":\"20\",\"text\":\"Mostly Cloudy\"},{\"code\":\"28\",\"date\":\"21 Dec 2016\",\"day\":\"Wed\",\"high\":\"40\",\"low\":\"21\",\"text\":\"Mostly Cloudy\"},{\"code\":\"28\",\"date\":\"22 Dec 2016\",\"day\":\"Thu\",\"high\":\"45\",\"low\":\"31\",\"text\":\"Mostly Cloudy\"},{\"code\":\"12\",\"date\":\"23 Dec 2016\",\"day\":\"Fri\",\"high\":\"43\",\"low\":\"35\",\"text\":\"Rain\"}],\"description\":\"<![CDATA[<img src=\\\"http:\\/\\/l.yimg.com\\/a\\/i\\/us\\/we\\/52\\/31.gif\\\"\\/>\\n<BR \\/>\\n<b>Current Conditions:<\\/b>\\n<BR \\/>Clear\\n<BR \\/>\\n<BR \\/>\\n<b>Forecast:<\\/b>\\n<BR \\/> Wed - Partly Cloudy. High: 40Low: 30\\n<BR \\/> Thu - Partly Cloudy. High: 29Low: 20\\n<BR \\/> Fri - Partly Cloudy. High: 28Low: 18\\n<BR \\/> Sat - Rain And Snow. High: 47Low: 24\\n<BR \\/> Sun - Scattered Showers. High: 56Low: 32\\n<BR \\/>\\n<BR \\/>\\n<a href=\\\"http:\\/\\/us.rd.yahoo.com\\/dailynews\\/rss\\/weather\\/Country__Country\\/*https:\\/\\/weather.yahoo.com\\/country\\/state\\/city-12761289\\/\\\">Full Forecast at Yahoo! Weather<\\/a>\\n<BR \\/>\\n<BR \\/>\\n(provided by <a href=\\\"http:\\/\\/www.weather.com\\\" >The Weather Channel<\\/a>)\\n<BR \\/>\\n]]>\",\"guid\":{\"isPermaLink\":\"false\"}}}}}}");
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            //super.onPostExecute(result);
            try{
                if(!(result.getJSONObject("query").getInt("count") == 0)) {
                    JSONObject main = result.getJSONObject("query").getJSONObject("results").getJSONObject("channel");
                    this.fcast.setText(main.getJSONObject("item").getJSONObject("condition").get("text").toString());
                    this.loc.setText(main.getJSONObject("location").get("city").toString() + ", " + main.getJSONObject("location").get("region").toString());
                    this.temp.setText(main.getJSONObject("item").getJSONObject("condition").get("temp").toString() + "°");
                    //this.temp.setText(main.getJSONObject("item").getJSONObject("forecast").getJSONObject("0").get("high").toString() + "°");
                    //this.temp.setText(main.getJSONObject("item").getJSONObject("forecast").getJSONObject("0").get("low").toString() + "°");
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
}

package tk.sbschools.projectouranos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Options extends AppCompatActivity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    Spinner themeList;
    ArrayList<String> themeArrList;
    //ImageView background;
    EditText setLocation;
    Button locSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        prefs = this.getSharedPreferences("tk.sbschools.projectouranos",Context.MODE_PRIVATE);
        editor = prefs.edit();

        themeList = (Spinner)findViewById(R.id.spinner_theme);
        setLocation = (EditText)findViewById(R.id.editText_location);
        locSet = (Button)findViewById(R.id.button_loc_enter);
        themeArrList = new ArrayList<>();
        themeArrList.add("RWBY");
        themeArrList.add("Code");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,themeArrList);
        themeList.setAdapter(spinnerAdapter);

        themeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    prefs.edit().putString("theme", "rwby").apply();;
                    Toast.makeText(Options.this, "Theme Selected: RWBY", Toast.LENGTH_SHORT).show();
                }else{
                    prefs.edit().putString("theme", "code").apply();;
                    Toast.makeText(Options.this, "Theme Selected: Code", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        locSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!setLocation.getText().equals("")){
                    prefs.edit().putString("location", Uri.parse(setLocation.getText().toString()).toString()).apply();
                    Toast.makeText(Options.this, "Location Set: " + prefs.getString("location", "08512"), Toast.LENGTH_SHORT).show();
                }
            }
        });

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
    }
    public void gotoForecast(View view){
        Intent i = new Intent(this, MainActivity.class);
        //i.putExtra(NAMELIST, nameList);
        startActivityForResult(i, 1);
        finish();
    }
    public void goto5Day(View view){
        Intent i = new Intent(this, MainActivity5day.class);
        //i.putExtra(NAMELIST, nameList);
        startActivityForResult(i, 1);
        finish();
    }
}

package global.org.minidapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity {

    ArrayList<String> urls = new ArrayList<String>();
    public String deviceIP;
    TextView tvHelp;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);

        // get devices IP
        getDeviceIP();

        // set buttons
        this.setBackBtn();



    }

    public void setBackBtn() {
        backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getDeviceIP() {
        new Thread(new Runnable(){
            public void run(){
                //TextView t; //to show the result, please declare and find it inside onCreate()

                try {
                    // Create a URL for the desired page
                    URL url = new URL("https://api.ipify.org/"); //My text file location
                    //First open the connection
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(60000); // timing out in a minute

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    //t=(TextView)findViewById(R.id.TextView1); // ideally do this in onCreate()
                    String str;
                    while ((str = in.readLine()) != null) {
                        urls.add(str);
                    }
                    in.close();
                } catch (Exception e) {
                    Log.d("MyTag",e.toString());
                }

                //since we are in background thread, to post results we have to go back to ui thread. do the following for that

                HelpActivity.this.runOnUiThread(new Runnable(){
                    public void run(){
                        try {
                            //Toast.makeText(HelpActivity.this, "Public IP: "+urls.get(0), Toast.LENGTH_SHORT).show();

                            deviceIP = urls.get(0) + ":21000";
                            tvHelp = findViewById(R.id.tv_help);
                            String tempStr = tvHelp.getText().toString();
                            // set string on tv
                            tvHelp.setText(tempStr+deviceIP+"/");

                        }
                        catch (Exception e){
                            Toast.makeText(HelpActivity.this, "TurnOn wiffi to get public ip", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }).start();
    }


}

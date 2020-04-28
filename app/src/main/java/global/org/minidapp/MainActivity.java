package global.org.minidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    public static MainActivity mMainLink;
    Button btnMini, btnHelp;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // start Minima node Foreground Service
        this.startForegroundService();

        mMainLink = this;



        // setting btn listeners
        this.setDAPPbtn();

    }

    public void setDAPPbtn() {
        btnMini = findViewById(R.id.btn_minidapp);
        // btn open 127.0.0.1:21000 `Minidapp`
        btnMini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://127.0.0.1:21000/"));
                startActivity(intent);
            }
        });
    }

    private void startForegroundService() {
        Intent intent = new Intent(this, NodeService.class);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }
    }
}

package de.leon_mueller.switchwidget;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.savebutton);
        EditText v = (EditText) findViewById(R.id.ip);

        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("ip", getBaseContext().MODE_PRIVATE);
        String ip = sharedPref.getString("ip", null);

        v.setText(ip);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText e = (EditText) findViewById(R.id.ip);
                SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("ip", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("ip", e.getText().toString());
                editor.commit();

                Snackbar.make(findViewById(R.id.main), "IP gespeichert", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        });
    }
}

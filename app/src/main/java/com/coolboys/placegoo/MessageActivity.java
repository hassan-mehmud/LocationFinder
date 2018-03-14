package com.coolboys.placegoo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.widget.Toast.makeText;

public class MessageActivity extends AppCompatActivity {
    public EditText userMessage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
       userMessage = findViewById(R.id.txt_msg);
        Button button = (Button) findViewById(R.id.msg_sv);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("userMessage", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userMsg", userMessage.getText().toString());
                editor.commit();
                Intent msg = new Intent (getApplicationContext(),MapActivity.class);
                startActivity (msg);


            }
        });
    }

}

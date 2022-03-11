package com.example.einzelbeispiel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.Buffer;

public class MainActivity extends AppCompatActivity {

    // Declarations
    TextView tvEingabe;
    TextView tvAusgabe;

    Button btnAbschicken;
    Button btnBerechnen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvEingabe = (TextView) findViewById(R.id.eingabeMartikelnummer);
        tvAusgabe = (TextView) findViewById(R.id.ServerAntwort);
        btnAbschicken = (Button) findViewById(R.id.button1);
        btnBerechnen = (Button) findViewById(R.id.button2);

        btnAbschicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ServerConnection().execute();
            }
        });
    }

    public class ServerConnection extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... arg0) {
            try {
                String inputUser = tvEingabe.getText().toString(); //read input from user
                Socket clientSocket = new Socket("se2-isys.aau.at", 53212); //create client socket connected to server
                DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream()); // create output stream attached to server
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // create input stream attached to socket
                outputStream.writeBytes(inputUser + "\n"); // send line to server
                String result = inputStream.readLine(); // read line from server
                tvAusgabe.setText(result); // write result into textview
                clientSocket.close(); // close client socket
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
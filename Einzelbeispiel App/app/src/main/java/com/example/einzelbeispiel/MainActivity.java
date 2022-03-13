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
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.Vector;

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

        btnBerechnen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aufgabe 5
                String inputUser = tvEingabe.getText().toString();

                // Store input to array
                int inputNumber = Integer.parseInt(inputUser);
                int length = inputUser.length();
                int[] arrayNumbers = new int[length];
                for (int i = 0; i < length; i++) {
                    arrayNumbers[i] = inputNumber % 10;
                    inputNumber /= 10;
                }

                // Delete primes from array - only possible primes between 0 and 9 are 2, 3, 5 and 7
                int i = 0;
                while (i < length) {
                    // man hätte eine eigene Funktion für Primzahlermittlung machen können aber da es nur 4 mögliche einstellige Primzahlen gibt habe ich diese statisch abgefragt
                    if (arrayNumbers[i] == 2 || arrayNumbers[i] == 3 || arrayNumbers[i] == 5 || arrayNumbers[i] == 7) {
                        arrayNumbers = removeTheElement(arrayNumbers, i);
                        length = arrayNumbers.length;
                    } else {
                        i++;
                    }
                }
                // sort array
                Arrays.sort(arrayNumbers);

                tvAusgabe.setText(Arrays.toString(arrayNumbers));
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

    // Function to remove the element
    public static int[] removeTheElement(int[] oldArray, int index)
    {
        if (oldArray == null || index < 0 || index >= oldArray.length) {
            return oldArray;
        }

        int[] newArray = new int[oldArray.length - 1];

        // Copy the elements except the index from original array to the other array
        for (int i = 0, j = 0; i < oldArray.length; i++) {

            // if the index is the removal element index
            if (i == index) {
                continue;
            }
            newArray[j++] = oldArray[i];
        }
        return newArray;
    }
}
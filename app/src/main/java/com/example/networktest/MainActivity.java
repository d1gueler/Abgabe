package com.example.networktest;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick(View v) {


        TextView textView2 = (TextView) findViewById(R.id.matrikelnummer);
        EditText editText = (EditText) findViewById(R.id.editText);
        int number = Integer.parseInt(editText.getText().toString());
        int summe = 0;

        while (0 != number) {

            summe = summe + (number % 10);
            number = number / 10;
        }

        String bin=(Integer.toBinaryString(summe));


        textView2.setText(bin);
        server();
    }


    String text;

    public void server(){

        Thread p=new Thread(new Runnable() {
            @Override
            public void run() {

                EditText editText = (EditText) findViewById(R.id.editText);
                Socket socket = null;

                try {
                    socket = new Socket("se2-isys.aau.at", 53212);
                    OutputStream raus = socket.getOutputStream();
                    PrintWriter ps = new PrintWriter(raus, true);
                    ps.println(editText.getText().toString());
                    ps.flush();
                    BufferedReader rein = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    text=rein.readLine();

                } catch (UnknownHostException e) {
                    text="Uknown Host...";
                    e.printStackTrace();

                } catch (IOException e) {
                    text="IOProbleme...";
                    e.printStackTrace();

                } finally {
                }

                if (socket != null)
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

        });
        p.start();
        try{
            p.join();
        }catch (InterruptedException e){e.printStackTrace();}
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                TextView Serverj = (TextView) findViewById(R.id.ServerJ);
                Serverj.setText(text);
            }
        });
    }

}

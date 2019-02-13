package com.example.prash.imageloaderapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    ProgressBar progressBar;
    Handler handler;
    ExecutorService threadPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Display Image");
        Button asyncButton = (Button) findViewById(R.id.asyncButton);
        Button threadButton = (Button) findViewById(R.id.threadButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageView = (ImageView) findViewById(R.id.imageView);
        threadPool = Executors.newFixedThreadPool(4);

        asyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                new DoWorkAsync().execute("https://cdn.pixabay.com/photo/2014/12/16/22/25/youth-570881_960_720.jpg");


            }
        });

        threadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        imageView.setImageBitmap( (Bitmap) msg.getData().getParcelable(ThreadWork.IMAGE_KEY));
                        imageView.setVisibility(View.VISIBLE);
                        progressBar=  (ProgressBar) findViewById(R.id.progressBar);
                        progressBar.setVisibility(View.INVISIBLE);


                        return false;
                    }
                });
              //  new Thread(new ThreadWork()).start();
                threadPool.execute(new ThreadWork());
            }
        });




    }


        class ThreadWork implements Runnable{

            static  final String IMAGE_KEY="IMAGE_KEY";
            @Override
            public void run() {
                try {
                    URL url = new URL("https://cdn.pixabay.com/photo/2017/12/31/06/16/boats-3051610_960_720.jpg");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(IMAGE_KEY,myBitmap);
                    Message message = new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                  //  return myBitmap;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }
           //     return null;
            }
        }

        class  DoWorkAsync extends AsyncTask<String,Integer,Bitmap>{
            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                ImageView myImageView =(ImageView) findViewById(R.id.imageView);
                myImageView.setImageBitmap(bitmap);
                myImageView.setVisibility(View.VISIBLE);
                progressBar=  (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {

            }

            @Override
            protected Bitmap doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    return myBitmap;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }

}

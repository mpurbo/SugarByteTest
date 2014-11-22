package org.purbo.sugarbytetest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;


public class MainActivity extends Activity {

    private static final String TAG = "sugarbytetest.MainActivity";

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView);

        ImageData savedImageData = Select.from(ImageData.class).where(Condition.prop("key").eq("test")).first();
        if (savedImageData == null) {
            Log.d(TAG, "Image not available, download first");
            new DownloadBitmapTask().execute("http://mamad.purbo.org/ultrag.jpg");
        } else {
            Log.d(TAG, "Image already saved, display it");
            byte [] rawImageData = savedImageData.getRawImageData();
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(rawImageData, 0, rawImageData.length));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadBitmapTask extends AsyncTask<String, Integer, Bitmap> {
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream is = null;
                try {
                    is = connection.getInputStream();
                    ImageData imageData = new ImageData("test", is);
                    imageData.save();

                    ImageData savedImageData = Select.from(ImageData.class).where(Condition.prop("key").eq("test")).first();
                    byte [] rawImageData = savedImageData.getRawImageData();
                    return BitmapFactory.decodeByteArray(rawImageData, 0, rawImageData.length);
                } finally {
                    try { is.close(); } catch (Throwable any) {}
                }
            } catch (Exception e) {
                Log.e(TAG, "Error downloading/saving", e);
                return null;
            }
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}

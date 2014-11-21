package org.purbo.sugarbytetest;

import com.orm.SugarRecord;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by purbo on 11/21/14.
 */
public class ImageData extends SugarRecord {

    String key;
    byte[] rawImageData;

    public ImageData() {}

    public ImageData(String key, InputStream is) throws Exception {
        this.key = key;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int read;
        byte [] buffer = new byte[16384];
        while ((read = is.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, read);
        }
        baos.flush();

        this.rawImageData = baos.toByteArray();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public byte[] getRawImageData() {
        return rawImageData;
    }

    public void setRawImageData(byte[] rawImageData) {
        this.rawImageData = rawImageData;
    }

}

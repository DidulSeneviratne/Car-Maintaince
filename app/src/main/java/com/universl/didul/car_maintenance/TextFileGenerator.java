package com.universl.didul.car_maintenance;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class TextFileGenerator {

    public static File generateTextFile(String content) {
        // Path for saving the text file
        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/Download/";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = directoryPath + "vehicle_document.txt";
        File file = new File(filePath);

        try {
            // Create a FileOutputStream and OutputStreamWriter to write to the file
            FileOutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);

            // Write the content to the file
            writer.write(content);

            // Close the writer and output stream
            writer.close();
            outputStream.close();

            return file;
        } catch (IOException e) {
            Log.e("TextFileGenerator", "Error generating text file", e);
            return null;
        }
    }
}
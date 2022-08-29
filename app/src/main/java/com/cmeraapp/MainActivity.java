package com.cmeraapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int CAM_REQ_CODE = 101;
    public static final int GAl_REQ_CODE = 105;
    ImageView capturedImage;
    Button startcamera,gallery;
    String currentImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        capturedImage = findViewById(R.id.image_view);
        startcamera = findViewById(R.id.open_camera);
        gallery = findViewById(R.id.open_gallery);


        startcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cmeraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cmeraIntent, CAM_REQ_CODE);
               // dispatchTakePictureIntent();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galIntent, GAl_REQ_CODE);

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CAM_REQ_CODE && resultCode!=RESULT_CANCELED ){
            List<Object> imagesList = new ArrayList<>();
           Bitmap image = (Bitmap) data.getExtras().get("data");
           imagesList.add(data.getExtras().get("data"));
           capturedImage.setImageBitmap(image);
            // Bitmap image = (Bitmap) data.getExtras().get("data");
            Bitmap images = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            images.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            byte[] b = bos.toByteArray();
            String imageStringBase64i = Base64.encodeToString(b, Base64.DEFAULT);
            Log.d("CCCCCFFFNNN", imageStringBase64i);


            /*File f = new File(currentImagePath);
            capturedImage.setImageURI(Uri.fromFile(f));
            Log.d("Image File Pah))",
            String.valueOf(Uri.fromFile(f)));
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);*/

        }

        if(requestCode==GAl_REQ_CODE){
         Uri contentUri =data.getData();
            String timestamp = new SimpleDateFormat("yyyyy-MM-dd_HHmmss").format(new Date());
            String imageFileName  ="jpeg_"+timestamp+"."+getFileExt(contentUri);
            capturedImage.setImageURI(contentUri);



        }

    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyy-MM-dd_HHmmss").format(new Date());
        String imageFileName  ="jpeg_"+timestamp+"_";
       // File storageDir= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpeg",storageDir);
        currentImagePath =image.getAbsolutePath();
        return  image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.cmeraapp.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAM_REQ_CODE);
            }
        }
    }

}
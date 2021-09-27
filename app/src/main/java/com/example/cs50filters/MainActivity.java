package com.example.cs50filters;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;

public class MainActivity extends AppCompatActivity {
private ImageView image_view;
private Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image_view=findViewById(R.id.image_view);
    }
    public void choosePhoto(View v){
        Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }
    public void applySepia(View v){
        Glide.with(this).load(image)
                .apply(RequestOptions.bitmapTransform(new SepiaFilterTransformation()))
                .into(image_view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK && data!=null){
            try {
                Uri uri = data.getData();
                ParcelFileDescriptor parcel = getContentResolver().openFileDescriptor(uri, "r");
                FileDescriptor descriptor=parcel.getFileDescriptor();
                image= BitmapFactory.decodeFileDescriptor(descriptor);
                parcel.close();
                image_view.setImageBitmap(image);
            }
            catch(IOException e){
                Log.e("filter","no image",e);
            }
        }
    }
}
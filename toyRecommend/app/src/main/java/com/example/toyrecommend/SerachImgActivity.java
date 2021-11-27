package com.example.toyrecommend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.toyrecommend.Prevalent.Prevalent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SerachImgActivity extends AppCompatActivity {

    private ImageView SearchProductImage;
    private Button searchbtn;
    private static final int GalleryPick = 1;
    private Uri ImageUri;

    BitmapDrawable drawable;
    Bitmap bitmap;

    String imageString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serach_img);

        SearchProductImage = findViewById(R.id.search_product_image);
        searchbtn = findViewById(R.id.search_img);

        if(!Python.isStarted())
            Python.start(new AndroidPlatform(this));

        final Python py = Python.getInstance();

        SearchProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawable = (BitmapDrawable)SearchProductImage.getDrawable();
                bitmap = drawable.getBitmap();
                imageString = getStringImage(bitmap);

                PyObject pyo = py.getModule("myScript");
                PyObject obj = pyo.callAttr("main", imageString);

                Toast.makeText(SerachImgActivity.this, "Loading..", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SerachImgActivity.this, imgProductsActivity.class);
                intent.putExtra("pc", obj.toString());
                startActivity(intent);
               // Log.d("TAGG", obj.toString());
            }
        });
    }

    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream( );
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageByte = baos.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(imageByte, Base64.DEFAULT);
        return encodedImage;
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            SearchProductImage.setImageURI(ImageUri);
        }
    }
}
package com.example.carson.yjenglish;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.carson.yjenglish.utils.ScreenUtils;

public class ImageShowActivity extends AppCompatActivity {

    private ImageView img;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);

        img = findViewById(R.id.show_img);

        url = getIntent().getStringExtra("img_url");

        Glide.with(this).load(url).asBitmap().format(DecodeFormat.PREFER_ARGB_8888)
                .listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                if (img.getScaleType() != ImageView.ScaleType.FIT_XY) {
                    img.setScaleType(ImageView.ScaleType.FIT_XY);
                }
                ViewGroup.LayoutParams lp = img.getLayoutParams();
                int vw = img.getWidth() - img.getPaddingLeft() - img.getPaddingRight();

                float scale = (float) vw / (float) resource.getWidth();

                Log.e("Image", "scale = " + scale);

                int vh = Math.round(resource.getHeight() * scale);

                lp.height = vh + img.getPaddingTop() + img.getPaddingBottom();

//                if (lp.height >= ScreenUtils.getScreenHeight(ImageShowActivity.this)) {
//                    int imgWidth = resource.getIntrinsicWidth();
//                    int imgHeight = resource.getIntrinsicHeight();
//                    int height = ScreenUtils.getScreenHeight(ImageShowActivity.this);
//                    int width = height * imgWidth / imgHeight;
////                    img.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                    lp.height = height;
//                    lp.width = width;
//                }

                img.setLayoutParams(lp);
                return false;
            }
        }).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}

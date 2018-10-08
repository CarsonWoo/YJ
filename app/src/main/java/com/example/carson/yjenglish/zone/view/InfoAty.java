package com.example.carson.yjenglish.zone.view;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.example.carson.yjenglish.BuildConfig;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.net.NullOnEmptyConverterFactory;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.UserInfoContract;
import com.example.carson.yjenglish.zone.UserInfoTask;
import com.example.carson.yjenglish.zone.ZoneService;
import com.example.carson.yjenglish.zone.model.UserBasicModel;
import com.example.carson.yjenglish.zone.presenter.UserInfoPresenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.WheelPicker;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class InfoAty extends AppCompatActivity implements UserInfoContract.View {

    private ImageView back;
    private CircleImageView ivPortrait;
    private TextView tvPortrait;
    private ImageView iv2Name;
    private EditText etName;
    private ImageView iv2Gender;
    private ImageView iv2Sign;
    private TextView tvGender;
    private EditText etSign;
    private TextView confirm;

    private ImageView bigImg;

    private FrameLayout photoView;

    private String name;
    private String signature;
    private String imgUrl;
    private String gender;

    private Uri photoUri;
    private File photoFile;

    private Dialog mDialog;
    private UserInfoContract.Presenter mPresenter;
    private UserInfoPresenter infoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_user_info);
        mDialog = DialogUtils.getInstance(this).newAnimatedLoadingDialog();

        Intent fromData = getIntent();
        name = fromData.getStringExtra("name");
        signature = fromData.getStringExtra("sign");
        imgUrl = fromData.getStringExtra("img_url");
        gender = fromData.getStringExtra("gender");
        if (gender != null) {
            if (gender.equals("0")) {
                gender = "男";
            } else {
                gender = "女";
            }
        }

        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.back);
        ivPortrait = findViewById(R.id.iv_portrait);
        tvPortrait = findViewById(R.id.tv_portrait);
        iv2Name = findViewById(R.id.iv_username);
        etName = findViewById(R.id.edit_username);
        iv2Gender = findViewById(R.id.iv_gender);
        iv2Sign = findViewById(R.id.iv_sign);
        tvGender = findViewById(R.id.edit_gender);
        etSign = findViewById(R.id.edit_sign);
        confirm = findViewById(R.id.btn_confirm);
        photoView = findViewById(R.id.photo_view);

        etName.setText(name);
        etSign.setText(signature);
        tvGender.setText(gender);
        Glide.with(this).load(imgUrl).thumbnail(0.5f).crossFade()
                .into(ivPortrait);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        iv2Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditAction(etName);
            }
        });

        iv2Gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPickerAction();
            }
        });

        iv2Sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditAction(etSign);
            }
        });

        tvPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPhotoView();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ableToPost = false;
                if (etName.isEnabled()) {
                    if (etName.getText().toString().isEmpty()) {
                        DialogUtils utils = DialogUtils.getInstance(InfoAty.this);
                        final AlertDialog mDialog = utils.newTipsDialog("请先输入用户名", TextView.TEXT_ALIGNMENT_CENTER);
                        utils.show(mDialog);
                        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
                        lp.width = ScreenUtils.dp2px(InfoAty.this, 260);
                        lp.height = ScreenUtils.dp2px(InfoAty.this, 240);
                        mDialog.getWindow().setAttributes(lp);
                        utils.setTipsListener(new DialogUtils.OnTipsListener() {
                            @Override
                            public void onConfirm() {
                                mDialog.dismiss();
                            }
                        });
                        return;
                    } else if (!etName.getText().toString().equals(name)) {
                        ableToPost = true;
//                        ableToPost = false;
                    }
//                    etName.setEnabled(false);
                }
                if (etSign.isEnabled()) {
                    if (!etSign.getText().toString().equals(signature)) {
                        ableToPost = true;
                    }
//                    etSign.setEnabled(false);
                }
                if (ableToPost) {
                    etName.setEnabled(false);
                    etSign.setEnabled(false);
                    doPostWork();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null  && imm.isActive()) {
                    imm.hideSoftInputFromWindow(confirm.getWindowToken(), 0);
                }
                confirm.setVisibility(View.GONE);
            }
        });
    }

    private void doPostWork() {
        Log.e("Info", "doPostWord");
        UserInfoTask task = UserInfoTask.getInstance();
        infoPresenter = new UserInfoPresenter(task, this);
        this.setPresenter(infoPresenter);
        mPresenter.getUserInfo(new UserBasicModel(UserConfig.getToken(this),
                etName.getText().toString(), tvGender.getText().toString().equals("男") ? "0" : "1",
                etSign.getText().toString()));
    }

    private void initPhotoView() {
        setViewsClickable(false);
        ObjectAnimator trans = ObjectAnimator.ofFloat(photoView, "translationX",
                500f, 0f).setDuration(300);
        photoView.setVisibility(View.VISIBLE);
        trans.start();
        View mView = LayoutInflater.from(InfoAty.this).inflate(R.layout.photo_view, null, false);
        ImageView mBack = mView.findViewById(R.id.back);
        bigImg = mView.findViewById(R.id.img);
        Button mBtn = mView.findViewById(R.id.change_portrait);
        photoView.addView(mView);
        Glide.with(InfoAty.this).load(imgUrl).thumbnail(0.5f).crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(bigImg);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View windowView = LayoutInflater.from(InfoAty.this).inflate(R.layout.popup_window_photo, null,
                        false);
                final PopupWindow window = new PopupWindow(windowView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setAnimationStyle(R.style.popup_window_photo_utils_animation);
                TextView takePhoto = windowView.findViewById(R.id.take_photo);
                TextView pickPhoto = windowView.findViewById(R.id.pick_photo);
                takePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onTakePhoto();
                        window.dismiss();
                    }
                });
                pickPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onPickPhoto();
                        window.dismiss();
                    }
                });
                window.setOutsideTouchable(true);
                window.setBackgroundDrawable(new BitmapDrawable());
                window.showAsDropDown(photoView, 0, -window.getHeight(), Gravity.BOTTOM);
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator trans = ObjectAnimator.ofFloat(photoView, "translationX",
                        0f, 1300f).setDuration(300);
                trans.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setViewsClickable(true);
                        photoView.setVisibility(View.GONE);
                        photoView.removeAllViews();
                    }
                }, 500);

            }
        });
    }

    private void setViewsClickable(boolean clickable) {
        if (!clickable) {
            back.setEnabled(false);
            tvPortrait.setEnabled(false);
            iv2Name.setEnabled(false);
            iv2Gender.setEnabled(false);
            iv2Sign.setEnabled(false);
        } else {
            back.setEnabled(true);
            tvPortrait.setEnabled(true);
            iv2Name.setEnabled(true);
            iv2Gender.setEnabled(true);
            iv2Sign.setEnabled(true);
        }

    }

    private void onPickPhoto() {
        checkReadStoragePermission();
    }

    private void onTakePhoto() {
        if (Build.VERSION.SDK_INT >= 24) {
            photoUri = get24MediaFileUri();
        } else {
            photoUri = getMediaFileUri();
        }
        checkCameraPermission();
    }

    private Uri getMediaFileUri() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "YuJingEnglish");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" +
                timeStamp + ".jpg");
        photoFile = mediaFile;
        return Uri.fromFile(photoFile);
    }

    private Uri get24MediaFileUri() {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/语境背单词/" + System.currentTimeMillis() + ".jpg";
        File mediaFile = new File(filePath);
        if (!mediaFile.getParentFile().exists()) {
            mediaFile.getParentFile().mkdir();
        }
        photoFile = mediaFile;
        //authority要与manifest中注册的一样
        return FileProvider.getUriForFile(this, "com.example.carson.yjenglish.fileprovider",
                mediaFile);
    }

    protected void checkReadStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        } else {
            Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(albumIntent, 2);
        }
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},
                    3);
        } else {
            checkWriteStoragePermission();
        }
    }

    private void checkWriteStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        if (photoUri != null) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(cameraIntent, 1);
        }
    }

    private void selectPic(Intent data) {
        Uri uri = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picPath = cursor.getString(columnIndex);
        cursor.close();
        photoFile = new File(picPath);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkCameraPermission();
            }
        } else if (requestCode == 3) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkWriteStoragePermission();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setPickerAction() {
        confirm.setVisibility(View.VISIBLE);
        OptionPicker genderPicker = new OptionPicker(InfoAty.this, new String[]{"男", "女"});
        genderPicker.setSelectedItem(tvGender.getText().toString());
        genderPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                tvGender.setText(item);
            }
        });
        genderPicker.setSubmitTextColor(Color.parseColor("#5ee1c9"));
        genderPicker.setTitleTextColor(Color.parseColor("#97919191"));
        genderPicker.setPressedTextColor(Color.parseColor("#5ee1c9"));
        genderPicker.setDividerColor(Color.parseColor("#5ee1c9"));
        genderPicker.setCancelVisible(false);
        genderPicker.setTitleText("性别");
        genderPicker.setTopLineColor(Color.parseColor("#97919191"));
        genderPicker.setTextColor(Color.parseColor("#5ee1c9"));
        genderPicker.show();
    }

    private void setEditAction(EditText editText) {
        confirm.setVisibility(View.VISIBLE);
        editText.setEnabled(true);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, editText.getPaintFlags());
        editText.setSelection(editText.getText().length());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    MediaStore.Images.Media.insertImage(getContentResolver(), photoFile.getAbsolutePath(),
                            photoFile.getName(), null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" +
                        photoFile.getAbsolutePath())));
                Glide.with(this).load(photoFile).into(bigImg);

                executeChangePortraitTask();
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                selectPic(data);
                Glide.with(this).load(photoFile).into(bigImg);

                executeChangePortraitTask();
            }
        }
    }

    private void executeChangePortraitTask() {
        OkHttpClient client = NetUtils.getInstance().getTokenClientInstance();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserConfig.HOST).addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        ZoneService service = retrofit.create(ZoneService.class);

        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("portrait", photoFile.getName(), fileBody);
        service.changeUserPortrait(/*UserConfig.getToken(InfoAty.this), */part).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                CommonInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    imgUrl = info.getData();
                    Glide.with(InfoAty.this).load(photoFile).thumbnail(0.5f).into(ivPortrait);
                    setBackIntent();
                } else {
                    Toast.makeText(InfoAty.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                    Log.e("InfoAty", info.getStatus());
                    Log.e("InfoAty", info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {
                Log.e("InfoAty", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.translate_dialog_in, R.anim.translate_dialog_out);
    }

    @Override
    public void setPresenter(UserInfoContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateUserInfo(CommonInfo info) {
        if (info.getStatus().equals("200")) {
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
            name = etName.getText().toString();
            signature = etSign.getText().toString();
            setBackIntent();
        } else {
            Toast.makeText(this, info.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setBackIntent() {
        Intent backIntent = new Intent();
        backIntent.putExtra("username", name);
        backIntent.putExtra("signature", signature);
        backIntent.putExtra("portrait_url", imgUrl);
        setResult(RESULT_OK, backIntent);
    }
}

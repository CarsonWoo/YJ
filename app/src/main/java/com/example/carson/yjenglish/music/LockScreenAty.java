package com.example.carson.yjenglish.music;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.music.model.MusicEvent;
import com.example.carson.yjenglish.music.view.MusicActivity;
import com.example.carson.yjenglish.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.wasabeef.glide.transformations.BlurTransformation;
import me.wcy.lrcview.LrcView;

public class LockScreenAty extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private ImageView bg;
    private TextView time;
    private TextView date;
    private TextView title;
    private ImageView former;
    private ImageView latter;
    private ImageView play;
    private Button speed;
    private ConstraintLayout moveView;
    private ImageView lockImg;
    private LrcView lrcView;

    private float startY;

    private boolean isPlaying;

    private MusicEvent mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock_screen);
        mEvent = EventBus.getDefault().getStickyEvent(MusicEvent.class);
        initViews();

    }

    private void initViews() {
        bg = findViewById(R.id.lock_bg);
        time = findViewById(R.id.lock_time);
        date = findViewById(R.id.lock_date);
        title = findViewById(R.id.lock_title);
        former = findViewById(R.id.music_play_former);
        latter = findViewById(R.id.music_play_latter);
        play = findViewById(R.id.music_play);
        speed = findViewById(R.id.music_play_speed);
        moveView = findViewById(R.id.move_view);
        lockImg = findViewById(R.id.lock_anim);
        lrcView = findViewById(R.id.lrc_view);

        former.setOnClickListener(this);
        latter.setOnClickListener(this);
        play.setOnClickListener(this);
        speed.setOnClickListener(this);

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date curDate = new Date();
        String timeStr = df.format(curDate);
        time.setText(timeStr);
        df = new SimpleDateFormat("MM月dd日 E");
        String dateStr = df.format(curDate);
        date.setText(dateStr);

        Drawable drawable = lockImg.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }

        doChangeWork();

        initMoveWork();
    }

    private void doChangeWork() {
        if (mEvent != null) {
            Glide.with(this).load(mEvent.getImgUrl()).thumbnail(0.5f)
                    .bitmapTransform(new BlurTransformation(this, 1, 3))
                    .crossFade()
                    .into(bg);
            title.setText(mEvent.getTitle());
            lrcView.loadLrc(mEvent.getLrcText());
            if (mEvent.isPlaying()) {
                isPlaying = true;
                play.setImageResource(R.drawable.ic_music_play_pause);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initMoveWork() {
        bg.setOnTouchListener(this);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.music_play_former:
                EventBus.getDefault().post("play_former");
                doChangeWork();
                break;
            case R.id.music_play:
                EventBus.getDefault().post("play");
                isPlaying = !isPlaying;
                if (isPlaying) {
                    play.setImageResource(R.drawable.ic_music_play_pause);
                } else {
                    play.setImageResource(R.mipmap.ic_music_play_large);
                }
                break;
            case R.id.music_play_latter:
                EventBus.getDefault().post("play_latter");
                doChangeWork();
                break;
            case R.id.music_play_speed:
                EventBus.getDefault().post("speed_change");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (view.getId() == R.id.lock_bg) {
            int action = event.getAction();
            float y = event.getY();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    startY = y;
                    if (lockImg.getDrawable() instanceof Animatable) {
                        ((Animatable) lockImg.getDrawable()).stop();
                    }
                case MotionEvent.ACTION_MOVE:
                    handleMoveView(y);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    doTriggerEvent(y);
                    break;
            }
        }
        return true;
    }

    private void doTriggerEvent(float y) {
        float moveY = y - startY;
        if (moveY < (-ScreenUtils.getScreenHeight(this) * 0.4)) {
            moveMoveView(ScreenUtils.getScreenHeight(this) - moveView.getBottom(), true);
        } else {
//            moveMoveView(moveView.getTop(), false);
        }

    }

    private void moveMoveView(float to, boolean exit) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(moveView, "translationY", to);
        animator.setDuration(250).start();
        if (exit) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                    super.onAnimationEnd(animation);
                }
            });
        }
    }

    private void handleMoveView(float y) {
        float moveY = y - startY;
        if (moveY > 0) {
            moveY = 0;
        }
        moveView.setTranslationY(moveY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().removeStickyEvent(MusicEvent.class);
    }
}

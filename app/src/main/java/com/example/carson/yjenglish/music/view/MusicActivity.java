package com.example.carson.yjenglish.music.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.music.model.MusicEvent;
import com.example.carson.yjenglish.service.MusicService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import me.wcy.lrcview.LrcView;

public class MusicActivity extends AppCompatActivity implements RememberTab.OnMusicSelectListener,
    DownloadTab.OnMusicSelectListener, LikeTab.OnMusicSelectListener {

    private ImageView back;
    private ImageSwitcher imgBg;
    private TextSwitcher title;
    private CircleImageView photo;
    private ImageView play;
    private ImageView control;
    private Switch mSwitch;
    private TextView curTime;
    private TextView totalTime;
    private SeekBar mSeekBar;
    private ImageView latterPlay;
    private ImageView formerPlay;
    private ImageView musicList;

    private String imgUrl;
    private boolean isPlaying = true;

    private ObjectAnimator photoRotate = null;

    private LrcView mLrcView;

    private MediaPlayer mPlayer;

    private String curTimeStr;

    private static final int UPDATE = 0;

    private MusicConn conn;
    private MusicService.MusicBinder musicControl;

    private Intent musicService;

    public static MusicActivity INSTANCE;


    private String lrcTest = "[ti:Rap God]\n" +
            "[ar:Eminem]\n" +
            "[al:Rap God - Single]\n" +
            "\n" +
            "[00:00.32]Rap God - Eminem\n" +
            "[00:01.51]Look I was gonna go easy on you and\n" +
            "[00:03.75]not to hurt your feelings\n" +
            "[00:05.07]But I’m only going to get this one chance\n" +
            "[00:08.37]\n" +
            "[00:09.74]Something wrong I can feel it\n" +
            "[00:11.93]Its just I feel it in my guts\n" +
            "[00:14.61]Like something about to happen\n" +
            "[00:15.86]\n" +
            "[00:16.54]but I don’t know what\n" +
            "[00:18.11]If that mean what I think it means\n" +
            "[00:19.67]were in trouble big trouble\n" +
            "[00:21.49]And if he’s banana as you say\n" +
            "[00:23.36]I’m not taking any chances\n" +
            "[00:24.80]You’re just what the sound wants\n" +
            "[00:26.48]I'm beginning to feel like a rap god, rap god\n" +
            "[00:29.35]All my people from the front to the back nod back nod\n" +
            "[00:32.54]Now who thinks their arms are long\n" +
            "[00:34.28]enough to slap box slap box\n" +
            "[00:35.78]They said I rap like a robot so call me rapbot\n" +
            "[00:38.23]But for me to rap like a computer\n" +
            "[00:39.54]it’s must be in my genes\n" +
            "[00:40.79]I got a laptop in my back pocket\n" +
            "[00:42.16]My pinnacle walk when I half cock it\n" +
            "[00:43.85]Got a fat knot from that rap profit\n" +
            "[00:45.17]Made a living and killing off it\n" +
            "[00:46.61]Every since Bill Clinton was still in office\n" +
            "[00:48.29]With Monica Lewinsky feeling on his nut sacks\n" +
            "[00:50.35]I’m a emcee still as honest but as rude\n" +
            "[00:52.16]as indecent as hell all syllables\n" +
            "[00:53.78]Killaholic kill em all with\n" +
            "[00:54.91]This ligiti hibidi hibidi hip-hop you\n" +
            "[00:56.35]don’t really want to get into it\n" +
            "[00:57.78]Put  with this back pack\n" +
            "[00:58.71]and a mac in the back of the yak\n" +
            "[01:00.22]back pack rat yap yap yap\n" +
            "[01:01.34]Now at the exact same time I take\n" +
            "[01:02.96]these lyrically acrobats stunts while I’m practicing\n" +
            "[01:04.83]I’ll be able break a mother fucking table\n" +
            "[01:06.51]over the back of a couple of faggets and crack it in half\n" +
            "[01:08.51]Tony realize he was ironic I was under aftermath after the fact\n" +
            "[01:11.51]How could I not blow all I do is drop F bombs\n" +
            "[01:14.06]Give him a wraithbatat rapper having a rough time period\n" +
            "[01:15.93]Here is a maxi pad its actually disasterly bad\n" +
            "[01:18.31]For the  for the master leak destructing master p ass\n" +
            "[01:21.24]I'm beginning to feel like a rap god, rap god\n" +
            "[01:24.30]All my people from the front to the back nod back nod\n" +
            "[01:27.61]Now who thinks their arms are\n" +
            "[01:28.98]long enough to slap box slap box\n" +
            "[01:30.79]Let me show you maintaining\n" +
            "[01:31.72]that shit ain’t that hard that hard\n" +
            "[01:33.97]Everybody wants the key and the secrets\n" +
            "[01:35.41]to rapping mortality like I have god\n" +
            "[01:37.28]Well to be truthful the blue prints\n" +
            "[01:38.52]simply raging youthful exuberance\n" +
            "[01:39.95]Everybody love the root of a nuisance\n" +
            "[01:41.57]50 got up like an asteroid and did\n" +
            "[01:42.98]nothing but shoot to the moon since “phew”\n" +
            "[01:44.85]Emcee get taking to school\n" +
            "[01:46.03]with this music cause I use it as a vehicle\n" +
            "[01:47.67]To bust a rhyme got to lead a school of new students\n" +
            "[01:49.61]Me I’m a product of Rakim Lakim  2pac N.\n" +
            "[01:52.71]\n" +
            "[01:53.27]W.A Cube  Ren  Easy thank you they got slim\n" +
            "[01:56.33]Inspired enough to one day grow up\n" +
            "[01:58.01]blow up and be in a position\n" +
            "[01:59.38]To meet Run DMC and induct them to the motherfucking\n" +
            "[02:02.26]Rock and roll hall of fame even though\n" +
            "[02:04.06]I walk in the church and burst in a ball of flames\n" +
            "[02:06.19]Only hall of fame I be inducted in is the\n" +
            "[02:08.18]alcohol of fame on the wall of shame\n" +
            "[02:09.87]You fags think it’s a game till I walk a flock of flames\n" +
            "[02:13.05]Off of planking tell me what in the fuck are you thinking\n" +
            "[02:16.17]Little gay looking boy so gay\n" +
            "[02:17.67]I can barely say it with a straight face looking boy\n" +
            "[02:19.66]You witnessing a massacre like your watching\n" +
            "[02:21.16]a church gathering take place looking boy\n" +
            "[02:22.72]Oy vey that boy is gay that’s all they say looking boy\n" +
            "[02:26.16]You get a thumbs up pat on the back and\n" +
            "[02:27.84]a way to go from your label everyday looking boy\n" +
            "[02:29.46]Hey looking boy what you say looking boy\n" +
            "[02:30.90]I got a hell yeah from trey looking boy\n" +
            "[02:32.46]I’mma work for everything\n" +
            "[02:33.64]I have never asked nobody for shit\n" +
            "[02:34.96]get outta my face looking boy\n" +
            "[02:35.95]Basically boy you’re never gonna\n" +
            "[02:37.13]be capable to be keeping up with the same pace looking boy\n" +
            "[02:39.07]I'm beginning to feel like a rap god, rap god\n" +
            "[02:42.07]All my people from the front to the back nod back nod\n" +
            "[02:45.78]The way I’m racing around the track call me Nascar Nascar\n" +
            "[02:48.58]There own heart of the trailer park the white trash god\n" +
            "[02:50.77]Kneel before Generals Zod this planet\n" +
            "[02:52.95]krypton no asgard asgard\n" +
            "[02:54.95]So you be Thor and I’ll be Odin you rodent\n" +
            "[02:57.63]I’m omnipotent\n" +
            "[02:58.25]Let off then I’m reloading immediately with there bombs I’m toteing\n" +
            "[03:02.06]And I should not be woken I’m walking dead but\n" +
            "[03:04.62]I’m just a talking head a zombie floating\n" +
            "[03:06.49]But I got your mom deep throating\n" +
            "[03:08.37]I’m out my ramen noodle\n" +
            "[03:09.87]We have nothing in common poodle\n" +
            "[03:11.42]I’m a Doberman pinch yourself\n" +
            "[03:12.98]in the arm and pay homage pupil\n" +
            "[03:14.92]Its me my honesty’s brutal\n" +
            "[03:17.60]But its honesty futile if I don’t utilize\n" +
            "[03:19.85]that I do though\n" +
            "[03:21.25]For good at least once in awhile\n" +
            "[03:22.37]so I wanna make sure somewhere chicken scratch scribble and doodle\n" +
            "[03:24.18]Enough rhymes to maybe to try and help\n" +
            "[03:26.30]get some people through tough times\n" +
            "[03:27.55]But I gotta keep a few punch lines\n" +
            "[03:29.05]Just in case cause even if you’re unsigned\n" +
            "[03:30.86]Rappers are hungry looking at me like it’s lunchtime\n" +
            "[03:33.05]I know there was a time where once\n" +
            "[03:34.54]I was king of the underground but I still rap like\n" +
            "[03:36.79]I’m on a pharaoh  grind\n" +
            "[03:37.91]So I crunch rhymes but sometimes\n" +
            "[03:39.29]when you combine a pill with the skin color of mine\n" +
            "[03:42.08]You get too big and  since you like that one line\n" +
            "[03:45.14]I said  Mathers LP\n" +
            "[03:46.58]\n" +
            "[03:47.14]When I tried  take seven kids from Columbine\n" +
            "[03:50.19]Put 'em all in line add an AK-47 a revolver and one nine\n" +
            "[03:53.63]See if I get away with it now then I ain’t as big as\n" +
            "[03:56.62]I was but I’m morph into an immortal\n" +
            "[03:58.81]Coming through the portal\n" +
            "[03:59.73]But you’re stuck in a time warp from 2004 though and\n" +
            "[04:02.29]I don’t know what the fuck that you rhyme for\n" +
            "[04:04.47]You’re pointless like Rapunzel with fucking cornrolls\n" +
            "[04:05.83]\n" +
            "[04:06.58]fuck being normal\n" +
            "[04:08.70]And I just bought a new ray gun\n" +
            "[04:10.07]from the future to just come and shoot ya\n" +
            "[04:11.57]Like when Fabolous made Ray J mad\n" +
            "[04:13.16]pick a fight Mayweather pants on the door\n" +
            "[04:16.40]Man oh man that was a 24 special on the cable channel\n" +
            "[04:19.65]So Ray J went straight to the radio station the very next day\n" +
            "[04:22.39]Hey fab I’mma kill you lyrics coming at you\n" +
            "[04:24.51]at super sonic speed (Ray J Fag)\n" +
            "[04:26.09]Uh sama lama dama lama you assuming I’m a human\n" +
            "[04:28.09]What I gotta do to get it through to you I’m superhuman\n" +
            "[04:30.39]Innovative made of rubber so anything you saying ricocheting off of me\n" +
            "[04:32.39]never stating more than demonstrating how to\n" +
            "[04:33.51]give a mother fucker  feeling like he’s levitating\n" +
            "[04:35.88]Never fading and I know that the haters are forever waiting\n" +
            "[04:37.65]For the day that they can say I fell off they’d be celebrating\n" +
            "[04:38.80]Cause I know that way to get ‘em motivated\n" +
            "[04:40.42]I make elevating music you make elevator music\n" +
            "[04:42.11]\n" +
            "[04:42.67]Oh he’s too mainstream well that’s what they doing jealous they confuse it\n" +
            "[04:45.66]Its not hip hop its pop cause I found a hella way to fuse it\n" +
            "[04:49.03]With rock shocked rap with Doc don’t  to make them lose it\n" +
            "[04:51.78]I don’t know how to make songs like that I don’t know what words to use\n" +
            "[04:55.33]Let me know when it occurs to you\n" +
            "[04:56.71]While I’m ripping any one of these verses the verse is huge\n" +
            "[04:59.08]inadvertently hurting you\n" +
            "[04:59.89]How many verses I gotta murder to prove\n" +
            "[05:01.76]that if you’re half as nice at songs you can sacrifice virgins too\n" +
            "[05:04.92]School flunky pill junky but look at the accolades the skill brung me\n" +
            "[05:09.66]Full of myself but still hungry\n" +
            "[05:11.41]I bully myself cause I make me do that I put my mind to\n" +
            "[05:14.59]And I’m a million leagues above you\n" +
            "[05:16.85]Ill when I speak in tongues but its still tongue in cheek fuck you\n" +
            "[05:19.91]I’m drunk so Satan take the fucking wheel I’m asleep in the front seat\n" +
            "[05:23.28]Bumping Heavy D and the boys still chunky but funky\n" +
            "[05:26.09]But in my head there’s something I can feel tugging and struggling\n" +
            "[05:29.34]Angels fight with devils and here’s what they want from me\n" +
            "[05:32.83]They asking me to eliminate some of the women hate\n" +
            "[05:35.26]But if you take into consideration the bitter hatred  had\n" +
            "[05:37.07]Then you may be a little patient and more sympathetic to the situation\n" +
            "[05:39.50]And understand the discrimination\n" +
            "[05:40.61]\n" +
            "[05:42.17]But fuck it life’s handing you lemons make lemonade then\n" +
            "[05:44.61]But if I cant batter other women how the fuck am\n" +
            "[05:47.10]I supposed to bake them a cake then\n" +
            "[05:48.41]Don’t mistake it for satan\n" +
            "[05:49.91]It’s a fatal mistake if you think I need to\n" +
            "[05:52.10]be overseas to take a vacation to trip a broad\n" +
            "[05:53.97]And make her fall on her face don’t be a retard\n" +
            "[05:56.47]Be a king think not why be a king when you can be a god";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE:
                    try {
                        mSeekBar.setProgress(musicControl.getCurrentPosition());
                        curTime.setText(parseDuration2Time(musicControl.getCurrentPosition()));
                        mLrcView.updateTime((musicControl.getCurrentPosition()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessageDelayed(UPDATE, 1000);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        Log.e("Music", "onCreate()");
        imgUrl = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2859719634,4239030051&fm=27&gp=0.jpg";
        initViews();
        EventBus.getDefault().register(this);
        INSTANCE = this;
    }

    private void initViews() {
        back = findViewById(R.id.back);
        imgBg = findViewById(R.id.bg_music);
        photo = findViewById(R.id.music_photo);
        play = findViewById(R.id.music_play);
        control = findViewById(R.id.music_control);
        mSwitch = findViewById(R.id.speed_control);
        mLrcView = findViewById(R.id.lrc_view);
        curTime = findViewById(R.id.progress_time);
        totalTime = findViewById(R.id.total_time);
        mSeekBar = findViewById(R.id.music_seek_bar);
        latterPlay = findViewById(R.id.music_play_latter);
        formerPlay = findViewById(R.id.music_play_former);
        musicList = findViewById(R.id.music_play_list);
        title = findViewById(R.id.music_title);

        curTime.setText("00:00");
        curTimeStr = "00:00";
//        if (mPlayer == null) {
//            mPlayer = new MediaPlayer();
//            Log.e("Music", "player = null");
//        } else {
//            Log.e("Music", "player != null");
//        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        latterPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNext();
            }
        });

        formerPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBefore();
            }
        });

        musicList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initMusicList();
            }
        });

        musicService = new Intent(this, MusicService.class);
        conn = new MusicConn();
        startService(musicService);
        bindService(musicService, conn, BIND_AUTO_CREATE);
        initBgPhoto();
        initTitle();
        initPlayAction();
    }

    private void initTitle() {
        title.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tv = new TextView(MusicActivity.this);
                tv.setTextSize(16);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(Color.WHITE);
                return tv;
            }
        });
        title.setText("Rap God");
    }

    private void initMusicList() {
        MyDialogFragment dialogFragment = new MyDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), null);
    }

    private void onBefore() {
        isPlaying = true;
        mHandler.removeMessages(UPDATE);
        musicControl.setDataSource(R.raw.rap_god);
        musicControl.onNext();
        resetControl();
        imgUrl = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2859719634,4239030051&fm=27&gp=0.jpg";
        Glide.with(this).load(imgUrl).thumbnail(0.8f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .bitmapTransform(new BlurTransformation(this, 10, 10))
                .crossFade().into((ImageView) imgBg.getCurrentView());
        Glide.with(this).load(imgUrl).thumbnail(0.8f).crossFade().into(photo);
        title.setText("Rap God");
        initPlayAction();
    }

    private String parseDuration2Time(int duration) {
        float parse2Sec = (float) duration / 1000;
        float parse2Min = parse2Sec / 60;
        int min = (int) parse2Min;
        int sec = (int) (parse2Sec - 60 * min);
        String minStr;
        String secStr;
        if (min < 10) {
            minStr = "0" + min;
        } else {
            minStr = "" + min;
        }
        if (sec < 10) {
            secStr = "0" + sec;
        } else {
            secStr = "" + sec;
        }
        return minStr + ":" + secStr;
    }

    private void initPlayAction() {
        EventBus.getDefault().removeStickyEvent(MusicEvent.class);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlaying = !isPlaying;
                //控制动画效果
                setRotateAnimation(isPlaying);
                //控制播放暂停
                if (isPlaying) {
                    musicControl.play();
                    if (!mHandler.hasMessages(UPDATE)) {
                        mHandler.sendEmptyMessage(UPDATE);
                    }
                    play.setImageResource(R.drawable.ic_music_play_pause);
                } else {
                    musicControl.play();
                    mHandler.removeMessages(UPDATE);
                    play.setImageResource(R.mipmap.ic_music_play_large);
                }
            }
        });
        //设置进度条
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int pos = seekBar.getProgress();
                musicControl.seekTo(pos);
                mLrcView.updateTime(seekBar.getProgress());
            }
        });
        //设置滚动歌词
        mLrcView.loadLrc(lrcTest);
        //与其他页面交互
        EventBus.getDefault().postSticky(new MusicEvent(imgUrl, "Rap God", lrcTest, isPlaying));
    }

    private void onNext() {
//        mPlayer.reset();
        isPlaying = true;
        mHandler.removeMessages(UPDATE);
        musicControl.setDataSource(R.raw.rap_god);
        musicControl.onNext();
        resetControl();
//        unbindService(conn);
        imgUrl = "http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg";
        Glide.with(this).load(imgUrl).thumbnail(0.8f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .bitmapTransform(new BlurTransformation(this, 10, 10))
                .crossFade().into((ImageView) imgBg.getCurrentView());
        Glide.with(this).load(imgUrl).thumbnail(0.8f).crossFade().into(photo);
        title.setText("Rap God");
        initPlayAction();
    }

    private void setRotateAnimation(boolean isPlaying) {
        ObjectAnimator controlRotate;
        if (isPlaying) {
            controlRotate = ObjectAnimator.ofFloat(control, "rotation",
                    control.getRotation(), -8f);
            if (photoRotate == null) {
                photoRotate = ObjectAnimator.ofFloat(photo, "rotation", 0.0f, 359.9f);
                photoRotate.setRepeatCount(ValueAnimator.INFINITE);
                photoRotate.setRepeatMode(ValueAnimator.RESTART);
                if (mSwitch.isChecked()) {
                    photoRotate.setDuration(4500);
                } else {
                    photoRotate.setDuration(3000);
                }
                photoRotate.setInterpolator(new LinearInterpolator());
                photoRotate.start();
            } else {
                photoRotate.resume();
            }
            controlRotate.setDuration(300);
            controlRotate.start();
        } else {
            controlRotate = ObjectAnimator.ofFloat(control, "rotation",
                    control.getRotation(), -25f);
            controlRotate.setDuration(300);
            controlRotate.start();
            if (photoRotate != null) {
                photoRotate.pause();
            }
        }
    }

    private void initBgPhoto() {
        imgBg.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView img = new ImageView(MusicActivity.this);
                img.setLayoutParams(new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
                ));
                img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return img;
            }
        });
        Glide.with(this).load(imgUrl).thumbnail(0.8f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .bitmapTransform(new BlurTransformation(this, 10, 10))
                .crossFade().into((ImageView) imgBg.getCurrentView());
        Glide.with(this).load(imgUrl).thumbnail(0.8f).crossFade().into(photo);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        moveTaskToBack(true);
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    public void onMusicSelect(String path) {
        Log.e("Music", "music click");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (musicControl != null) {
            mHandler.sendEmptyMessage(UPDATE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        stopService(musicService);
        EventBus.getDefault().unregister(this);
        Log.e("Music", "onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacksAndMessages(null);
    }

    private class MusicConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //获得service中的IBinder
            musicControl = (MusicService.MusicBinder) iBinder;
            resetControl();
            musicControl.setCompleteListner(new MusicService.OnCompleteListener() {
                @Override
                public void onComplete() {
                    onNext();
                }
            });
            Log.e("MusicActivity", "onServiceConnect");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    private void resetControl() {
        //设置进度条最大值
        mSeekBar.setMax(musicControl.getDuration());
        //设置进度条进度
        mSeekBar.setProgress(0);
        mSeekBar.setEnabled(true);
        curTimeStr = "00:00";
        curTime.setText(curTimeStr);
        totalTime.setText(parseDuration2Time(musicControl.getDuration()));
        setRotateAnimation(true);
        play.setImageResource(R.drawable.ic_music_play_pause);
        if (!mHandler.hasMessages(UPDATE)) {
            mHandler.sendEmptyMessage(UPDATE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(String msg) {
        switch (msg) {
            case "play_former":
                onBefore();
                break;
            case "play":
                isPlaying = !isPlaying;
                setRotateAnimation(isPlaying);
                if (isPlaying) {
                    musicControl.play();
                    if (!mHandler.hasMessages(UPDATE)) {
                        mHandler.sendEmptyMessage(UPDATE);
                    }
                    play.setImageResource(R.drawable.ic_music_play_pause);
                } else {
                    musicControl.play();
                    mHandler.removeMessages(UPDATE);
                    play.setImageResource(R.mipmap.ic_music_play_large);
                }
                break;
            case "play_latter":
                onNext();
                break;
            case "speed_change":
                break;
        }
    }

}

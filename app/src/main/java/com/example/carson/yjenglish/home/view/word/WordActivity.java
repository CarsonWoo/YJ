package com.example.carson.yjenglish.home.view.word;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.carson.yjenglish.DownloadService;
import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.customviews.CorrectOrWrongImageView;
import com.example.carson.yjenglish.customviews.CorrectOrWrongTextView;
import com.example.carson.yjenglish.home.HomeService;
import com.example.carson.yjenglish.home.WordInfoContract;
import com.example.carson.yjenglish.home.WordInfoTask;
import com.example.carson.yjenglish.home.model.word.WordInfo;
import com.example.carson.yjenglish.home.presenter.WordPresenter;
import com.example.carson.yjenglish.home.view.HomeFragment;
import com.example.carson.yjenglish.net.NullOnEmptyConverterFactory;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.FileUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class WordActivity extends AppCompatActivity implements WordInfoContract.View {

    private final String TAG = getClass().getSimpleName();

    public static final int REQUEST_TO_DETAIL_NEW = 1000;
    public static final int REQUEST_TO_DETAIL_OLD = 1001;
    public static final int RESULT_WORD_NEXT = 1002;
    public static final int RESULT_WORD_PASS = 1003;

    private int type;//判断是再来20个 还是开始背单词的类型

    private final int TYPE_GRAPH = 0x1011;//图册选择
    private final int TYPE_MEAN = 0x1012;//释义选择

    private List<WordInfo.ListObject.WordData> newList;
    private List<WordInfo.ListObject.WordData> oldList;
    private List<WordInfo.ListObject.WordData> totalList = new ArrayList<>();

    private List<WordInfo.ListObject.WordData> passList = new ArrayList<>();

    private int mTotalSize;

    private FloatingActionButton sound;

    private ImageView back;
    private ProgressBar mProgressBar;
    private TextView pass;
    private TextView senStart;
    private TextView word;
    private TextView soundMark;
    private TextSwitcher mTextSwitcher;
    private FrameLayout mLoadingView;
    private View divider;

    private CorrectOrWrongImageView pic1, pic2, pic3, pic4;//分别对应于四张图片
    private CorrectOrWrongTextView tran1, tran2, tran3, tran4;

    private List<CorrectOrWrongImageView> mImgs = new ArrayList<>();
    private List<CorrectOrWrongTextView> mTexts = new ArrayList<>();
    private FrameLayout mWordContainer;
    private int correctAnswer = 1;

    private int mCurPos = 1;
    private int mTotalProgress = 1;
    private int wrongCount = 0;

    private MediaPlayer soundPlayer;

    private int mWordType = TYPE_GRAPH;
    private int mLastType;

    private boolean isReviewed = false;

    private boolean isOnBackPressed = false;

    private WordInfoContract.Presenter mPresenter;
    private WordPresenter wordPresenter;

    private int tipsCount;

    private SoundPool correctRingTone, wrongRingTone;
    private int correctStreamId, wrongStreamId;

    private SharedPreferences sp;

    private boolean isFileSuccess = false;

    private String filePath;

    private boolean isFileDirectoryExist = false;

    private String word_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        getWindow().setStatusBarColor(Color.WHITE);

        setContentView(R.layout.activity_word);

        //保持屏幕亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sp = getSharedPreferences("YJEnglish", MODE_PRIVATE);



        filePath = Environment.getExternalStorageDirectory().getPath() + "/背呗背单词/"
                + UserConfig.getSelectedPlan(this) + "/";

        //为了不让系统检测到图片
        String fileName = ".nomedia";
        File nonMediaFile = new File(filePath + fileName);
        if (!nonMediaFile.exists()) {
            try {
                nonMediaFile.createNewFile();
                Log.e(TAG, "create non media file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//
//        File directory = new File(filePath);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }

        bindViews();
        Intent fromIntent = getIntent();
        correctRingTone = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes
                (new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()).build();
        correctRingTone.load(this, R.raw.correct, 1);
        wrongRingTone = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes
                (new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()).build();
        wrongRingTone.load(this, R.raw.wrong, 1);
        type = fromIntent.getIntExtra("type", HomeFragment.REQUEST_WORD_CODE);
        tipsCount = getSharedPreferences("YJEnglish", MODE_PRIVATE).getInt("tips_count", 0);
        Log.e(TAG, tipsCount + "");
        executeLoadTask();
    }

    private void bindViews() {
        sound = findViewById(R.id.fab_sound);
        back = findViewById(R.id.back);
        pass = findViewById(R.id.pass);
        divider = findViewById(R.id.divider);
        mProgressBar = findViewById(R.id.progress_bar);
        senStart = findViewById(R.id.sentence);
        word = findViewById(R.id.word);
        soundMark = findViewById(R.id.soundmark);
        mTextSwitcher = findViewById(R.id.text_switcher);
        mWordContainer = findViewById(R.id.word_container);
        mLoadingView = findViewById(R.id.loading_view);
        mLoadingView.setVisibility(View.VISIBLE);

        soundPlayer = new MediaPlayer();
        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tv = new TextView(WordActivity.this);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                tv.setTextColor(Color.DKGRAY);
                return tv;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!soundPlayer.isPlaying()) {
                    soundPlayer.start();
                }
                final Drawable drawable = sound.getDrawable();
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((Animatable) drawable).stop();
                        }
                    }, 2000);
                }
            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mTotalProgress ++;
                if (tipsCount < 5) {
                    showmDialog();
                } else {
                    doPassWork();
                }
            }

        });
    }

    private void showmDialog() {
        SharedPreferences.Editor editor = getSharedPreferences("YJEnglish", MODE_PRIVATE).edit();
        editor.putInt("tips_count", tipsCount + 1).apply();
        tipsCount++;
        DialogUtils dialogUtils = DialogUtils.getInstance(WordActivity.this);
        AlertDialog mDialog = dialogUtils.newTipsDialog("小语知道你学会该单词了，不会再让这个单词出现了噢~");
        dialogUtils.setTipsListener(new DialogUtils.OnTipsListener() {
            @Override
            public void onConfirm() {
                doPassWork();
            }

            @Override
            public void onCancel() {

            }
        });
        dialogUtils.show(mDialog);
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = ScreenUtils.dp2px(WordActivity.this, 260);
        params.height = ScreenUtils.dp2px(WordActivity.this, 240);
        mDialog.getWindow().setAttributes(params);
    }

    private void doPassWork() {
//        mTotalProgress ++;
        int level = Integer.parseInt(totalList.get(mCurPos - 1).getLevel());
        WordInfo.ListObject.WordData data = totalList.get(mCurPos - 1);
        data.setLevel("5");
        totalList.remove(data);
        passList.add(data);
        if (level < 2) {
            //新单词点了pass
            newList.remove(data);
            if (mCurPos == totalList.size() + 1) {
                //当前位置到了totalList的尽头
                if (mWordType == TYPE_GRAPH) {
                    //还能进行复习
                    if (newList.size() > 0) {
                        mCurPos -= newList.size();
                        if (level < 1) {
                            mTotalProgress += 4;
                            //还能进行复习
                            Toast.makeText(WordActivity.this, "下面进行复习噢~", Toast.LENGTH_SHORT).show();
                            isReviewed = true;
                            initTextViews();
                            initSound();
                            initPics();
                        } else {
                            mTotalProgress += 3;
                            //进行复习释义
                            mWordType = TYPE_MEAN;
                            loadDatas();
                        }
                    } else {
                        doPostWork();
                    }
                } else {
                    doPostWork();
                }
            } else {
                if (mWordType == TYPE_GRAPH) {
                    mTotalProgress += 2;
                    initTextViews();
                    initSound();
                    initPics();
                } else {
                    mTotalProgress ++;
                    initTextViews();
                    initSound();
                    initList();
                }
            }
        } else {
            mTotalProgress ++;
            //旧单词点了pass
            oldList.remove(data);
//            initTextViews();
//            initSound();
            mWordType = TYPE_GRAPH;
            loadDatas();
        }
    }

    private void executeLoadTask() {
        WordInfoTask task = WordInfoTask.getInstance();
        wordPresenter = new WordPresenter(task, this);
        this.setPresenter(wordPresenter);
        mPresenter.getInfo(UserConfig.getToken(this));
    }

    private void initViews() {
        initLoadingView();
    }

    private void doPostWork() {

        /**
         * 可作为清理缓存的一部分
         */
//        File mDirectory = new File(Environment.getExternalStorageDirectory().getPath() +
//                "/YuJingRecorder");
//        File[] mRecorders = mDirectory.listFiles();
//        for (File file : mRecorders) {
//            if (file.exists()) {
//                file.delete();
//            }
//        }
//        Log.e(TAG, new Date().getTime() + "");

        if (tran1 != null) {
            tran1.setEnabled(false);
            tran2.setEnabled(false);
            tran3.setEnabled(false);
            tran4.setEnabled(false);
            tran1.setClickable(false);
            tran2.setClickable(false);
            tran3.setClickable(false);
            tran4.setClickable(false);
        }

        if (pic1 != null) {
            pic1.setEnabled(false);
            pic2.setEnabled(false);
            pic3.setEnabled(false);
            pic4.setEnabled(false);
            pic1.setClickable(false);
            pic2.setClickable(false);
            pic3.setClickable(false);
            pic4.setClickable(false);
        }

        DialogUtils utils = DialogUtils.getInstance(this);
        //到时需要将asGif参数调为true
        final Dialog dialog = utils.newCommonDialog("正在上传中", R.mipmap.gif_loading_video, true);
        dialog.setCanceledOnTouchOutside(false);
        utils.show(dialog);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = ScreenUtils.dp2px(WordActivity.this, 260);
        lp.height = ScreenUtils.dp2px(WordActivity.this, 240);
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        if (Build.VERSION.SDK_INT >= 24) {
            mProgressBar.setProgress(100, true);
        } else {
            mProgressBar.setProgress(100);
        }
        totalList.addAll(passList);
//        for (WordInfo.ListObject.WordData data : totalList) {
//            Log.e(TAG, "level = " + data.getLevel());
//        }

        if (word_list == null || word_list.isEmpty()) {
            Gson gson = new Gson();
            word_list = gson.toJson(totalList, new TypeToken<ArrayList<WordInfo.ListObject.WordData>>(){}.getType());
            Log.e(TAG, word_list);
        }
//        Log.e(TAG, word_list);
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).postWords(UserConfig.getToken(this), word_list)
                .enqueue(new Callback<CommonInfo>() {
                    @Override
                    public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                        CommonInfo info = response.body();
                        if (info.getStatus().equals("200")) {
                            dialog.dismiss();
                            Intent backIntent = new Intent();
                            int length;
                            if (!UserConfig.getDailyWord(WordActivity.this).isEmpty()) {
                                if (newList.size() < Integer.parseInt(UserConfig.getDailyWord(WordActivity.this))) {
                                    length = newList.size() + passList.size();
                                } else {
                                    length = newList.size();
                                }
                            } else {
                                length = newList.size();
                            }
                            backIntent.putExtra("learned_word", String.valueOf(length));
                            if (type == HomeFragment.REQUEST_WORD_CODE) {
                                setResult(HomeFragment.RESULT_WORD_OK, backIntent);
                            } else {
                                setResult(RESULT_OK, backIntent);
                            }
//                            onBackPressed();
                            finishAfterTransition();
                            overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonInfo> call, Throwable t) {
                        dialog.dismiss();
                        DialogUtils du = DialogUtils.getInstance(WordActivity.this);
                        Dialog errorDialog = du.newCommonDialog("小呗建议您换一个网络好一点的环境去加载噢~",
                                R.mipmap.welfare_place_holder, false);
                        errorDialog.show();
                        WindowManager.LayoutParams lp = errorDialog.getWindow().getAttributes();
                        lp.width = ScreenUtils.dp2px(WordActivity.this, 260);
                        lp.height = ScreenUtils.dp2px(WordActivity.this, 240);
                        lp.gravity = Gravity.CENTER;
                        errorDialog.getWindow().setAttributes(lp);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                errorDialog.dismiss();
                                doPostWork();
                            }
                        }, 1500);
//                        doPostWork();
                    }
                });
    }

    private void initLoadingView() {
        getWindow().setStatusBarColor(Color.BLACK);
        //加载页
        mLoadingView.removeAllViews();
        View loadingView = LayoutInflater.from(this).inflate(R.layout.loading_view, mLoadingView,
                false);
        ImageView bg = loadingView.findViewById(R.id.img);
        ImageView loadingBack = loadingView.findViewById(R.id.back);
        Glide.with(this).load(R.mipmap.word_loading_img)
                .listener(new RequestListener<Integer, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (bg.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            bg.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        return false;
                    }
                })
                .thumbnail(0.5f)
                .into(bg);
        final ProgressBar loadingProgress = loadingView.findViewById(R.id.progress_bar);
        mLoadingView.addView(loadingView, -1, -1);

        File tmpFile;
        if (totalList.get(0).getPic().endsWith("jpg")) {
            tmpFile = new File(filePath + totalList.get(0).getWord() + ".jpg");
        } else {
            tmpFile = new File(filePath + totalList.get(0).getWord() + ".png");
        }
        if (tmpFile.exists()) {
            //存在则可加载快一点
            final CountDownTimer timer = new CountDownTimer(3000, 100) {
                @Override
                public void onTick(long l) {
//                Log.e(TAG, l + "");
                    if (Build.VERSION.SDK_INT >= 24) {
                        loadingProgress.setProgress((int) (((float) (3000 - l) / 3000) * 100), true);
                    } else {
                        loadingProgress.setProgress((int) (((float) (3000 - l) / 3000) * 100));
                    }
                }

                @Override
                public void onFinish() {
                    Log.e(TAG, "onFinish");
                    getWindow().setStatusBarColor(Color.TRANSPARENT);
                }
            }.start();
            mLoadingView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isOnBackPressed) {
                        //判断是否下载过此单词包 若下载过则直接从数据库中读取
                        mLoadingView.removeAllViews();
                        mLoadingView.setVisibility(View.GONE);
                        timer.cancel();
//                    loadJsonData();
                        loadDatas();
                        mProgressBar.setVisibility(View.VISIBLE);
                        back.setVisibility(View.VISIBLE);
                        pass.setVisibility(View.VISIBLE);
                        sound.setVisibility(View.VISIBLE);
                        divider.setVisibility(View.VISIBLE);
                    }

                }
            }, 3000);
        } else {
            final CountDownTimer timer = new CountDownTimer(5000, 100) {
                @Override
                public void onTick(long l) {
//                Log.e(TAG, l + "");
                    if (Build.VERSION.SDK_INT >= 24) {
                        loadingProgress.setProgress((int) (((float) (5000 - l) / 5000) * 100), true);
                    } else {
                        loadingProgress.setProgress((int) (((float) (5000 - l) / 5000) * 100));
                    }
                }

                @Override
                public void onFinish() {
                    Log.e(TAG, "onFinish");
                    getWindow().setStatusBarColor(Color.TRANSPARENT);
                }
            }.start();
            mLoadingView.postDelayed(() -> {
                if (!isOnBackPressed) {
                    //判断是否下载过此单词包 若下载过则直接从数据库中读取
                    mLoadingView.removeAllViews();
                    mLoadingView.setVisibility(View.GONE);
                    timer.cancel();
//                    loadJsonData();
                    loadDatas();
                    mProgressBar.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                    pass.setVisibility(View.VISIBLE);
                    sound.setVisibility(View.VISIBLE);
                    divider.setVisibility(View.VISIBLE);
                }

            }, 5000);
        }

        loadingBack.setOnClickListener(view -> {
            isOnBackPressed = true;
//                onBackPressed();
            finishAfterTransition();
            overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
        });


    }

    private void loadDatas() {
        initSound();
        initTextViews();
        mWordContainer.removeAllViews();
        if (mWordType == TYPE_GRAPH) {
            addPicView();
            initPics();
        } else if (mWordType == TYPE_MEAN) {
            addTranView();
            initList();
        }
    }

    private void initSound() {
        String word = totalList.get(mCurPos - 1).getWord();
        String filePath = Environment.getExternalStorageDirectory().getPath() + "/背呗背单词/BeibeiRecorder/"
                + word + ".mp3";
        File recorderFile = new File(filePath);
        soundPlayer.reset();
        if (recorderFile.exists()) {
            try {
                soundPlayer.setDataSource(filePath);
                soundPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "online");
            try {
                soundPlayer.setDataSource(totalList.get(mCurPos - 1).getSentence_audio());
                soundPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        soundPlayer.start();
    }

    private void addTranView() {
        View transView = LayoutInflater.from(this).inflate(R.layout.word_mean, mWordContainer, false);
        mWordContainer.addView(transView);
        tran1 = transView.findViewById(R.id.trans_1);
        tran2 = transView.findViewById(R.id.trans_2);
        tran3 = transView.findViewById(R.id.trans_3);
        tran4 = transView.findViewById(R.id.trans_4);
    }

    private void addPicView() {
        View picView = LayoutInflater.from(this).inflate(R.layout.word_graph, mWordContainer, false);
        mWordContainer.addView(picView);
        pic1 = picView.findViewById(R.id.pic_1);
        pic2 = picView.findViewById(R.id.pic_2);
        pic3 = picView.findViewById(R.id.pic_3);
        pic4 = picView.findViewById(R.id.pic_4);
    }

    private void initTextViews() {
        mTextSwitcher.setText("");
        if (totalList.size() >= mCurPos) {
            word.setText(totalList.get(mCurPos - 1).getWord());
            soundMark.setText(totalList.get(mCurPos - 1).getPhonetic_symbol_us());
        }

        if (Build.VERSION.SDK_INT >= 24) {
            mProgressBar.setProgress((int) ((float) mTotalProgress * 100 / mTotalSize) , true);
        } else {
            mProgressBar.setProgress((int) ((float) mTotalProgress * 100 / mTotalSize));
        }
        Log.e(TAG, ((float) mTotalProgress / mTotalSize) + "");
        if (totalList.size() >= mCurPos) {
            String sentence = totalList.get(mCurPos - 1).getSentence().trim();
//        Log.e(TAG, sentence.trim());
            if (!sentence.endsWith(".") && !sentence.endsWith("?") && !sentence.endsWith("!")) {
                sentence = sentence + ".";
            }
            String[] sentencePieces = sentence.split(totalList.get(mCurPos - 1).getWord());
            //为句子中的单词设置不同颜色与大小
            try{
                setmSentence(sentencePieces);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void setGraphTextSwitcher(int wrongCount) {
        int level = Integer.parseInt(totalList.get(mCurPos - 1).getLevel());
        if (level < 2) {
            //新单词
            switch (wrongCount) {
                case 1:
                    if (totalList.get(mCurPos - 1).getParaphrase() == null
                            || totalList.get(mCurPos - 1).getParaphrase().isEmpty()) {
                        totalList.get(mCurPos - 1).setParaphrase("null");
                    }
                    mTextSwitcher.setText(totalList.get(mCurPos - 1).getParaphrase());
                    break;
                case 2:
                    mTextSwitcher.setText(totalList.get(mCurPos - 1).getSentence_cn());
                    break;
                case 3:
                    mTextSwitcher.setText(totalList.get(mCurPos - 1).getMeaning());
                    break;
                default:
                    resetCheckImg();
                    goToDetail(REQUEST_TO_DETAIL_NEW);
                    break;
            }
        } else {
            //旧单词
            if (wrongCount == 1) {
                if (totalList.get(mCurPos - 1).getParaphrase() == null
                        || totalList.get(mCurPos - 1).getParaphrase().isEmpty()) {
                    totalList.get(mCurPos - 1).setParaphrase("null");
                }
                mTextSwitcher.setText(totalList.get(mCurPos - 1).getParaphrase());
            } else {
                goToDetail(REQUEST_TO_DETAIL_OLD);
            }
        }

    }

    private void goToDetail(int requestCode) {
        Intent toDetail = new Intent(WordActivity.this, WordDetailActivity.class);
        toDetail.putExtra("word_id", totalList.get(mCurPos - 1).getId());
        toDetail.putExtra("tag", totalList.get(mCurPos - 1).getWord());
        toDetail.putExtra("soundMark", totalList.get(mCurPos - 1).getPhonetic_symbol_us());
        toDetail.putExtra("trans", totalList.get(mCurPos - 1).getMeaning());
        toDetail.putExtra("paraphrase", totalList.get(mCurPos - 1).getParaphrase());
        toDetail.putExtra("sentence", totalList.get(mCurPos - 1).getSentence());
        toDetail.putExtra("sentenceTrans", totalList.get(mCurPos - 1).getSentence_cn());

        String imgUrl;
        if (totalList.get(mCurPos - 1).getPic().endsWith("jpg")) {
            imgUrl = filePath + totalList.get(mCurPos - 1).getWord() + ".jpg";
        } else {
            imgUrl = filePath + totalList.get(mCurPos - 1).getWord() + ".png";
        }
        toDetail.putExtra("imgUrl", imgUrl);
        toDetail.putExtra("pronunciation", totalList.get(mCurPos - 1).getPronunciation_us());
        toDetail.putExtra("synonym", totalList.get(mCurPos - 1).getSynonym());
        toDetail.putExtra("sentence_audio", totalList.get(mCurPos - 1).getSentence_audio());
        toDetail.putExtra("phrase", totalList.get(mCurPos - 1).getPhrase());
        toDetail.putExtra("from_intent", 0);
        startActivityForResult(toDetail, requestCode);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    private void setListTextSwitcher(int wrongCount) {
        int level = Integer.parseInt(totalList.get(mCurPos - 1).getLevel());
        if (level < 2) {
            switch (wrongCount) {
                case 1:
                    if (totalList.get(mCurPos - 1).getParaphrase() == null) {
                        totalList.get(mCurPos - 1).setParaphrase("null");
                    }
                    mTextSwitcher.setText(totalList.get(mCurPos - 1).getParaphrase());
                    break;
                default:
                    resetTextImg();
                    goToDetail(REQUEST_TO_DETAIL_NEW);
                    break;
            }
        } else {
//            /**
//             * 错一次才跳
//             */
            if (wrongCount == 1) {
                if (totalList.get(mCurPos - 1).getParaphrase() == null
                        || totalList.get(mCurPos - 1).getParaphrase().isEmpty()) {
                    totalList.get(mCurPos - 1).setParaphrase("null");
                }
                mTextSwitcher.setText(totalList.get(mCurPos - 1).getParaphrase());
            } else {
                goToDetail(REQUEST_TO_DETAIL_OLD);
            }
        }
    }

    private void resetTextImg() {
        tran1.resetCheckImg();
        tran2.resetCheckImg();
        tran3.resetCheckImg();
        tran4.resetCheckImg();
    }

    private void initList() {
        if (tran1 == null) {
            //说明还未被加载到framelayout中
            addTranView();
        }
        resetTextImg();

        mTexts.clear();
        mTexts.add(tran1);
        mTexts.add(tran2);
        mTexts.add(tran3);
        mTexts.add(tran4);

        List<String> mTrans = new ArrayList<>();

        if (totalList.size() >= 4) {
            int index;
            //取得当前正确的选项
            String correctItem = totalList.get(mCurPos - 1).getMeaning();
            //记录随机数 作为正确选项应放的地方
            index = (int) (Math.random() * 4);
            for (; mTrans.size() < 3; ) {
                int random = (int) (Math.random() * totalList.size());
                Log.e(TAG, "rNum = " + random);
                if (correctItem.equals(totalList.get(random).getMeaning())) {
                    continue;
                }
                boolean isAdded = false;
                for (String text : mTrans) {
                    if (text.equals(totalList.get(random).getMeaning())) {
                        isAdded = true;
                        break;
                    }
                }
                if (!isAdded) {
                    mTrans.add(totalList.get(random).getMeaning());
                }
            }
            mTrans.add(index, correctItem);
            correctAnswer = index + 1;
//            boolean isCorrectInList = false;
//
//            for (int i = 1; mTrans.size() < 4;) {
//                int rNum = (int) (Math.random() * totalList.size())/* + (mCurPos - 1)*/;//index:mCurPos - 1 ~ mCurPos + 9
//                if (rNum > totalList.size() - 1) {
//                    rNum -= totalList.size();
//                }
//                boolean isAdded = false;
//                if (mTrans.size() == 3 && !isCorrectInList) {
//                    mTrans.add(totalList.get(mCurPos - 1).getMeaning());
//                    correctAnswer = 4;
//                    break;
//                }
//                for (String tran : mTrans) {
//                    if (tran.equals(totalList.get(rNum).getMeaning())) {
//                        isAdded = true;
//                        break;
//                    }
//                }
//                if (!isAdded) {
//                    if (rNum == mCurPos - 1) {
//                        correctAnswer = i;
//                        isCorrectInList = true;
//                    }
//                    i++;
//                    mTrans.add(totalList.get(rNum).getMeaning());
//                }
//            }
        } else {
            switch (totalList.size()) {
                case 3:
                case 2:
                case 1:
                    correctAnswer = 1;//设定1为正确答案
                    mTrans.add(totalList.get(mCurPos - 1).getMeaning());
                    addTextList(newList, mTrans);
//                    Log.e(TAG, "mTrans.size() = " + mTrans.size());
                    if (mTrans.size() == 4) {
                        break;
                    }
                    addTextList(oldList, mTrans);
//                    Log.e(TAG, "mTrans.size() = " + mTrans.size());
                    if (mTrans.size() == 4) {
                        break;
                    }
                    addTextList(passList, mTrans);
//                    Log.e(TAG, "mTrans.size() = " + mTrans.size());
                    if (mTrans.size() == 4) {
                        break;
                    }
                    break;
                case 0:
//                    Log.e(TAG, "totalList.size() = 0");
                    break;
                default:
                    break;
            }
        }

        int l = 0, i = 1;
        for (final CorrectOrWrongTextView tv : mTexts) {
            final int j = i;
            tv.setText(mTrans.get(l++));
            tv.setOnClickListener(view -> {
                resetTextImg();
                if (correctAnswer == j) {
                    if (sp.getBoolean("sound", true)) {
                        correctStreamId = correctRingTone.play(1, 1, 1, 0, 0, 1);
                    }
                    tv.setAction(true);
                    setCorrectTextAction(j);
                } else {
                    if (sp.getBoolean("sound", true)) {
                        wrongStreamId = wrongRingTone.play(1, 1, 1, 0, 0, 1);
                    }
                    tv.setAction(false);
                    wrongCount++;
                    setListTextSwitcher(wrongCount);
                }
            });
            i++;
        }
    }

    private void setCorrectTextAction(int position) {
        final List<AnimatorSet> mAnimSets = new ArrayList<>();
        for (int i = 1; i <= mTexts.size(); i++) {
            if (position == i) {
                continue;
            }
            mTexts.get(i - 1).clearWrong();
            AnimatorSet mSet = new AnimatorSet();
            ObjectAnimator rotateStart = ObjectAnimator
                    .ofFloat(mTexts.get(i - 1), "rotationX", 0, 90).setDuration(400);
            ObjectAnimator rotateEnd = ObjectAnimator
                    .ofFloat(mTexts.get(i - 1), "rotationX", 270, 360).setDuration(400);
            mSet.play(rotateStart).before(rotateEnd);
            mAnimSets.add(mSet);
        }
        for (AnimatorSet set : mAnimSets) {
            set.start();
        }

        new Handler().postDelayed(() -> {
            //释义选择正确
            int level = Integer.parseInt(totalList.get(mCurPos - 1).getLevel());
            if (level < 2) {
                //新单词
                if (wrongCount > 0) {
                    //有出错
                    Log.e(TAG, "text has wrong.");
//                        WordInfo.ListObject.WordData data = totalList.get(mCurPos - 1);
//                        totalList.remove(mCurPos - 1);
//                        data.setLevel("1");
//                        totalList.add(data);
//                        initSound();
//                        initTextViews();
//                        initList();
//                        wrongCount = 0;//清零
                    goToDetail(REQUEST_TO_DETAIL_NEW);
                } else {
                    //没出错
                    //将其level设为2
                    totalList.get(mCurPos - 1).setLevel(String.valueOf(level + 1));
                    mCurPos ++;
                    if (mCurPos == totalList.size() + 1) {
                        //post数据到后台
                        if (Build.VERSION.SDK_INT >= 24) {
                            mProgressBar.setProgress(100, true);
                        } else {
                            mProgressBar.setProgress(100);
                        }
                        doPostWork();
                    } else {
                        //继续是释义题 正在复习当天的新单词
                        mTotalProgress ++;
                        initTextViews();
                        initSound();
                        initList();
                    }
                }
            } else {
                //旧单词 则直接进行下一个单词的图册选择 并将其level设为+1
                totalList.get(mCurPos - 1).setLevel(String.valueOf(level + 1));
                mCurPos ++;
                mTotalProgress ++;
                wrongCount = 0;
                //下一环节是图册题
                mWordType = TYPE_GRAPH;
                loadDatas();
            }
        }, 1000);
    }

    private void resetCheckImg() {
        pic1.getCheckImg().setImageResource(R.drawable.bg_alpha);
        pic2.getCheckImg().setImageResource(R.drawable.bg_alpha);
        pic3.getCheckImg().setImageResource(R.drawable.bg_alpha);
        pic4.getCheckImg().setImageResource(R.drawable.bg_alpha);
    }

    private void initPics() {
        if (pic1 == null) {
            addPicView();
        }
        resetCheckImg();
        mImgs.clear();

        mImgs.add(pic1);
        mImgs.add(pic2);
        mImgs.add(pic3);
        mImgs.add(pic4);

        List<String> imgUrls = new ArrayList<>();
//        List<File> fileUrls = new ArrayList<>();

        if (totalList.size() >= 4) {
            int index;
            //取得当前正确的选项
            String correctItem = filePath + totalList.get(mCurPos - 1).getWord();
            if (totalList.get(mCurPos - 1).getPic().endsWith("jpg")) {
                correctItem = correctItem + ".jpg";
            } else {
                correctItem = correctItem + ".png";
            }
            //记录随机数 作为正确选项应放的地方
            index = (int) (Math.random() * 4);
            for (; imgUrls.size() < 3; ) {
                int random = (int) (Math.random() * totalList.size());
                Log.e(TAG, "rNum = " + random);
                String s = filePath + totalList.get(random).getWord();
                if (totalList.get(random).getPic().endsWith("jpg")) {
                    s = s + ".jpg";
                } else {
                    s = s + ".png";
                }
                if (correctItem.equals(s)) {
                    continue;
                }
                boolean isAdded = false;
                for (String url : imgUrls) {
                    String tmp;
                    if (totalList.get(random).getPic().endsWith(".jpg")) {
                        tmp = filePath + totalList.get(random).getWord() + ".jpg";
                    } else {
                        tmp = filePath + totalList.get(random).getWord() + ".png";
                    }
                    if (url.equals(tmp)) {
                        isAdded = true;
                        break;
                    }
                }
                if (!isAdded) {
                    String url = filePath + totalList.get(random).getWord();
                    if (totalList.get(random).getPic().endsWith("jpg")) {
                        url = url + ".jpg";
                    } else {
                        url = url + ".png";
                    }
                    imgUrls.add(url);
                }
            }
            imgUrls.add(index, correctItem);
            correctAnswer = index + 1;
//            boolean isCorrectInList = false;
//            Log.e(TAG, "mCurPos = " + mCurPos);
//            for (int i = 1; imgUrls.size() < 4;) {
//                int rNum = (int) (Math.random() * totalList.size())/* + (mCurPos - 1)*/;
//                if (rNum > totalList.size() - 1) {
//                    rNum -= totalList.size();
//                }
//                Log.e(TAG, "rNum = " + rNum);//index:mCurPos - 1 ~ mCurPos + 4
//                boolean isAdded = false;
//                if (imgUrls.size() == 3 && !isCorrectInList) {
//                    //如果正确答案在size = 3时还没有加进来过
////                    File file = new File(filePath + totalList.get(mCurPos - 1).getWord() + ".jpg");
////                    imgUrls.add(totalList.get(mCurPos - 1).getPic());
//                    if (totalList.get(mCurPos - 1).getPic().endsWith("jpg")) {
//                        imgUrls.add(filePath + totalList.get(mCurPos - 1).getWord() + ".jpg");
//                    } else {
//                        imgUrls.add(filePath + totalList.get(mCurPos - 1).getWord() + ".png");
//                    }
////                    fileUrls.add(file);
//                    correctAnswer = 4;
//                    break;
//                }
//                for (String url : imgUrls) {
////                    if (url.equals(totalList.get(rNum).getPic())) {
////                        isAdded = true;
////                        break;
////                    }
//                    String s;
//                    if (totalList.get(rNum).getPic().endsWith(".jpg")) {
//                        s = filePath + totalList.get(rNum).getWord() + ".jpg";
//                    } else {
//                        s = filePath + totalList.get(rNum).getWord() + ".png";
//                    }
//                    if (url.equals(s)) {
//                        isAdded = true;
//                        break;
//                    }
//                }
//                if (!isAdded) {
//                    if (rNum == mCurPos - 1) {
//                        //rNum == mCurPos - 1 则为正确答案
//                        correctAnswer = i;//index:1-4
//                        isCorrectInList = true;
//                    }
//                    i++;
//                    if (totalList.get(rNum).getPic().endsWith("jpg")) {
//                        imgUrls.add(filePath + totalList.get(rNum).getWord() + ".jpg");
//                    } else {
//                        imgUrls.add(filePath + totalList.get(rNum).getWord() + ".png");
//                    }
////                    imgUrls.add(totalList.get(rNum).getPic());
//                }
//            }
        } else {
            switch (totalList.size()) {
                case 3:
                case 2:
                case 1:
                    correctAnswer = 1;//设定1为正确答案
//                    imgUrls.add(totalList.get(mCurPos - 1).getPic());
                    if (totalList.get(mCurPos - 1).getPic().endsWith("jpg")) {
                        imgUrls.add(filePath + totalList.get(mCurPos - 1).getWord() + ".jpg");
                    } else {
                        imgUrls.add(filePath + totalList.get(mCurPos - 1).getWord() + ".png");
                    }
                    addImgList(newList, imgUrls);
                    if (imgUrls.size() == 4) {
                        break;
                    }

                    addImgList(oldList, imgUrls);
                    if (imgUrls.size() == 4) {
                        break;
                    }

                    addImgList(passList, imgUrls);
                    if (imgUrls.size() == 4) {
                        break;
                    }
                    break;
                case 0:
//                    Log.e(TAG, "totalList.size() = 0");
                    break;
                default:
                    break;
            }
        }

        int l = 0;
        for (CorrectOrWrongImageView img : mImgs) {
            Glide.with(this).load(imgUrls.get(l))
                    .placeholder(R.mipmap.word_placeholder)
                    .thumbnail(0.05f)
//                    .crossFade()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(img.getMainImg());
            l++;
        }
        int i = 1;
        for (CorrectOrWrongImageView img : mImgs) {
            final int j = i;
            img.setOnClickListener(view -> {
                resetCheckImg();
                if (correctAnswer == j) {
                    //证明本图是正确答案
                    if (sp.getBoolean("sound", true)) {
                        correctStreamId = correctRingTone.play(1, 1, 1, 0, 0, 1);
                    }
                    setCorrectPicAction(mImgs.get(j - 1), j);
                } else {
                    if (sp.getBoolean("sound", true)) {
                        wrongStreamId = wrongRingTone.play(1, 1, 1, 0, 0, 1);
                    }
                    setWrongAction(mImgs.get(j - 1));
                }
            });
            i++;
        }
    }

//    private void addFileList(List<WordInfo.ListObject.WordData> mList, List<File> fileList) {
//        if (mList.size() != 0) {
//            for (int i = 0; i < mList.size(); i++) {
//                if (totalList.get(mCurPos - 1).getPic()
//                        .equals(mList.get(i).getPic())) {
//                    continue;
//                }
//                fileList.add(new File(filePath + "/" + mList.get(i).getWord() + ".jpg"));
//                if (fileList.size() == 4) {
//                    //如果满了4 就退出循环
//                    break;
//                }
//            }
//        }
//    }

    private void addImgList(List<WordInfo.ListObject.WordData> mList, List<String> imgList) {
        if (mList.size() != 0) {
            for (int i = 0; i < mList.size(); i++) {
                String s;
                if (totalList.get(mCurPos - 1).getPic().endsWith("jpg")) {
                    s = filePath + totalList.get(mCurPos - 1).getWord() + ".jpg";
                } else {
                    s = filePath + totalList.get(mCurPos - 1).getWord() + ".png";
                }
                String tmpS;
                if (mList.get(i).getPic().endsWith("jpg")) {
                    tmpS = filePath + mList.get(i).getWord() + ".jpg";
                } else {
                    tmpS = filePath + mList.get(i).getWord() + ".png";
                }
                if (s.equals(tmpS)) {
                    continue;
                }
                imgList.add(tmpS);
                if (imgList.size() == 4) {
                    //如果满了4 就退出循环
                    break;
                }
            }
        }
    }

    private void addTextList(List<WordInfo.ListObject.WordData> mList, List<String> textList) {
        if (mList.size() != 0) {
            for (int i = 0; i < mList.size(); i++) {
                if (totalList.get(mCurPos - 1).getMeaning()
                        .equals(mList.get(i).getMeaning())) {
                    continue;
                }
                textList.add(mList.get(i).getMeaning());
                if (textList.size() == 4) {
                    //如果满了4 就退出循环
                    break;
                }
            }
        }
    }

    private void setWrongAction(CorrectOrWrongImageView img) {
        wrongCount++;
        img.getCheckImg().setImageResource(R.drawable.bg_wrong);
        setGraphTextSwitcher(wrongCount);
    }

    private void setCorrectPicAction(CorrectOrWrongImageView img, final int position) {
        //将正确的src覆盖于图片之上
        img.getCheckImg().setImageResource(R.drawable.bg_correct);
        final List<AnimatorSet> mAnimSets = new ArrayList<>();
        for (int i = 1; i <= mImgs.size(); i++) {
            if (position == i) {
                continue;
            }
            mImgs.get(i - 1).getMainImg().setImageResource(R.drawable.bg_gray);
            AnimatorSet mSet = new AnimatorSet();
            /**
             * 测试一下效果
             */
            ObjectAnimator rotateStart = ObjectAnimator
                    .ofFloat(mImgs.get(i - 1), "rotationY", 0, 90).setDuration(400);
            ObjectAnimator rotateEnd = ObjectAnimator
                    .ofFloat(mImgs.get(i - 1), "rotationY", 270, 360).setDuration(400);
            mSet.play(rotateStart).before(rotateEnd);
            mAnimSets.add(mSet);
        }
        for (AnimatorSet set : mAnimSets) {
            set.start();
        }
        new Handler().postDelayed(() -> {
            int level = Integer.parseInt(totalList.get(mCurPos - 1).getLevel());
            if (level < 2) {
                //新单词
                //将新学单词的level设为1
                if (level < 1) {
                    totalList.get(mCurPos - 1).setLevel("1");
                    goToDetail(REQUEST_TO_DETAIL_NEW);
                } else {
                    if (wrongCount == 0) {
                        //复习时无错则直接跳下一个单词
                        mCurPos ++;
                        mTotalProgress ++;
                        if (mCurPos == totalList.size() + 1) {
                            //已经图册复习到尽头 开始释义复习
                            mWordType = TYPE_MEAN;
                            mCurPos -= newList.size();
                            loadDatas();
                        } else {
                            initTextViews();
                            initSound();
                            initPics();
                        }
                    } else {
                        //若出错了 跳转单词卡片
                        goToDetail(REQUEST_TO_DETAIL_NEW);
                    }
                }
            } else {
                //旧单词复习并正确时直接进入释义复习题
                mWordType = TYPE_MEAN;
                wrongCount = 0;
                loadDatas();
            }
        }, 1000);
    }

    private void setmSentence(String[] sentencePieces) {
        setTextEmpty();
        if (sentencePieces.length == 1) {
            String start = totalList.get(mCurPos - 1).getWord();
            //除了单词在开头外 还可能使专有名词 或全部大写
            //先转成全部小写 然后去匹配
            if (sentencePieces[0].toLowerCase().startsWith(start)) {
                start = start.substring(0, 1).toUpperCase() + start.substring(1);
                //还需去除开头的单词
                String follow = sentencePieces[0];
                follow = follow.substring(totalList.get(mCurPos - 1).getWord().length());
                if (!follow.startsWith(" ")) {
                    start = start + follow.split(" ")[0];
                    follow = sentencePieces[0].substring(start.length());
                }
                String mSentence = "<big><font color=\"#5ee1c9\">" + start + "</font></big>"
                        + follow;
                senStart.setText(Html.fromHtml(mSentence));
            } else {
                String sentenceLower = sentencePieces[0].toLowerCase();
                if (sentenceLower.contains(start)) {
                    //如果包含有当前单词
                    int index = sentenceLower.indexOf(start);
                    String replacement = "<big><font color=\"#5ee1c9\">" +
                            sentencePieces[0].substring(index, index + start.length()) + "</font></big>";
                    String mSentence = sentencePieces[0].substring(0, index) + replacement +
                            sentencePieces[0].substring(index + start.length());
                    senStart.setText(Html.fromHtml(mSentence));
                } else {
                    //还是匹配不到 则匹配前四个字母
                    //即可能出现变型 匹配前四个字符 若相等 则将其设为高亮
                    String word = totalList.get(mCurPos - 1).getWord();
                    String mSentence = sentencePieces[0];
                    if (word.length() > 4) {
                        String regex = word.substring(0, 4);
                        String target = word;
                        String[] tmp = mSentence.split(" ");
                        for (String s : tmp) {
                            if (s.startsWith(regex)) {
                                target = s;
                                break;
                            }
                        }
                        if (!target.equals(word)) {
                            if (mSentence.split(target).length>1){
                                mSentence = mSentence.split(target)[0] +
                                        "<big><font color=\"#5ee1c9\">"
                                        + target + "</font></big>"+ mSentence.split(target)[1] ;
                            }else{
                                mSentence = mSentence.split(target)[0] +
                                        "<big><font color=\"#5ee1c9\">"
                                        + target + "</font></big>" ;
                            }

                        }
                    }
                    senStart.setText(Html.fromHtml(mSentence));
                }
            }
//            if (start.equalsIgnoreCase(sentencePieces[0].substring(0, start.length()))) {
//                //单词在句子开头 需大写
//                start = start.substring(0, 1).toUpperCase() + start.substring(1);
//                //还需去除开头的单词
//                String follow = sentencePieces[0];
//                follow = follow.substring(totalList.get(mCurPos - 1).getWord().length());
//                if (!follow.startsWith(" ")) {
//                    start = start + follow.split(" ")[0];
//                    follow = sentencePieces[0].substring(start.length());
//                }
//                String mSentence = "<big><font color=\"#5ee1c9\">" + start + "</font></big>"
//                        + follow;
//                senStart.setText(Html.fromHtml(mSentence));
//            } else {
//                //即可能出现变型 匹配前四个字符 若相等 则将其设为高亮
//                String word = totalList.get(mCurPos - 1).getWord();
//                String mSentence = sentencePieces[0];
//                if (word.length() > 4) {
//                    String regex = word.substring(0, 4);
//                    String target = word;
//                    String[] tmp = mSentence.split(" ");
//                    for (String s : tmp) {
//                        if (s.startsWith(regex)) {
//                            target = s;
//                            break;
//                        }
//                    }
//                    if (!target.equals(word)) {
//                        mSentence = mSentence.split(target)[0] +
//                                "<big><font color=\"#5ee1c9\">"
//                                + target + "</font></big>" + mSentence.split(target)[1];
//                    }
//                }
//                senStart.setText(Html.fromHtml(mSentence));
//            }
        } else {
            //只出现一次
            String start = sentencePieces[0];
            String end = sentencePieces[1];
            String target = totalList.get(mCurPos - 1).getWord();
            if (!start.endsWith(" ")) {
                target = start.split(" ")[start.split(" ").length - 1] + target;
                start = start.substring(0, start.length()
                        - start.split(" ")[start.split(" ").length - 1].length());
            }
            if (!end.startsWith(" ")) {
                target = target + end.split(" ")[0];
                end = end.substring(end.split(" ")[0].length());
            }
            if (target.endsWith(".") || target.endsWith("!") || target.endsWith("?")) {
                end = target.substring(target.length() - 1);
                target = target.substring(0, target.length() - 1);
            }
            if (target.endsWith(",")) {
                end = "," + end;
                target = target.substring(0, target.length() - 1);
            }
            if (target.endsWith(";")) {
                end = ";" + end;
                target = target.substring(0, target.length() - 1);
            }

            if (sentencePieces.length > 2) {
                int length = start.length() + target.length() + end.length();
                String remain = totalList.get(mCurPos - 1).getSentence().substring(length);
                String replacement = "<big><font color=\"#5ee1c9\">"
                        + totalList.get(mCurPos - 1).getWord() + "</font></big>";
                remain = remain.replace(totalList.get(mCurPos - 1).getWord(), replacement);
                end = end + remain;
            }

            String mSentence = start + "<big><font color=\"#5ee1c9\">"
                    + target + "</font></big>"
                    + end;
            if (Build.VERSION.SDK_INT >= 24) {
                senStart.setText(Html.fromHtml(mSentence, Html.FROM_HTML_MODE_LEGACY));
            } else {
                senStart.setText(Html.fromHtml(mSentence));
            }
        }
    }

    private void setTextEmpty() {
        senStart.setText("");
        senStart.setTextColor(Color.BLACK);
        senStart.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TO_DETAIL_NEW && resultCode == RESULT_WORD_NEXT) {
            Log.e(TAG, "wrongCount = " + wrongCount);
            if (wrongCount > 0 && isReviewed) {
                Log.e(TAG, "mCurPos = " + mCurPos);
//                如果当前单词在复习图册选择有出错过 则添加其到列表后头
//                然后当前mCurPos不变
                WordInfo.ListObject.WordData mWrongWord = totalList.get(mCurPos - 1);
                totalList.remove(mCurPos - 1);
                wrongCount = 0;
                mWrongWord.setLevel("1");
                totalList.add(mWrongWord);
                initTextViews();
                initSound();
                if (mWordType == TYPE_GRAPH) {
                    initPics();
                } else {
                    initList();
                }
//                if (mWordType == TYPE_GRAPH) {
//                    if (!isReviewed) {
//                        //学习新单词时
//                        mWrongWord.setLevel("0");
//                    } else {
//                        //复习新单词时
//                        mWrongWord.setLevel("1");
//                    }
//                    totalList.add(mWrongWord);
//                    initTextViews();
//                    initSound();
//
//                } else {
//                    mWrongWord.setLevel("1");
//                    totalList.add(mWrongWord);
//                    initTextViews();
//                    initSound();
//                    initList();
//                }
            } else {
                if (wrongCount > 0) {
                    wrongCount = 0;
                }
//                /**
//                 * 需要先进行图册复习
//                 */
                mCurPos++;
                Log.e(TAG, "mCurPos = " + mCurPos);
                if (mCurPos == totalList.size() + 1) {
                    mTotalProgress++;
//                    mWordType = TYPE_MEAN;
                    mCurPos -= newList.size();
                    if (!isReviewed) {
                        //开始进行复习图册
                        Toast.makeText(WordActivity.this, "下面进行复习噢~", Toast.LENGTH_SHORT).show();
                        isReviewed = true;
                        initTextViews();
                        initSound();
                        initPics();
                    } else {
                        //要复习释义选择
                        mWordType = TYPE_MEAN;
                        loadDatas();
                    }
                } else {
                    mTotalProgress++;
                    Log.e(TAG, mTotalProgress + "");
                    //继续图册选择题
                    initTextViews();
                    initSound();
                    initPics();
                }
            }

        } else if (requestCode == REQUEST_TO_DETAIL_OLD && resultCode == RESULT_WORD_NEXT) {
//            if (totalList.size() >= mCurPos) {
//
//            }
            WordInfo.ListObject.WordData mData = totalList.get(mCurPos - 1);
            wrongCount = 0;
            if (mWordType == TYPE_GRAPH) {
                //在图册选择题时就已经选择错误 则将其打回原形 加到新单词队列后
                totalList.remove(mData);
                mData.setLevel("0");
                totalList.add(mData);
                oldList.remove(mData);
                newList.add(mData);
                initTextViews();
                initSound();
                initPics();
            } else {
                //在释义选择题时选择错误
                totalList.remove(mData);
                mData.setLevel("0");
                totalList.add(mData);
                oldList.remove(mData);
                newList.add(mData);
                //进行下一个单词图册的复习 即便是旧单词最后一个 下一个也是图册选择题
                mWordType = TYPE_GRAPH;
                loadDatas();
            }
            mTotalSize += 2;//因为将旧单词添加到新单词列表后 所以newList.size() + 1 oldLise.size() - 1, mTotalSize就会 +1
        } else if (resultCode == RESULT_WORD_PASS) {
            //如果在单词卡片中按了pass
            doPassWork();
        }
    }

    @Override
    public void onBackPressed() {
        View contentView = View.inflate(this, R.layout.layout_tips_dialog, null);
        TextView tipsText = contentView.findViewById(R.id.tips_text);
        TextView tipsCancel = contentView.findViewById(R.id.tips_cancel);
        TextView tipsConfirm = contentView.findViewById(R.id.tips_confirm);
        tipsText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tipsText.setText("现在还不能保存进度噢~程序员小哥哥正在努力开发此功能~");
        final AlertDialog mDialog = new AlertDialog.Builder(this).setView(contentView).create();
        Window window = mDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        mDialog.show();

        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = ScreenUtils.dp2px(this, 260);
            lp.height = ScreenUtils.dp2px(this, 240);
            lp.gravity = Gravity.CENTER;
            window.setAttributes(lp);
        }

        tipsCancel.setOnClickListener(view -> mDialog.dismiss());
        tipsConfirm.setOnClickListener(view -> {
            mDialog.dismiss();
            finishAfterTransition();
            isOnBackPressed = true;
            overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
        });

//        super.onBackPressed();

    }

    @Override
    public void setPresenter(WordInfoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, "小呗检测到网络环境不好噢...请先确保网络通常噢",
                Toast.LENGTH_SHORT).show();
        isOnBackPressed = true;
        finishAfterTransition();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    public void setInfo(WordInfo wordInfo) {
        if (wordInfo.getStatus().equals("200")) {
            newList = wordInfo.getData().getNew_list();
            oldList = wordInfo.getData().getOld_list();

            for (WordInfo.ListObject.WordData data : newList) {
                data.setLevel("0");
            }

            totalList.addAll(oldList);
            totalList.addAll(newList);

            Log.e(TAG, newList.size() + "");
            Log.e(TAG, oldList.size() + "");
            Log.e(TAG, totalList.size() + "");

            //复习旧单词：图册选择->释义选择（错误则将level设为1）
            mTotalSize = newList.size() * 3 + oldList.size();//需要复习旧单词以及开始新单词的学习以及复习新单词
            String serverHost = "http://47.107.62.22/";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(serverHost)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .build();
            DownloadService downloadService = retrofit.create(DownloadService.class);
//            File imgFile = new File(filePath + "/" + totalList.get(0).getWord() + ".jpg");
//            if (imgFile.exists()) {
//                isFileDirectoryExist = true;
//            }
            for (int i = 0; i < totalList.size(); i++) {
                String word = totalList.get(i).getWord();
                String url = totalList.get(i).getSentence_audio().substring(serverHost.length());
                final File file = FileUtils.createRecorderFile(WordActivity.this, word + ".mp3");
//                File imgFile = FileUtils.createImageFile(WordActivity.this, word + ".jpg",
//                        UserConfig.getMyPlan(WordActivity.this));
                Log.e(TAG, word);
                Log.e(TAG, url);
                Log.e(TAG, totalList.get(i).getSentence());
//                Log.e(TAG, imgFile.getPath());
                if (!file.exists()) {
                    doDownLoadTask(word, url, downloadService, file);
                } else {
                    Log.e(TAG, "file is exist");
                }

                File imgFile;
                if (totalList.get(i).getPic().endsWith("jpg")) {
                    imgFile = new File(filePath + totalList.get(i).getWord() + ".jpg");
                } else {
                    imgFile = new File(filePath + totalList.get(i).getWord() + ".png");
                }

                if (!imgFile.exists()) {
                    downloadImg(imgFile, totalList.get(i).getPic());
                }

//                if (!imgFile.exists()) {
//                    doDownLoadTask(word, totalList.get(i).getPic(), downloadService, imgFile);
//                } else {
//                    Log.e(TAG, "img file is exist");
//                }
            }
            initViews();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void downloadImg(File imgFile, String url) {
        new AsyncTask<Void, Integer, File>() {

            @Override
            protected File doInBackground(Void... voids) {
                File tmpFile = null;

                try {
                    FutureTarget<File> future = Glide.with(WordActivity.this)
                            .load(url)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

                    tmpFile = future.get();

                    FileInputStream fis = new FileInputStream(tmpFile);

                    FileOutputStream fos = new FileOutputStream(imgFile);

                    int len = 0;

                    byte[] b = new byte[1024];

                    while ((len = fis.read(b)) != -1) {
                        fos.write(b, 0, len);
                    }

                    fos.close();

                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return tmpFile;
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void doDownLoadTask(final String word, String url, DownloadService downloadService, final File file) {
        new AsyncTask<Void, Integer, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
//                downloadService.downFile(url)
//                        .enqueue(new Callback<ResponseBody>() {
//                            @Override
//                            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        FileUtils.writeFile2Disk(response, file);
//                                        Log.e(TAG, word + "success");
//                                    }
//                                }).start();
//                            }
//
//                            @Override
//                            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                Log.e(TAG, "download Failed");
//                            }
//                        });
                long currentLength = 0;
                try {

                    URL tmpUrl = new URL("http://47.107.62.22/" + url);
                    HttpURLConnection urlConn = (HttpURLConnection) tmpUrl.openConnection();
                    InputStream is = urlConn.getInputStream();

                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    byte[] buffer = new byte[1024];

                    int len;

                    while ((len = is.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                        currentLength += len;
                        Log.e("vivi", "current" + currentLength);
                    }

                    fileOutputStream.close();
                    is.close();
                    Log.e(TAG, word + "success");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPlayer != null) {
            soundPlayer.release();
        }
        if (correctRingTone != null) {
            correctRingTone.release();
        }
        if (wrongRingTone != null) {
            wrongRingTone.release();
        }
    }
}

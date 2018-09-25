package com.example.carson.yjenglish.home.view.word;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
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
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.customviews.CorrectOrWrongImageView;
import com.example.carson.yjenglish.customviews.CorrectOrWrongTextView;
import com.example.carson.yjenglish.home.WordInfoContract;
import com.example.carson.yjenglish.home.WordInfoTask;
import com.example.carson.yjenglish.home.model.word.BaseWord;
import com.example.carson.yjenglish.home.model.word.WordInfo;
import com.example.carson.yjenglish.home.presenter.WordPresenter;
import com.example.carson.yjenglish.home.view.HomeFragment;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.UserConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private boolean isOnBackPressed = false;

    private WordInfoContract.Presenter mPresenter;
    private WordPresenter wordPresenter;

    private int tipsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        bindViews();
        Intent fromIntent = getIntent();
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
                tv.setTextSize(16);
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
        });
        dialogUtils.show(mDialog);
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = ScreenUtils.dp2px(WordActivity.this, 260);
        params.height = ScreenUtils.dp2px(WordActivity.this, 240);
        mDialog.getWindow().setAttributes(params);
    }

    private void doPassWork() {
//        mTotalProgress ++;
        if (mCurPos == totalList.size()) {
            Log.e(TAG, "mCurPos >= totalList.size()");
            if (mWordType == TYPE_GRAPH && totalList.size() > 1) {
                //还能复习释义选择题
                Log.e(TAG, "mWordType = TYPE_GRAPH");
                WordInfo.ListObject.WordData data = totalList.get(mCurPos - 1);
                if (Integer.parseInt(data.getLevel()) < 2) {
                    //新单词
                    mCurPos += 2;
                    newList.remove(data);
                } else {
                    mCurPos += 1;
                    oldList.remove(data);
                }
                data.setLevel("5");
                totalList.remove(mCurPos - 1);
                passList.add(data);
                mCurPos -= totalList.size();
                mWordType = TYPE_MEAN;
                loadDatas();
            } else {
                totalList.get(mCurPos - 1).setLevel("5");
                doPostWork();
            }
        } else {
            Log.e(TAG, "mCurPos < totalList.size()");
            WordInfo.ListObject.WordData data = totalList.get(mCurPos - 1);
            if (Integer.parseInt(data.getLevel()) < 2) {
                //新单词
                mCurPos += 2;
                newList.remove(data);
            } else {
                mCurPos += 1;
                oldList.remove(data);
            }
            data.setLevel("5");
            totalList.remove(mCurPos - 1);
            passList.add(data);
            if (mWordType == TYPE_GRAPH) {
                initTextViews();
                initSound();
                initPics();
            } else {
                initTextViews();
                initSound();
                initList();
            }
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
         * 还要 setResult() 并传回intent putStringExtra("learned_word", newList.size() + oldList.size());
         */

        Log.e(TAG, new Date().getTime() + "");
        //将mediaplayer资源释放
        if (soundPlayer != null) {
            soundPlayer.release();
        }
        DialogUtils utils = DialogUtils.getInstance(this);
        //到时需要将asGif参数调为true
        Dialog dialog = utils.newCommonDialog("正在上传中", R.mipmap.gif_loading_video, true);
        dialog.setCanceledOnTouchOutside(false);
        utils.show(dialog);
        mProgressBar.setProgress(100, true);
        totalList.addAll(passList);
        for (WordInfo.ListObject.WordData data : totalList) {
            Log.e(TAG, "level = " + data.getLevel());
        }
        Gson gson = new Gson();
        String jsonData = gson.toJson(totalList, new TypeToken<ArrayList<WordInfo.ListObject.WordData>>(){}.getType());
        Log.e(TAG, jsonData);
    }

    private void initLoadingView() {
        //加载页
        mLoadingView.removeAllViews();
        View loadingView = LayoutInflater.from(this).inflate(R.layout.loading_view, null, false);
        ImageView bg = loadingView.findViewById(R.id.img);
        ImageView loadingBack = loadingView.findViewById(R.id.back);
        Glide.with(this).load(R.mipmap.word_loading_img).thumbnail(0.5f).into(bg);
        final ProgressBar loadingProgress = loadingView.findViewById(R.id.progress_bar);
        mLoadingView.addView(loadingView);

        final CountDownTimer timer = new CountDownTimer(3000, 100) {
            @Override
            public void onTick(long l) {
//                Log.e(TAG, l + "");
                loadingProgress.setProgress((int) (((float) (3000 - l) / 3000) * 100), true);
            }

            @Override
            public void onFinish() {
                Log.e(TAG, "onFinish");
            }
        }.start();
        loadingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isOnBackPressed = true;
                onBackPressed();
            }
        });
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
        }, 2500);

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
        soundPlayer.reset();
        try {
            soundPlayer.setDataSource(totalList.get(mCurPos - 1).getPronunciation_us());
            soundPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
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
        word.setText(totalList.get(mCurPos - 1).getWord());
        soundMark.setText(totalList.get(mCurPos - 1).getPhonetic_symbol_us());

        mProgressBar.setProgress((int) (((float) mTotalProgress / mTotalSize) * 100), true);
        Log.e(TAG, ((float) mTotalProgress / mTotalSize) + "");
        String sentence = totalList.get(mCurPos - 1).getSentence();
        if (sentence.startsWith(" ")) {
            Log.e(TAG, "before sentence : " + sentence);
            int end;
            String regPattern = "[" + " " + "]*+";
            Pattern pattern = Pattern.compile(regPattern, Pattern.CASE_INSENSITIVE);
            //去掉开头空格
            Matcher matcher = pattern.matcher(sentence);
            if (matcher.lookingAt()) {
                end = matcher.end();
                sentence = sentence.substring(end);
            }
            Log.e(TAG, "after sentence : " + sentence);
        }
        if (!sentence.endsWith(".") && !sentence.endsWith("?") && !sentence.endsWith("!")) {
            sentence = sentence + ".";
        }
        String[] sentencePieces = sentence.split(totalList.get(mCurPos - 1).getWord());
        //为句子中的单词设置不同颜色与大小
        setmSentence(sentencePieces);
    }

    private void setGraphTextSwitcher(int wrongCount) {
        int level = Integer.parseInt(totalList.get(mCurPos - 1).getLevel());
        if (level < 2) {
            //新单词
            switch (wrongCount) {
                case 1:
                    if (totalList.get(mCurPos - 1).getParaphrase() == null) {
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
            goToDetail(REQUEST_TO_DETAIL_OLD);
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
        toDetail.putExtra("imgUrl", totalList.get(mCurPos - 1).getPic());
        toDetail.putExtra("pronunciation", totalList.get(mCurPos - 1).getPronunciation_us());
        toDetail.putExtra("synonym", totalList.get(mCurPos - 1).getSynonym());
        toDetail.putExtra("sentence_audio", totalList.get(mCurPos - 1).getSentence_audio());
        toDetail.putExtra("phrase", totalList.get(mCurPos - 1).getPhrase());
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
            goToDetail(REQUEST_TO_DETAIL_OLD);
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
            boolean isCorrectInList = false;

            for (int i = 1; mTrans.size() < 4;) {
                int rNum = (int) (Math.random() * 4) + (mCurPos - 1);//index:mCurPos - 1 ~ mCurPos + 4
                if (rNum > totalList.size() - 1) {
                    rNum -= totalList.size();
                }
                boolean isAdded = false;
                if (mTrans.size() == 3 && !isCorrectInList) {
                    mTrans.add(totalList.get(mCurPos - 1).getMeaning());
                    correctAnswer = 4;
                    break;
                }
                for (String tran : mTrans) {
                    if (tran.equals(totalList.get(rNum).getMeaning())) {
                        isAdded = true;
                        break;
                    }
                }
                if (!isAdded) {
                    if (rNum == mCurPos - 1) {
                        correctAnswer = i;
                        isCorrectInList = true;
                    }
                    i++;
                    mTrans.add(totalList.get(rNum).getMeaning());
                }
            }
        } else {
            switch (totalList.size()) {
                case 3:
                case 2:
                case 1:
                    correctAnswer = 1;//设定1为正确答案
                    mTrans.add(totalList.get(mCurPos - 1).getMeaning());
                    addTextList(newList, mTrans);
                    Log.e(TAG, "mTrans.size() = " + mTrans.size());
                    if (mTrans.size() == 4) {
                        break;
                    }
                    addTextList(oldList, mTrans);
                    Log.e(TAG, "mTrans.size() = " + mTrans.size());
                    if (mTrans.size() == 4) {
                        break;
                    }
                    addTextList(passList, mTrans);
                    Log.e(TAG, "mTrans.size() = " + mTrans.size());
                    if (mTrans.size() == 4) {
                        break;
                    }
                    break;
                case 0:
                    Log.e(TAG, "totalList.size() = 0");
                    break;
                default:
                    break;
            }
        }

        int l = 0, i = 1;
        for (final CorrectOrWrongTextView tv : mTexts) {
            final int j = i;
            tv.setText(mTrans.get(l++));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetTextImg();
                    if (correctAnswer == j) {
                        tv.setAction(true);
                        setCorrectTextAction(j);
                    } else {
                        tv.setAction(false);
                        wrongCount++;
                        setListTextSwitcher(wrongCount);
                    }
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
        /** 存在bug */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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
                            mProgressBar.setProgress(100, true);
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
                    //下一环节是图册题
                    mWordType = TYPE_GRAPH;
                    loadDatas();
                }
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

        if (totalList.size() >= 4) {
            boolean isCorrectInList = false;
            for (int i = 1; imgUrls.size() < 4;) {
                int rNum = (int) (Math.random() * 4) + (mCurPos - 1);
                if (rNum > totalList.size() - 1) {
                    rNum -= totalList.size();
                }
                Log.e(TAG, "rNum = " + rNum);//index:mCurPos - 1 ~ mCurPos + 4
                boolean isAdded = false;
                if (imgUrls.size() == 3 && !isCorrectInList) {
                    //如果正确答案在size = 3时还没有加进来过
                    imgUrls.add(totalList.get(mCurPos - 1).getPic());
                    correctAnswer = 4;
                    break;
                }
                for (String url : imgUrls) {
                    if (url.equals(totalList.get(rNum).getPic())) {
                        isAdded = true;
                        break;
                    }
                }
                if (!isAdded) {
                    if (rNum == mCurPos - 1) {
                        //rNum == mCurPos - 1 则为正确答案
                        correctAnswer = i;//index:1-4
                        isCorrectInList = true;
                    }
                    i++;
                    imgUrls.add(totalList.get(rNum).getPic());
                }
            }
        } else {
            switch (totalList.size()) {
                case 3:
                case 2:
                case 1:
                    correctAnswer = 1;//设定1为正确答案
                    imgUrls.add(totalList.get(mCurPos - 1).getPic());
                    addImgList(newList, imgUrls);
                    Log.e(TAG, "imgUrl.size() = " + imgUrls.size());
                    if (imgUrls.size() == 4) {
                        break;
                    }
                    addImgList(oldList, imgUrls);
                    Log.e(TAG, "imgUrl.size() = " + imgUrls.size());
                    if (imgUrls.size() == 4) {
                        break;
                    }
                    addImgList(passList, imgUrls);
                    Log.e(TAG, "imgUrl.size() = " + imgUrls.size());
                    if (imgUrls.size() == 4) {
                        break;
                    }
                    break;
                case 0:
                    Log.e(TAG, "totalList.size() = 0");
                    break;
                default:
                    break;
            }
        }

        int l = 0;
        for (CorrectOrWrongImageView img : mImgs) {
            Glide.with(this).load(imgUrls.get(l))
                    .placeholder(R.mipmap.loading)
                    .thumbnail(0.1f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(img.getMainImg());
            l++;
        }
        int i = 1;
        for (CorrectOrWrongImageView img : mImgs) {
            final int j = i;
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetCheckImg();
                    if (correctAnswer == j) {
                        //证明本图是正确答案
                        setCorrectPicAction(mImgs.get(j - 1), j);
                    } else {
                        setWrongAction(mImgs.get(j - 1));
                    }
                }
            });
            i++;
        }
    }

    private void addImgList(List<WordInfo.ListObject.WordData> mList, List<String> imgList) {
        if (mList.size() != 0) {
            for (int i = 0; i < mList.size(); i++) {
                if (totalList.get(mCurPos - 1).getPic()
                        .equals(mList.get(i).getPic())) {
                    continue;
                }
                imgList.add(mList.get(i).getPic());
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int level = Integer.parseInt(totalList.get(mCurPos - 1).getLevel());
                if (level < 2) {
                    //新单词
//                    goToNext();
                    //将新学单词的level设为1
                    totalList.get(mCurPos - 1).setLevel(String.valueOf(level + 1));
                    goToDetail(REQUEST_TO_DETAIL_NEW);
                } else {
                    //旧单词复习并正确时直接进入释义复习题
                    mWordType = TYPE_MEAN;
                    loadDatas();
                }
            }
        }, 1000);
    }

    private void setmSentence(String[] sentencePieces) {
        setTextEmpty();
        if (sentencePieces.length == 1) {
            String start = totalList.get(mCurPos - 1).getWord();
            if (start.equalsIgnoreCase(sentencePieces[0].substring(0, start.length()))) {
                //单词在句子开头 需大写
                start = start.substring(0, 1).toUpperCase() + start.substring(1);
                //还需去除开头的单词
                String follow = sentencePieces[0];
                follow = follow.substring(totalList.get(mCurPos - 1).getWord().length());
                String mSentence = "<font size=\"22\" color=\"#5ee1c9\">" + start + "</font>"
                        + follow;
                senStart.setText(Html.fromHtml(mSentence));
            } else {
                //即可能出现变型
                senStart.setText(sentencePieces[0]);
            }
        } else {
            //可能会出现bug 若同一单词出现两次 则会split多次
            String start = sentencePieces[0];
            String end = sentencePieces[1];
            /* 字号暂时无法放大 */
            String mSentence = start + "<font size=\"22\" color=\"#5ee1c9\">"
                    + totalList.get(mCurPos - 1).getWord() + "</font>"
                    + end;
            senStart.setText(Html.fromHtml(mSentence, Html.FROM_HTML_MODE_LEGACY));
        }
    }

    private void setTextEmpty() {
        senStart.setText("");
        senStart.setTextColor(Color.BLACK);
        senStart.setTextSize(18);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TO_DETAIL_NEW && resultCode == RESULT_WORD_NEXT) {
            Log.e(TAG, "wrongCount = " + wrongCount);
            if (wrongCount > 0) {
                //如果当前当前单词在图册选择有出错过 则添加其到列表后头
                //然后当前mCurPos不变
                WordInfo.ListObject.WordData mWrongWord = totalList.get(mCurPos - 1);
                totalList.remove(mCurPos - 1);
                wrongCount = 0;
                if (mWordType == TYPE_GRAPH) {
                    mWrongWord.setLevel("0");
                    totalList.add(mWrongWord);
                    initTextViews();
                    initSound();
                    initPics();
                } else {
                    mWrongWord.setLevel("1");
                    totalList.add(mWrongWord);
                    initTextViews();
                    initSound();
                    initList();
                }
            } else {
                /**
                 * 需要先进行图册复习
                 */
                mCurPos ++;
                if (mCurPos == totalList.size() + 1) {
                    //说明已经全部load完图册 然后进行释义选择题
                    Toast.makeText(WordActivity.this, "下面进行复习噢~", Toast.LENGTH_SHORT).show();
                    mWordType = TYPE_MEAN;
                    mCurPos -= newList.size();
                    loadDatas();
                } else {
                    mTotalProgress ++;
                    Log.e(TAG, mTotalProgress + "");
                    //继续图册选择题
                    initTextViews();
                    initSound();
                    initPics();
                }
            }
        } else if (requestCode == REQUEST_TO_DETAIL_OLD && resultCode == RESULT_WORD_NEXT) {
            WordInfo.ListObject.WordData mData = totalList.get(mCurPos - 1);
            wrongCount = 0;
            if (mWordType == TYPE_GRAPH) {
                //在图册选择题时就已经选择错误 则将其打回原形 加到新单词队列后
                totalList.remove(mData);
                mData.setLevel("0");
                totalList.add(mData);
                initTextViews();
                initSound();
                initPics();
            } else {
                //在释义选择题时选择错误
                totalList.remove(mData);
                mData.setLevel("1");
                totalList.add(mData);
                //进行下一个单词图册的复习 即便是旧单词最后一个 下一个也是图册选择题
                mWordType = TYPE_GRAPH;
                loadDatas();
            }
            mTotalSize ++;//因为将旧单词添加到新单词列表后 所以newList.size() + 1 oldLise.size() - 1, mTotalSize就会 +1
        } else if (resultCode == RESULT_WORD_PASS) {
            //如果在单词卡片中按了pass
            doPassWork();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    public void setPresenter(WordInfoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showError(String msg) {
        Log.e(TAG, msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setInfo(WordInfo wordInfo) {
        if (wordInfo.getStatus().equals("200")) {
            newList = wordInfo.getData().getNew_list();
            oldList = wordInfo.getData().getOld_list();

            totalList.addAll(oldList);
            totalList.addAll(newList);

            Log.e(TAG, newList.size() + "");
            Log.e(TAG, oldList.size() + "");
            Log.e(TAG, totalList.size() + "");

            //复习旧单词：图册选择->释义选择（错误则将level设为1）
            mTotalSize = newList.size() * 2 + oldList.size();//需要复习旧单词以及开始新单词的学习以及复习新单词

            for (int i = 0; i < newList.size(); i++) {
                Log.e(TAG, i + "");
                WordInfo.ListObject.WordData data = newList.get(i);
                data.setLevel("0");
                Log.e(TAG, data.getLevel());
                Log.e(TAG, data.getMeaning());
//                if (data.getMeaning().length() > )
                Log.e(TAG, data.getPhonetic_symbol_us());
                Log.e(TAG, data.getPronunciation_us());
                Log.e(TAG, data.getPic());
                Log.e(TAG, data.getSentence());
                Log.e(TAG, data.getWord());
                Log.e(TAG, data.getSentence_cn());
            }
            initViews();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPlayer != null) {
            soundPlayer.release();
        }
    }
}

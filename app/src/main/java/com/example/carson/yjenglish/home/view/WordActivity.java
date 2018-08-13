package com.example.carson.yjenglish.home.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.customviews.CorrectOrWrongImageView;
import com.example.carson.yjenglish.customviews.CorrectOrWrongTextView;
import com.example.carson.yjenglish.home.model.BaseWord;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    public static final int REQUEST_TO_DETAIL = 1000;

    private final int TYPE_GRAPH = 0x1011;//图册选择
    private final int TYPE_MEAN = 0x1012;//释义选择

    private FloatingActionButton sound;
    private SoundPool mSoundPool;
    private int streamId;

    private ImageView back;
    private ProgressBar mProgressBar;
    private TextView pass;
    private TextView senStart, senMiddle, senEnd;//分别代表三个TextView 用于装载targetWord
    private TextView word;
    private TextView soundMark;
    private TextSwitcher mTextSwitcher;
    private CorrectOrWrongImageView pic1, pic2, pic3, pic4;//分别对应于四张图片
    private CorrectOrWrongTextView tran1, tran2, tran3, tran4;

    private List<BaseWord> mWords;
    private List<CorrectOrWrongImageView> mImgs = new ArrayList<>();
    private List<CorrectOrWrongTextView> mTexts = new ArrayList<>();
    private FrameLayout mWordContainer;
    private int correctAnswer = 1;

    private int mCurPos = 1;
    private int mTotalProgress = 1;
    private int wrongCount = 0;

    private int mWordType = TYPE_GRAPH;
    private int mLastType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        initViews();
    }

    private void initViews() {
        sound = findViewById(R.id.fab_sound);
        back = findViewById(R.id.back);
        mProgressBar = findViewById(R.id.progress_bar);
        pass = findViewById(R.id.pass);
        senStart = findViewById(R.id.sentence_1);
        senMiddle = findViewById(R.id.sentence_2);
        senEnd = findViewById(R.id.sentence_3);
        word = findViewById(R.id.word);
        soundMark = findViewById(R.id.soundmark);
        mTextSwitcher = findViewById(R.id.text_switcher);
        mWordContainer = findViewById(R.id.word_container);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mSoundPool = new SoundPool.Builder().setMaxStreams(5).setAudioAttributes
                (new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()).build();
        mSoundPool.load(this, R.raw.travel, 1);
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Drawable drawable = sound.getDrawable();
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                    streamId = mSoundPool.play(1, 1, 1, 0, 0, 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((Animatable) drawable).stop();
                            mSoundPool.stop(streamId);
                        }
                    }, 2000);
                }
            }
        });
        loadJsonData();
        loadDatas();

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils dialogUtils = DialogUtils.getInstance(WordActivity.this);
                AlertDialog mDialog = dialogUtils.newTipsDialog("小语知道你学会该单词了，不会再让这个单词出现了噢~");
                dialogUtils.setTipsListener(new DialogUtils.OnTipsListener() {
                    @Override
                    public void onConfirm() {
                        Log.e("Word", "onConfirm");
                    }
                });
                dialogUtils.show(mDialog);
                WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
                params.width = ScreenUtils.dp2px(WordActivity.this, 260);
                params.height = ScreenUtils.dp2px(WordActivity.this, 240);
                mDialog.getWindow().setAttributes(params);
            }
        });
    }

    private void loadJsonData() {
        //TODO 联网后从这里开始读入数据库 若非首次加载 则直接从数据库中拿取 以供fragment使用
        String jsonData = "[{" +
                "\"word\":\"travel\"," +
                "\"trans\":\"n.旅行；v.旅游\"," +
                "\"soundMark\":\"/'trea:val/\"," +
                "\"sentence\":\"I'm going to travel this weekend\"," +
                "\"sentenceTrans\":\"这个周末我打算去旅行\"," +
                "\"paraphrase\":\"the act of going from one place to another\"," +
                "\"wordUrl\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533826261766&di=f516711e90b88667d87da24031eaa3b8&imgtype=0&src=http%3A%2F%2Fpic1.16pic.com%2F00%2F07%2F76%2F16pic_776847_b.jpg\"," +
                "\"wordPronounce\":null," +
                "\"count\":0}," +
                "{" +
                "\"word\":\"go\"," +
                "\"trans\":\"v.走，去\"," +
                "\"soundMark\":\"/gau:/\"," +
                "\"sentence\":\"Go to the left way.\"," +
                "\"sentenceTrans\":\"向左走\"," +
                "\"paraphrase\":\"the act of going\"," +
                "\"wordUrl\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533826261767&di=8b81407c5d3b2a0ff05218b7ea730e5a&imgtype=0&src=http%3A%2F%2Fpic109.nipic.com%2Ffile%2F20160910%2F8890415_143246750926_2.jpg\"," +
                "\"wordPronounce\":null," +
                "\"count\":0}," +
                "{" +
                "\"word\":\"play\"," +
                "\"trans\":\"n.演出；v.玩耍\"," +
                "\"soundMark\":\"/plai/\"," +
                "\"sentence\":\"What an amazing play\"," +
                "\"sentenceTrans\":\"多么令人震惊的表演啊\"," +
                "\"paraphrase\":\"like a show\"," +
                "\"wordUrl\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533826261766&di=c4a6f56197a411cb061a56901c142ea3&imgtype=0&src=http%3A%2F%2Fpic23.photophoto.cn%2F20120410%2F0010023544770104_b.jpg\"," +
                "\"wordPronounce\":null," +
                "\"count\":0}," +
                "{" +
                "\"word\":\"get\"," +
                "\"trans\":\"v.得到\"," +
                "\"soundMark\":\"/gat/\"," +
                "\"sentence\":\"Let's get it.\"," +
                "\"sentenceTrans\":\"让我们动起来吧!\"," +
                "\"paraphrase\":\"a meaning close to gain\"," +
                "\"wordUrl\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg\"," +
                "\"wordPronounce\":null," +
                "\"count\":0}," +
                "{" +
                "\"word\":\"home\"," +
                "\"trans\":\"n.家\"," +
                "\"soundMark\":\"/hau:m/\"," +
                "\"sentence\":\"We're back home\"," +
                "\"sentenceTrans\":\"我们到家了\"," +
                "\"paraphrase\":\"a place where you live\"," +
                "\"wordUrl\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533826261765&di=ddddb62777aef0ec9b91a9029471fa5b&imgtype=0&src=http%3A%2F%2Fpic25.photophoto.cn%2F20121216%2F0010023993176540_b.jpg\"," +
                "\"wordPronounce\":null," +
                "\"count\":0" +
                "}]";
        Gson gson = new Gson();
        jsonData = jsonData.trim();
        mWords = gson.fromJson(jsonData, new TypeToken<ArrayList<BaseWord>>(){}.getType());
    }

    private void loadDatas() {
        initTextViews();
        mWordContainer.removeAllViews();
        if (mWordType == TYPE_GRAPH) {
            addPicView();
            initPics();
        } else if (mWordType == TYPE_MEAN) {
            addTranView();
            initList();
        }

        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tv = new TextView(WordActivity.this);
                tv.setTextSize(16);
                tv.setTextColor(Color.DKGRAY);
                return tv;
            }
        });
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
        word.setText(mWords.get(mCurPos - 1).getWord());
        soundMark.setText(mWords.get(mCurPos - 1).getSoundMark());
        mProgressBar.setProgress((int) (((float) mTotalProgress / (mWords.size() * 2)) * 100));
        String sentence = mWords.get(mCurPos - 1).getSentence();
        String[] sentencePieces = sentence.split(mWords.get(mCurPos - 1).getWord());
        setmSentence(sentencePieces);
    }

    private void goToNext() {

        mCurPos ++;
        mTotalProgress++;
        if (mCurPos > mWords.size()) {
//            Log.e("Word", "到头了");
            if (mWordType == TYPE_GRAPH) {
                mWordType = TYPE_MEAN;
                mCurPos = 1;
                mWordContainer.removeAllViews();
                mTextSwitcher.setText("");
                wrongCount = 0;
                initTextViews();
                initList();
            } else {
                //如果是释义复习题也答完了
                Log.e(TAG, "到头了");
            }
        } else {
            wrongCount = 0;
            initTextViews();
            if (mWordType == TYPE_GRAPH) {
                initPics();
            } else {
                initList();
            }
            mTextSwitcher.setText("");
        }
    }

    private void setGraphTextSwitcher(int wrongCount) {
        switch (wrongCount) {
            case 1:
                mTextSwitcher.setText(mWords.get(mCurPos - 1).getParaphrase());
                break;
            case 2:
                mTextSwitcher.setText(mWords.get(mCurPos - 1).getSentenceTrans());
                break;
            case 3:
                mTextSwitcher.setText(mWords.get(mCurPos - 1).getTrans());
                break;
            default:
                //TODO 需要跳转到单词卡片
                Intent toDetail = new Intent(WordActivity.this, WordDetailActivity.class);
                toDetail.putExtra("tag", mWords.get(mCurPos - 1).getWord());
                toDetail.putExtra("soundMark", mWords.get(mCurPos - 1).getSoundMark());
                toDetail.putExtra("trans", mWords.get(mCurPos - 1).getTrans());
                toDetail.putExtra("paraphrase", mWords.get(mCurPos - 1).getParaphrase());
                toDetail.putExtra("sentence", mWords.get(mCurPos - 1).getSentence());
                toDetail.putExtra("sentenceTrans", mWords.get(mCurPos - 1).getSentenceTrans());
                toDetail.putExtra("imgUrl", mWords.get(mCurPos - 1).getWordUrl());
                startActivityForResult(toDetail, REQUEST_TO_DETAIL);
                break;
        }
    }

    private void setListTextSwitcher(int wrongCount) {
        switch (wrongCount) {
            case 1:
                mTextSwitcher.setText(mWords.get(mCurPos - 1).getParaphrase());
                break;
            default:
                //TODO 需要跳转到单词卡片
                break;
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

        boolean isCorrectInList = false;

        for (int i = 1; mTrans.size() < 4;) {
            Random r = new Random();
            int rNum = Math.abs(r.nextInt() % mWords.size());//index:0-3
            boolean isAdded = false;
            if (mTrans.size() == 3 && !isCorrectInList) {
                mTrans.add(mWords.get(mCurPos - 1).getTrans());
                correctAnswer = 4;
                break;
            }
            for (String tran : mTrans) {
                if (tran.equals(mWords.get(rNum).getTrans())) {
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
                mTrans.add(mWords.get(rNum).getTrans());
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
                        doCorrectAnimation(j);
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

    private void doCorrectAnimation(int position) {
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                goToNext();
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

        boolean isCorrectInList = false;

        for (int i = 1; imgUrls.size() < 4;) {
            Random r = new Random();
            int rNum = Math.abs(r.nextInt() % mWords.size());//index:0-3
            boolean isAdded = false;
            if (imgUrls.size() == 3 && !isCorrectInList) {
                //如果正确答案在size = 3时还没有加进来过
                imgUrls.add(mWords.get(mCurPos - 1).getWordUrl());
                correctAnswer = 4;
                break;
            }
            for (String url : imgUrls) {
                if (url.equals(mWords.get(rNum).getWordUrl())) {
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
                imgUrls.add(mWords.get(rNum).getWordUrl());
            }
        }

        int l = 0;
        for (CorrectOrWrongImageView img : mImgs) {
            Glide.with(this).load(imgUrls.get(l)).thumbnail(0.5f).into(img.getMainImg());
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
                        setCorrectAction(mImgs.get(j - 1), j);
                    } else {
                        setWrongAction(mImgs.get(j - 1));
                    }
                }
            });
            i++;
        }
    }

    private void setWrongAction(CorrectOrWrongImageView img) {
        wrongCount++;
        img.getCheckImg().setImageResource(R.drawable.bg_wrong);
        setGraphTextSwitcher(wrongCount);
    }

    private void setCorrectAction(CorrectOrWrongImageView img, final int position) {
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

                goToNext();
            }
        }, 1000);
    }

    private void setmSentence(String[] sentencePieces) {
        setTextEmpty();
        if (sentencePieces.length == 1) {
            if (mWords.get(mCurPos - 1).getSentence().endsWith(mWords.get(mCurPos - 1).getWord())) {
//                setTextEmpty();
                senMiddle.setText(sentencePieces[0]);
                senEnd.setText(mWords.get(mCurPos - 1).getWord());
                senEnd.setTextColor(Color.parseColor("#5ee1c9"));
                senEnd.setTextSize(22);
            } else {
                //单词在句子开头 需大写
//                setTextEmpty();
                String start = mWords.get(mCurPos - 1).getWord();
                start = start.substring(0, 1).toUpperCase() + start.substring(1);
                senStart.setText(start);
                senStart.setTextColor(Color.parseColor("#5ee1c9"));
                senStart.setTextSize(22);
                //还需去除开头的单词
                String follow = sentencePieces[0];
                follow = follow.substring(mWords.get(mCurPos - 1).getWord().length());
                senMiddle.setText(follow);
            }
        } else {
            //可能会出现bug 若同一单词出现两次 则会split多次
            senStart.setText(sentencePieces[0]);
            senMiddle.setText(mWords.get(mCurPos - 1).getWord());
            senEnd.setText(sentencePieces[1]);
            senMiddle.setTextColor(Color.parseColor("#5ee1c9"));
            senMiddle.setTextSize(22);
        }
    }

    private void setTextEmpty() {
        senStart.setText("");
        senMiddle.setText("");
        senEnd.setText("");
        senStart.setTextColor(Color.BLACK);
        senMiddle.setTextColor(Color.BLACK);
        senEnd.setTextColor(Color.BLACK);
        senStart.setTextSize(18);
        senMiddle.setTextSize(18);
        senEnd.setTextSize(18);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}

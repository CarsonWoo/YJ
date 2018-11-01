package com.example.carson.yjenglish.home.view.word;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.EmptyValue;
import com.example.carson.yjenglish.FullScreenVideo;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.VideoCaptionInfo;
import com.example.carson.yjenglish.VideoCaptionModel;
import com.example.carson.yjenglish.VideoService;
import com.example.carson.yjenglish.customviews.MyVideoView;
import com.example.carson.yjenglish.home.HomeService;
import com.example.carson.yjenglish.home.WordService;
import com.example.carson.yjenglish.home.model.forviewbinder.Change;
import com.example.carson.yjenglish.home.model.forviewbinder.Header;
import com.example.carson.yjenglish.home.model.forviewbinder.Sentence;
import com.example.carson.yjenglish.home.model.forviewbinder.Text;
import com.example.carson.yjenglish.home.model.forviewbinder.Video;
import com.example.carson.yjenglish.home.model.forviewbinder.VideoList;
import com.example.carson.yjenglish.home.model.forviewbinder.WordSituation;
import com.example.carson.yjenglish.home.model.word.WordDetailInfo;
import com.example.carson.yjenglish.home.viewbinder.word.FieldTitleViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.HeaderViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.HorizontalViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.MiniSizeTextViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.PostWrongBinder;
import com.example.carson.yjenglish.home.viewbinder.word.SentenceSoundViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.SentenceViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.SituationViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.TextDrawableViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.TextViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.VideoViewBinder;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ReadAACFileThread;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import me.drakeet.multitype.ClassLinker;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WordDetailActivity extends AppCompatActivity implements View.OnClickListener,
        TextDrawableViewBinder.OnEditSelectListener, HeaderViewBinder.HeaderSoundListener,
        SituationViewBinder.SituationListener, VideoViewBinder.OnVideoClickListener,
        SentenceSoundViewBinder.OnSentenceSoundListener, PostWrongBinder.OnErrorPostListener {

    private RecyclerView recyclerView;
    private ImageView back;
    private ImageView like;
    private TextView pass;
    private Button next;

    private FrameLayout videoContainer, videoFrame;
    private ImageView closeVideo;

    private MultiTypeAdapter adapter;
    private Items items;

    private String wordTag;//用于记录当前的单词 并用作sharedpreference存值
    private String word;
    private String soundMark;
    private String trans;
    private String basicTrans;
    private String paraphrase;
    private String imgUrl;
    private String sentenceTrans;
    private String sentence;
    private String sentence_audio;
    private String synonym;
    private String word_id;
    private String pronunciation;
    private String phrase;

    private int tipsCount;
    private List<WordDetailInfo.Word.VideoInfo> mVideoList;
    private String stem_affix;
    private String word_similar_form;

    private MediaPlayer mPlayer;

    private MyVideoView mVideo;

    private List<VideoCaptionModel> mCaptions;

    private SharedPreferences sp;

    private int fromIntent;

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
        if (StatusBarUtil.checkDeviceHasNavigationBar(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        setContentView(R.layout.activity_word_detail);

        sp = getSharedPreferences("word_favours", MODE_PRIVATE);

        mPlayer = new MediaPlayer();
        tipsCount = getSharedPreferences("YJEnglish", MODE_PRIVATE).getInt("tips_count", 0);
        Log.e("WordDetail", "tipsCount = " + tipsCount);
        initData();
        executeWordTask();
        bindViews();
    }

    private void executeWordTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        WordService service = retrofit.create(WordService.class);
        service.getWordDetail(UserConfig.getToken(this), word_id).enqueue(new Callback<WordDetailInfo>() {
            @Override
            public void onResponse(Call<WordDetailInfo> call, Response<WordDetailInfo> response) {
                WordDetailInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    stem_affix = info.getData().getStem_affix();
                    word_similar_form = info.getData().getWord_of_similar_form();
                    mVideoList = info.getData().getVideo_info();
                    if (soundMark == null || soundMark.isEmpty()) {
                        soundMark = info.getData().getPhonetic_symbol_us();
                    }
                    if (trans == null || trans.isEmpty()) {
                        trans = info.getData().getMeaning();
                    }
                    basicTrans = trans.trim().split("；")[0];
                    if (paraphrase == null || paraphrase.isEmpty()) {
                        paraphrase = info.getData().getParaphrase();
                    }
                    if (sentence == null || sentence.isEmpty()) {
                        sentence = info.getData().getSentence();
                    }
                    if (sentenceTrans == null || sentenceTrans.isEmpty()) {
                        sentenceTrans = info.getData().getSentence_cn();
                    }
                    if (imgUrl == null || imgUrl.isEmpty()) {
                        imgUrl = info.getData().getPic();
                    }
                    if (pronunciation == null || pronunciation.isEmpty()) {
                        pronunciation = info.getData().getPronunciation_us();
                    }
                    if (synonym == null || synonym.isEmpty()) {
                        synonym = info.getData().getSynonym();
                    }
                    if (sentence_audio == null || sentence_audio.isEmpty()) {
                        sentence_audio = info.getData().getSentence_audio();
                    }
                    if (phrase == null || phrase.isEmpty()) {
                        phrase = info.getData().getPhrase();
                    }

                    if (word == null || word.isEmpty()) {
                        word = info.getData().getWord();
                    }
                    initRecycler();
                } else {
                    Toast.makeText(WordDetailActivity.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WordDetailInfo> call, Throwable t) {
                Log.e("WordDetail", "连接超时");
            }
        });
    }

    private void bindViews() {
        recyclerView = findViewById(R.id.recycler_view);
        back = findViewById(R.id.back);
        like = findViewById(R.id.like);
        pass = findViewById(R.id.pass);
        next = findViewById(R.id.next);

        videoContainer = findViewById(R.id.video_container);
        videoFrame = findViewById(R.id.video_frame);
        closeVideo = findViewById(R.id.close_video);

        closeVideo.setOnClickListener(this);
        back.setOnClickListener(this);
        like.setOnClickListener(this);
        pass.setOnClickListener(this);
        next.setOnClickListener(this);

        boolean isFavour = getIntent().getBooleanExtra("is_favour", false);

        if (isFavour || sp.getBoolean(word, false)) {
            like.setSelected(true);
        }

        if (fromIntent != 0) {
            recyclerView.setPadding(0, 0, 0, 0);
            next.setVisibility(View.GONE);
            pass.setVisibility(View.GONE);
        }

//        initRecycler();
    }

    private void initData() {
        Intent mIntent = getIntent();

        fromIntent = mIntent.getIntExtra("from_intent", 0);

        wordTag = mIntent.getStringExtra("tag");
        word = wordTag;
        soundMark = mIntent.getStringExtra("soundMark");
        trans = mIntent.getStringExtra("trans");
        if (trans != null && !trans.isEmpty()) {
            basicTrans = trans.trim().split("；")[0];
        }
        paraphrase = mIntent.getStringExtra("paraphrase");
        sentence = mIntent.getStringExtra("sentence");
        sentenceTrans = mIntent.getStringExtra("sentenceTrans");
        imgUrl = mIntent.getStringExtra("imgUrl");
        word_id = mIntent.getStringExtra("word_id");
        pronunciation = mIntent.getStringExtra("pronunciation");
        synonym = mIntent.getStringExtra("synonym");
        sentence_audio = mIntent.getStringExtra("sentence_audio");
        phrase = mIntent.getStringExtra("phrase");
    }

    private void initRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MultiTypeAdapter();
        recyclerView.setAdapter(adapter);
        adapter.register(Header.class, new HeaderViewBinder(this));
        adapter.register(EmptyValue.class, new FieldTitleViewBinder());
        adapter.register(Text.class).to(new TextViewBinder(), new TextDrawableViewBinder(this))
                .withClassLinker(new ClassLinker<Text>() {
                    @NonNull
                    @Override
                    public Class<? extends ItemViewBinder<Text, ?>> index(@NonNull Text text) {
                        if (text.hasDrawable) {
                            return TextDrawableViewBinder.class;
                        } else {
                            return TextViewBinder.class;
                        }
                    }
                });
        adapter.register(WordSituation.class, new SituationViewBinder(this));
        adapter.register(VideoList.class, new HorizontalViewBinder(this));
        adapter.register(Change.class, new MiniSizeTextViewBinder());
        adapter.register(Sentence.class).to(new SentenceViewBinder(), new SentenceSoundViewBinder(this))
                .withClassLinker(new ClassLinker<Sentence>() {
                    @NonNull
                    @Override
                    public Class<? extends ItemViewBinder<Sentence, ?>> index(@NonNull Sentence sentence) {
                        if (sentence.getSentenceSound() != null) {
                            return SentenceSoundViewBinder.class;
                        }
                        return SentenceViewBinder.class;
                    }
                });
        adapter.register(String.class, new PostWrongBinder(this));

        items = new Items();
        //头部
        items.add(new Header(word, basicTrans, "美" + soundMark, pronunciation));
        //paraphrase
        items.add(new EmptyValue("英文释义"));
        items.add(new Text(paraphrase, false));
        //meaning
        items.add(new EmptyValue("中文释义"));
        /**
         * 需要用正则表达式匹配
         */
        List<String> mList = new ArrayList<>();
        String[] splits = trans.trim().split("；");
        int j = 0;
        for (String split : splits) {
            String start = split.trim().substring(0, 1);
            boolean b = Pattern.compile("^[A-Za-z]+$").matcher(start).matches();
            if (!b) {
                if (mList.size() > 0) {
                    String tmp = mList.get(j - 1);
                    mList.remove(j - 1);
                    mList.add(tmp + "；" + split.trim());
                }
            } else {
                j++;
                mList.add(split.trim());
            }
        }
        for (String s : mList) {
            items.add(new Text(s, false));
        }
        //语境
        items.add(new EmptyValue("语境"));
        items.add(new WordSituation(imgUrl, sentence, sentenceTrans, null));
        //视频
        List<Video> mVideoItems = new ArrayList<>();
        for (int i = 0; i < mVideoList.size(); i++) {
            mVideoItems.add(new Video(mVideoList.get(i).getId(), mVideoList.get(i).getImg(),
                    mVideoList.get(i).getVideo()));
        }
        items.add(new VideoList(mVideoItems));
        //词形变化


//        items.add(new EmptyValue("词形变化"));
////        for (int i = 0; i < 5; i++) {
////            items.add(new Change("过去式:", word));
////        }
//        items.add(new Change("过去式:", word + "ed"));
//        items.add(new Change("复数:", word + "s"));
//        items.add(new Change("现在分词:", word + "ing"));
        //短语
        if (phrase != null && !phrase.isEmpty()) {
            items.add(new EmptyValue("短语"));
            for (int i = 0; i < 3; i++) {
                items.add(new Sentence(phrase, trans, null));
            }
        }

        //其他例句
        if (mVideoList != null && mVideoList.size() != 0) {
            items.add(new EmptyValue("其他例句"));
            for (int i = 0; i < mVideoList.size(); i++) {
                items.add(new Sentence(mVideoList.get(i).getSentence(), mVideoList.get(i).getTranslation(),
                        mVideoList.get(i).getSentence_audio(), mVideoList.get(i).getVideo_name()));
            }
        }

        //同义词
        if (synonym != null && !synonym.isEmpty()) {
            items.add(new EmptyValue("同义词"));
            items.add(new Text(synonym, false));
        }
        //形近词
        if (word_similar_form != null && !word_similar_form.isEmpty()) {
            items.add(new EmptyValue("形近词"));
            items.add(new Text(word_similar_form, false));
        }
        //词根词缀
        if (stem_affix != null && !stem_affix.isEmpty()) {
            items.add(new EmptyValue("词根词缀"));
            items.add(new Text(stem_affix, false));
        }
        items.add(new EmptyValue("单词笔记"));
        //需要记录
        SharedPreferences pref = getSharedPreferences("YJEnglish", MODE_PRIVATE);
        boolean b = false;
        if (pref != null) {
            b = pref.getString(wordTag, "").isEmpty();
        }
        if (b) {
            //无单词笔记
            items.add(new Text("添加单词笔记", true));
        } else {
            items.add(new Text("查看单词笔记", true));
        }
        items.add("");

        if (fromIntent != 0) {
            items.add(new EmptyValue(""));
            items.add(new EmptyValue(""));
        }
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pass:
                if (tipsCount < 5) {
                    showmDialog();
                } else {
                    //不知道是否存在问题
                    setResult(WordActivity.RESULT_WORD_PASS);
                    overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
                    finishAfterTransition();
                }
                break;
            case R.id.like:
                executeFavourTask();
                break;
            case R.id.next:
            case R.id.back:
                onBackPressed();
                break;
            case R.id.close_video:
                if (mVideo != null) {
//                    mVideo.stop();
                    mVideo.release();
                    mVideo = null;
                }
                videoFrame.removeAllViews();
                videoContainer.setVisibility(View.GONE);
                if (fromIntent == 0) {
                    next.setVisibility(View.VISIBLE);
                    pass.setEnabled(true);
                }
                back.setEnabled(true);
                recyclerView.setEnabled(true);
                break;
            default:
                break;
        }
    }

    private void executeFavourTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).postWordFavours(UserConfig.getToken(this), word_id)
                .enqueue(new Callback<CommonInfo>() {
                    @Override
                    public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                        CommonInfo info = response.body();
                        if (info.getStatus().equals("200")) {
                            if (sp.getBoolean(word, false)) {
                                //证明点了就变成false了
                                sp.edit().putBoolean(word, false).apply();
                                like.setSelected(false);
                            } else {
                                sp.edit().putBoolean(word, true).apply();
                                like.setSelected(true);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonInfo> call, Throwable t) {
                        Toast.makeText(WordDetailActivity.this, "连接超时了哦~请重试", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showmDialog() {
        SharedPreferences.Editor editor = getSharedPreferences("YJEnglish", MODE_PRIVATE).edit();
        editor.putInt("tips_count", tipsCount + 1).apply();
        tipsCount++;
        DialogUtils dialogUtils = DialogUtils.getInstance(WordDetailActivity.this);
        AlertDialog mDialog = dialogUtils.newTipsDialog("小语知道你学会该单词了，不会再让这个单词出现了噢~");
        dialogUtils.setTipsListener(new DialogUtils.OnTipsListener() {
            @Override
            public void onConfirm() {
                setResult(WordActivity.RESULT_WORD_PASS);
                overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
                finishAfterTransition();
                supportFinishAfterTransition();
            }

            @Override
            public void onCancel() {

            }
        });
        dialogUtils.show(mDialog);
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = ScreenUtils.dp2px(WordDetailActivity.this, 260);
        params.height = ScreenUtils.dp2px(WordDetailActivity.this, 240);
        mDialog.getWindow().setAttributes(params);
    }

    @Override
    public void onBackPressed() {
        if (mVideo != null) {
//            mVideo.stop();
            mVideo.release();
            mVideo = null;
        }
        setResult(WordActivity.RESULT_WORD_NEXT);
        finishAfterTransition();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    public void onEditSelect() {
        Intent toEdit = new Intent(this, EditAty.class);
        toEdit.putExtra("tag", wordTag);
        toEdit.putExtra("soundMark", soundMark);
        startActivity(toEdit);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    @Override
    public void onPronunciationClick() {
        initPlayer(pronunciation);
    }

    private void initPlayer(String url) {
        if (mPlayer != null) {
            mPlayer.reset();
            try {
                mPlayer.setDataSource(url);
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.start();
        }
    }

    @Override
    public void onSentenceSoundClick(String mAACFile) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/背呗背单词/BeibeiRecorder/" + word + ".mp3");
        if (file.exists()) {
            //解析aac音频帧 例句
            initPlayer(file.getPath());
        } else {
            if (sentence_audio.trim().endsWith("mp3")) {
                initPlayer(sentence_audio);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
        }
    }

    @Override
    public void onVideoClick(String video_id, String videoUrl, int pos) {
//        Log.e("WordDetail", video_id);
//        Log.e("WordDetail", videoUrl);
        executeCaptionTask(pos, videoUrl, video_id);
//        showVideo(pos, videoUrl);
    }

    private void executeCaptionTask(final int pos, final String videoUrl, final String video_id) {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(VideoService.class).getVideoInfo(UserConfig.getToken(this),
                video_id).enqueue(new Callback<VideoCaptionInfo>() {
            @Override
            public void onResponse(Call<VideoCaptionInfo> call, Response<VideoCaptionInfo> response) {
                VideoCaptionInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    mCaptions = info.getData();
                    if (mVideo != null) {
                        mVideo.setCaption(mCaptions);
                    }
                    showVideo(pos, videoUrl);
                } else {
                    Log.e("WordDetail", info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<VideoCaptionInfo> call, Throwable t) {
                Toast.makeText(WordDetailActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showVideo(final int pos, final String path) {
        next.setVisibility(View.GONE);
        pass.setEnabled(false);
        back.setEnabled(false);
        recyclerView.setEnabled(false);
        if (videoContainer.getVisibility() == View.GONE) {
            videoContainer.setVisibility(View.VISIBLE);
        }
        if (mVideo == null) {
            mVideo = new MyVideoView(this, true, false, mCaptions);
        }
        videoFrame.removeAllViews();
        videoFrame.addView(mVideo, new ViewGroup.LayoutParams(-1, -1));
//        mVideo.stop();
        mVideo.setVideoPath(path);
        mVideo.start();
        mVideo.setOnStopListener(new MyVideoView.IOnStopListener() {
            @Override
            public void onVideoStop() {
//                mVideo.stop();
//                videoFrame.removeAllViews();
                if (pos + 1 < mVideoList.size()) {
                    //还有下一个视频
                    executeCaptionTask(pos + 1, mVideoList.get(pos + 1).getVideo(), mVideoList.get(pos + 1).getId());
                } else {
                    //没有视频了
                    Toast.makeText(WordDetailActivity.this, "没有下一个视频咯", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mVideo.setChangeSourceListener(new MyVideoView.IOnChangeSourceListener() {
            @Override
            public void onFormerClick() {
                if (pos > 0) {
                    //存在上一个视频
                    executeCaptionTask(pos - 1, mVideoList.get(pos - 1).getVideo(), mVideoList.get(pos - 1).getId());
                } else {
                    Toast.makeText(WordDetailActivity.this, "没有上一个视频噢", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLatterClick() {
                if (pos + 1 < mVideoList.size()) {
                    //还有下一个视频
                    executeCaptionTask(pos + 1, mVideoList.get(pos + 1).getVideo(), mVideoList.get(pos + 1).getId());
                } else {
                    //没有视频了
                    Toast.makeText(WordDetailActivity.this, "没有下一个视频咯",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        mVideo.setFullScreenListener(new MyVideoView.IFullScreenListener() {
            @Override
            public void onClickFull(boolean isFull) {
                int progress = mVideo.getPosition();
                Intent toFullScreen = new Intent(WordDetailActivity.this, FullScreenVideo.class);
                toFullScreen.putExtra("progress", progress);
                String mPathList;
                StringBuilder sb = new StringBuilder();
                String video_ids;
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < mVideoList.size(); i++) {
                    if (i != mVideoList.size() - 1) {
                        sb.append(mVideoList.get(i).getVideo()).append("；");
                        stringBuilder.append(mVideoList.get(i).getId()).append("；");
                    } else {
                        sb.append(mVideoList.get(i).getVideo());
                        stringBuilder.append(mVideoList.get(i).getId());
                    }
                }
                mPathList = sb.toString();
                video_ids = stringBuilder.toString();
                toFullScreen.putExtra("path", mPathList);
                toFullScreen.putExtra("has_multi_videos", true);
                toFullScreen.putExtra("caption_type", 1);
                toFullScreen.putExtra("video_ids", video_ids);
                toFullScreen.putExtra("current_position", pos);
                startActivity(toFullScreen);
                overridePendingTransition(R.anim.anim_top_rotate_get_into,
                        R.anim.anim_top_rotate_sign_out);
                mVideo.release();
                videoFrame.removeAllViews();
                videoContainer.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onVideoSentenceSoundClick(String soundUrl) {
        //其他例句
        initPlayer(soundUrl);
    }

    @Override
    public void onPostError(String type, String text) {
        String typeInt;
        if (type.equals("英文释义")) {
            typeInt = "0";
        } else if (type.equals("中文释义")) {
            typeInt = "1";
        } else if (type.equals("语境句子")) {
            typeInt = "2";
        } else if (type.equals("其它例句")) {
            typeInt = "3";
        } else {
            typeInt = "4";
        }
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).sendError(UserConfig.getToken(this),
                typeInt, text, word_id).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                CommonInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    Toast.makeText(WordDetailActivity.this,
                            "您的修改意见大队长已经呈交给领队~请耐心等候反馈~", Toast.LENGTH_SHORT).show();
                    //可以send一个广播通知系统消息 并存入数据库
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {
            }
        });
    }
}

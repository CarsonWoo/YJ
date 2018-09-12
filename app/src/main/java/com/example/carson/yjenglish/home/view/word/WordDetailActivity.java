package com.example.carson.yjenglish.home.view.word;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carson.yjenglish.EmptyValue;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.home.model.forviewbinder.Change;
import com.example.carson.yjenglish.home.model.forviewbinder.Header;
import com.example.carson.yjenglish.home.model.forviewbinder.Sentence;
import com.example.carson.yjenglish.home.model.forviewbinder.Text;
import com.example.carson.yjenglish.home.model.forviewbinder.Video;
import com.example.carson.yjenglish.home.model.forviewbinder.VideoList;
import com.example.carson.yjenglish.home.model.forviewbinder.WordSituation;
import com.example.carson.yjenglish.home.viewbinder.word.FieldTitleViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.HeaderViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.HorizontalViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.MiniSizeTextViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.SentenceSoundViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.SentenceViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.SituationViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.TextDrawableViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.TextViewBinder;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ClassLinker;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class WordDetailActivity extends AppCompatActivity implements View.OnClickListener,
        TextDrawableViewBinder.OnEditSelectListener{

    private RecyclerView recyclerView;
    private ImageView back;
    private ImageView like;
    private TextView pass;
    private Button next;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);

        initViews();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        back = findViewById(R.id.back);
        like = findViewById(R.id.like);
        pass = findViewById(R.id.pass);
        next = findViewById(R.id.next);
        back.setOnClickListener(this);
        like.setOnClickListener(this);
        pass.setOnClickListener(this);
        next.setOnClickListener(this);

        initData();

        initRecycler();
    }

    private void initData() {
        Intent mIntent = getIntent();
        wordTag = mIntent.getStringExtra("tag");
        word = wordTag;
        soundMark = mIntent.getStringExtra("soundMark");
        trans = mIntent.getStringExtra("trans");
        basicTrans = trans.substring(0, 1);
        paraphrase = mIntent.getStringExtra("paraphrase");
        sentence = mIntent.getStringExtra("sentence");
        sentenceTrans = mIntent.getStringExtra("sentenceTrans");
        imgUrl = mIntent.getStringExtra("imgUrl");
    }

    private void initRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MultiTypeAdapter();
        recyclerView.setAdapter(adapter);
        adapter.register(Header.class, new HeaderViewBinder());
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
        adapter.register(WordSituation.class, new SituationViewBinder());
        adapter.register(VideoList.class, new HorizontalViewBinder());
        adapter.register(Change.class, new MiniSizeTextViewBinder());
        adapter.register(Sentence.class).to(new SentenceViewBinder(), new SentenceSoundViewBinder())
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
        ;

        items = new Items();
        items.add(new Header(word, basicTrans, soundMark, null));
        items.add(new EmptyValue("英文释义"));
        items.add(new Text(paraphrase, false));
        items.add(new EmptyValue("中文释义"));
        for (int i = 0; i < 3; i++) {
            items.add(new Text(trans, false));
        }
        items.add(new EmptyValue("语境"));
        items.add(new WordSituation(imgUrl, sentence, sentenceTrans, null));

        List<Video> mVideoItems = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mVideoItems.add(new Video("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg",
                    "http://dn-chunyu.qbox.me/fwb/static/images/home/video/video_aboutCY_A.mp4"));
        }
        items.add(new VideoList(mVideoItems));
        items.add(new EmptyValue("词形变化"));
        for (int i = 0; i < 5; i++) {
            items.add(new Change("过去式:", word));
        }
        items.add(new EmptyValue("短语"));
        for (int i = 0; i < 3; i++) {
            items.add(new Sentence(word + " to", trans, null));
        }
        items.add(new EmptyValue("其他例句"));
        for (int i = 0; i < 3; i++) {
            items.add(new Sentence(word + " to", trans, "test"));
        }
        items.add(new EmptyValue("同义词"));
        StringBuilder sb = new StringBuilder();
        sb.append(word + "    ").append(word + "    ").append(word + "    ").append(word + "    ");
        items.add(new Text(sb.toString(), false));
        items.add(new EmptyValue("形近词"));
        items.add(new Text("", false));
        items.add(new EmptyValue("词根词缀"));
        items.add(new Text("", false));
        items.add(new EmptyValue("单词笔记"));
        //需要记录
        items.add(new Text("添加单词笔记", true));

        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.pass:
                break;
            case R.id.like:
                break;
            case R.id.next:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO back to the parent with animation
    }

    @Override
    public void onEditSelect() {
        Intent toEdit = new Intent(this, EditAty.class);
        toEdit.putExtra("tag", wordTag);
        toEdit.putExtra("soundMark", soundMark);
        startActivity(toEdit);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }
}

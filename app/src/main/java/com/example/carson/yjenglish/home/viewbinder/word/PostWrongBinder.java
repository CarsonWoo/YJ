package com.example.carson.yjenglish.home.viewbinder.word;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.home.HomeService;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.UserConfig;

import me.drakeet.multitype.ItemViewBinder;
import retrofit2.Retrofit;

/**
 * Created by 84594 on 2018/10/16.
 */

public class PostWrongBinder extends ItemViewBinder<String, PostWrongBinder.ViewHolder> {

    public OnErrorPostListener listener;

    public PostWrongBinder(OnErrorPostListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.layout_post_wrong, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull String item) {
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Binder", "onClick");
                initWindow(v);
            }
        });
    }

    private void initWindow(final View view) {
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setCanceledOnTouchOutside(false);
        View contentView = LayoutInflater.from(view.getContext()).inflate(R.layout.layout_report_dialog, null,
                false);
        ImageView delete = contentView.findViewById(R.id.delete_dialog);
        final TextView field = contentView.findViewById(R.id.field);
        final EditText editReport = contentView.findViewById(R.id.edit_report);
        Button submit = contentView.findViewById(R.id.btn_submit);
        dialog.setContentView(contentView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = ScreenUtils.dp2px(view.getContext(), 300);
        lp.height = ScreenUtils.dp2px(view.getContext(), 340);
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        dialog.show();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editReport.getText().toString().isEmpty()) {
                    Toast.makeText(view.getContext(), "请先填写修改的内容噢~", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (listener != null) {
                    listener.onPostError(field.getText().toString(), editReport.getText().toString());
                }
                dialog.dismiss();
            }
        });

        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupWindow(view);
            }

            private void initPopupWindow(View view) {
                View windowView = LayoutInflater.from(view.getContext()).inflate(R.layout.report_item_window,
                        null, false);
                final TextView paraphrase = windowView.findViewById(R.id.paraphrase);
                final TextView meaning = windowView.findViewById(R.id.meaning);
                final TextView sentenceMeaning = windowView.findViewById(R.id.sentence_meaning);
                final TextView tvSentence = windowView.findViewById(R.id.tv_sentence_meaning);
                final TextView other = windowView.findViewById(R.id.other);
                final PopupWindow window = new PopupWindow(windowView, ScreenUtils.dp2px(
                        view.getContext(), 100), ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setOutsideTouchable(true);
                window.setBackgroundDrawable(new BitmapDrawable());
                window.showAsDropDown(field, 0, 0, Gravity.BOTTOM);

                paraphrase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        field.setText(paraphrase.getText().toString());
                        window.dismiss();
                    }
                });

                meaning.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        field.setText(meaning.getText().toString());
                        window.dismiss();
                    }
                });

                sentenceMeaning.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        field.setText(sentenceMeaning.getText().toString());
                        window.dismiss();
                    }
                });

                tvSentence.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        field.setText(tvSentence.getText().toString());
                        window.dismiss();
                    }
                });

                other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        field.setText(other.getText().toString());
                        window.dismiss();
                    }
                });
            }
        });

    }

    public class ViewHolder extends BaseViewHolder {
        private TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_report);
        }
    }

    public interface OnErrorPostListener {
        void onPostError(String type, String text);
    }
}

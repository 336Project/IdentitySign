package com.team.hbase.widget;

import com.team.hbase.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 具有标准UI的通用对话框<br/>
 * <br/>
 */
public class GeneralDialog extends Dialog {
    /**
     * 对话框默认布局
     */
    public static final int RES_LAYOUT_MAIN = R.layout.generate_dialog_main;
    /**
     * 对话框标题ID
     */
    public static final int RES_ID_TITLE = R.id.dialog_title;
    /**
     * 对话框标题分隔线ID
     */
    public static final int RES_ID_TITLE_LINE = R.id.dialog_title_line;
    /**
     * 对话框内容ID
     */
    public static final int RES_ID_CONTENT = R.id.dialog_content;
    /**
     * 积极按钮ID
     */
    public static final int RES_ID_POSITIVE = R.id.dialog_positive;
    /**
     * 消极按钮ID
     */
    public static final int RES_ID_NEGATIVE = R.id.dialog_negative;

    private View view;
    private Drawable background;

    private OnClickListener confirmClickListener;
    private OnClickListener cancelClickListener;

    private boolean blockDismiss = false;

    @SuppressLint("NewApi")
	public GeneralDialog(Context context) {
        super(context);
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        if(Build.VERSION.SDK_INT >= 14){
            window.setDimAmount(0.3f);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setBackgroundDrawable(background);

        setCanceledOnTouchOutside(true);
        setCancelable(true);

        //int width = EnvironmentUtils.getScreenWidth() - EnvironmentUtils.dip2px(60);
        setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	int id = v.getId();
        	if(id == R.id.dialog_positive){
        		blockDismiss = false;
                if(confirmClickListener!=null){
                    confirmClickListener.onClick(GeneralDialog.this, v);
                }
                if(!blockDismiss) {
                    dismiss();
                }
        	}else if(id == R.id.dialog_negative){
        		blockDismiss = false;
                if(cancelClickListener!=null){
                    cancelClickListener.onClick(GeneralDialog.this, v);
                }
                if(!blockDismiss) {
                    cancel();
                }
        	}
        }
    };

    /**
     * 阻止当前按钮事件导致的对话框的关闭事件，仅当在按钮事件回调中有效
     */
    public void blockDismiss(){
        blockDismiss = true;
    }

    /**
     * 获取标题View
     * @return 如果为自定义标题，可能返回null
     */
    public TextView getTitleView(){
        return (TextView) findViewById(R.id.dialog_title);
    }

    /**
     * 获取积极按钮
     * @return 如果为自定义视图，可能返回null
     */
    public TextView getPositiveButton(){
        return (TextView) findViewById(R.id.dialog_positive);
    }

    /**
     * 获取消极按钮
     * @return 如果为自定义视图，可能返回null
     */
    public TextView getNegativiButton(){
        return (TextView) findViewById(R.id.dialog_negative);
    }

    /**
     * 显示一个简单单按钮对话框
     */
    public static GeneralDialog showSimple(Context context, String title, String msg, String positive){
        return showSimple(context, title, msg, positive, null);
    }

    /**
     * 显示一个简单单按钮对话框
     */
    public static GeneralDialog showSimple(Context context, String title, String msg, String positive, OnClickListener positiveListener){
        return new Builder(context)
                .setTitle(title)
                .setContent(msg)
                .setPositiveButton(positive, positiveListener)
                .createAndShow();
    }

    /**
     * 创建一个简单单按钮对话框
     */
    public static GeneralDialog creatSimple(Context context, String title, String msg, String positive, OnClickListener positiveListener){
        return new Builder(context)
                .setTitle(title)
                .setContent(msg)
                .setPositiveButton(positive, positiveListener)
                .create();
    }

    /**
     * 显示一个标准双按钮对话框
     */
    public static GeneralDialog showNormal(Context context, String title, String msg, String positive, OnClickListener positiveListener, String negative, OnClickListener negativeListener){
        return new Builder(context)
                .setTitle(title)
                .setContent(msg)
                .setPositiveButton(positive, positiveListener)
                .setNegativeButton(negative, negativeListener)
                .createAndShow();
    }

    /**
     * 创建一个标准双按钮对话框
     */
    public static GeneralDialog creatNormal(Context context, String title, String msg, String positive, OnClickListener positiveListener, String negative, OnClickListener negativeListener){
        return new Builder(context)
                .setTitle(title)
                .setContent(msg)
                .setPositiveButton(positive, positiveListener)
                .setNegativeButton(negative, negativeListener)
                .create();
    }

    /**
     * 显示一个部份自定义的对话框
     */
    public static GeneralDialog showCustom(Context context, String title, View content, String positive, OnClickListener positiveListener, String negative, OnClickListener negativeListener){
        return new Builder(context)
                .setTitle(title)
                .setContentView(content)
                .setPositiveButton(positive, positiveListener)
                .setNegativeButton(negative, negativeListener)
                .createAndShow();
    }

    /**
     * 显示一个部份自定义的对话框
     */
    public static GeneralDialog showCustom(Context context, View title, View content, String positive, OnClickListener positiveListener, String negative, OnClickListener negativeListener){
        return new Builder(context)
                .setTitleView(title)
                .setContentView(content)
                .setPositiveButton(positive, positiveListener)
                .setNegativeButton(negative, negativeListener)
                .createAndShow();
    }

    /**
     * 显示一个完全自定义的对话框
     * @param context
     * @param view
     * @return
     */
    public static GeneralDialog showCustom(Context context, View view){
        return new Builder(context)
                .setView(view)
                .createAndShow();
    }

    public interface OnClickListener{
        /**
         * 按钮回调
         * @param dialog 对话框
         * @param view 事件来源
         */
        void onClick(GeneralDialog dialog, View view);
    }

    /**
     * 标准对话框构造器
     */
    public static class Builder{
        private Context context;
        private View customView;
        private View customTitleView;
        private View customContentView;

        private String title;
        private String content;

        private String confirmText;
        private String cancelText;

        private OnClickListener confirmClickListener;
        private OnClickListener cancelClickListener;

        private Drawable background;

        public Builder(Context context){
            this.context = context;
            background = context.getResources().getDrawable(R.drawable.dialog_background);
        }

        /**
         * 设置对话框标题
         * @param title 如果为空，则不显示标题
         * @return
         */
        public Builder setTitle(String title){
            this.title = title;
            return this;
        }

        /**
         * 设置自定义对话框标题
         * @param view 如果为空，则不显示标题
         * @return
         */
        public Builder setTitleView(View view){
            customTitleView = view;
            return this;
        }

        /**
         * 设置自定义对话框标题
         * @param resId 布局文件
         * @return
         */
        public Builder setTitleView(int resId){
            customTitleView = LayoutInflater.from(context).inflate(resId, null);
            return this;
        }

        /**
         * 设置对话框内容
         * @param content
         * @return
         */
        public Builder setContent(String content){
            this.content = content;
            customContentView = null;
            return this;
        }

        /**
         * 设置自定义对话框内容
         * @param view
         * @return
         */
        public Builder setContentView(View view){
            customContentView = view;
            content = null;
            return this;
        }

        /**
         * 设置自定义对话框内容
         * @param resId 布局文件
         * @return
         */
        public Builder setContentView(int resId){
            customContentView = LayoutInflater.from(context).inflate(resId, null);
            content = null;
            return this;
        }

        /**
         * 设置积极按钮
         * @param text 显示文字
         * @param listener 回调
         * @return
         */
        public Builder setPositiveButton(String text, OnClickListener listener){
            this.confirmText = text;
            this.confirmClickListener = listener;
            return this;
        }

        /**
         * 设置消极按钮
         * @param text 显示文字，如果为空，则不显示
         * @param listener 回调
         * @return
         */
        public Builder setNegativeButton(String text, OnClickListener listener){
            this.cancelText = text;
            this.cancelClickListener = listener;
            return this;
        }

        /**
         * 设置为自定义对话框
         * @param view 整体布局
         * @return
         */
        public Builder setView(View view){
            this.customView = view;
            return this;
        }

        /**
         * 设置对话框背景
         * @param drawable
         * @return
         */
        public Builder setBackground(Drawable drawable){
            this.background = drawable;
            return this;
        }

        /**
         * 生成对话框
         * @return
         */
        public GeneralDialog create(){
            GeneralDialog dialog = new GeneralDialog(context);
            View root;
            if(customView!=null){
                root = customView;
            } else {
                LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(RES_LAYOUT_MAIN, null);
                root = view;
                TextView titleView = (TextView) view.findViewById(RES_ID_TITLE);
                View titleLine = view.findViewById(RES_ID_TITLE_LINE);
                if(customTitleView!=null || title == null){
                    titleView.setVisibility(View.GONE);
                    titleLine.setVisibility(View.GONE);
                    if(customTitleView!=null){
                        view.addView(customTitleView, view.indexOfChild(titleLine)+1);
                    }
                } else {
                    titleView.setText(title);
                }
                TextView contentView = (TextView) view.findViewById(RES_ID_CONTENT);
                if(customContentView!=null){
                    contentView.setVisibility(View.GONE);
                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
                    p.topMargin = 5;
                    view.addView(customContentView, view.indexOfChild(contentView)+1,p);
                } else {
                    contentView.setText(content == null ? "" : Html.fromHtml(content));
                }
                TextView confirmView = (TextView) view.findViewById(RES_ID_POSITIVE);
                TextView cancelView = (TextView) view.findViewById(RES_ID_NEGATIVE);
                if(cancelText==null){
                    cancelView.setVisibility(View.GONE);
                } else {
                    cancelView.setText(cancelText);
                    dialog.cancelClickListener = cancelClickListener;
                }
                dialog.confirmClickListener = confirmClickListener;
                confirmView.setText(confirmText == null ? "" : confirmText);
                confirmView.setOnClickListener(dialog.listener);
                cancelView.setOnClickListener(dialog.listener);
            }
            dialog.background = background;
            dialog.view = root;
            return dialog;
        }

        /**
         * 生成对话框，并显示
         * @return
         */
        public GeneralDialog createAndShow(){
            GeneralDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}

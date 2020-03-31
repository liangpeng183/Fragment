package com.smart.fragment.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;

/*
 *   获取验证码  倒计时 工具
 * */
public class SendSmsTimerUtils extends CountDownTimer {

    private int inFuture;
    private int downInterval;
    private Button timeDown_btn;


    /**
     * 第一个参数：按钮(需要实现倒计时的控件)
     * 第二个参数：倒计时总时间，以毫秒为单位；
     * 第三个参数：渐变事件，最低1秒，也就是说设置0-1000都是以一秒渐变，设置1000以上改变渐变时间
     * 第四个个参数：点击按钮之前的背景
     * 第五个参数：点击按钮之后的背景
     */
    public SendSmsTimerUtils(Button timeDown_btn, long millisInFuture, long countDownInterval, int inFuture, int downInterval) {
        super(millisInFuture, countDownInterval);
        this.inFuture = inFuture;
        this.downInterval = downInterval;
        this.timeDown_btn = timeDown_btn;

    }

    @Override
    public void onTick(long millisUntilFinished) {
        timeDown_btn.setEnabled(false);
        timeDown_btn.setText(millisUntilFinished / 1000 + "秒后可重新发送");
        timeDown_btn.setBackgroundResource(downInterval);

        SpannableString spannableString = new SpannableString(timeDown_btn.getText().toString());
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
        //设置秒数为红色
        if (millisUntilFinished/1000 > 9) {
            spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        } else {
            spannableString.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        timeDown_btn.setText(spannableString);
    }

    @Override
    public void onFinish() {
        timeDown_btn.setText("重新获取验证码");
        timeDown_btn.setEnabled(true);
        timeDown_btn.setBackgroundResource(inFuture);
    }
}

package com.smart.fragment.utils;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class MobileFormatCheck {

    /*
     *   手机号码 格式验证    共11位
     *    其中 1 开头  第二位：【 3 4 5 7 8 】  剩下 ： 9位
     *    1  3/4/5/7/8  XXXXXXXXX
     * */
    public static boolean mobileNumberFormatCheck(String phone) {

        String regex="^1[34578]\\d{9}$";
        if (phone.length()!=11){
            Log.i(TAG, "isPhone: 手机位数不对");
            return false;
        }else {
            Pattern p=Pattern.compile(regex);
            Matcher m=p.matcher(phone);
            boolean isMatch=m.matches();
            Log.i(TAG, "isPhone: 是否正则匹配"+isMatch);
            return isMatch;
        }

    }

}

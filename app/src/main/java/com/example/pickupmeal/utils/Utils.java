package com.example.pickupmeal.utils;

import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static Boolean isEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    public static String fmNormalDay(Long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(time);
        return simpleDateFormat.format(date);
    }

}

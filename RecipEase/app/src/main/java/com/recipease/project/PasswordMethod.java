package com.recipease.project;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 * Created by Robert Matyjek on 4/3/2018.
 */

public class PasswordMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;
        public PasswordCharSequence(CharSequence source) {
            //Store the actual Password
            mSource = source;
        }
        public char charAt(int index) {
            //Mask the String with Asterisks
            return '*';
        }
        public int length() {
            //Use the length of the actual Password
            return mSource.length();
        }
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end);
        }
    }
}


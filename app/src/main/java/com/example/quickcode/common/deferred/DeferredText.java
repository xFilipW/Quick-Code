package com.example.quickcode.common.deferred;

import android.content.Context;

public interface DeferredText {

    String getText(Context context);

    class Resource implements DeferredText {
        private final Integer resId;

        public Resource(Integer resId) {
            this.resId = resId;
        }

        @Override
        public String getText(Context context) {
            return context.getString(resId);
        }
    }

    class Const implements DeferredText {
        private final String text;

        public Const(String text) {
            this.text = text;
        }

        @Override
        public String getText(Context context) {
            return text;
        }
    }

}

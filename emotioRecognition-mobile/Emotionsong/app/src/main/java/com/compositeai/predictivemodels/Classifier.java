package com.compositeai.predictivemodels;

public interface Classifier {
    String name();

    Classification recognize(final float[] pixels);
}

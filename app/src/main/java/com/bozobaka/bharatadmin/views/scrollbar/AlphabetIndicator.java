package com.bozobaka.bharatadmin.views.scrollbar;

import android.annotation.SuppressLint;
import android.content.Context;

@SuppressLint("ViewConstructor")
public class AlphabetIndicator extends Indicator<INameableAdapter, AlphabetIndicator>{

    public AlphabetIndicator (Context c) {
        super(c, INameableAdapter.class);
    }

    @Override
    protected String getTextElement(Integer currentSection, INameableAdapter adapter) {
        Character provided = adapter.getCharacterForElement(currentSection);
        return String.valueOf(Character.toUpperCase(provided));
    }

    @Override
    protected int getIndicatorHeight() {
        return 75;
    }

    @Override
    protected int getIndicatorWidth() {
        return 75;
    }

    @Override
    protected int getTextSize() {
        return 40;
    }

}

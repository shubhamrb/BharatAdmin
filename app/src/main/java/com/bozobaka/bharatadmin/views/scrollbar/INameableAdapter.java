package com.bozobaka.bharatadmin.views.scrollbar;

public interface INameableAdapter {

    /**
     * @param element of the adapter that should be titled.
     * @return The character that the AlphabetIndicator should display for the corresponding element.
     */
    Character getCharacterForElement(int element);
}

package com.bozobaka.bharatadmin.utils;

import android.content.Context;
import android.os.Build;
import android.util.LayoutDirection;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.ParameterizedType;

import androidx.annotation.IdRes;

public class ScrollUtil {
    /**
     * @param dp Desired size in dp (density-independent pixels)
     * @param v View
     * @return Number of corresponding density-dependent pixels for the given device
     */
    public static int getDP(int dp, View v) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, v.getResources().getDisplayMetrics());
    }

    /**
     * @param dp Desired size in dp (density-independent pixels)
     * @param c Context
     * @return Number of corresponding density-dependent pixels for the given device
     */
    public static int getDP(int dp, Context c) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }

    /**
     *
     * @param c Context
     * @return True if the current layout is RTL.
     */
    public static boolean isRightToLeft(Context c) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                c.getResources().getConfiguration().getLayoutDirection() == LayoutDirection.RTL;
    }

    public static <T> String getGenericName(T object) {
        return ((Class<T>) ((ParameterizedType) object.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName();
    }

    /**
     * Like findViewById(), but traverses upwards from the view given instead of downwards,
     * ignores the view itself and its direct ascendants, and prefers siblings of the initial
     * view over the siblings of one of its ascendants.
     *
     * @param id the id to search for.
     * @param viewToStartFrom the view whose siblings (and whose parents' siblings) should be searched.
     * @return the view found, or null if none could be located.
     */
    public static View findNearestNeighborWithID(@IdRes int id, View viewToStartFrom) {
        if (viewToStartFrom == null) return null;

        ViewGroup parent;
        try {
            parent = (ViewGroup) viewToStartFrom.getParent();
        } catch (ClassCastException e) {
            return null;
        }
        for (int i = 0; i < parent.getChildCount(); i++) { // Checks the children of the given view's parent
            if (viewToStartFrom == parent.getChildAt(i)) continue; // Excluding the given view itself
            View result = parent.getChildAt(i).findViewById(id);
            if (result != null) {
                return result;
            }
        }
        return findNearestNeighborWithID(id, parent); // If the view could not be found, check the next higher generation
    }
}

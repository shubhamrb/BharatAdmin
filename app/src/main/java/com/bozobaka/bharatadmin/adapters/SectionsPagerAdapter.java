package com.bozobaka.bharatadmin.adapters;

import android.content.Context;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.ui.classdetails.LiveFragment;
import com.bozobaka.bharatadmin.ui.classdetails.NotesFragment;
import com.bozobaka.bharatadmin.ui.classdetails.attendance.AttendanceFragment;
import com.bozobaka.bharatadmin.ui.classdetails.studyMaterial.StudyMaterialFragment;
import com.bozobaka.bharatadmin.ui.classmember.ClassMemberFragment;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2,
            R.string.tab_text_3, R.string.tab_text_4,R.string.tab_text_5};
    private final Context mContext;
    private String classId;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String classId) {
        super(fm);
        mContext = context;
        this.classId = classId;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0) {
            return LiveFragment.newInstance(classId);
        } else if (position == 1) {
            return ClassMemberFragment.newInstance(classId);
        } else if (position == 2) {
            return NotesFragment.newInstance(classId);
        }else if (position==3){
            return AttendanceFragment.newInstance(classId);
        }
        else {
            return StudyMaterialFragment.newInstance(classId);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 5;
    }
}
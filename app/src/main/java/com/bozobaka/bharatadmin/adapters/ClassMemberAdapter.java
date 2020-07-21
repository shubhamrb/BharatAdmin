package com.bozobaka.bharatadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.MemberModel;
import com.bozobaka.bharatadmin.views.TextDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassMemberAdapter extends RecyclerView
        .Adapter<ClassMemberAdapter
        .DataObjectHolder> {
    static int fixed = 0;
    private static ClassMemberAdapter.MyClickListener myClickListener;
    private static Context context;
    private List<MemberModel> classMembers = new ArrayList<>();
    private ArrayList<MemberModel> arraylist;

    public ClassMemberAdapter(Context context,
                              List<MemberModel> classMembers) {
        this.context = context;
        this.classMembers = classMembers;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void setData(List<MemberModel> classMembers) {
        this.arraylist = new ArrayList<MemberModel>();
        this.arraylist.addAll(classMembers);
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_member_row_layout, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DataObjectHolder holder, int position) {
        MemberModel classMemberList = classMembers.get(position);
        holder.name.setText(classMemberList.getUserName());
//        holder.userType.setText(classMemberList.getUserType());

        TextDrawable textDrawable;
        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().endConfig().round();
        if (position % 5 == 0) {
            textDrawable = builder.build(classMemberList.getUserName().toUpperCase().charAt(0) + "", context.getResources().getColor(R.color.color4));
        } else if (position % 5 == 1) {
            textDrawable = builder.build(classMemberList.getUserName().toUpperCase().charAt(0) + "", context.getResources().getColor(R.color.color5));
        } else if (position % 5 == 2) {
            textDrawable = builder.build(classMemberList.getUserName().toUpperCase().charAt(0) + "", context.getResources().getColor(R.color.color6));
        } else if (position % 5 == 3) {
            textDrawable = builder.build(classMemberList.getUserName().toUpperCase().charAt(0) + "", context.getResources().getColor(R.color.color7));
        } else {
            textDrawable = builder.build(classMemberList.getUserName().toUpperCase().charAt(0) + "", context.getResources().getColor(R.color.color8));
        }
        holder.textDrawableImg.setImageDrawable(textDrawable);

        
    }

    public void filter(String charText) {
//        if (charText != null) {
//            charText = charText.toLowerCase(Locale.getDefault());
//            classMembers.clear();
//            if (charText.length() == 0) {
//                classMembers.addAll(arraylist);
//            } else {
//                for (MemberModel fp : arraylist) {
//                    if (fp.getUserMobNo().toLowerCase(Locale.getDefault()).contains(charText) ||
//                            fp.getUserName().toLowerCase(Locale.getDefault()).contains(charText)) {
//                        classMembers.add(fp);
//                    }
//                }
//            }
//            notifyDataSetChanged();
//        }
        if (charText != null && charText.contentEquals("student")){
            classMembers.clear();
            for (MemberModel fp : arraylist) {
                if (fp.getUserType().toLowerCase(Locale.ENGLISH).trim().contentEquals("student")) {
                    classMembers.add(fp);
                }
            }
        }else if (charText != null && charText.contentEquals("teacher")){
            classMembers.clear();
            for (MemberModel fp : arraylist) {
                if (fp.getUserType().toLowerCase(Locale.ENGLISH).trim().contentEquals("teacher")) {
                    classMembers.add(fp);
                }
            }
        }else {
            classMembers.clear();
            classMembers.addAll(arraylist);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return classMembers.size();
    }


    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.iv_text_drawable)
        ImageView textDrawableImg;
//        @BindView(R.id.tv_user_type)
//        TextView userType;
        @BindView(R.id.iv_send_msg)
        ImageView sendMsg;
        @BindView(R.id.iv_call)
        ImageView call;
        @BindView(R.id.rl_parent)
        RelativeLayout parentLayout;

        public DataObjectHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            sendMsg.setOnClickListener(this);
            call.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

}

package com.bozobaka.bharatadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.MemberModel;
import com.bozobaka.bharatadmin.views.TextDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassMemberPagedAdapter extends PagedListAdapter<MemberModel,
        ClassMemberPagedAdapter.DataObjectHolder> implements Filterable {

    public static DiffUtil.ItemCallback<MemberModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MemberModel>() {
                @Override
                public boolean areItemsTheSame(@NonNull MemberModel rank,
                                               @NonNull MemberModel rankTwo) {
                    return rank.getUserMobNo() == rankTwo.getUserMobNo();
                }

                @Override
                public boolean areContentsTheSame(@NonNull MemberModel rank,
                                                  @NonNull MemberModel rankTwo) {
                    return rank.getUserMobNo().equals(rankTwo.getUserMobNo());
                }
            };
    private static Context context;
    private static ClassMemberPagedAdapter.MyClickListener myClickListener;

    public ClassMemberPagedAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
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
        MemberModel classMemberList = getItem(position);
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

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().trim();

                PagedList<MemberModel> filtered = getCurrentList();

                if (query.isEmpty()) {
                    filtered = getCurrentList();
                } else {
                    for (MemberModel memberModel : getCurrentList().snapshot()) {
                        if (memberModel.getUserType().toLowerCase().contains("student")) {
                            filtered.add(memberModel);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.count = filtered.size();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                submitList((PagedList<MemberModel>) results.values);
            }
        };
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

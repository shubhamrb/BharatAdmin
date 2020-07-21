package com.bozobaka.bharatadmin.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.ContactModel;
import com.bozobaka.bharatadmin.views.TextDrawable;
import com.bozobaka.bharatadmin.views.scrollbar.INameableAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsAdapter extends RecyclerView
        .Adapter<ContactsAdapter
        .DataObjectHolder> implements INameableAdapter {
    static int fixed = 0;
    private static ContactsAdapter.MyClickListener myClickListener;
    private static Context context;
    private List<ContactModel> contacts = new ArrayList<>();
    private ArrayList<ContactModel> arraylist;
    private boolean filterStaus = false;
    private String cardPrivacy;

    public ContactsAdapter(Context context,
                           List<ContactModel> contacts, String cardPrivacy) {
        this.context = context;
        this.contacts = contacts;
        this.cardPrivacy = cardPrivacy;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void setData(List<ContactModel> contacts) {
        this.arraylist = new ArrayList<ContactModel>();
        this.arraylist.addAll(contacts);
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_row_layout, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DataObjectHolder holder, int position) {
        ContactModel contact = contacts.get(position);
        holder.name.setText(contact.getName());
        holder.mobNo.setText(contact.getNumber());

        TextDrawable textDrawable;
        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().endConfig().round();
        if (contact.getName() == null) {
            textDrawable = builder.build("", context.getResources().getColor(R.color.color7));
        } else if (position % 5 == 0) {
            textDrawable = builder.build(contact.getName().toUpperCase().charAt(0) + "", context.getResources().getColor(R.color.color4));
        } else if (position % 5 == 1) {
            textDrawable = builder.build(contact.getName().toUpperCase().charAt(0) + "", context.getResources().getColor(R.color.color5));
        } else if (position % 5 == 2) {
            textDrawable = builder.build(contact.getName().toUpperCase().charAt(0) + "", context.getResources().getColor(R.color.color6));
        } else if (position % 5 == 3) {
            textDrawable = builder.build(contact.getName().toUpperCase().charAt(0) + "", context.getResources().getColor(R.color.color7));
        } else {
            textDrawable = builder.build(contact.getName().toUpperCase().charAt(0) + "", context.getResources().getColor(R.color.color8));
        }
        holder.textDrawableImg.setImageDrawable(textDrawable);
        if (contact.isSelected()) {
            if (cardPrivacy.contentEquals("MY_CONTACTS_EXCEPT")) {
                holder.checkImg.setImageDrawable(context.getResources()
                        .getDrawable(R.drawable.ic_check_circle_red_24dp));
            } else {
                holder.checkImg.setImageDrawable(context.getResources()
                        .getDrawable(R.drawable.ic_check_circle_black_24dp));
            }
        } else {
            holder.checkImg
                    .setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.ic_radio_button_unchecked_black_24dp));
        }
    }

    @Override
    public int getItemCount() {
        Log.d("contacts size: ", String.valueOf(contacts.size()));
        return contacts.size();
    }

    // Filter Class
    public void filter(String charText) {
        if (charText != null) {
            charText = charText.toLowerCase(Locale.getDefault());
            contacts.clear();
            if (arraylist != null && arraylist.size() > 0) {
                if (charText.length() == 0) {
                    contacts.addAll(arraylist);
                } else {
                    for (ContactModel fp : arraylist) {
                        if (fp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                            contacts.add(fp);
                        }
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    // Filter Class
    public void filterStatus(boolean status) {
        this.filterStaus = status;
    }

    @Override
    public Character getCharacterForElement(int element) {
        if (contacts.size() != 0 && !filterStaus) {
            Character c = contacts.get(element).getName().charAt(0);
            if (Character.isDigit(c)) {
                c = '#';
            }
            return c;
        } else {
            return '#';
        }
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.tv_mob_no)
        TextView mobNo;
        @BindView(R.id.iv_text_drawable)
        ImageView textDrawableImg;
        //        @BindView(R.id.parentLayout)
//        RelativeLayout parentLayout;
        @BindView(R.id.iv_check)
        ImageView checkImg;

        public DataObjectHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

}

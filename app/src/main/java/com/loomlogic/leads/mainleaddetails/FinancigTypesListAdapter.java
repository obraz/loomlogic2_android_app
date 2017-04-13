package com.loomlogic.leads.mainleaddetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.leads.entity.FinancingTypeItem;

import java.util.List;

/**
 * Created by alex on 05.10.2016.
 */

public class FinancigTypesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface FinancingTypeSelectorCallback {
        void onFinancingTypeSelected(FinancingTypeItem item);
    }

    private List<FinancingTypeItem> list;
    private FinancingTypeSelectorCallback callback;

    public FinancigTypesListAdapter(FinancingTypeSelectorCallback callback) {
        this.callback = callback;
    }

    public void setFinancingTypeList(List<FinancingTypeItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_financing_type, parent, false);
        return new ViewHolderList(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderList vh = (ViewHolderList) holder;
        final FinancingTypeItem item = list.get(position);
        vh.name.setText(item.name);

        vh.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onFinancingTypeSelected(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderList extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolderList(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}

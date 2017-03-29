package com.loomlogic.leads.create;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.leads.entity.LeadSourceItem;

import java.util.List;

/**
 * Created by alex on 05.10.2016.
 */

public class SourcesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface SourceSelectorCallback {
        void onSourceSelected(LeadSourceItem source);
    }

    private List<LeadSourceItem> sourcesList;
    private SourceSelectorCallback callback;

    public SourcesListAdapter(SourceSelectorCallback callback) {
        this.callback = callback;
    }

    public void setSourcesList(List<LeadSourceItem> sourcesList) {
        this.sourcesList = sourcesList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.tem_lead_source, parent, false);
        return new ViewHolderSourcesList(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderSourcesList vh = (ViewHolderSourcesList) holder;
        final LeadSourceItem source = sourcesList.get(position);
        vh.sourceName.setText(source.name);

        vh.sourceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onSourceSelected(source);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sourcesList.size();
    }

    public class ViewHolderSourcesList extends RecyclerView.ViewHolder {
        TextView sourceName;

        public ViewHolderSourcesList(View view) {
            super(view);
            sourceName = (TextView) view.findViewById(R.id.tv_sourceName);
        }
    }
}

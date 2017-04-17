package com.loomlogic.leads.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;

/**
 * Created by alex on 4/17/17.
 */

public class FooterAdapter extends AbstractHeaderFooterWrapperAdapter<FooterAdapter.HeaderViewHolder, FooterAdapter.FooterViewHolder>
{

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public FooterAdapter(RecyclerView.Adapter adapter) {
        setAdapter(adapter);
    }

    @Override
    public int getHeaderItemCount() {
        return 0;
    }

    @Override
    public int getFooterItemCount() {
        return 1;
    }

    @Override
    public HeaderViewHolder onCreateHeaderItemViewHolder(ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item, parent, false);
       // HeaderViewHolder vh = new HeaderViewHolder(v);

        return null;
    }

    @Override
    public FooterViewHolder onCreateFooterItemViewHolder(ViewGroup parent, int viewType) {
        View v = new View(parent.getContext());//LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_item, parent, false);
        FooterViewHolder vh = new FooterViewHolder(v);

        return vh;
    }

    @Override
    public void onBindHeaderItemViewHolder(HeaderViewHolder holder, int localPosition) {

    }

    @Override
    public void onBindFooterItemViewHolder(FooterViewHolder holder, int localPosition) {

    }


}
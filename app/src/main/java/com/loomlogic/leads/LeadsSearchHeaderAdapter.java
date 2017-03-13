package com.loomlogic.leads;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;
import com.loomlogic.R;

/**
 * Created by alex on 3/13/17.
 */

public class LeadsSearchHeaderAdapter extends AbstractHeaderFooterWrapperAdapter<LeadsSearchHeaderAdapter.HeaderViewHolder, LeadsSearchHeaderAdapter.HeaderViewHolder> {
    private SearchView.OnQueryTextListener onQueryTextListener;

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private SearchView searchView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            searchView = (SearchView) itemView.findViewById(R.id.searchView);
        }
    }

    public LeadsSearchHeaderAdapter(RecyclerView.Adapter adapter, SearchView.OnQueryTextListener onQueryTextListener) {
        setAdapter(adapter);
        this.onQueryTextListener = onQueryTextListener;
    }

    @Override
    public int getHeaderItemCount() {
        return 1;
    }

    @Override
    public int getFooterItemCount() {
        return 0;
    }

    @Override
    public HeaderViewHolder onCreateHeaderItemViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search, parent, false);
        HeaderViewHolder vh = new HeaderViewHolder(v);
        vh.searchView.setIconifiedByDefault(false);
        vh.searchView.setOnQueryTextListener(onQueryTextListener);
        return vh;
    }

    @Override
    public HeaderViewHolder onCreateFooterItemViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindHeaderItemViewHolder(HeaderViewHolder holder, int localPosition) {
    }

    @Override
    public void onBindFooterItemViewHolder(HeaderViewHolder holder, int localPosition) {

    }

}

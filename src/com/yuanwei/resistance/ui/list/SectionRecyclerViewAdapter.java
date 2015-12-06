package com.yuanwei.resistance.ui.list;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuanwei.resistance.R;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by chenyuanwei on 15/11/24.
 */
public class SectionRecyclerViewAdapter extends RecyclerView.Adapter<CheckableViewHolder> {

    private static final int SECTION_TYPE = 0;

    private boolean mValid = true;
    private boolean mRoleValid = true;
    private boolean mOptionValid = true;
    private RecyclerView.Adapter mRoleAdapter;
    private RecyclerView.Adapter mOptionAdapter;
    private SparseArray<Section> mSections = new SparseArray<>();


    public SectionRecyclerViewAdapter(
            RecyclerView.Adapter roleAdapter,
            RecyclerView.Adapter optionAdapter) {

        mRoleAdapter = roleAdapter;
        mOptionAdapter = optionAdapter;

        mRoleAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mRoleValid = mRoleAdapter.getItemCount() > 0;
                mValid = mRoleValid || mOptionValid;
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                mRoleValid = mRoleAdapter.getItemCount() > 0;
                mValid = mRoleValid || mOptionValid;
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRoleValid = mRoleAdapter.getItemCount() > 0;
                mValid = mRoleValid || mOptionValid;
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                mRoleValid = mRoleAdapter.getItemCount() > 0;
                mValid = mRoleValid || mOptionValid;
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        });

        mOptionAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mOptionValid = mOptionAdapter.getItemCount() > 0;
                mValid = mRoleValid || mOptionValid;
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                mOptionValid = mOptionAdapter.getItemCount() > 0;
                mValid = mRoleValid || mOptionValid;
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mOptionValid = mOptionAdapter.getItemCount() > 0;
                mValid = mRoleValid || mOptionValid;
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                mOptionValid = mOptionAdapter.getItemCount() > 0;
                mValid = mRoleValid || mOptionValid;
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }

    @Override
    public CheckableViewHolder onCreateViewHolder(ViewGroup parent, int typeView) {

            return new CheckableViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.listitem_game_setup, parent, false));
    }

    @Override
    public void onBindViewHolder(CheckableViewHolder viewHolder, int position) {
        if (isSectionHeaderPosition(position)) {
            viewHolder.container.getBackground().setAlpha(127);
            viewHolder.name.setText(mSections.get(position).title);
            viewHolder.description.setVisibility(View.GONE);
            viewHolder.image.setVisibility(View.GONE);
            viewHolder.checkBox.setVisibility(View.GONE);
        } else if (getSection(position) == 0){
            mOptionAdapter.onBindViewHolder(viewHolder, getSectionPosition(position));
        } else {
            mRoleAdapter.onBindViewHolder(viewHolder, getSectionPosition(position));
        }

    }

    @Override
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position)
                ? SECTION_TYPE
                : getSectionPosition(position) + 1;
    }


    public static class Section {
        int position;
        CharSequence title;

        public Section(int position, CharSequence title) {
            this.position = position;
            this.title = title;
        }

        public CharSequence getTitle() {
            return title;
        }
    }


    public void setSections(Section[] sections) {
        mSections.clear();

        Arrays.sort(sections, new Comparator<Section>() {
            @Override
            public int compare(Section o, Section o1) {
                return (o.position == o1.position)
                        ? 0
                        : ((o.position < o1.position) ? -1 : 1);
            }
        });

        for (Section section : sections) {
            mSections.append(section.position, section);
        }

        notifyDataSetChanged();
    }

    public boolean isSectionHeaderPosition(int position) {
        return mSections.get(position) != null;
    }


    private int getSection(int position) {
        if (isSectionHeaderPosition(position)) {
            return RecyclerView.NO_POSITION;
        }

        int offset = 0;
        for (int i = mSections.size() -1; i >= 0; i--) {
            if (mSections.valueAt(i).position < position) {
                offset = i;
                break;
            }
        }
        return offset;
    }

    private int getSectionPosition(int position) {
        if (isSectionHeaderPosition(position)) {
            return RecyclerView.NO_POSITION;
        }

        int offset = 0;
        for (int i = mSections.size() -1; i >= 0; i--) {
            if (mSections.valueAt(i).position < position) {
                offset = mSections.valueAt(i).position + 1;
                break;
            }
        }
        return position - offset;
    }


    @Override
    public long getItemId(int position) {
        if (isSectionHeaderPosition(position)) {
            return Integer.MAX_VALUE - mSections.indexOfKey(position);
        }

        return getSection(position) == 0
                ? mOptionAdapter.getItemId(getSectionPosition(position))
                : mRoleAdapter.getItemId(getSectionPosition(position));
    }

    @Override
    public int getItemCount() {
        return (mValid
                ? mRoleAdapter.getItemCount() + +mOptionAdapter.getItemCount() + mSections.size()
                : 0);
    }
}

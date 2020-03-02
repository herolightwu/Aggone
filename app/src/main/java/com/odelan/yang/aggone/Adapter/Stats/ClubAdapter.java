package com.odelan.yang.aggone.Adapter.Stats;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.odelan.yang.aggone.Model.Cell;
import com.odelan.yang.aggone.R;

public class ClubAdapter extends AbstractTableAdapter {
    Context context;

    public ClubAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getColumnHeaderItemViewType(int columnPosition) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int rowPosition) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int columnPosition) {
        return 0;
    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_cell, parent, false);
        return new CellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int columnPosition, int rowPosition) {
        Cell cell = (Cell) cellItemModel;
        ((CellViewHolder)(holder)).txt_value.setText(cell.value);
        if (cell.row == -1) {
            ((CellViewHolder)(holder)).img_background.setImageResource(R.mipmap.stats_total_background);
            ((CellViewHolder)(holder)).txt_value.setTextColor(Color.WHITE);
            ((CellViewHolder)(holder)).txt_value.setTextSize(12);
        } else if (cell.row % 2 == 0) {
            ((CellViewHolder)(holder)).img_background.setImageResource(R.mipmap.stats_odd_background);
            ((CellViewHolder)(holder)).txt_value.setTextColor(Color.parseColor("#404040"));
            ((CellViewHolder)(holder)).txt_value.setTextSize(10);
        } else {
            ((CellViewHolder)(holder)).img_background.setImageResource(R.mipmap.stats_even_background);
            ((CellViewHolder)(holder)).txt_value.setTextColor(Color.parseColor("#404040"));
            ((CellViewHolder)(holder)).txt_value.setTextSize(10);
        }
    }

    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_column_header, parent, false);
        return new ColumnHeaderViewHolder(layout);
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int columnPosition) {
        String value = (String) columnHeaderItemModel;
        ((ColumnHeaderViewHolder)(holder)).txt_value.setText(value);
    }

    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_row_header, parent, false);
        return new RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {
        String value = (String) rowHeaderItemModel;
        ((RowHeaderViewHolder)(holder)).txt_value.setText(value);
        if (rowPosition == mCellItems.size()-1) {
            ((RowHeaderViewHolder)(holder)).img_background.setImageResource(R.mipmap.stats_total_background);
            ((RowHeaderViewHolder)(holder)).txt_value.setTextColor(Color.WHITE);
            ((RowHeaderViewHolder)(holder)).txt_value.setTextSize(12);
        } else if (rowPosition % 2 == 0) {
            ((RowHeaderViewHolder)(holder)).img_background.setImageResource(R.mipmap.stats_odd_background);
            ((RowHeaderViewHolder)(holder)).txt_value.setTextColor(Color.parseColor("#404040"));
            ((RowHeaderViewHolder)(holder)).txt_value.setTextSize(10);
        } else {
            ((RowHeaderViewHolder)(holder)).img_background.setImageResource(R.mipmap.stats_even_background);
            ((RowHeaderViewHolder)(holder)).txt_value.setTextColor(Color.parseColor("#404040"));
            ((RowHeaderViewHolder)(holder)).txt_value.setTextSize(10);
        }
    }

    @Override
    public View onCreateCornerView() {
        return LayoutInflater.from(context).inflate(R.layout.item_club_corner, null);
    }

    class CellViewHolder extends AbstractViewHolder {
        public TextView txt_value;
        public ImageView img_background;
        public CellViewHolder(View itemView) {
            super(itemView);
            txt_value = itemView.findViewById(R.id.txt_value);
            img_background = itemView.findViewById(R.id.img_background);
        }
    }

    class ColumnHeaderViewHolder extends AbstractViewHolder {
        public TextView txt_value;
        public ColumnHeaderViewHolder(View itemView) {
            super(itemView);
            txt_value = itemView.findViewById(R.id.txt_value);
        }
    }

    class RowHeaderViewHolder extends AbstractViewHolder {
        public TextView txt_value;
        public ImageView img_background;
        public RowHeaderViewHolder(View itemView) {
            super(itemView);
            txt_value = itemView.findViewById(R.id.txt_value);
            img_background = itemView.findViewById(R.id.img_background);
        }
    }
}

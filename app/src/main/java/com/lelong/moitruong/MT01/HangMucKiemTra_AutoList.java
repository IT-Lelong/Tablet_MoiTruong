package com.lelong.moitruong.MT01;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import java.util.ArrayList;
import java.util.List;

public class HangMucKiemTra_AutoList extends ArrayAdapter<String> implements Filterable {

    private List<String> originalSuggestions;
    private List<String> filteredSuggestions; // Danh sách mục đã lọc
    private List<Integer> originalPositions; // Danh sách vị trí tương ứng với các mục gợi ý ban đầu

    public HangMucKiemTra_AutoList(Context context, List<String> suggestions) {
        super(context, android.R.layout.simple_dropdown_item_1line);
        this.originalSuggestions = new ArrayList<>(suggestions);
        this.filteredSuggestions = new ArrayList<>();
        this.originalPositions = new ArrayList<>();
        for (int i = 0; i < originalSuggestions.size(); i++) {
            originalPositions.add(i);
        }
    }

    @Override
    public Filter getFilter() {
        return new CustomFilter();
    }

    private class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            filteredSuggestions.clear();
            originalPositions.clear(); // Xóa danh sách vị trí và cập nhật lại

            for (int i = 0; i < originalSuggestions.size(); i++) {
                String suggestion = originalSuggestions.get(i);
                if (suggestion.contains(constraint)) {
                    filteredSuggestions.add(suggestion);
                    originalPositions.add(i); // Cập nhật danh sách vị trí
                }
            }

            results.values = filteredSuggestions;
            results.count = filteredSuggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results.count > 0) {
                addAll((List<String>) results.values);
                notifyDataSetChanged();
            }
        }
    }

    // Phương thức để lấy vị trí của mục đã chọn
    public int getPositionOfSelected(String selectedText) {
        int position = -1;
        int itemCount = getCount();
        for (int i = 0; i < itemCount; i++) {
            String item = getItem(i);
            if (item != null && item.equals(selectedText)) {
                position = originalPositions.get(i);
                break;
            }
        }
        return position;
    }
}

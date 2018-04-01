package studio.weiweima.cake.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import studio.weiweima.cake.R;

public class SimpleAdapter extends ArrayAdapter {

    private List<String> data;
    private boolean[] isChecks;
    private int selectedPosition = -1;
    private int selectedColorRes = -1;

    public SimpleAdapter(Context context, int resourceId, List<String> data) {
        super(context, resourceId, data);
        this.data = data;
    }

    public void setSelectedColorRes(int selectedColorRes) {
        this.selectedColorRes = selectedColorRes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (view instanceof CheckBox && isChecks != null && position < isChecks.length) {
            ((CheckBox) view).setChecked(isChecks[position]);
        }
        if (position == selectedPosition && selectedColorRes != -1) {
            view.setBackgroundResource(selectedColorRes);
        } else {
            view.setBackgroundResource(R.color.transplant);
        }
        return view;
    }

    public void update(List<String> newData) {
        data.clear();
        data.addAll(newData);
        if (selectedPosition != -1 && selectedPosition >= data.size()) {
            selectedPosition = data.size() - 1;
        }
        notifyDataSetChanged();
    }

    public void setChecked(int position, boolean check) {
        if (isChecks == null) {
            Log.e("check", "check state is null");
            isChecks = new boolean[data.size()];
        }
        isChecks[position] = check;
    }

    public void updateChecks(List<String> checked) {
        isChecks = new boolean[data.size()];
        checked.forEach(c -> {
            int index = data.indexOf(c);
            if (index != -1) {
                isChecks[index] = true;
            }
        });
        notifyDataSetChanged();
    }

    public List<String> getCheckedData() {
        if (isChecks == null) {
            Log.e("adapter", "check state is null");
            return new ArrayList<>();
        }
        List<String> checked = new ArrayList<>();
        for (int i = 0; i < isChecks.length; i++) {
            if (isChecks[i]) {
                checked.add(data.get(i));
            }
        }
        return checked;
    }

    public List<String> getData() {
        return new ArrayList<>(data);
    }

    public int getPosition(String s) {
        int index = data.indexOf(s);
        return index == -1 ? 0 : index;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void clearItemBackground(AdapterView<?> adapterView) {
        if (adapterView == null) {
            return;
        }
        int childCount = adapterView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            adapterView.getChildAt(i).setBackgroundResource(R.color.transplant);
        }
    }
}

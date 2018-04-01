package studio.weiweima.cake.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.function.Function;

public class CommonAdapter extends ArrayAdapter {

    private List data;
    private Function<Object[], View> bindViewFunction;
    private int resourceId;

    public CommonAdapter(Context context, int resourceId, List data) {
        super(context, resourceId, data);
        this.data = data;
        this.resourceId = resourceId;
    }

    public CommonAdapter(Context context, int resourceId, List data, Function<Object[], View> onBindView) {
        this(context, resourceId, data);
        this.bindViewFunction = onBindView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        if (bindViewFunction == null) {
            return view;
        }
        return bindViewFunction.apply(new Object[]{position, view, this});
    }

    @SuppressWarnings("unchecked")
    public void update(List newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

}

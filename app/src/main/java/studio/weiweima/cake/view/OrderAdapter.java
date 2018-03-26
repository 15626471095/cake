package studio.weiweima.cake.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import studio.weiweima.cake.MainActivity;
import studio.weiweima.cake.R;
import studio.weiweima.cake.bean.Order;

public class OrderAdapter extends ArrayAdapter {

    private MainActivity mainActivity;
    private final int resourceId;
    private Map<Integer, Order> orderMap = new HashMap<>();


    public OrderAdapter(MainActivity mainActivity, int resourceId, List<Order> orderList) {
        super(mainActivity, resourceId, orderList);
        this.mainActivity = mainActivity;
        this.resourceId = resourceId;
        orderList.forEach(o -> orderMap.put(o.getId(), o));
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Order order = (Order) getItem(position);
        assert order != null;
        order.setPosition(position);
        @SuppressLint("ViewHolder")
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ((TextView) view.findViewById(R.id.name)).setText(order.getName());
        ((TextView) view.findViewById(R.id.phone)).setText(order.getPhone());
        ((TextView) view.findViewById(R.id.mode)).setText(order.getMode());
        ((TextView) view.findViewById(R.id.targetTime)).setText(order.getTargetTime());
        ((TextView) view.findViewById(R.id.destination)).setText(order.getDestination());
        //((ImageView) view.findViewById(R.id.name)).setImageURI();
        ((TextView) view.findViewById(R.id.price)).setText(order.getPrice());
        ((TextView) view.findViewById(R.id.priceState)).setText(order.getPriceState());
        ((TextView) view.findViewById(R.id.progress)).setText(order.getProgress());
        ((TextView) view.findViewById(R.id.additional)).setText(order.getAdditional());

        view.findViewById(R.id.editBtn).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(getContext(), EditActivity.class);
            mainActivity.startActivityForResult(intent, MainActivity.REQUEST_CODE);
        });
        return view;
    }

    public void updateItem(Order order) {
        assert order != null;
        orderMap.get(order.getId()).valueOf(order);
    }
}

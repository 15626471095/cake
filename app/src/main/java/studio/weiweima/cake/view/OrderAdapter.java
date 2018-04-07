package studio.weiweima.cake.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import studio.weiweima.cake.MainActivity;
import studio.weiweima.cake.R;
import studio.weiweima.cake.bean.Cake;
import studio.weiweima.cake.bean.Order;
import studio.weiweima.cake.bean.Progress;
import studio.weiweima.cake.bean.RequestCode;
import studio.weiweima.cake.database.StorageManager;
import studio.weiweima.cake.util.BitmapCache;
import studio.weiweima.cake.util.DialogUtils;
import studio.weiweima.cake.util.StringUtils;
import studio.weiweima.cake.util.Utils;

public class OrderAdapter extends ArrayAdapter {

    private MainActivity mainActivity;
    private final int resourceId;

    private List<Order> orderList;
    private Map<Integer, Order> orderMap = new HashMap<>();
    private Calendar calendar;


    public OrderAdapter(MainActivity mainActivity, int resourceId, List<Order> orderList) {
        super(mainActivity, resourceId, orderList);
        this.mainActivity = mainActivity;
        this.resourceId = resourceId;
        updateOrderList(orderList, false);
    }

    public Calendar getCalendar() {
        if (calendar == null) {
            calendar = Calendar.getInstance();
            calendar.setTime(new Date(System.currentTimeMillis()));
        }
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void updateOrderList(List<Order> orders, boolean notify) {
        orderMap.clear();
        if (this.orderList == null) {
            this.orderList = orders;
        } else {
            orderList.clear();
            orderList.addAll(orders);
        }
        orderList.forEach(o -> orderMap.put(o.getId(), o));
        if (notify) {
            notifyDataSetChanged();
        }
    }

    private void addOrUpdateOrder(Order order) {
        if (orderMap.containsKey(order.getId())) {
            orderMap.get(order.getId()).valueOf(order);
        } else {
            orderList.add(order);
            orderMap.put(order.getId(), order);
        }
        assert orderMap.size() == orderList.size();
    }

    public void updateItemCakes(int orderId, List<Cake> cakes) {
        if (!orderMap.containsKey(orderId)) {
            Toast.makeText(mainActivity, "系统订单ID出错，请找哥哥解决！", Toast.LENGTH_LONG).show();
            return;
        }
        orderMap.get(orderId).setCakes(cakes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Order order = (Order) getItem(position);
        assert order != null;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        setOrderItemProgress(view, order.getProgress());
        ((TextView) view.findViewById(R.id.name)).setText(order.getName());
        ((TextView) view.findViewById(R.id.phone)).setText(order.getPhone());
        ((TextView) view.findViewById(R.id.mode)).setText(order.getMode().value());
        ((TextView) view.findViewById(R.id.targetTime)).setText(order.getSimpleTargetTime());
        ((TextView) view.findViewById(R.id.destination)).setText(order.getDestination());
        ((TextView) view.findViewById(R.id.price)).setText(order.getPrice());
        ((TextView) view.findViewById(R.id.payState)).setText(order.getPayState().value());
        ((TextView) view.findViewById(R.id.progress)).setText(order.getProgress().value());
        ((TextView) view.findViewById(R.id.additional)).setText(order.getAdditional());
        view.findViewById(R.id.editBtn).setOnClickListener(v -> {
            Intent intent = Utils.encodeOrder(order);
            intent.setClass(getContext(), EditActivity.class);
            mainActivity.startActivityForResult(intent, RequestCode.EDIT_ORDER);
        });
        view.findViewById(R.id.deleteBtn).setOnClickListener(v -> DialogUtils.showDeleteConfirmDialog(mainActivity, t -> {
            removeOrder(position);
        }));
        setCakeList(view, order);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("unchecked")
    private void setCakeList(View view, Order order) {
        //((ImageView) view.findViewById(R.id.name)).setImageURI();
        ListView listView = view.findViewById(R.id.orderItems);
        listView.setBackgroundResource(R.color.transplant);
        listView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                mainActivity.getListView().requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });
        listView.setOnItemClickListener((parent, view1, p, id) -> {
            Intent intent = Utils.encodeCakes(order.getCakes(), order.getId());
            intent.setClass(mainActivity, CakeActivity.class);
            mainActivity.startActivityForResult(intent, RequestCode.EDIT_CAKES);
        });
        CommonAdapter adapter;
        if (listView.getAdapter() == null) {
            adapter = new CommonAdapter(mainActivity, R.layout.order_item_abstract, new ArrayList<>(), args -> {
                int pos = (int) args[0];
                View v = (View) args[1];
                CommonAdapter self = (CommonAdapter) args[2];
                Cake cake = (Cake) self.getItem(pos);
                assert cake != null;
                ((TextView) v.findViewById(R.id.cakeAbstract)).setText(cake.getAbstract());
                ImageView image = v.findViewById(R.id.cakeAbstractPicture);
                if (!StringUtils.isEmpty(cake.getPictureUri())) {
                    Bitmap bitmap = BitmapCache.getInstance().getBitmap(mainActivity, cake.getPictureUri(), 30, 30);
                    if (bitmap != null) {
                        image.setImageBitmap(bitmap);
                    }
                } else {
                    image.setImageResource(R.drawable.ic_launcher_background);
                }
                return v;
            });
            listView.setAdapter(adapter);
        } else {
            adapter = (CommonAdapter) listView.getAdapter();
        }
        adapter.update(order.getCakes());
    }

    public void updateItem(Order order) {
        assert order != null;
        addOrUpdateOrder(order);
        notifyDataSetChanged();
    }

    public void removeOrder(int position) {
        Order order = orderList.remove(position);
        orderMap.remove(order.getId());
        assert orderMap.size() == orderList.size();
        StorageManager.getInstance().updateOrders(calendar, orderList);
        notifyDataSetChanged();
    }

    private void setOrderItemProgress(View view, Progress progress) {
        view.setBackgroundResource(progress.color());
    }

    /**
     * true if increase
     * false if decrease
     */
    private boolean currentSortMode = false;

    @SuppressWarnings("unchecked")
    public void sort(String sortKey) {
        sort((a1, a2) -> {
            Order o1 = (Order) a1;
            Order o2 = (Order) a2;
            int res;
            switch (sortKey) {
                case "progress":
                    res = o1.getProgress().compareTo(o2.getProgress());
                    break;
                case "mode":
                    res = o1.getMode().compareTo(o2.getMode());
                    break;
                case "targetTime":
                    res = o1.getTargetTime().compareTo(o2.getTargetTime());
                    break;
                case "destination":
                    res = o1.getDestination().compareTo(o2.getDestination());
                    break;
                case "price":
                    res = Double.compare(Double.valueOf(o1.getPrice()), Double.valueOf(o2.getPrice()));
                    break;
                case "payState":
                    res = o1.getPayState().compareTo(o2.getPayState());
                    break;
                default:
                    res = 0;
                    break;
            }
            if (currentSortMode) {
                res *= -1;
            }
            return res;
        });
        currentSortMode = !currentSortMode;
    }
}

package studio.weiweima.cake;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import studio.weiweima.cake.bean.Cake;
import studio.weiweima.cake.bean.Order;
import studio.weiweima.cake.bean.RequestCode;
import studio.weiweima.cake.database.StorageManager;
import studio.weiweima.cake.util.PermissionUtils;
import studio.weiweima.cake.util.Utils;
import studio.weiweima.cake.view.EditActivity;
import studio.weiweima.cake.view.OrderAdapter;


public class MainActivity extends AppCompatActivity {

    private List<Order> orderList = new ArrayList<>();
    private ListView listView;
    private static int STORE_INTERVAL = 60 * 10 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        PermissionUtils.verifyStoragePermissions(this);
        initOrderList();
        initListView();
        initCreateBtn();
        initHeaders();
        setUpTimer();
    }

    public void setUpTimer() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                StorageManager.getInstance().onUpdate(MainActivity.this);
                handler.postDelayed(this, STORE_INTERVAL);
            }
        };
        handler.postDelayed(runnable, STORE_INTERVAL);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        StorageManager.getInstance().updateOrders(new Date(System.currentTimeMillis()), orderList);
        StorageManager.getInstance().onUpdate(this);
    }

    private void initOrderList() {
        orderList = StorageManager.getInstance().getOrders(this, new Date(System.currentTimeMillis()));
    }

    private void initListView() {
        listView = findViewById(R.id.table);
        OrderAdapter adapter = new OrderAdapter(MainActivity.this, R.layout.item, orderList);
        listView.setAdapter(adapter);
    }

    private void initCreateBtn() {
        findViewById(R.id.create).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(this, EditActivity.class);
            this.startActivityForResult(intent, RequestCode.EDIT_ORDER);
        });
    }

    private Calendar calendar = null;

    private void initHeaders() {
        OrderAdapter adapter = (OrderAdapter) listView.getAdapter();
        findViewById(R.id.header_mode).setOnClickListener(v -> adapter.sort("mode"));
        findViewById(R.id.header_targetTime).setOnClickListener(v -> adapter.sort("targetTime"));
        findViewById(R.id.header_destination).setOnClickListener(v -> adapter.sort("destination"));
        findViewById(R.id.header_price).setOnClickListener(v -> adapter.sort("price"));
        findViewById(R.id.header_payState).setOnClickListener(v -> adapter.sort("payState"));
        findViewById(R.id.header_progress).setOnClickListener(v -> adapter.sort("progress"));
        TextView datePicker = findViewById(R.id.datePicker);
        datePicker.setOnClickListener(v -> {
            if (calendar == null) {
                calendar = Calendar.getInstance();
                calendar.setTime(new Date(System.currentTimeMillis()));
            }
            new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
                datePicker.setText(String.format("%s月%s日", monthOfYear + 1, dayOfMonth));
                this.calendar.set(year, monthOfYear, dayOfMonth);
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                ((OrderAdapter) listView.getAdapter()).updateOrderList(StorageManager.getInstance().getOrders(this, cal), true);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.EDIT_ORDER && resultCode == RESULT_OK) {
            Order order = Utils.decodeOrder(data);
            ((OrderAdapter) listView.getAdapter()).updateItem(order);
        } else if (requestCode == RequestCode.EDIT_CAKES && resultCode == RESULT_OK) {
            Pair<Integer, List<Cake>> args = Utils.decodeCakesWithOrderId(data);
            ((OrderAdapter) listView.getAdapter()).updateItemCakes(args.first, args.second);
        }
        StorageManager.getInstance().updateOrders(new Date(System.currentTimeMillis()), orderList);
    }

    public ListView getListView() {
        return listView;
    }
}


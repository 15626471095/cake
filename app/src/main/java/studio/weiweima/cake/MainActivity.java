package studio.weiweima.cake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import studio.weiweima.cake.bean.Cake;
import studio.weiweima.cake.bean.Mode;
import studio.weiweima.cake.bean.Order;
import studio.weiweima.cake.bean.PayState;
import studio.weiweima.cake.bean.Progress;
import studio.weiweima.cake.util.PermissionUtils;
import studio.weiweima.cake.util.Utils;
import studio.weiweima.cake.view.EditActivity;
import studio.weiweima.cake.view.OrderAdapter;


public class MainActivity extends AppCompatActivity {


    public static final int EDIT_ORDER = 200;

    private List<Order> orderList = new ArrayList<>();
    private ListView listView;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
        PermissionUtils.verifyStoragePermissions(this);
    }

    private void init() {
        orderList.add(new Order("陈小姐", "15626471095", Mode.Self, "生日快乐",
                "北栅", "200", PayState.Alipay, Progress.ToDistribute,
                Arrays.asList(new Cake(), new Cake("盒子蛋糕"), new Cake(), new Cake(), new Cake(), new Cake())));
        listView = findViewById(R.id.table);
        orderAdapter = new OrderAdapter(MainActivity.this, R.layout.item, orderList);
        listView.setAdapter(orderAdapter);
        findViewById(R.id.create).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(this, EditActivity.class);
            this.startActivityForResult(intent, MainActivity.EDIT_ORDER);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ORDER && resultCode == RESULT_OK) {
            Order order = Utils.decodeOrder(data);
            orderAdapter.updateItem(order);
        }
    }
}


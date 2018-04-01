package studio.weiweima.cake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
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
import studio.weiweima.cake.bean.RequestCode;
import studio.weiweima.cake.util.PermissionUtils;
import studio.weiweima.cake.util.Utils;
import studio.weiweima.cake.view.EditActivity;
import studio.weiweima.cake.view.OrderAdapter;


public class MainActivity extends AppCompatActivity {

    private List<Order> orderList = new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        PermissionUtils.verifyStoragePermissions(this);
        initOrderList();
        initListView();
        initCreateBtn();
        initSortHeader();
    }

    private void initOrderList() {
        orderList.add(new Order("陈小姐", "15626471095", Mode.Self, "生日快乐",
                "北栅", "200", PayState.Alipay, Progress.ToDistribute,
                Arrays.asList(new Cake(), new Cake("盒子蛋糕"), new Cake(), new Cake(), new Cake(), new Cake())));
        orderList.add(new Order("陈小姐", "15626471095", Mode.Self, "生日快乐",
                "北栅", "200", PayState.Alipay, Progress.ToDistribute,
                Arrays.asList(new Cake(), new Cake("盒子蛋糕"), new Cake(), new Cake(), new Cake(), new Cake())));
        orderList.add(new Order("陈小姐", "15626471095", Mode.Self, "生日快乐",
                "北栅", "200", PayState.Alipay, Progress.ToDistribute,
                Arrays.asList(new Cake(), new Cake("盒子蛋糕"), new Cake(), new Cake(), new Cake(), new Cake())));
        orderList.add(new Order("陈小姐", "15626471095", Mode.Self, "生日快乐",
                "北栅", "200", PayState.Alipay, Progress.ToDistribute,
                Arrays.asList(new Cake(), new Cake("盒子蛋糕"), new Cake(), new Cake(), new Cake(), new Cake())));
        orderList.add(new Order("陈小姐", "15626471095", Mode.Self, "生日快乐",
                "北栅", "200", PayState.Alipay, Progress.ToDistribute,
                Arrays.asList(new Cake(), new Cake("盒子蛋糕"), new Cake(), new Cake(), new Cake(), new Cake())));
        orderList.add(new Order("陈小姐", "15626471095", Mode.Self, "生日快乐",
                "北栅", "200", PayState.Alipay, Progress.ToDistribute,
                Arrays.asList(new Cake(), new Cake("盒子蛋糕"), new Cake(), new Cake(), new Cake(), new Cake())));
        orderList.add(new Order("陈小姐", "15626471095", Mode.Self, "生日快乐",
                "北栅", "200", PayState.Alipay, Progress.ToDistribute,
                Arrays.asList(new Cake(), new Cake("盒子蛋糕"), new Cake(), new Cake(), new Cake(), new Cake())));
        orderList.add(new Order("陈小姐", "15626471095", Mode.Self, "生日快乐",
                "北栅", "200", PayState.Alipay, Progress.ToDistribute,
                Arrays.asList(new Cake(), new Cake("盒子蛋糕"), new Cake(), new Cake(), new Cake(), new Cake())));
        orderList.add(new Order("陈小姐", "15626471095", Mode.Self, "生日快乐",
                "北栅", "200", PayState.Alipay, Progress.ToDistribute,
                Arrays.asList(new Cake(), new Cake("盒子蛋糕"), new Cake(), new Cake(), new Cake(), new Cake())));
        orderList.add(new Order("陈小姐", "15626471095", Mode.Self, "生日快乐",
                "北栅", "200", PayState.Alipay, Progress.ToDistribute,
                Arrays.asList(new Cake(), new Cake("盒子蛋糕"), new Cake(), new Cake(), new Cake(), new Cake())));
        orderList.add(new Order("陈小姐", "15626471095", Mode.Self, "生日快乐",
                "北栅", "200", PayState.Alipay, Progress.ToDistribute,
                Arrays.asList(new Cake(), new Cake("盒子蛋糕"), new Cake(), new Cake(), new Cake(), new Cake())));
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

    private void initSortHeader() {
        OrderAdapter adapter = (OrderAdapter) listView.getAdapter();
        findViewById(R.id.header_mode).setOnClickListener(v -> adapter.sort("mode"));
        findViewById(R.id.header_targetTime).setOnClickListener(v -> adapter.sort("targetTime"));
        findViewById(R.id.header_destination).setOnClickListener(v -> adapter.sort("destination"));
        findViewById(R.id.header_price).setOnClickListener(v -> adapter.sort("price"));
        findViewById(R.id.header_payState).setOnClickListener(v -> adapter.sort("payState"));
        findViewById(R.id.header_progress).setOnClickListener(v -> adapter.sort("progress"));
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
    }

    public ListView getListView() {
        return listView;
    }
}


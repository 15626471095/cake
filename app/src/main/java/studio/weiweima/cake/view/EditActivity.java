package studio.weiweima.cake.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import studio.weiweima.cake.MainActivity;
import studio.weiweima.cake.R;
import studio.weiweima.cake.bean.Order;

public class EditActivity extends AppCompatActivity {

    private Button back;
    private Button confirm;

    private Order order = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        back = findViewById(R.id.back);
        confirm = findViewById(R.id.confirm);
        init();
    }

    private void init() {
        back.setOnClickListener(v -> finish());
        confirm.setOnClickListener(v -> {
            order.setName("hello");
            setResult(MainActivity.REQUEST_CODE);
            Intent intent = new Intent();
            intent.putExtra("order", order);
            finishActivity(MainActivity.REQUEST_CODE);
        });
    }

}

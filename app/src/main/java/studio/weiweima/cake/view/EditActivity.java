package studio.weiweima.cake.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import studio.weiweima.cake.MainActivity;
import studio.weiweima.cake.R;
import studio.weiweima.cake.bean.Mode;
import studio.weiweima.cake.bean.Order;
import studio.weiweima.cake.bean.PayState;
import studio.weiweima.cake.bean.Progress;
import studio.weiweima.cake.util.StringUtils;

public class EditActivity extends AppCompatActivity {


    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        initButton();
        initMode();
        initPayState();
        initProgress();
        initTimePicker();
        set();
    }

    private void initButton() {
        findViewById(R.id.back).setOnClickListener(v -> finish());
        findViewById(R.id.confirm).setOnClickListener(v -> {
            if (update()) {
                Intent intent = new Intent();
                intent.putExtra("order", order);
                setResult(MainActivity.REQUEST_CODE, intent);
                finish();
            }
        });
    }

    private void initMode() {
        Spinner modeSpinner = findViewById(R.id.mode);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.simple_item, Mode.allValues());
        modeSpinner.setAdapter(adapter);
    }

    private void initPayState() {
        Spinner payStateSpinner = findViewById(R.id.payState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.simple_item, PayState.allValues());
        payStateSpinner.setAdapter(adapter);
    }

    private void initProgress() {
        Spinner progressSpinner = findViewById(R.id.progress);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.simple_item, Progress.allValues());
        progressSpinner.setAdapter(adapter);
    }

    private Calendar calendar = null;

    private void initTimePicker() {
        TextView dateView = findViewById(R.id.targetDate);
        TextView timeView = findViewById(R.id.targetTime);
        dateView.setOnClickListener(v -> {
            if (calendar == null) {
                calendar = Calendar.getInstance();
                calendar.setTime(new Date(System.currentTimeMillis()));
            }
            new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
                dateView.setText(String.format("%s月%s日", monthOfYear + 1, dayOfMonth));
                this.calendar.set(year, monthOfYear, dayOfMonth);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        timeView.setOnClickListener(v -> {
            if (calendar == null) {
                calendar = Calendar.getInstance();
                calendar.setTime(new Date(System.currentTimeMillis()));
            }
            new TimePickerDialog(this, (view, hour, minute) -> {
                timeView.setText(String.format("%s:%s", hour, minute));
                this.calendar.set(Calendar.HOUR_OF_DAY, hour);
                this.calendar.set(Calendar.MINUTE, minute);
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
        });
    }

    private void set() {
        order = (Order) getIntent().getSerializableExtra("order");
        if (order == null) {
            order = new Order();
        } else {
            ((TextInputEditText) findViewById(R.id.name)).setText(order.getName());
            ((TextInputEditText) findViewById(R.id.phone)).setText(order.getPhone());
            ((TextInputEditText) findViewById(R.id.destination)).setText(order.getDestination());
            ((TextInputEditText) findViewById(R.id.price)).setText(order.getPrice());
            ((TextInputEditText) findViewById(R.id.additional)).setText(order.getAdditional());
            ((Spinner) findViewById(R.id.mode)).setSelection(order.getMode().ordinal());
            ((Spinner) findViewById(R.id.payState)).setSelection(order.getPayState().ordinal());
            ((Spinner) findViewById(R.id.progress)).setSelection(order.getProgress().ordinal());
        }
        calendar = Calendar.getInstance();
        calendar.setTime(order.getTargetTime());
        setDateTime();
    }

    private void setDateTime() {
        ((TextView) findViewById(R.id.targetDate))
                .setText(String.format("%s月%s日", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        ((TextView) findViewById(R.id.targetTime))
                .setText(String.format("%s:%s", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
    }

    private boolean update() {
        String name = ((TextInputEditText) findViewById(R.id.name)).getText().toString();
        String phone = ((TextInputEditText) findViewById(R.id.phone)).getText().toString();
        String destination = ((TextInputEditText) findViewById(R.id.destination)).getText().toString();
        String price = ((TextInputEditText) findViewById(R.id.price)).getText().toString();
        String additional = ((TextInputEditText) findViewById(R.id.additional)).getText().toString();
        String mode = ((Spinner) findViewById(R.id.mode)).getSelectedItem().toString();
        String payState = ((Spinner) findViewById(R.id.payState)).getSelectedItem().toString();
        String progress = ((Spinner) findViewById(R.id.progress)).getSelectedItem().toString();

        if (StringUtils.isEmpty(name)) {
            Toast.makeText(this, "姓名未填", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtils.isEmpty(phone)) {
            Toast.makeText(this, "电话未填", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtils.isEmpty(destination)) {
            Toast.makeText(this, "目的地未填", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtils.isEmpty(price)) {
            Toast.makeText(this, "价格未填", Toast.LENGTH_SHORT).show();
            return false;
        }

        order.setName(name);
        order.setPhone(phone);
        order.setDestination(destination);
        order.setPrice(price);
        order.setAdditional(additional);
        order.setMode(Mode.parse(mode));
        order.setPayState(PayState.parse(payState));
        order.setProgress(Progress.parse(progress));
        order.setTargetTime(calendar.getTime());
        return true;
    }

}

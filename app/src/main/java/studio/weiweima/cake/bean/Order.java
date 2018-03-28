package studio.weiweima.cake.bean;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Order implements Serializable {

    private static final DateFormat format = new SimpleDateFormat("hh:mm", Locale.CHINA);
    private static Random random = new Random(System.currentTimeMillis());

    private int id;
    private String name;
    private String phone;
    private Date createTime;
    private Mode mode = Mode.Self;
    private Date targetTime;
    private String additional;
    private String destination;
    private Void picture = null;

    private String price;
    private PayState payState = PayState.Unpay;
    private Progress progress = Progress.ToStart;

    public Order() {
        id = random.nextInt();
        createTime = new Date(System.currentTimeMillis());
        targetTime = new Date(System.currentTimeMillis());
    }

    public Order(String name, String phone, Mode mode, String additional, String destination, String price, PayState payState, Progress progress) {
        this();
        setUp(name, phone, mode, additional, destination, price, payState, progress);
    }

    public Order valueOf(Order order) {
        setUp(order.name, order.phone, order.mode, order.additional, order.destination, order.price, order.payState, order.progress);
        this.targetTime = order.targetTime;
        return this;
    }

    public void setUp(String name, String phone, Mode mode, String additional, String destination, String price, PayState payState, Progress progress) {
        this.name = name;
        this.phone = phone;
        this.mode = mode;
        this.additional = additional;
        this.destination = destination;
        this.price = price;
        this.payState = payState;
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Mode getMode() {
        return mode;
    }

    public Date getTargetTime() {
        return targetTime;
    }

    public String getSimpleTargetTime() {
        return format.format(targetTime);
    }

    public String getDestination() {
        return destination;
    }

    public String getPrice() {
        return price;
    }

    public PayState getPayState() {
        return payState;
    }

    public Progress getProgress() {
        return progress;
    }

    public String getAdditional() {
        return additional;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setPayState(PayState payState) {
        this.payState = payState;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public void setTargetTime(Date targetTime) {
        this.targetTime = targetTime;
    }
}

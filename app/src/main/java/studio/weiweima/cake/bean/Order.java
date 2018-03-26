package studio.weiweima.cake.bean;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Order implements Serializable {

    private static final DateFormat format = new SimpleDateFormat("hh:mm");
    private static Random random = new Random(System.currentTimeMillis());

    private int id;
    private int position;
    private String name = "nihao";
    private String phone = "";
    private Date createTime;
    private String mode = "自提";
    private Date targetTime;
    private String additional = "";
    private String destination = "";
    private Void picture = null;

    private String price = "0";
    private String priceState = "未付款";

    private String progress = "待装饰";

    public Order() {
        id = random.nextInt();
        createTime = new Date(System.currentTimeMillis());
        targetTime = new Date(System.currentTimeMillis());
    }

    public Order(String name, String phone, String mode, String additional, String destination, String price, String priceState, String progress) {
        this();
        setUp(name, phone, mode, additional, destination, price, priceState, progress);
    }

    public Order valueOf(Order order) {
        setUp(order.name, order.phone, order.mode, order.additional, order.destination, order.price, order.priceState, order.progress);
        this.targetTime = order.targetTime;
        return this;
    }

    public void setUp(String name, String phone, String mode, String additional, String destination, String price, String priceState, String progress) {
        this.name = name;
        this.phone = phone;
        this.mode = mode;
        this.additional = additional;
        this.destination = destination;
        this.price = price;
        this.priceState = priceState;
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getMode() {
        return mode;
    }

    public String getTargetTime() {
        return format.format(targetTime);
    }

    public String getDestination() {
        return destination;
    }

    public String getPrice() {
        return price;
    }

    public String getPriceState() {
        return priceState;
    }

    public String getProgress() {
        return progress;
    }

    public String getAdditional() {
        return additional;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
}

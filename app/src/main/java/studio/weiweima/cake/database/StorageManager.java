package studio.weiweima.cake.database;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import studio.weiweima.cake.bean.Order;
import studio.weiweima.cake.external.Json;
import studio.weiweima.cake.external.JsonArray;
import studio.weiweima.cake.external.JsonObject;
import studio.weiweima.cake.util.FileUtils;
import studio.weiweima.cake.util.StringUtils;

public final class StorageManager {


    private StorageManager() {
    }

    private static StorageManager instance = new StorageManager();

    public static StorageManager getInstance() {
        return instance;
    }

    private Map<String, String> cache = new HashMap<>();
    private Map<String, AtomicBoolean> dirtyMap = new HashMap<>();

    public String get(Context context, String key) {
        return cache.compute(key, (k, v) -> {
            if (v == null) {
                v = FileUtils.readFrom(context, key);
                Log.e("ReadFile", key + ": " + v);
            }
            return v;
        });
    }

    public void update(String key, String value) {
        dirtyMap.compute(key, (k, v) -> {
            if (v == null) {
                v = new AtomicBoolean(false);
            }
            v.set(true);
            return v;
        });
        cache.put(key, value);
    }

    public void onUpdate(Context context) {
        Log.e("onUpdate", "running");
        dirtyMap.forEach((k, v) -> {
            if (v.compareAndSet(true, false)) {
                String content = cache.get(k);
                FileUtils.writeTo(context, k, content);
                Log.e("WriteFile", k + ": " + content);
            }
        });
    }

    public List<Object> getAsList(Context context, String key, List<String> defaultList) {
        String content = StringUtils.wrap(get(context, key), "[]");
        LinkedHashSet<Object> arrays = new JsonArray(content).stream().collect(Collectors.toCollection(LinkedHashSet::new));
        int dbSize = arrays.size();
        arrays.addAll(defaultList);
        if (dbSize != arrays.size()) {
            update(key, Json.encode(arrays));
        }
        return new ArrayList<>(arrays);
    }

    public void addAsString(Context context, String key, String value) {
        String content = StringUtils.wrap(get(context, key), "[]");
        JsonArray arrays = new JsonArray(content);
        arrays.add(value);
        update(key, arrays.toString());
    }

    /**
     * Cake Weight
     */
    public static final String WEIGHT = "weight.db";
    private List<String> defaultWeights = Arrays.asList("四寸", "1磅", "1.5磅", "2磅", "3磅", "4磅", "5磅", "6磅", "7磅", "其他");

    public List<String> getWeights(Context context) {
        return StringUtils.toStringList(getAsList(context, WEIGHT, defaultWeights));
    }

    public void addWeight(Context context, String weight) {
        addAsString(context, WEIGHT, weight);
    }

    /**
     * Cake taste
     */
    public static final String TASTE = "taste.db";
    private List<String> defaultTastes = Arrays.asList("芒果", "杂果", "草莓", "栗子", "奥利奥", "榴莲", "芝士夹心水果", "抹茶夹心蜜豆", "巧克力夹心水果", "巧克力夹心奥利奥", "海盐红丝绒", "慕斯", "抹茶");

    public List<String> getTastes(Context context) {
        return StringUtils.toStringList(getAsList(context, TASTE, defaultTastes));
    }

    public void addTaste(Context context, String taste) {
        addAsString(context, TASTE, taste);
    }


    /**
     * Cake require
     */
    public static final String REQUIRE = "require.db";
    private List<String> defaultRequires = Arrays.asList("无", "少奶油", "多奶油", "少甜", "多甜");

    public List<String> getRequires(Context context) {
        return StringUtils.toStringList(getAsList(context, REQUIRE, defaultRequires));
    }

    public void addRequire(Context context, String require) {
        addAsString(context, REQUIRE, require);
    }

    /**
     * Cake type
     */
    public static final String TYPE = "type.db";
    private List<String> defaultTypes = Arrays.asList("生日蛋糕", "盒子蛋糕", "饮料", "其他");

    public List<String> getTypes(Context context) {
        return StringUtils.toStringList(getAsList(context, TYPE, defaultTypes));
    }

    public void addType(Context context, String type) {
        addAsString(context, TYPE, type);
    }

    /**
     * Style type
     */
    public static final String STYLE = "style.db";
    public List<String> defaultStyles = Arrays.asList("裸", "红包", "皇冠", "其他");

    public List<String> getStyles(Context context) {
        return StringUtils.toStringList(getAsList(context, STYLE, defaultStyles));
    }

    public void addStyle(Context context, String style) {
        addAsString(context, STYLE, style);
    }

    /**
     * Order
     */
    public static final String ORDER_FORMAT = "order_%d_%d_%d.db";

    public String getOrderKey(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.format(Locale.CHINESE, ORDER_FORMAT, year, month, day);
    }

    public List<Order> getOrders(Context context, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getOrders(context, calendar);
    }

    public List<Order> getOrders(Context context, Calendar calendar) {
        String orderKey = getOrderKey(calendar);
        List<Object> orders = getAsList(context, orderKey, new ArrayList<>());
        return orders.stream().map(o -> Json.decodeValue(o.toString(), Order.class)).collect(Collectors.toList());
    }

    public void updateOrders(Date date, List<Order> orderList) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        updateOrders(calendar, orderList);
    }

    public void updateOrders(Calendar calendar, List<Order> orderList) {
        String orderKey = getOrderKey(calendar);
        update(orderKey, Json.encode(orderList));
    }

    public boolean checkOrderIdExist(int id) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);
        String orderKey = getOrderKey(calendar);
        if (new JsonArray(cache.get(orderKey)).stream().anyMatch(order -> ((JsonObject) order).getInteger("id") == id)) {
            return true;
        }
        return false;
    }
}

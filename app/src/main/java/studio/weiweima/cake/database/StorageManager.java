package studio.weiweima.cake.database;


import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import studio.weiweima.cake.external.JsonArray;
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
        dirtyMap.forEach((k, v) -> {
            if (v.compareAndSet(true, false)) {
                FileUtils.writeTo(context, k, cache.get(k));
            }
        });
    }

    public List<String> getAsStringList(Context context, String key, List<String> defaultList) {
        String content = StringUtils.wrap(get(context, key), "[]");
        JsonArray arrays = new JsonArray(content);
        List<String> list = new ArrayList<>(defaultList);
        arrays.forEach(w -> {
            if (!list.contains(w)) {
                addWeight(context, (String) w);
                list.add((String) w);
            }
        });
        return list;
    }

    public void addAsString(Context context, String key, String value) {
        String content = StringUtils.wrap(get(context, key), "[]");
        JsonArray arrays = new JsonArray(content);
        arrays.add(value);
        update(WEIGHT, arrays.toString());
    }

    /**
     * Cake Weight
     */
    public static final String WEIGHT = "weight.db";
    private List<String> defaultWeights = Arrays.asList("四寸", "1磅", "1.5磅", "2磅", "3磅", "4磅", "5磅", "6磅", "7磅");

    public List<String> getWeights(Context context) {
        return getAsStringList(context, WEIGHT, defaultWeights);
    }

    public void addWeight(Context context, String weight) {
        addAsString(context, WEIGHT, weight);
    }

    /**
     * Cake taste
     */
    public static final String TASTE = "taste.db";
    private List<String> defaultTastes = Arrays.asList("芒果", "草莓", "杂果", "榴莲", "栗子", "咸奶油", "慕斯", "海盐红丝绒", "巧克力", "抹茶");

    public List<String> getTastes(Context context) {
        return getAsStringList(context, TASTE, defaultTastes);
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
        return getAsStringList(context, REQUIRE, defaultRequires);
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
        return getAsStringList(context, TYPE, defaultTypes);
    }

    public void addType(Context context, String type) {
        addAsString(context, TYPE, type);
    }
}

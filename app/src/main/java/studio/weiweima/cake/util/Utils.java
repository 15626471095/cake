package studio.weiweima.cake.util;

import android.content.Intent;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import studio.weiweima.cake.bean.Cake;
import studio.weiweima.cake.bean.Order;
import studio.weiweima.cake.external.Json;
import studio.weiweima.cake.external.JsonArray;

public final class Utils {

    private Utils() {
    }

    public static Pair<List<String>, List<String>> splitList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return new Pair<>(new ArrayList<>(), new ArrayList<>());
        }
        int len = list.size();
        List<String> left = new ArrayList<>(list.subList(0, (int) Math.ceil(len / 2.0)));
        List<String> right = new ArrayList<>(list.subList((int) Math.ceil(len / 2.0), len));
        return new Pair<>(left, right);
    }

    public static Intent encodeCakes(List<Cake> cakes) {
        Intent intent = new Intent();
        intent.putExtra("cakes", Json.encode(cakes));
        return intent;
    }

    public static List<Cake> decodeCakes(Intent intent) {
        String string = intent.getStringExtra("cakes");
        if (StringUtils.isEmpty(string)) {
            return new ArrayList<>();
        }
        return new JsonArray(string).stream().map(s -> Json.decodeValue(s.toString(), Cake.class)).collect(Collectors.toList());
    }

    public static List<String> getCakesAbstract(List<Cake> cakes) {
        return cakes.stream().map(Cake::getAbstract).collect(Collectors.toList());
    }

    public static Order decodeOrder(Intent intent) {
        if (intent == null) {
            return null;
        }
        return Json.decodeValue(intent.getStringExtra("order"), Order.class);
    }

    public static Intent encodeOrder(Order order) {
        Intent intent = new Intent();
        intent.putExtra("order", Json.encode(order));
        return intent;
    }
}

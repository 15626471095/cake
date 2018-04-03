package studio.weiweima.cake.util;

import java.util.List;
import java.util.stream.Collectors;

import studio.weiweima.cake.external.Json;

public final class StringUtils {

    private StringUtils() {
    }

    public static boolean isEmpty(String s) {
        return s == null || "".equals(s);
    }

    public static String wrap(String target, String replace) {
        if (isEmpty(target)) {
            return replace;
        }
        return target;
    }

    public static List<String> toStringList(List<Object> objects) {
        return objects.stream().map(Object::toString).collect(Collectors.toList());
    }

    public static String toString(List<? extends Object> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        return list.stream().map(l -> Json.encode(l) + "\n").collect(Collectors.toList()).toString();
    }
}

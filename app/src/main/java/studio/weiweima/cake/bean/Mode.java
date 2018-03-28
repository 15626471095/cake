package studio.weiweima.cake.bean;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Mode {
    Self("自提"),
    Distribution("配送"),
    Other("其他");

    String value;

    Mode(String value) {
        this.value = value;
    }

    public static Mode parse(String mode) {
        for (Mode m : Mode.values()) {
            if (m.value.equals(mode)) {
                return m;
            }
        }
        return Other;
    }

    public String value() {
        return this.value;
    }

    public static String[] allValues() {
        return Arrays.stream(Mode.values()).map(Mode::value).collect(Collectors.toList()).toArray(new String[]{});
    }
}

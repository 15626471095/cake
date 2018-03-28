package studio.weiweima.cake.bean;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Progress {
    ToStart("待开始"),
    ToDecorate("待装饰"),
    ToDistribute("待配送"),
    ToTake("待自提"),
    Other("其他");

    String value;

    Progress(String value) {
        this.value = value;
    }

    public static Progress parse(String mode) {
        for (Progress p : Progress.values()) {
            if (p.value.equals(mode)) {
                return p;
            }
        }
        return Other;
    }

    public String value() {
        return this.value;
    }

    public static String[] allValues() {
        return Arrays.stream(Progress.values()).map(Progress::value).collect(Collectors.toList()).toArray(new String[]{});
    }
}

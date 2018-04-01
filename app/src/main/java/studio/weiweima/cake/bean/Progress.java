package studio.weiweima.cake.bean;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import studio.weiweima.cake.R;

public enum Progress {
    ToStart("待开始", R.color.green),
    Done("已完成", R.color.yellow),
    ToDecorate("待装饰", R.color.blue),
    Decorated("已装饰", R.color.gray),
    ToDistribute("待配送", R.color.gray),
    ToTake("待自提", R.color.gray),
    Distributed("已配送", R.color.red),
    Taked("已自提", R.color.red),
    Other("其他");

    String value;
    int hintColorResId;

    Progress(String value) {
        this.value = value;
        this.hintColorResId = R.color.transplant;
    }

    Progress(String value, int hintColorResId) {
        this.value = value;
        this.hintColorResId = hintColorResId;
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

    public int color() {
        return hintColorResId;
    }

    public static Progress random(Random random) {
        return values()[Math.abs(random.nextInt() % values().length)];
    }

    public static String[] allValues() {
        return Arrays.stream(Progress.values()).map(Progress::value).collect(Collectors.toList()).toArray(new String[]{});
    }
}

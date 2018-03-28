package studio.weiweima.cake.bean;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum PayState {
    Unpay("未支付"),
    Wechet("微信"),
    Alipay("支付宝"),
    Cash("现金"),
    Other("其他");

    String value;

    PayState(String value) {
        this.value = value;
    }

    public static PayState parse(String mode) {
        for (PayState p : PayState.values()) {
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
        return Arrays.stream(PayState.values()).map(PayState::value).collect(Collectors.toList()).toArray(new String[]{});
    }
}

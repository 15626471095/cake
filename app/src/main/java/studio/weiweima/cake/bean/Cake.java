package studio.weiweima.cake.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cake {
    private String type = "生日蛋糕";
    private String weight = "1磅";
    private String taste = "杂果";
    private List<String> requires = new ArrayList<>();
    private String pictureUri = "";
    private String additional = "";
    private String style = "裸";

    static Random random = new Random(System.currentTimeMillis());

    public Cake() {
        weight = (random.nextInt(6) + 1) + "磅";
    }

    public Cake(String type) {
        this.type = type;
        requires.addAll(Arrays.asList("少奶油", "多奶油", "少甜"));
    }

    public void setUp(String type, String weight, String taste, String style, List<String> requires, String pictureUri, String additional) {
        this.type = type;
        this.weight = weight;
        this.taste = taste;
        this.style = style;
        this.requires = requires;
        this.pictureUri = pictureUri;
        this.additional = additional;
    }

    public String getType() {
        return type;
    }

    public String getTaste() {
        return taste;
    }

    public String getStyle() {
        return style;
    }

    public String getWeight() {
        return weight;
    }

    public List<String> getRequires() {
        return requires;
    }

    public String getAdditional() {
        return additional;
    }

    public String getPictureUri() {
        return pictureUri;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setPictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getAbstract() {
        switch (type) {
            case "生日蛋糕":
                return weight + taste + style;
            default:
                return taste;
        }
    }
}

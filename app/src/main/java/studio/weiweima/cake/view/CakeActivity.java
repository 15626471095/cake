package studio.weiweima.cake.view;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import studio.weiweima.cake.R;
import studio.weiweima.cake.bean.Cake;
import studio.weiweima.cake.bean.RequestCode;
import studio.weiweima.cake.database.StorageManager;
import studio.weiweima.cake.util.BitmapCache;
import studio.weiweima.cake.util.StringUtils;
import studio.weiweima.cake.util.Utils;

public class CakeActivity extends AppCompatActivity {

    private List<Cake> cakes;
    private int orderId = -1;

    private ListView orderedCakeListView;
    private Spinner typeSpinner;
    private Spinner weightSpinner;
    private Spinner tasteSpinner;
    private Spinner styleSpinner;
    private ListView requireListViewLeft;
    private ListView requireListViewRight;
    private TextInputEditText additionalEditText;
    private ImageView picture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cake);
        parseIntent();
        initButton();
        initPicture();
        initTypes();
        initWeights();
        initTastes();
        initRequires();
        initStyles();
        initAdditional();
        initOrderedCakeList();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        Pair<Integer, List<Cake>> args = Utils.decodeCakesWithOrderId(intent);
        cakes = args.second;
        orderId = args.first;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void onAddCake() {
        SimpleAdapter adapter = (SimpleAdapter) orderedCakeListView.getAdapter();
        cakes.add(new Cake());
        adapter.update(Utils.getCakesAbstract(cakes));
        adapter.setSelectedPosition(cakes.size() - 1);
        onSelectedCake(cakes.get(adapter.getSelectedPosition()));
        if (cakes.size() > 5) {
            orderedCakeListView.scrollBy(0, 50);
        }
    }

    private void onRemoveCake() {
        SimpleAdapter adapter = (SimpleAdapter) orderedCakeListView.getAdapter();
        cakes.remove(adapter.getSelectedPosition());
        adapter.update(Utils.getCakesAbstract(cakes));
        onSelectedCake(cakes.get(adapter.getSelectedPosition()));
    }

    private void onConfirm() {
        SimpleAdapter adapter = (SimpleAdapter) orderedCakeListView.getAdapter();
        collectData(adapter.getSelectedPosition());
        Intent intent = Utils.encodeCakes(cakes, orderId);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initButton() {
        findViewById(R.id.back).setOnClickListener(v -> this.finish());
        findViewById(R.id.delete).setOnClickListener(v -> onRemoveCake());
        findViewById(R.id.add).setOnClickListener(v -> onAddCake());
        findViewById(R.id.confirm).setOnClickListener(v -> onConfirm());
    }

    private void collectData(int position) {
        String type = (String) typeSpinner.getSelectedItem();
        String weight = (String) weightSpinner.getSelectedItem();
        String taste = (String) tasteSpinner.getSelectedItem();
        String style = (String) styleSpinner.getSelectedItem();
        List<String> requires = ((SimpleAdapter) requireListViewLeft.getAdapter()).getCheckedData();
        List<String> requiresRight = ((SimpleAdapter) requireListViewRight.getAdapter()).getCheckedData();

        requires.addAll(requiresRight);
        String additional = additionalEditText.getText().toString();
        String pictureUri = consumePictureUri();
        SimpleAdapter cakeListAdapter = (SimpleAdapter) orderedCakeListView.getAdapter();
        cakes.get(position).setUp(type, weight, taste, style, requires, pictureUri, additional);
        cakeListAdapter.update(Utils.getCakesAbstract(cakes));
    }

    private void initOrderedCakeList() {
        orderedCakeListView = findViewById(R.id.ordered_cakes);
        SimpleAdapter adapter = new SimpleAdapter(this, R.layout.simple_item, Utils.getCakesAbstract(cakes));
        adapter.setSelectedColorRes(R.color.colorAccent);
        orderedCakeListView.setAdapter(adapter);
        orderedCakeListView.setOnItemClickListener((adapterView, view, i, l) -> {
            int previousSelectedPosition = adapter.getSelectedPosition();
            if (i == previousSelectedPosition) {
                return;
            }
            if (previousSelectedPosition != -1) {
                collectData(previousSelectedPosition);
            }
            onSelectedCake(cakes.get(i));
            if (view != null) {
                adapter.clearItemBackground(adapterView);
                view.setBackgroundResource(R.color.colorAccent);
            }
            adapter.setSelectedPosition(i);
        });
        if (!cakes.isEmpty()) {
            orderedCakeListView.performItemClick(null, 0, 0);
        }
    }

    private void initTypes() {
        typeSpinner = findViewById(R.id.type);
        List<String> types = StorageManager.getInstance().getTypes(this);
        setUpSpinner(typeSpinner, R.layout.simple_item, types);
    }

    private void initWeights() {
        weightSpinner = findViewById(R.id.weight);
        List<String> weights = StorageManager.getInstance().getWeights(this);
        setUpSpinner(weightSpinner, R.layout.simple_item, weights);
    }

    private void initTastes() {
        tasteSpinner = findViewById(R.id.taste);
        List<String> tastes = StorageManager.getInstance().getTastes(this);
        setUpSpinner(tasteSpinner, R.layout.simple_item, tastes);
    }

    private void initRequires() {
        requireListViewLeft = findViewById(R.id.requiresListLeft);
        requireListViewRight = findViewById(R.id.requiresListRight);
        Pair<List<String>, List<String>> requires = Utils.splitList(StorageManager.getInstance().getRequires(this));
        SimpleAdapter adapterLeft = new SimpleAdapter(this, R.layout.checkbox_item, requires.first);
        SimpleAdapter adapterRight = new SimpleAdapter(this, R.layout.checkbox_item, requires.second);
        requireListViewLeft.setAdapter(adapterLeft);
        requireListViewRight.setAdapter(adapterRight);
        requireListViewLeft.setOnItemClickListener((adapterView, view, i, l) -> {
            CheckBox checkBox = (CheckBox) view;
            boolean check = !checkBox.isChecked();
            checkBox.setChecked(check);
            adapterLeft.setChecked(i, check);
        });
        requireListViewRight.setOnItemClickListener((adapterView, view, i, l) -> {
            CheckBox checkBox = (CheckBox) view;
            boolean check = !checkBox.isChecked();
            checkBox.setChecked(check);
            adapterRight.setChecked(i, check);
        });
    }

    private void initStyles() {
        styleSpinner = findViewById(R.id.style);
        List<String> styles = StorageManager.getInstance().getStyles(this);
        setUpSpinner(styleSpinner, R.layout.simple_item, styles);
    }

    public void showRequiresDialog(List<String> requires, List<String> selectedRequires) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        boolean[] isChecks = new boolean[requires.size()];
        String[] requiresArrays = requires.toArray(new String[requires.size()]);
        selectedRequires.forEach(s -> {
            int index = requires.indexOf(s);
            if (index != -1) {
                isChecks[index] = true;
            }
        });
        builder.setMultiChoiceItems(requiresArrays, isChecks, (dialog, which, isChecked) -> {
            isChecks[which] = isChecked;
            Log.e("setMultiChoiceItems", "" + which + " " + isChecked);
        }).setNegativeButton("取消", (dialog, id) -> dialog.dismiss()).setPositiveButton("确定", (dialog, id) -> {
            List<String> result = new ArrayList<>();
            for (int i = 0; i < isChecks.length; i++) {
                if (isChecks[i]) {
                    result.add(requiresArrays[i]);
                }
            }
            dialog.dismiss();
        }).show();
    }

    private void setUpSpinner(Spinner spinner, int itemLayoutId, List<String> data) {
        SimpleAdapter adapter = new SimpleAdapter(this, itemLayoutId, data);
        spinner.setAdapter(adapter);
    }

    private void onSelectedCake(Cake selectedCake) {
        typeSpinner.setSelection(((SimpleAdapter) typeSpinner.getAdapter()).getPosition(selectedCake.getType()));
        weightSpinner.setSelection(((SimpleAdapter) weightSpinner.getAdapter()).getPosition(selectedCake.getWeight()));
        tasteSpinner.setSelection(((SimpleAdapter) tasteSpinner.getAdapter()).getPosition(selectedCake.getTaste()));
        styleSpinner.setSelection(((SimpleAdapter) styleSpinner.getAdapter()).getPosition(selectedCake.getStyle()));
        additionalEditText.setText(selectedCake.getAdditional());
        ((SimpleAdapter) requireListViewLeft.getAdapter()).updateChecks(selectedCake.getRequires());
        ((SimpleAdapter) requireListViewRight.getAdapter()).updateChecks(selectedCake.getRequires());
        setPicture(selectedCake.getPictureUri());
    }

    private void initAdditional() {
        additionalEditText = findViewById(R.id.additional);
    }

    private void initPicture() {
        picture = findViewById(R.id.picture);
        picture.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, RequestCode.SELECT_PICTURE);
        });
    }

    private void setPicture(String pictureUri) {
        if (StringUtils.isEmpty(pictureUri)) {
            picture.setImageResource(R.drawable.ic_launcher_background);
            return;
        }
        Log.e("path:", pictureUri);
        providePictureUri(pictureUri);
        Bitmap bitmap = BitmapCache.getInstance().getBitmap(this, pictureUri, 300, 300);
        if (bitmap != null) {
            picture.setImageBitmap(bitmap);
        }
    }

    private String pictureUri;

    public String consumePictureUri() {
        String p = pictureUri;
        pictureUri = "";
        return p;
    }

    public void providePictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.SELECT_PICTURE && resultCode == RESULT_OK) {
            setPicture(data.getDataString());
        }
    }
}

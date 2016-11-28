package com.patient.simulate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.patient.android.activity.BaseActivity;
import com.patient.simulate.event.util.DataCleanManager;

/**
 * @author zs
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvCacheSize;

    @Override
    protected void init(Bundle bundle) {
        setContentView(R.layout.act_main);

        tvCacheSize = getViewById(R.id.tv_cache_size);
        try {
            tvCacheSize.setText(DataCleanManager.getTotalCacheSize(getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open: {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.KEY_URL, "https://www.amazon.com/");
//                intent.putExtra(WebViewActivity.KEY_URL, "https://www.amazon.com/gp/aw/d/B0169EXN2M/ref=mp_s_a_1_4?ie=UTF8&qid=1480315073&sr=8-4&pi=AC_SX236_SY340_FMwebp_QL65&keywords=hose+holder");
//                intent.putExtra(WebViewActivity.KEY_URL, "http://www.baidu.com/");
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataCleanManager.clearAllCache(getApplicationContext());
    }
}

package com.sacot41.espconnector_example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.sacot41.espconnector.ESPConnectorConfig;
import com.sacot41.espconnector_example.base.BaseActivity;
import com.sacot41.espconnector_example.base.ESPWifiEdittextview;
import com.unstoppable.submitbuttonview.SubmitButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Samuel on 2017-10-31.
 */

public class ConfigurationActivity extends BaseActivity {

    @BindView(R.id.espwifiedittextview_config_activity_espssid)
    ESPWifiEdittextview espSSIDEdittextview;

    @BindView(R.id.espwifiedittextview_config_activity_esppassphrase)
    ESPWifiEdittextview espPassPhraseEdittextview;

    @BindView(R.id.espwifiedittextview_config_activity_esppipaddress)
    ESPWifiEdittextview espIPAddressEdittextview;

    @BindView(R.id.espwifiedittextview_config_activity_espport)
    ESPWifiEdittextview espPortEdittextview;

    @BindView(R.id.espwifiedittextview_config_activity_timeout)
    ESPWifiEdittextview timeoutEdittextview;

    @BindView(R.id.submitbutton_config_activity_save)
    SubmitButton saveSubmitButton;

    private ESPConnectorConfig connectorConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_configuration);

        Bundle bundleData = getIntent().getExtras();
        ESPConnectorConfig config = bundleData.getParcelable(ESPConnectorConfig.class.getName());
        setConnectorConfig(config);
    }

    public void setConnectorConfig(ESPConnectorConfig connectorConfig) {
        this.connectorConfig = connectorConfig;

        espSSIDEdittextview.setText(null);
        espPassPhraseEdittextview.setText(null);
        espIPAddressEdittextview.setText(null);
        espPortEdittextview.setText(null);
        timeoutEdittextview.setText(null);

        if (this.connectorConfig == null) return;

        espSSIDEdittextview.setText(this.connectorConfig.getEspSSID());
        espPassPhraseEdittextview.setText(this.connectorConfig.getEspPassPhrase());
        espIPAddressEdittextview.setText(this.connectorConfig.getIpAddress());
        espPortEdittextview.setText(String.valueOf(this.connectorConfig.getPort()));
        timeoutEdittextview.setText(String.valueOf(this.connectorConfig.getConnectionTimeout()));
    }

    @OnClick(R.id.submitbutton_config_activity_save)
    public void onClickSaveButton(View view) {

        boolean valid = true;

        if (espSSIDEdittextview.getText() == null || espSSIDEdittextview.getText().isEmpty()) {
            espSSIDEdittextview.shakeView();
            valid = false;
        }

        if (espPassPhraseEdittextview.getText() == null || espPassPhraseEdittextview.getText().isEmpty()) {
            espPassPhraseEdittextview.shakeView();
            valid = false;
        }

        if (espIPAddressEdittextview.getText() == null || espIPAddressEdittextview.getText().isEmpty()) {
            espIPAddressEdittextview.shakeView();
            valid = false;
        }

        if (espPortEdittextview.getText() == null || espPortEdittextview.getText().isEmpty()) {
            espPortEdittextview.shakeView();
            valid = false;
        }

        if (timeoutEdittextview.getText() == null || timeoutEdittextview.getText().isEmpty()) {
            timeoutEdittextview.shakeView();
            valid = false;
        }

        if (!valid) {
            saveSubmitButton.reset();
            return;
        }

        connectorConfig.setEspSSID(espSSIDEdittextview.getText());
        connectorConfig.setEspPassPhrase(espPassPhraseEdittextview.getText());
        connectorConfig.setIpAddress(espIPAddressEdittextview.getText());
        connectorConfig.setPort(Integer.valueOf(espPortEdittextview.getText()));
        connectorConfig.setPort(Integer.valueOf(timeoutEdittextview.getText()));

        Intent intent = new Intent();
        intent.putExtra(ESPConnectorConfig.class.getName(), connectorConfig);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}

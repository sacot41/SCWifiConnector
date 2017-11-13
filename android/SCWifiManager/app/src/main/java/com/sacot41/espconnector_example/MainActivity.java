package com.sacot41.espconnector_example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.sacot41.espconnector.ESPConnector;
import com.sacot41.espconnector.ESPConnectorConfig;
import com.sacot41.espconnector.ESPConnectorInterface;
import com.sacot41.espconnector_example.base.BaseActivity;
import com.unstoppable.submitbuttonview.SubmitButton;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements SendCredentialFragment.SendCredentialListener {

    private static final int CONFIG_ACTIVITY_RESULT_CODE = 1;

    @BindView(R.id.submitbutton_main_activity_connect)
    SubmitButton connectSubmitButton;

    private ESPConnectorConfig connectorConfig;
    private ESPConnector espConnector;

    private SendCredentialFragment sendCredentialFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        connectorConfig = new ESPConnectorConfig();

        sendCredentialFragment = (SendCredentialFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_send_credential);
        sendCredentialFragment.setSendCredentialListener(this);
        getSupportFragmentManager().beginTransaction()
                .hide(sendCredentialFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONFIG_ACTIVITY_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            Bundle bundleData = data.getExtras();
            connectorConfig = bundleData.getParcelable(ESPConnectorConfig.class.getName());
        }
    }

    @OnClick(R.id.textview_main_activity_settings)
    public void onClickSettingsButton(View view) {
        Intent intent = new Intent(MainActivity.this, ConfigurationActivity.class);
        intent.putExtra(ESPConnectorConfig.class.getName(), connectorConfig);
        startActivityForResult(intent, CONFIG_ACTIVITY_RESULT_CODE);
    }

    @OnClick(R.id.submitbutton_main_activity_connect)
    public void onClickConfigButton(View view) {
        espConnector = new ESPConnector.Builder(this)
                .setConfig(connectorConfig)
                .build();

        espConnector.setOnESPConnectorInterface(new ESPConnectorInterface() {
            @Override
            public void onESPConnect() {
                connectSubmitButton.doResult(true);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
                                .show(sendCredentialFragment)
                                .commitAllowingStateLoss();
                    }
                }, 500);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        connectSubmitButton.reset();
                    }
                }, 1100);
            }

            @Override
            public void onESPConnectFailure() {
                connectSubmitButton.doResult(false);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        connectSubmitButton.reset();
                    }
                }, 2000);
            }

            @Override
            public void onReceiveDataFromESP(String data) {

            }

        });

        try {
            espConnector.start();
            espConnector.connectionToESP();

        } catch (Exception e) {
            e.printStackTrace();
            espConnector.stop();
        }
    }

    @OnClick(R.id.imageview_main_activity_github)
    public void onClickGitHubButton(View view) {

    }

    @Override
    public void onSendCredentialClick(String ssid, String passphrase) {
        boolean success = espConnector.sendWifiConfigurationToESP(ssid, passphrase);
        if (success) sendCredentialFragment.setSendCredentialFailure();
        else sendCredentialFragment.setSendCredentialSuccess();
    }

    @Override
    public void onClickFlahsLed() {
        espConnector.sendDeviceIdentificationRequest("1");
    }

    @Override
    public void onClickDisconnect() {
        espConnector.stop();
        getSupportFragmentManager().beginTransaction()
                .hide(sendCredentialFragment)
                .commitAllowingStateLoss();
    }

}

package com.sacot41.espconnector_example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sacot41.espconnector_example.base.ESPWifiEdittextview;
import com.unstoppable.submitbuttonview.SubmitButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Samuel on 2017-11-02.
 */

public class SendCredentialFragment extends Fragment {

    public interface SendCredentialListener {
        void onSendCredentialClick(String ssid, String passphrase);
        void onClickFlahsLed();
        void onClickDisconnect();
    }

    @BindView(R.id.espwifiedittextview_send_credential_ssid)
    ESPWifiEdittextview ssidEdittextview;

    @BindView(R.id.espwifiedittextview_send_credential_passphrase)
    ESPWifiEdittextview passphraseEdittextview;

    @BindView(R.id.submitbutton_send_credential_send)
    SubmitButton sendCredentialSubmitButton;

    @BindView(R.id.submitbutton_send_credential_make_flash)
    SubmitButton flashLedSubmitButton;

    @BindView(R.id.submitbutton_send_credential_disconnect)
    SubmitButton disconnectSubmitButton;

    private SendCredentialListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_credential, container);
        ButterKnife.bind(this, view);

        return view;
    }

    public void setSendCredentialListener(SendCredentialListener listener) { this.listener = listener; }

    public void setSendCredentialSuccess() {
        sendCredentialSubmitButton.doResult(true);
    }

    public void setSendCredentialFailure() {
        sendCredentialSubmitButton.doResult(false);
    }

    @OnClick(R.id.submitbutton_send_credential_send)
    public void onClickSendCredential(View view) {

        boolean valid = true;

        if (ssidEdittextview.getText() == null || ssidEdittextview.getText().isEmpty()) {
            ssidEdittextview.shakeView();
            valid = false;
        }

        if (passphraseEdittextview.getText() == null || passphraseEdittextview.getText().isEmpty()) {
            passphraseEdittextview.shakeView();
            valid = false;
        }

        if (!valid) {
            sendCredentialSubmitButton.reset();
            return;
        }

        if (this.listener != null) this.listener.onSendCredentialClick(ssidEdittextview.getText(), passphraseEdittextview.getText());
    }

    @OnClick(R.id.submitbutton_send_credential_make_flash)
    public void onClickFlashLed(View view) {
        flashLedSubmitButton.reset();
        if (this.listener != null) this.listener.onClickFlahsLed();
    }

    @OnClick(R.id.submitbutton_send_credential_disconnect)
    public void onClickDisconnect(View view) {
        disconnectSubmitButton.reset();
        if (this.listener != null) this.listener.onClickDisconnect();
    }

}

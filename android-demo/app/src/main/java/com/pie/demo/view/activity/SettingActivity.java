package com.pie.demo.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.acu.pie.model.AsrProduct;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.pie.demo.Constants;
import com.pie.demo.R;
import com.pie.demo.utils.SpUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingActivity extends AppCompatActivity {

    private Toolbar mToolbarSetting;
    private EditText mEtIp;
    private EditText mEtPort;
    private Button mBT;
    private RadioGroup mRadioGroup;
    private Switch mSwitch;
    private boolean mSwitchIsChecked;
    private String flag;

    private Button mBTTTS;
    private EditText mEtPortIpTTS, mEtSpd, mEtPit, mEtVol;

//    private boolean isChanged = false;
    private AsrProduct[] values;
    private int isCheckId = -1;

    private TextView mTVmm, mTvchToken, mTVtoken, mTvtime, mTVpass;
    private EditText mEtAccout, mEtpwd, mEttoken;
    private EditText mDhAddr, mDhAppId, mDhPhoneToken;
    private Button mBtLogin;
    private LinearLayout mLltoken, mLlpwd, mLlaccout, mLltokentime;
    private boolean isShowPwd, isShowToken, isShowNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        flag = getIntent().getStringExtra("flag");
        initView();

        setSupportActionBar(mToolbarSetting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initSp();
        initLisener();
        initRadioGroup();
    }


    private void initView() {
        mToolbarSetting = findViewById(R.id.mToolbarSetting);
        mEtIp = findViewById(R.id.mEtIp);
        mEtPort = findViewById(R.id.mEtPort);
        mBT = findViewById(R.id.mBT);
        mRadioGroup = findViewById(R.id.mRadioGroup);
        mSwitch = findViewById(R.id.mSwitch);


        this.mEtPortIpTTS = ((EditText) findViewById(R.id.mEtPortIpTTS));
        this.mBTTTS = ((Button) findViewById(R.id.mBTTTS));
        mEtSpd = findViewById(R.id.mEtSpd);
        mEtPit = findViewById(R.id.mEtPit);
        mEtVol = findViewById(R.id.mEtVol);

        mDhAddr = findViewById(R.id.dh_addr);
        mDhAppId = findViewById(R.id.dh_app_id);
        mDhPhoneToken = findViewById(R.id.dh_phone_token);

        LinearLayout localLinearLayout1 = (LinearLayout) findViewById(R.id.mLOne);
        LinearLayout localLinearLayout2 = (LinearLayout) findViewById(R.id.mLTwo);
        if ("tts".equals(this.flag)) {
            localLinearLayout1.setVisibility(View.GONE);
            localLinearLayout2.setVisibility(View.VISIBLE);
        } else {
            localLinearLayout1.setVisibility(View.VISIBLE);
            localLinearLayout2.setVisibility(View.GONE);
        }

        mTVmm = findViewById(R.id.mTVmm);
        mTVtoken = findViewById(R.id.mTVtoken);
        mEtAccout = findViewById(R.id.mEtAccout);
        mEtpwd = findViewById(R.id.mEtpwd);
        mEttoken = findViewById(R.id.mEttoken);
        mTvchToken = findViewById(R.id.mTvchToken);
        mTvtime = findViewById(R.id.mTvtime);
        mBtLogin = findViewById(R.id.mBtLogin);
        mLltoken = findViewById(R.id.mLltoken);
        mLlpwd = findViewById(R.id.mLlpwd);
        mTVpass = findViewById(R.id.mTVpass);
        mLlaccout = findViewById(R.id.mLlaccout);
        mLltokentime = findViewById(R.id.mLltokentime);

        mBtLogin.setVisibility(View.GONE);
        mEtAccout.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        mEttoken.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSp() {
        /**
         *
         * 登陆相关
         * */
        String loginstyleone = SpUtils.getInstance().getString(Constants.LOGINSTYLEONE);
        String oneAccout = SpUtils.getInstance().getString(Constants.ACCOUTONE);
        String onePwd = SpUtils.getInstance().getString(Constants.PWDONE);
        String oneToken = SpUtils.getInstance().getString(Constants.TOKENONE);
        String oneTime = SpUtils.getInstance().getString(Constants.TIMEONE);
        String dhApp = SpUtils.getInstance().getString(Constants.DH_APP_ID);
        String dhAddr = SpUtils.getInstance().getString(Constants.DH_ADDR);
        String dhPhoneToken = SpUtils.getInstance().getString(Constants.DH_PHONE_TOKEN);

        if (!TextUtils.isEmpty(dhAddr)) {
            mDhAddr.setText(dhAddr);
        }
        if (!TextUtils.isEmpty(dhApp)) {
            mDhAppId.setText(dhApp);
        }
        if (!TextUtils.isEmpty(dhApp)) {
            mDhPhoneToken.setText(dhPhoneToken);
        }


        /**
         *
         * asr相关
         * */
        switch (flag) {
            case "one":
                String oneAddress = SpUtils.getInstance().getString(Constants.ONEADDRESS);
                String onePort = SpUtils.getInstance().getString(Constants.ONEPORT);
                if (!TextUtils.isEmpty(oneAddress) && !TextUtils.isEmpty(onePort)) {
                    mEtIp.setText(oneAddress);
                    mEtPort.setText(onePort);
                }

                boolean switchone = SpUtils.getInstance().getBool(Constants.SWITCHISCHECKEDONE);
                mSwitchIsChecked = switchone;
                Log.e("tag", switchone + "");
                mSwitch.setChecked(switchone);

                switch (loginstyleone) {
                    case "loginpwd":
                        loginPwd();
                        mEtAccout.setText(oneAccout);
                        mEtpwd.setText(onePwd);
                        mTvtime.setText(oneTime);
                        break;
                    case "logintoken":
                        loginToken();
                        mEtAccout.setText(oneAccout);
                        mEttoken.setText(oneToken);
                        mTvtime.setText(oneTime);
                        break;
                    case "loginno":
                        loginNo();
                        break;
                }

                break;
        }
        /**
         *
         * tts相关
         * */

        String str1 = SpUtils.getInstance().getString(Constants.SERVER_IP_ADDR_PORT_TTS);
        this.mEtPortIpTTS.setText(str1);

        int spd = SpUtils.getInstance().getInt(Constants.SPD);
        int pit = SpUtils.getInstance().getInt(Constants.PIT);
        int vol = SpUtils.getInstance().getInt(Constants.VOl);

        mEtSpd.setText(String.valueOf(spd));
        mEtPit.setText(String.valueOf(pit));
        mEtVol.setText(String.valueOf(vol));

    }

    private void initLisener() {

        /**
         *
         * asr相关
         * */

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSwitchIsChecked = isChecked;
            }
        });

        mBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ip = mEtIp.getText().toString().trim();
                String port = mEtPort.getText().toString().trim();

                SpUtils.getInstance().putString(Constants.DH_ADDR, mDhAddr.getText().toString().trim());
                SpUtils.getInstance().putString(Constants.DH_APP_ID, mDhAppId.getText().toString().trim());
                SpUtils.getInstance().putString(Constants.DH_PHONE_TOKEN, mDhPhoneToken.getText().toString().trim());

                switch (flag) {
                    case "one":
                        if (!TextUtils.isEmpty(ip) && !TextUtils.isEmpty(port)) {
                            SpUtils.getInstance().putString(Constants.ONEADDRESS, ip);
                            SpUtils.getInstance().putString(Constants.ONEPORT, port);
                            SpUtils.getInstance().putBool(Constants.SWITCHISCHECKEDONE, mSwitchIsChecked);

                            if (isShowPwd) {
                                String accout = mEtAccout.getText().toString().trim();
                                if (TextUtils.isEmpty(accout)) {
                                    Toast.makeText(SettingActivity.this, "请填写账号", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String pwd = mEtpwd.getText().toString().trim();
                                if (TextUtils.isEmpty(pwd)) {
                                    Toast.makeText(SettingActivity.this, "请填写密码", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String time = mTvtime.getText().toString().trim();

                                SpUtils.getInstance().putString(Constants.ACCOUTONE, accout);
                                SpUtils.getInstance().putString(Constants.PWDONE, pwd);
                                SpUtils.getInstance().putString(Constants.TIMEONE, time);
                                SpUtils.getInstance().putString(Constants.TOKENONE, null);
                                SpUtils.getInstance().putString(Constants.LOGINSTYLEONE, "loginpwd");
                            }


                            if (isShowToken) {
                                String accout = mEtAccout.getText().toString().trim();
                                if (TextUtils.isEmpty(accout)) {
                                    Toast.makeText(SettingActivity.this, "请填写账号", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String token = mEttoken.getText().toString().trim();
                                if (TextUtils.isEmpty(token)) {
                                    Toast.makeText(SettingActivity.this, "请填写token", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String time = mTvtime.getText().toString().trim();

                                SpUtils.getInstance().putString(Constants.ACCOUTONE, accout);
                                SpUtils.getInstance().putString(Constants.PWDONE, null);
                                SpUtils.getInstance().putString(Constants.TIMEONE, time);
                                SpUtils.getInstance().putString(Constants.TOKENONE, token);
                                SpUtils.getInstance().putString(Constants.LOGINSTYLEONE, "logintoken");
                            }

                            if (isShowNo) {
                                SpUtils.getInstance().putString(Constants.ACCOUTONE, null);
                                SpUtils.getInstance().putString(Constants.PWDONE, null);
                                SpUtils.getInstance().putString(Constants.TIMEONE, null);
                                SpUtils.getInstance().putString(Constants.TOKENONE, null);
                                SpUtils.getInstance().putString(Constants.LOGINSTYLEONE, "loginno");
                            }


//                            if (isChanged) {
//
//                                int one = SpUtils.getInstance().getInt(Constants.ONEASRPRODUCT);
//                                if (values[one].getSampleRate() == values[checkedRadioButtonId - 1].getSampleRate()) {
//                                    SpUtils.getInstance().putInt(Constants.ONEASRPRODUCT, checkedRadioButtonId - 1);
//                                    finish();
//                                } else {
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
//                                    builder.setTitle("确定要修改采样率吗？确定会删除掉采样率不同的板块。")
//                                            .setNegativeButton("取消", null)
//                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    SpUtils.getInstance().putInt(Constants.ONEASRPRODUCT, checkedRadioButtonId - 1);
//                                                    SpUtils.getInstance().putBool(Constants.ISCHANGEHZONE, false);
//                                                    dialog.dismiss();
//                                                    finish();
//                                                }
//                                            })
//                                            .show();
//                                }
//
//                            } else {
//                                finish();
//                            }

                        }
                        break;
                }

            }
        });

        /**
         *
         * tts相关
         * */

        mBTTTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ipAndport = mEtPortIpTTS.getText().toString().trim();

                if (!TextUtils.isEmpty(ipAndport)) {
                    SpUtils.getInstance().putString(Constants.SERVER_IP_ADDR_PORT_TTS, ipAndport);
                }

                String spd = mEtSpd.getText().toString().trim();
                String pit = mEtPit.getText().toString().trim();
                String vol = mEtVol.getText().toString().trim();

                if (!TextUtils.isEmpty(spd)) {
                    SpUtils.getInstance().putInt(Constants.SPD, Integer.parseInt(spd));
                }
                if (!TextUtils.isEmpty(pit)) {
                    SpUtils.getInstance().putInt(Constants.PIT, Integer.parseInt(pit));

                }
                if (!TextUtils.isEmpty(vol)) {
                    SpUtils.getInstance().putInt(Constants.VOl, Integer.parseInt(vol));

                }

                finish();
            }
        });
        /**
         *
         * 登陆相关
         * */
        mTVmm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPwd();
                //todo
            }
        });

        mTVtoken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginToken();
            }
        });

        mTVpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginNo();
            }
        });
        mTvchToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                TimePickerView pvTime = new TimePickerBuilder(SettingActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        SimpleDateFormat format = new SimpleDateFormat(com.baidu.acu.pie.model.Constants.UTC_DATE_TIME_FORMAT);
                        String time = format.format(date);
                        mTvtime.setText(time);
                    }
                })
                        .setType(new boolean[]{true, true, true, true, true, true})//分别对应年月日时分秒，默认全部显示
                        .setLabel("年", "月", "日", "时", "分", "秒")
                        .build();
                pvTime.show();
            }
        });
    }


    private void initRadioGroup() {

        switch (flag) {
            case "one":
                int oneAsr = SpUtils.getInstance().getInt(Constants.ONEASRPRODUCT);
                isCheckId = oneAsr;
                break;
        }
        mRadioGroup.removeAllViews();
        values = AsrProduct.values();
        for (int i = 0; i < values.length; i++) {
            RadioButton radioButton = new RadioButton(SettingActivity.this);
            radioButton.setId(i + 1);
            radioButton.setText(values[i].getName() + "(" + values[i].getSampleRate() + ")");
            mRadioGroup.addView(radioButton);
        }
        if (isCheckId != -1) {
            mRadioGroup.check(isCheckId + 1);
        }

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });
    }

    private void loginPwd() {
        mTVmm.setBackgroundResource(R.color.colorblue);
        mTVmm.setTextColor(getResources().getColor(R.color.white));
        mTVtoken.setBackgroundResource(R.color.white);
        mTVtoken.setTextColor(getResources().getColor(R.color.black));
        mTVpass.setBackgroundResource(R.color.white);
        mTVpass.setTextColor(getResources().getColor(R.color.black));
        mLltoken.setVisibility(View.GONE);
        mLlpwd.setVisibility(View.VISIBLE);
        mLlaccout.setVisibility(View.VISIBLE);
        mLltokentime.setVisibility(View.VISIBLE);
        isShowPwd = true;
        isShowToken = false;
        isShowNo = false;
    }

    private void loginToken() {
        mTVtoken.setBackgroundResource(R.color.colorblue);
        mTVtoken.setTextColor(getResources().getColor(R.color.white));
        mTVmm.setBackgroundResource(R.color.white);
        mTVmm.setTextColor(getResources().getColor(R.color.black));
        mTVpass.setBackgroundResource(R.color.white);
        mTVpass.setTextColor(getResources().getColor(R.color.black));
        mLltoken.setVisibility(View.VISIBLE);
        mLlpwd.setVisibility(View.GONE);
        mLlaccout.setVisibility(View.VISIBLE);
        mLltokentime.setVisibility(View.VISIBLE);
        isShowPwd = false;
        isShowToken = true;
        isShowNo = false;
    }

    private void loginNo() {
        mTVpass.setBackgroundResource(R.color.colorblue);
        mTVpass.setTextColor(getResources().getColor(R.color.white));
        mTVmm.setBackgroundResource(R.color.white);
        mTVmm.setTextColor(getResources().getColor(R.color.black));
        mTVtoken.setBackgroundResource(R.color.white);
        mTVtoken.setTextColor(getResources().getColor(R.color.black));
        mLltoken.setVisibility(View.GONE);
        mLlpwd.setVisibility(View.GONE);
        mLlaccout.setVisibility(View.GONE);
        mLltokentime.setVisibility(View.GONE);
        isShowPwd = false;
        isShowToken = false;
        isShowNo = true;
    }

    private void hideSoftKeyboard() {
        View localView = getCurrentFocus();
        if (localView != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    private void initTime() {
        SimpleDateFormat format = new SimpleDateFormat(com.baidu.acu.pie.model.Constants.UTC_DATE_TIME_FORMAT);
        long currentTimeMillis = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
        Date date = new Date(currentTimeMillis);
        String time = format.format(date);
        mTvtime.setText(time);
    }
}

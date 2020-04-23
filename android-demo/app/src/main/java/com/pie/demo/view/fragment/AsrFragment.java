package com.pie.demo.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.acu.pie.model.AsrProduct;
import com.pie.demo.Constants;
import com.pie.demo.R;
import com.pie.demo.base.BaseFragment;
import com.pie.demo.manager.RecoringManaegerInterfaceOne;
import com.pie.demo.manager.RecoringManeger;
import com.pie.demo.utils.SpUtils;
import com.pie.demo.view.activity.SettingActivity;
import com.pie.demo.view.weiget.VoiceImgView;

public class AsrFragment extends BaseFragment {

    private VoiceImgView vivButton;
    private LinearLayout mRlOne;
    private TextView mTvOne;
    private TextView mTvOneError;
    private EditText ttvText1;
    private ScrollView mSlOne;
    private RadioButton rbQuery, rbRender;
    private RadioGroup rbDhGroup;

    private Button dhSendDirectly;

    private RecoringManeger recoringManeger;

    private StringBuilder s1 = null;
    /**
     * 记录关闭了几个client，最多3个
     */
    private int stopThread = 0;
    /**
     * 模型
     */
    private AsrProduct[] values;
    /**
     * 初始化第一个客户端，默认使用 客服模型：金融领域
     */
    private int defaultAsr;

    private boolean switchone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(mActivity, R.layout.fragment_asr, null);

        initView(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.recoringManeger = RecoringManeger.getInstance();
        this.values = AsrProduct.values();
        initLisener();
    }

    private void initView(View view) {

        vivButton = view.findViewById(R.id.vivButton);
        mRlOne = view.findViewById(R.id.mRlOne);
        mTvOne = view.findViewById(R.id.mTvOne);
        mTvOneError = view.findViewById(R.id.mTvOneError);
        ttvText1 = view.findViewById(R.id.ttvText1);

        mSlOne = view.findViewById(R.id.mSlOne);
        rbQuery = view.findViewById(R.id.rb_dh_query);
        rbRender = view.findViewById(R.id.rb_dh_render);
        rbDhGroup = view.findViewById(R.id.rg_dh_text);

        dhSendDirectly = view.findViewById(R.id.dh_send_directly);
    }

    private void initLisener() {
        /**
         * button监听
         */
        vivButton.setOnVoiceButtonInterface(new VoiceImgView.onVoiceButtonInterface() {
            @Override
            public void onStartVoice() {
                s1 = new StringBuilder();

//                mTvOneError.setVisibility(View.GONE);
                ttvText1.setText("");

//                if (mRlOne.getVisibility() == View.GONE) {
//                    Toast.makeText(getActivity(), "请先点击加号选择一个模型在录音", Toast.LENGTH_SHORT).show();
//                    vivButton.stopAnim();
//                    return;
//                } else {
                    recoringManeger.startRecord();
//                }
            }

            @Override
            public void onStopVoice() {
                recoringManeger.stopRecord();

                stopThread = 0;
            }
        });

        rbDhGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RecoringManeger.DhTextType dhTextType = RecoringManeger.DhTextType.query;
                if (rbQuery.isChecked()) {
                    dhTextType = RecoringManeger.DhTextType.query;
                } else if (rbRender.isChecked()) {
                    dhTextType = RecoringManeger.DhTextType.render;
                }
                recoringManeger.setDhTextType(dhTextType);
            }
        });

        dhSendDirectly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoringManeger.sendText(ttvText1.getText().toString());
            }
        });

        mTvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecord();
                Intent intent = new Intent(mActivity, SettingActivity.class);
                intent.putExtra("flag", "one");
                startActivity(intent);
            }
        });

        RecoringManeger.getInstance().setRecoringManaegerInterfaceOne(new RecoringManaegerInterfaceOne() {
            @Override
            public void onNext(final String result, final Boolean completed) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSlOne.fullScroll(ScrollView.FOCUS_DOWN);// 滚动到底部
                        if (switchone) {
                            if (!TextUtils.isEmpty(result)) {
                                if (completed) {
                                    s1.append(result);
                                    ttvText1.setText(s1);
                                } else {
                                    ttvText1.setText(s1.toString() + result);
                                }
                            }
                        } else {
                            if (completed) {
                                s1.append(result);
                                ttvText1.setText(s1);
                            }
                        }

                    }
                });
            }

            @Override
            public void onError(final String message) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvOneError.setVisibility(View.VISIBLE);
                        mTvOneError.setText(message);

                        errorStopVoice();
                    }
                });
            }
        });
    }

    public void isRecord() {
        boolean record = RecoringManeger.getInstance().isRecord();
        if (record) {
            vivButton.stopAnim();
            RecoringManeger.getInstance().stopRecord();
            stopThread = 0;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                if (mRlOne.getVisibility() == View.GONE) {
                    mRlOne.setVisibility(View.VISIBLE);
                    mTvOne.setText("点击这里进行设置");
                    SpUtils.getInstance().putString(Constants.ONEADDRESS, Constants.SERVER_IP_ADDR);
                    SpUtils.getInstance().putString(Constants.ONEPORT, Constants.SERVER_IP_PORT + "");
                    SpUtils.getInstance().putInt(Constants.ONEASRPRODUCT, defaultAsr);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        /**
         * 初始化AsrClient配置
         */
        String oneAddress = SpUtils.getInstance().getString(Constants.ONEADDRESS);
        String onePort = SpUtils.getInstance().getString(Constants.ONEPORT);

        switchone = SpUtils.getInstance().getBool(Constants.SWITCHISCHECKEDONE);

        defaultAsr = AsrProduct.INPUT_METHOD.ordinal();
        mRlOne.setVisibility(View.VISIBLE);
        mTvOne.setText("点击这里进行设置");

    }

    private void errorStopVoice() {
        stopThread++;
        if (stopThread == RecoringManeger.getInstance().getHavaThread()) {
            vivButton.stopAnim();
            RecoringManeger.getInstance().stopRecord();
            stopThread = 0;
        }
    }

    private int getSampleHz(AsrProduct value) {
        return 16000;
    }
}

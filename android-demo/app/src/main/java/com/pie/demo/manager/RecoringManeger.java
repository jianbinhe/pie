package com.pie.demo.manager;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.acu.pie.AudioStreaming;
import com.baidu.acu.pie.client.Consumer;
import com.baidu.acu.pie.grpc.AsrClientGrpcImpl;
import com.baidu.acu.pie.model.AsrConfig;
import com.baidu.acu.pie.model.AsrProduct;
import com.baidu.acu.pie.model.RecognitionResult;
import com.baidu.acu.pie.model.StreamContext;
import com.google.protobuf.ByteString;
import com.pie.demo.Constants;
import com.pie.demo.dh.DhClient;
import com.pie.demo.dh.DhResponse;
import com.pie.demo.dh.TextRequest;
import com.pie.demo.utils.SpUtils;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class RecoringManeger {


    //44100、22050、11025，4000、8000。
    private int SAMPLERATEINHZ = 8000;
    //MONO单声道，STEREO立体声
    private static final int CHANNELCONFIG = AudioFormat.CHANNEL_IN_MONO;
    //采样大小16bit 或者8bit。
    private static final int AUDIOFORMAT = AudioFormat.ENCODING_PCM_16BIT;
    // 采集数据缓冲区的大小
    private int MINBUFFERSIZE = 0;

    private boolean isRecord = false;
//    private final AsrProduct[] values;

    public boolean isRecord() {
        return isRecord;
    }

    private AudioRecord audioRecord = null;

    private int HavaThread = 0;

    private DhTextType dhTextType = DhTextType.query;

    private DhClient dhClient;
    private String appId;
    private String phoneToken;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    
    private final String TAG = "RecordingManager";


    public int getHavaThread() {
        return HavaThread;
    }

    private static RecoringManeger instance;

    public static RecoringManeger getInstance() {
        if (null == instance) {
            synchronized (RecoringManeger.class) {
                if (null == instance) {
                    instance = new RecoringManeger();
                }
            }
        }
        return instance;
    }

    private RecoringManeger() {
    }


    public void setDhTextType(DhTextType type) {
        this.dhTextType = type;
    }

    /**
     * 开始录音
     */
    public void startRecord() {

        try {

            if (audioRecord == null) {
                initAudioRecord();
            }


            isRecord = true;
            audioRecord.startRecording();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    writeData();
                }
            }).start();

        } catch (Exception e) {
            Log.e(TAG, "error " + e.getMessage());
        }

    }

    /**
     * 停止录音
     */

    public void stopRecord() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    isRecord = false;
                    if (audioRecord != null) {
                        audioRecord.stop();
//                        audioRecord.release();
//                        audioRecord = null;
                    }


                    if (streamObserverOne != null) {
                        streamObserverOne.getFinishLatch().await();
                        streamObserverOne.complete();
                    }

//                    if (asrClientGrpcOne != null) {
//                        asrClientGrpcOne.shutdown();
//                    }

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    Log.e(TAG, "Fail to stop record, message=" + throwable.getMessage());
                }
            }
        }).start();
    }

    public void sendText(String text) {
        executorService.submit(() -> sendTextToDh(text));
    }

    /**
     * 初始化AudioRecord
     */

    private void initAudioRecord() {

        SAMPLERATEINHZ = 16000;
        Log.e(TAG, SAMPLERATEINHZ + "");

        MINBUFFERSIZE = AudioRecord.getMinBufferSize(SAMPLERATEINHZ, CHANNELCONFIG, AUDIOFORMAT);

        Log.e("min buffer size", MINBUFFERSIZE + "");
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLERATEINHZ, CHANNELCONFIG, AUDIOFORMAT, MINBUFFERSIZE);
    }

    private void writeData() {

        HavaThread = 0;

        /**
         * 初始化AsrClientGrpcImpl
         */
//        initSp();

        byte[] audiodata = new byte[MINBUFFERSIZE];

        try {
            while (isRecord) {
                int readSize = audioRecord.read(audiodata, 0, MINBUFFERSIZE);
                //  -3(可能录音被禁止)
                if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {

                    AudioStreaming.AudioFragmentRequest request = AudioStreaming.AudioFragmentRequest.newBuilder()
                            .setAudioData(ByteString.copyFrom(audiodata))
                            .build();
                    if (streamObserverOne != null) {
//                        streamObserverOne.getSender().onNext(request);
                        streamObserverOne.send(audiodata);
                    }
                }
            }
        } catch (Exception e) {
            if (streamObserverOne != null) {
                streamObserverOne.getSender().onError(e);
            }
        }
    }

    private void sendTextToDh(String text) {
        TextRequest textRequest = new TextRequest();
        textRequest.setAppId(appId);
        textRequest.setText(text);
        textRequest.setPhoneToken(phoneToken);
        Call<DhResponse> query;
        if (DhTextType.query == dhTextType) {
            query = dhClient.queryText().textQuery(textRequest);
        } else if (DhTextType.render == dhTextType) {
            query = dhClient.queryText().textRender(textRequest);
        } else {
            Log.e(TAG, "Fail to parse query type");
            return;
        }
        try {
            Response<DhResponse> response = query.execute();
            if (response.isSuccessful()) {
                Log.e(TAG, response.body().toString());
            } else {
                Log.e(TAG, response.toString());
            }
        } catch (IOException e) {
            Log.e(TAG, "Fail to send query text to dh", e);
        }
    }

    private void initSp() {
        String oneAddress = SpUtils.getInstance().getString(Constants.ONEADDRESS);
        String onePort = SpUtils.getInstance().getString(Constants.ONEPORT);

        String oneAccout = SpUtils.getInstance().getString(Constants.ACCOUTONE);
        String onePwd = SpUtils.getInstance().getString(Constants.PWDONE);
        String oneTime = SpUtils.getInstance().getString(Constants.TIMEONE);
        String oneToken = SpUtils.getInstance().getString(Constants.TOKENONE);

        if (!TextUtils.isEmpty(oneAddress) && !TextUtils.isEmpty(onePort)) {
            HavaThread++;
            AsrConfig config = new AsrConfig();
            config.serverIp(oneAddress)
                    .serverPort(Integer.parseInt(onePort))
                    .appName("android")
                    .product(AsrProduct.INPUT_METHOD)
                    .userName(oneAccout)
                    .password(onePwd)
                    .token(oneToken);
            if (!TextUtils.isEmpty(oneTime)) {
                config.expireDateTime(DateTime.parse(oneTime));
            }

            try {
                dhClient = new DhClient(SpUtils.getInstance().getString(Constants.DH_ADDR));
                appId = SpUtils.getInstance().getString(Constants.DH_APP_ID);
                phoneToken = SpUtils.getInstance().getString(Constants.DH_PHONE_TOKEN);

                AsrClientGrpcImpl asrClientGrpcOne = new AsrClientGrpcImpl(config);
                streamObserverOne = asrClientGrpcOne.asyncRecognize(new Consumer<RecognitionResult>() {
                    @Override
                    public void accept(RecognitionResult recognitionResult) {

                        String result = recognitionResult.getResult();
                        Log.e(TAG, "res:" + result);
                        if (TextUtils.isEmpty(result)) {
                            Log.e(TAG, "error");
                            if (recoringManaegerInterfaceOne != null) {
                                recoringManaegerInterfaceOne.onError("err");
                            }
                        } else {
                            boolean completed = recognitionResult.isCompleted();
                            if (recoringManaegerInterfaceOne != null) {
                                recoringManaegerInterfaceOne.onNext(result, completed);
                            }
                            if (completed) {
                                sendTextToDh(result);
                            }
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "catch:");
                if (recoringManaegerInterfaceOne != null) {
                    recoringManaegerInterfaceOne.onError(e.getMessage());
                }
            }
        }
    }

    private StreamContext streamObserverOne;
    private RecoringManaegerInterfaceOne recoringManaegerInterfaceOne = null;

    public void setRecoringManaegerInterfaceOne(RecoringManaegerInterfaceOne recoringManaegerInterfaceOne) {
        this.recoringManaegerInterfaceOne = recoringManaegerInterfaceOne;
    }


    public enum DhTextType {
        query, render
    }
}

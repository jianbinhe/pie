package com.pie.demo.dh;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DigitalHumanQueryText {

    @POST("/cloud/api/digitalhuman/agent/v1/query/text")
    Call<DhResponse> textQuery(@Body TextRequest request);

    @POST("/cloud/api/digitalhuman/agent/v1/render/text")
    Call<DhResponse> textRender(@Body TextRequest request);
}

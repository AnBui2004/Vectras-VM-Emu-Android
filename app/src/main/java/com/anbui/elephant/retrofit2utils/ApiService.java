/*
 * Copyright (C) 2026 Nguyen Bao An Bui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.anbui.elephant.retrofit2utils;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiService {
    @GET
    Call<ResponseBody> getRawJson(@Url String url);

    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String url);

    @POST
    Call<ResponseBody> post(@Url String url, @Body RequestBody body);
}

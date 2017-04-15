package com.blogspot.sontx.bottle.fs.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

public final class ConnectionUtils {

    private static OkHttpClient client = new OkHttpClient();
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T post(String url, String authHeader, Class<T> clazz) throws IOException {
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse(MediaType.APPLICATION_JSON), "");
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .build();

        Response response = client.newCall(request).execute();
        return objectMapper.readValue(response.body().byteStream(), clazz);
    }

    private ConnectionUtils() {
    }
}

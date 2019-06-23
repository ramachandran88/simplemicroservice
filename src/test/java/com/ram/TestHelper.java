package com.ram;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by Ravi on 23-Jun-19.
 */
public abstract class TestHelper {

    protected UrlResponse postMethod(String api, String requestBody) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:4567" + api);
        StringEntity entity = new StringEntity(requestBody);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        UrlResponse urlResponse = new UrlResponse();
        urlResponse.body = IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
        urlResponse.status = response.getStatusLine().getStatusCode();
        return urlResponse;
    }

    protected UrlResponse getMethod(String api) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:4567" + api);
        CloseableHttpResponse response = client.execute(httpGet);
        UrlResponse urlResponse = new UrlResponse();
        urlResponse.body = IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
        urlResponse.status = response.getStatusLine().getStatusCode();
        return urlResponse;
    }

    protected static class UrlResponse {
        public Map<String, List<String>> headers;
        public String body;
        public int status;
    }

}

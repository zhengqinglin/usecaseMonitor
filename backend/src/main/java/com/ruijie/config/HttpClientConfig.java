package com.ruijie.config;

import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

@Configuration
public class HttpClientConfig {

        @Value("${httpclient.connMaxTotal:20}")
        private int connMaxTotal;

        @Value("${httpclient.maxPerRoute:20}")
        private int maxPerRoute;

        @Value("${httpclient.retryTime:3}")
        private int retryTime;

        @Value("${httpclient.timeToLive:60}")
        private int timeToLive;

        @Value("${httpclient.keepAliveTime:30}")
        private int keepAliveTime;

        @Value("${httpclient.connectTimeout:2000}")
        private int connectTimeout;

        @Value("${httpclient.connectRequestTimeout:2000}")
        private int connectRequestTimeout;

        @Value("${httpclient.socketTimeout:2000}")
        private int socketTimeout;

        @Value("${httpclient.validateAfterInactivity:2000}")
        private int validateAfterInactivity;


        @Bean(name = "httpClientConnectionManager")
        public PoolingHttpClientConnectionManager getHttpClientConnectionManager(){
            PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager(timeToLive, TimeUnit.SECONDS);
            httpClientConnectionManager.setMaxTotal(connMaxTotal);
            httpClientConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
            httpClientConnectionManager.setValidateAfterInactivity(validateAfterInactivity);

            return httpClientConnectionManager;
        }


        @Bean("requestConfig")
        public RequestConfig getRequestConfig(){
            return RequestConfig.custom()
                    .setConnectionRequestTimeout(connectRequestTimeout)
                    .setConnectTimeout(connectTimeout)
                    .setSocketTimeout(socketTimeout)
                    .build();
        }


        @Bean(name = "httpRequestRetryHandler")
        public HttpRequestRetryHandler httpRequestRetryHandler() {

            return new HttpRequestRetryHandler() {
                public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                    // Do not retry if over max retry count,如果重试次数超过了retryTime,则不再重试请求
                    if (executionCount >= retryTime) {
                        return false;
                    }
                    // 服务端断掉客户端的连接异常
                    if (exception instanceof NoHttpResponseException) {
                        return true;
                    }
                    // time out 超时重试
                    if (exception instanceof InterruptedIOException) {
                        return true;
                    }
                    // Unknown host
                    if (exception instanceof UnknownHostException) {
                        return false;
                    }
                    // Connection refused
                    if (exception instanceof ConnectTimeoutException) {
                        return false;
                    }
                    // SSL handshake exception
                    if (exception instanceof SSLException) {
                        return false;
                    }
                    HttpClientContext clientContext = HttpClientContext.adapt(context);
                    HttpRequest request = clientContext.getRequest();
                    if (!(request instanceof HttpEntityEnclosingRequest)) {
                        return true;
                    }
                    return false;
                }
            };
        }

    @Bean("connectionKeepAliveStrategy")
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
        return new ConnectionKeepAliveStrategy() {

            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                // Honor 'keep-alive' header
                HeaderElementIterator it = new BasicHeaderElementIterator(
                        response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        try {
                            return Long.parseLong(value) * 1000;
                        } catch (NumberFormatException ignore) {
                        }
                    }
                }
                return keepAliveTime * 1000;
            }
        };
    }

    @Bean(name = "httpClientBuilder")
    public HttpClientBuilder getHttpClientBuilder(@Qualifier("httpClientConnectionManager")PoolingHttpClientConnectionManager httpClientConnectionManager,
                                                  @Qualifier("httpRequestRetryHandler")HttpRequestRetryHandler httpRequestRetryHandler,
                                                  @Qualifier("connectionKeepAliveStrategy")ConnectionKeepAliveStrategy connectionKeepAliveStrategy,
                                                  @Qualifier("requestConfig")RequestConfig requestConfig){

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        httpClientBuilder.setConnectionManager(httpClientConnectionManager);
        httpClientBuilder.setRetryHandler(httpRequestRetryHandler);
        httpClientBuilder.setKeepAliveStrategy(connectionKeepAliveStrategy);
        httpClientBuilder.setDefaultRequestConfig(requestConfig);

        return httpClientBuilder;
    }

    @Bean("httpclient")
    public CloseableHttpClient getCloseableHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder){
        return httpClientBuilder.build();
    }
}

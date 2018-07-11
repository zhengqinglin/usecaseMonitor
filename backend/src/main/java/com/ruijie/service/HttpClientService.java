package com.ruijie.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by zhengqinglin on 2017/7/29.
 */
@Service
public class HttpClientService {

    protected static Logger LOG = LoggerFactory.getLogger(HttpClientService.class);

    @Autowired
    private CloseableHttpClient httpClient;

    public String doGet(String url){
        String result = null;
        HttpGet httpGet = new HttpGet(url);
        HttpEntity entity = null;
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            entity = response.getEntity();
            result = EntityUtils.toString(entity,Consts.UTF_8);
            EntityUtils.consumeQuietly(entity);
        } catch (IOException e) {
            LOG.error("执行GET请求，URL为{},出现异常【{}】",url,e);
        }finally{
            if(null != entity){// 释放连接
                EntityUtils.consumeQuietly(entity);
            }
        }
        return result;
    }

    public String doPost(String url,String postData){
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(postData, Consts.UTF_8));
        HttpEntity entity = null;
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            entity = response.getEntity();
            result = EntityUtils.toString(entity,Consts.UTF_8);
            EntityUtils.consumeQuietly(entity);
        } catch (IOException e) {
            LOG.error("执行POST请求，URL为{},PostData为{},出现异常【{}】",url,postData,e);
        }finally{
            if(null != entity){// 释放连接
                EntityUtils.consumeQuietly(entity);
            }
        }
        return result;
    }

    /**
     * 处理post请求，get请求req传空
     * @param url
     * @param req
     * @param repsClazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T doPost(String url, Object req, Class<T> repsClazz) throws Exception {
        HttpPost post = new HttpPost(url);
        T res = null;
        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonStr = jsonMapper.writeValueAsString(req);
        StringEntity reqEntity = new StringEntity(jsonStr);
        reqEntity.setContentEncoding("UTF-8");
        reqEntity.setContentType("application/json");
        post.setEntity(reqEntity);

        HttpEntity respEntity = null;
        try {
            HttpResponse resp = httpClient.execute(post);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                respEntity = resp.getEntity();
                String content = EntityUtils.toString(respEntity, "UTF-8");
                if(LOG.isDebugEnabled()){
                    LOG.debug("httpclient post response : {}", content);
                }
                res = jsonMapper.readValue(content, repsClazz);
            } else {
                LOG.error("post fail, URL为{},PostData为{},statusCode : {}", url,req,String.valueOf(resp.getStatusLine().getStatusCode()));
            }
            EntityUtils.consumeQuietly(respEntity);
        }catch (Exception e){
            LOG.error("执行POST请求，URL为{},PostData为{},出现异常【{}】",url,req,e);
        }finally{
            if(null != respEntity){// 释放连接
                EntityUtils.consumeQuietly(respEntity);
            }
        }
        return res;
    }
}

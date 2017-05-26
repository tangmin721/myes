package com.cachexic.elasticsearch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @author tangmin
 * @version V1.0
 * @Title: UserCRUD.java
 * @Package com.gasq.cloud.web.elasticsearch.mytest
 * @Description: elasticsearch api测试  本机单机版
 * @date 2017-05-26 10:23:33
 */
public class MyUserCRUD {

    private static final Logger log = LogManager.getLogger(MyUserCRUD.class);

    public static void main(String[] args) throws Exception {
        log.debug("hello");

        Settings settings = Settings.builder()
                .put("cluster.name","myes522")
                .build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.1.100"),9300));


        createUser(client);

        client.close();
    }

    private static void createUser(TransportClient client) throws IOException {
        IndexResponse indexResponse = client.prepareIndex("company", "user", "1")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("name", "zhang san")
                        .field("age", 27)
                        .field("position", "technique")
                        .field("country", "china")
                        .field("join_date", "2017-01-04")
                        .field("salary", 10000)
                        .endObject())
                .get();
        System.out.println("=====================");
        System.out.println(indexResponse.getResult());
    }
}

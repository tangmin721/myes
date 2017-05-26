package com.cachexic.elasticsearch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.elasticsearch.xpack.security.authc.support.SecuredString;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;

import static org.elasticsearch.xpack.security.authc.support.UsernamePasswordToken.basicAuthHeaderValue;

/**
 * @author tangmin
 * @version V1.0
 * @Title: UserCRUD.java
 * @Package com.gasq.cloud.web.elasticsearch.mytest
 * @Description: elasticsearch api测试
 * @date 2017-05-26 10:23:33
 */
public class UserCRUD {

    private static final Logger log = LogManager.getLogger(UserCRUD.class);

    public static void main(String[] args) throws Exception {
        log.debug("hello");

        Settings settings = Settings.builder()
                .put("cluster.name","s2f-cloud-es")
                .put("xpack.security.user", "tangmin:tangmin")
                .put("client.transport.sniff",true)//自动探查，每隔5秒回自动刷新
                .build();

        TransportClient client = new PreBuiltXPackTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("cloud1"),9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("cloud2"),9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("cloud3"),9300));
        String token = basicAuthHeaderValue("elastic", new SecuredString("changeme".toCharArray()));

        client.filterWithHeader(Collections.singletonMap("Authorization", token))
                .prepareSearch().get();

        createUser(client);

        client.close();
    }

    /**
     * 创建索引
     * @param client
     * @throws IOException
     */
    private static void createUser(TransportClient client) throws IOException {
        IndexResponse indexResponse = client.prepareIndex("company", "user", "1")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("name", "zhang san")
                        .field("age", 27)
                        .field("position", "technique")
                        .field("country", "china")
                        .field("join_date", "2017-01-03")
                        .field("salary", 10000)
                        .endObject())
                .get();
        System.out.println(indexResponse.toString());
    }
}

package com.epicmed.developer.assessment.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.NetworkConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

import static com.epicmed.developer.assessment.util.constant.GeneralConstant.SEMICOLON_SEPARATOR;
import static com.epicmed.developer.assessment.util.constant.GeneralConstant.VAR_CACHE_USER_DUMMY;

@Log4j2
@Configuration
@EnableCaching
public class HazelcastConfiguration {

    @Value(value = "${hazelcast.cluster.name}")
    private String hazelcastClusterName;

    @Value(value = "${hazelcast.host.pair}")
    private String hazelcastHostPair;

    @Bean
    public Config hazelcastConfig() {
        var hosts = hazelcastHostPair.split(SEMICOLON_SEPARATOR);

        var config = new Config();
        config.setClusterName(hazelcastClusterName);
        config.addMapConfig(
                new MapConfig(VAR_CACHE_USER_DUMMY)
                        .setEvictionConfig(
                                new EvictionConfig()
                                        .setEvictionPolicy(EvictionPolicy.LRU)
                                        .setMaxSizePolicy(MaxSizePolicy.PER_NODE) // PER_NODE per size entries and USED_HEAP_SIZE in MB
                                        .setSize(100_000)
                        )
                        .setBackupCount(1)
        );
        NetworkConfig networkConfig = config.getNetworkConfig();
        JoinConfig joinConfig = networkConfig.getJoin();

        // TCP/IP Discovery
        joinConfig.getTcpIpConfig().setEnabled(true);
        for (String host : hosts) {
            try {
                var localhost = InetAddress.getLocalHost().getHostAddress();
                if (!Objects.equals(localhost,host)) {
                    joinConfig.getTcpIpConfig().addMember(host);
                    log.info("Add hazelcast host {}", host);
                }
            } catch (UnknownHostException e) {
                log.error("Failed add hazelcast host {}",host);
                log.error(e);
            }
        }

        return config;
    }
}

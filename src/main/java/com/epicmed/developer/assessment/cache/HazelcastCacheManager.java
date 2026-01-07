package com.epicmed.developer.assessment.cache;

import com.epicmed.developer.assessment.dto.DummyDto;
import com.hazelcast.collection.IList;
import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.epicmed.developer.assessment.util.constant.GeneralConstant.VAR_CACHE_USER_DUMMY;

@Getter
@Component
public class HazelcastCacheManager {

    private final IMap<String,DummyDto> userDummyData;

    public HazelcastCacheManager(HazelcastInstance hazelcastInstance) {
        this.userDummyData = hazelcastInstance.getMap(VAR_CACHE_USER_DUMMY);
    }

    public void setDummyDto(String key, DummyDto dummyDto) {
        userDummyData.put(key, dummyDto, 10, TimeUnit.MINUTES);
    }

    public DummyDto getDummyDto(String key) {
        return userDummyData.get(key);
    }
}

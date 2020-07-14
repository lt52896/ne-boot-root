/**
 * Copyright 2015 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ne.boot.persist.mybatis.cache;

import com.ne.boot.common.util.SerializeUtil;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.HashOperations;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Cache adapter for Redis.
 *
 * @author Eduardo Macarron
 */
public final class RedisCache implements Cache {

    private final ReadWriteLock readWriteLock = new DummyReadWriteLock();

    private String id;

    private static HashOperations<String, Object, byte[]> template;

    public RedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getSize() {
        if (template == null) {
            return 0;
        }
        return template.size(id).intValue();
    }

    @Override
    public void putObject(final Object key, final Object value) {
        if (template == null) {
            return;
        }
        if (key == null || value == null) {
            return;
        }
        Cacher cacher = new Cacher();
        cacher.setValue(value);
        template.put(id, key, SerializeUtil.serialize(cacher));
    }

    @Override
    public Object getObject(final Object key) {
        if (template == null) {
            return null;
        }
        byte[] bytes = template.get(id, key);
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        Cacher cacher = SerializeUtil.deserialize(bytes, Cacher.class);
        return cacher.getValue();
    }

    @Override
    public Object removeObject(final Object key) {
        if (template == null) {
            return null;
        }
        return template.delete(id, key);
    }

    @Override
    public void clear() {
        if (template == null) {
            return;
        }
        template.delete(id);
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    @Override
    public String toString() {
        return "Redis {" + id + "}";
    }

    public static void setTemplate(HashOperations<String, Object, byte[]> template) {
        RedisCache.template = template;
    }

}

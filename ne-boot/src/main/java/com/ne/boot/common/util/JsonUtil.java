package com.ne.boot.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.ne.boot.common.exception.NEException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Robin on 11/16/16.
 */
public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper objectMapper;

    private static final Configuration conf;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        conf = Configuration.builder()
                .mappingProvider(new JacksonMappingProvider(objectMapper))
                .jsonProvider(new JacksonJsonProvider(objectMapper)).build();
    }

    public static String writeValueAsString(Object value) {
        if (value == null) {
            return null;
        }
        String result = null;
        try {
            result = objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new NEException(e);
        }
        return result;
    }

    public static String writeValueAsPrettyString(Object value) {
        if (value == null) {
            return null;
        }
        String result = null;
        try {
            result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new NEException(e);
        }
        return result;
    }


    public static <T> T readValue(String content, Class<T> valueType) {
        if (content == null) {
            return null;
        }
        T result = null;
        try {
            result = objectMapper.readValue(content, valueType);
        } catch (IOException e) {
            throw new NEException(e);
        }
        return result;
    }

    public static <T> T readValue(String content, TypeReference valueTypeRef) {
        if (content == null) {
            return null;
        }
        T result = null;
        try {
            result = objectMapper.readValue(content, valueTypeRef);
        } catch (IOException e) {
            throw new NEException(e);
        }
        return result;
    }

    public static <T> T readValue(String json, String jsonPath) {
        try {
            return JsonPath.read(json, jsonPath);
        } catch (PathNotFoundException e) {
            logger.debug(e.getMessage());
            return null;
        } catch (Exception e) {
            throw new NEException(e);
        }
    }

    public static <T> T readValue(String json, String jsonPath, TypeRef<T> typeRef) {
        try {
            return JsonPath.using(conf).parse(json).read(jsonPath, typeRef);
        } catch (PathNotFoundException e) {
            logger.debug(e.getMessage());
            return null;
        } catch (Exception e) {
            throw new NEException(e);
        }
    }

    public static <T> T readValue(String json, String jsonPath, Predicate... filters) {
        try {
            return JsonPath.read(json, jsonPath, filters);
        } catch (PathNotFoundException e) {
            logger.debug(e.getMessage());
            return null;
        } catch (Exception e) {
            throw new NEException(e);
        }
    }
}

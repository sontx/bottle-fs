package com.blogspot.sontx.bottle.fs.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JacksonProvider implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper;

    public JacksonProvider() {
        objectMapper = new ObjectMapper()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }


    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}

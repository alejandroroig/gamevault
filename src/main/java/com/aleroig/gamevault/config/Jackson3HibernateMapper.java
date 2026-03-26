package com.aleroig.gamevault.config;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.format.FormatMapper;
import tools.jackson.databind.ObjectMapper;

public class Jackson3HibernateMapper implements FormatMapper {
    // Instanciamos el nuevo Jackson 3
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T fromString(CharSequence string, JavaType<T> javaType, WrapperOptions wrapperOptions) {
        if (string == null) return null;
        try {
            return mapper.readValue(string.toString(), javaType.getJavaTypeClass());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deserializando JSON con Jackson 3", e);
        }
    }

    @Override
    public <T> String toString(T value, JavaType<T> javaType, WrapperOptions wrapperOptions) {
        if (value == null) return null;
        try {
            return mapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error serializando JSON con Jackson 3", e);
        }
    }
}

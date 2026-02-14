package com.project.fitness.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JavaType;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.format.FormatMapper;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class Jackson3FormatMapper implements FormatMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T fromString(
            CharSequence charSequence,
            org.hibernate.type.descriptor.java.JavaType<T> javaType,
            WrapperOptions wrapperOptions) {

        try {
            JavaType jacksonType =
                    objectMapper.getTypeFactory().constructType(javaType.getJavaType());
            return objectMapper.readValue(charSequence.toString(), jacksonType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize JSON", e);
        }
    }

    public <T> T fromReader(
            Reader reader,
            org.hibernate.type.descriptor.java.JavaType<T> javaType,
            WrapperOptions wrapperOptions) {

        try {
            JavaType jacksonType =
                    objectMapper.getTypeFactory().constructType(javaType.getJavaType());
            return objectMapper.readValue(reader, jacksonType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize JSON from Reader", e);
        }
    }

    public <T> void toString(
            T value,
            org.hibernate.type.descriptor.java.JavaType<T> javaType,
            Writer writer,
            WrapperOptions wrapperOptions) {

        try {
            JavaType jacksonType =
                    objectMapper.getTypeFactory().constructType(javaType.getJavaType());
            objectMapper.writerFor(jacksonType).writeValue(writer, value);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize JSON", e);
        }
    }

    @Override
    public <T> String toString(
            T value,
            org.hibernate.type.descriptor.java.JavaType<T> javaType,
            WrapperOptions wrapperOptions) {

        try {
            JavaType jacksonType =
                    objectMapper.getTypeFactory().constructType(javaType.getJavaType());
            return objectMapper.writerFor(jacksonType).writeValueAsString(value);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize JSON to String", e);
        }
    }
}

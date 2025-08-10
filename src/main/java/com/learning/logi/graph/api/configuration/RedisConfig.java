package com.learning.logi.graph.api.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.learning.logi.graph.api.domain.optimization.entities.OptaRoutePlan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, OptaRoutePlan> optaRoutePlanRedisTemplate(final RedisConnectionFactory connectionFactory) {

        final RedisTemplate<String, OptaRoutePlan> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final BasicPolymorphicTypeValidator polymorphicTypeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(new BasicPolymorphicTypeValidator.TypeMatcher() {
                    @Override
                    public boolean match(MapperConfig<?> config, Class<?> clazz) {
                        return clazz.getName().startsWith("com.learning.logi.graph.api.domain.optimization");
                    }
                })
                .allowIfSubType(new BasicPolymorphicTypeValidator.TypeMatcher() {
                    @Override
                    public boolean match(MapperConfig<?> config, Class<?> clazz) {
                        return clazz.getName().startsWith("java.util");                    }
                })
                .build();

        objectMapper.activateDefaultTyping(polymorphicTypeValidator, ObjectMapper.DefaultTyping.NON_FINAL);

        final GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jsonRedisSerializer);

        return template;
    }
}

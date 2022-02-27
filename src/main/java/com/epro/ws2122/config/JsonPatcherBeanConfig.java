package com.epro.ws2122.config;

import com.epro.ws2122.dto.KrUpdateDTO;
import com.epro.ws2122.util.JsonPatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Configuration file for instances of {@link JsonPatcher}.</p>
 * <p>For every type, that needs to be patched with a json patch, a bean needs to be created here.</p>
 */
@Configuration
public class JsonPatcherBeanConfig {

    @Bean
    public JsonPatcher<KrUpdateDTO> krUpdatePatcher() {
        return new JsonPatcher<>(KrUpdateDTO.class);
    }
}

package com.epro.ws2122.config;

import com.epro.ws2122.dto.*;
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

    @Bean
    public JsonPatcher<CkrDTO> ckrPatcher() {
        return new JsonPatcher<>(CkrDTO.class);
    }

    @Bean
    public JsonPatcher<BukrDTO> bukrPatcher() {
        return new JsonPatcher<>(BukrDTO.class);
    }

    @Bean
    public JsonPatcher<CoDTO> coPatcher() {
        return new JsonPatcher<>(CoDTO.class);
    }

    @Bean
    public JsonPatcher<BuoDTO> buoPatcher() {
        return new JsonPatcher<>(BuoDTO.class);
    }
}

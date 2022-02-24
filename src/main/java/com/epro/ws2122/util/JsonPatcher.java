package com.epro.ws2122.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Instances of this generic class can apply a {@link JsonPatch} to any object of any type.</p>
 * <p>Since this class is generic, it can't be marked as a bean directly. To create beans for this class,
 * you have to create a method in your java bean config, that returns a typed instance, and annotate it with @Bean</p>
 *
 * @param <T> any type
 */
public class JsonPatcher<T> {
    private Class<T> clazz;

    @Autowired
    @Setter
    private ObjectMapper om;

    /**
     * Create a new instance. An instance of {@link Class} is necessary for the {@link ObjectMapper} to work.
     * @param clazz instance of {@link Class} for the type, this patcher is intended to be used on
     */
    public JsonPatcher(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Applies a {@link JsonPatch} to the given object
     * @param obj object, the patch is applied to
     * @param patch the patch with the changes
     * @return the patched object
     * @throws JsonPatchException
     * @throws JsonProcessingException
     */
    public T applyPatch(T obj, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(om.convertValue(obj, JsonNode.class));
        return om.treeToValue(patched, clazz);
    }
}

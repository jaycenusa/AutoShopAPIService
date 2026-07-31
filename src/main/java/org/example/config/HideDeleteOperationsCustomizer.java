package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import org.springdoc.core.customizers.OpenApiCustomizer;

/**
 * Removes DELETE operations from the generated OpenAPI document so they do not
 * appear in Swagger UI, while leaving the runtime endpoints unchanged.
 */
public class HideDeleteOperationsCustomizer implements OpenApiCustomizer {

    @Override
    public void customise(OpenAPI openApi) {
        Paths paths = openApi.getPaths();
        if (paths == null || paths.isEmpty()) {
            return;
        }

        paths.values().forEach(pathItem -> pathItem.setDelete(null));
        paths.entrySet().removeIf(entry -> hasNoOperations(entry.getValue()));
    }

    private static boolean hasNoOperations(PathItem pathItem) {
        return pathItem.readOperations() == null || pathItem.readOperations().isEmpty();
    }
}

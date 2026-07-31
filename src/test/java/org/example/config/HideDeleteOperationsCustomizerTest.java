package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HideDeleteOperationsCustomizerTest {

    private final HideDeleteOperationsCustomizer customizer = new HideDeleteOperationsCustomizer();

    @Test
    void removesDeleteOperationsAndKeepsOtherMethods() {
        PathItem partsItem = new PathItem()
                .get(new Operation().operationId("listParts"))
                .delete(new Operation().operationId("deletePart"));
        PathItem deleteOnlyItem = new PathItem()
                .delete(new Operation().operationId("deleteOnly"));

        OpenAPI openApi = new OpenAPI().paths(new Paths()
                .addPathItem("/api/parts/{id}", partsItem)
                .addPathItem("/api/orphan/{id}", deleteOnlyItem));

        customizer.customise(openApi);

        assertThat(openApi.getPaths()).containsOnlyKeys("/api/parts/{id}");
        PathItem remaining = openApi.getPaths().get("/api/parts/{id}");
        assertThat(remaining.getGet()).isNotNull();
        assertThat(remaining.getGet().getOperationId()).isEqualTo("listParts");
        assertThat(remaining.getDelete()).isNull();
    }

    @Test
    void ignoresMissingOrEmptyPaths() {
        OpenAPI empty = new OpenAPI();
        customizer.customise(empty);
        assertThat(empty.getPaths()).isNull();

        OpenAPI withEmptyPaths = new OpenAPI().paths(new Paths());
        customizer.customise(withEmptyPaths);
        assertThat(withEmptyPaths.getPaths()).isEmpty();
    }
}

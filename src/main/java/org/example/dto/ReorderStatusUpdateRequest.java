package org.example.dto;

import jakarta.validation.constraints.NotNull;
import org.example.entity.ReorderStatus;

public record ReorderStatusUpdateRequest(
        @NotNull ReorderStatus status
) {
}

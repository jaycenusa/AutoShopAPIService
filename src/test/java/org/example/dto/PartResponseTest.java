package org.example.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PartResponseTest {

    @Test
    void deriveStatusMatchesFrontendRules() {
        assertThat(PartResponse.deriveStatus(0, 10)).isEqualTo("out");
        assertThat(PartResponse.deriveStatus(3, 10)).isEqualTo("critical");
        assertThat(PartResponse.deriveStatus(8, 10)).isEqualTo("low");
        assertThat(PartResponse.deriveStatus(20, 10)).isEqualTo("ok");
    }
}

package com.lot.smartcossiol.domain

import com.lot.smartcossiol.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MeasuresTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Measures::class)
        val measures1 = Measures()
        measures1.id = 1L
        val measures2 = Measures()
        measures2.id = measures1.id
        assertThat(measures1).isEqualTo(measures2)
        measures2.id = 2L
        assertThat(measures1).isNotEqualTo(measures2)
        measures1.id = null
        assertThat(measures1).isNotEqualTo(measures2)
    }
}

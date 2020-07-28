package com.lot.smartcossiol.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.*
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

/**
 * A Measures.
 */
@Entity
@Table(name = "measures")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
data class Measures(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @get: NotNull
    @Column(name = "temp", nullable = false)
    var temp: Int? = null,

    @get: NotNull
    @Column(name = "soil", nullable = false)
    var soil: Int? = null,

    @get: NotNull
    @Column(name = "light", nullable = false)
    var light: Int? = null,

    @get: NotNull
    @Column(name = "insert_at", nullable = false)
    var insertAt: Instant? = null,

    @ManyToOne @JsonIgnoreProperties("measures")
    var devices: Devices? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Measures) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Measures{" +
        "id=$id" +
        ", temp=$temp" +
        ", soil=$soil" +
        ", light=$light" +
        ", insertAt='$insertAt'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}

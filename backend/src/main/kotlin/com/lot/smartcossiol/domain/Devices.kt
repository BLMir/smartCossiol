package com.lot.smartcossiol.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.lot.smartcossiol.domain.enumeration.Type
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.*
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

/**
 * A Devices.
 */
@Entity
@Table(name = "devices")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
data class Devices(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @get: NotNull
    @Column(name = "title", nullable = false)
    var title: String? = null,

    @get: NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    var type: Type? = null,

    @Column(name = "identification")
    var identification: String? = null,

    @Column(name = "active")
    var active: Boolean? = null,

    @ManyToOne @JsonIgnoreProperties("devices")
    var user: User? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Devices) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Devices{" +
        "id=$id" +
        ", title='$title'" +
        ", type='$type'" +
        ", identification='$identification'" +
        ", active='$active'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}

package com.lot.smartcossiol.repository

import com.lot.smartcossiol.domain.Devices
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Devices] entity.
 */
@Suppress("unused")
@Repository
interface DevicesRepository : JpaRepository<Devices, Long> {

    @Query("select devices from Devices devices where devices.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<Devices>
}

package com.lot.smartcossiol.repository

import com.lot.smartcossiol.domain.Measures
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Measures] entity.
 */
@Suppress("unused")
@Repository
interface MeasuresRepository : JpaRepository<Measures, Long>

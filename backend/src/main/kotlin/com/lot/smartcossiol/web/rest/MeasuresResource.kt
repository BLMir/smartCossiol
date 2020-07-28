package com.lot.smartcossiol.web.rest

import com.lot.smartcossiol.domain.Measures
import com.lot.smartcossiol.repository.MeasuresRepository
import com.lot.smartcossiol.web.rest.errors.BadRequestAlertException
import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
import javax.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private const val ENTITY_NAME = "measures"
/**
 * REST controller for managing [com.lot.smartcossiol.domain.Measures].
 */
@RestController
@RequestMapping("/api")
@Transactional
class MeasuresResource(
    private val measuresRepository: MeasuresRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /measures` : Create a new measures.
     *
     * @param measures the measures to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new measures, or with status `400 (Bad Request)` if the measures has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/measures")
    fun createMeasures(@Valid @RequestBody measures: Measures): ResponseEntity<Measures> {
        log.debug("REST request to save Measures : {}", measures)
        if (measures.id != null) {
            throw BadRequestAlertException(
                "A new measures cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = measuresRepository.save(measures)
        return ResponseEntity.created(URI("/api/measures/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /measures` : Updates an existing measures.
     *
     * @param measures the measures to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated measures,
     * or with status `400 (Bad Request)` if the measures is not valid,
     * or with status `500 (Internal Server Error)` if the measures couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/measures")
    fun updateMeasures(@Valid @RequestBody measures: Measures): ResponseEntity<Measures> {
        log.debug("REST request to update Measures : {}", measures)
        if (measures.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = measuresRepository.save(measures)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     measures.id.toString()
                )
            )
            .body(result)
    }
    /**
     * `GET  /measures` : get all the measures.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of measures in body.
     */
    @GetMapping("/measures")
    fun getAllMeasures(): MutableList<Measures> {
        log.debug("REST request to get all Measures")
        return measuresRepository.findAll()
    }

    /**
     * `GET  /measures/:id` : get the "id" measures.
     *
     * @param id the id of the measures to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the measures, or with status `404 (Not Found)`.
     */
    @GetMapping("/measures/{id}")
    fun getMeasures(@PathVariable id: Long): ResponseEntity<Measures> {
        log.debug("REST request to get Measures : {}", id)
        val measures = measuresRepository.findById(id)
        return ResponseUtil.wrapOrNotFound(measures)
    }
    /**
     *  `DELETE  /measures/:id` : delete the "id" measures.
     *
     * @param id the id of the measures to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/measures/{id}")
    fun deleteMeasures(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Measures : {}", id)

        measuresRepository.deleteById(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}

package com.lot.smartcossiol.web.rest

import com.lot.smartcossiol.domain.Devices
import com.lot.smartcossiol.repository.DevicesRepository
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

private const val ENTITY_NAME = "devices"
/**
 * REST controller for managing [com.lot.smartcossiol.domain.Devices].
 */
@RestController
@RequestMapping("/api")
@Transactional
class DevicesResource(
    private val devicesRepository: DevicesRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /devices` : Create a new devices.
     *
     * @param devices the devices to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new devices, or with status `400 (Bad Request)` if the devices has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/devices")
    fun createDevices(@Valid @RequestBody devices: Devices): ResponseEntity<Devices> {
        log.debug("REST request to save Devices : {}", devices)
        if (devices.id != null) {
            throw BadRequestAlertException(
                "A new devices cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = devicesRepository.save(devices)
        return ResponseEntity.created(URI("/api/devices/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /devices` : Updates an existing devices.
     *
     * @param devices the devices to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated devices,
     * or with status `400 (Bad Request)` if the devices is not valid,
     * or with status `500 (Internal Server Error)` if the devices couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/devices")
    fun updateDevices(@Valid @RequestBody devices: Devices): ResponseEntity<Devices> {
        log.debug("REST request to update Devices : {}", devices)
        if (devices.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = devicesRepository.save(devices)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     devices.id.toString()
                )
            )
            .body(result)
    }
    /**
     * `GET  /devices` : get all the devices.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of devices in body.
     */
    @GetMapping("/devices")
    fun getAllDevices(): MutableList<Devices> {
        log.debug("REST request to get all Devices")
        return devicesRepository.findAll()
    }

    /**
     * `GET  /devices/:id` : get the "id" devices.
     *
     * @param id the id of the devices to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the devices, or with status `404 (Not Found)`.
     */
    @GetMapping("/devices/{id}")
    fun getDevices(@PathVariable id: Long): ResponseEntity<Devices> {
        log.debug("REST request to get Devices : {}", id)
        val devices = devicesRepository.findById(id)
        return ResponseUtil.wrapOrNotFound(devices)
    }
    /**
     *  `DELETE  /devices/:id` : delete the "id" devices.
     *
     * @param id the id of the devices to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/devices/{id}")
    fun deleteDevices(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Devices : {}", id)

        devicesRepository.deleteById(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}

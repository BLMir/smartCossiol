package com.lot.smartcossiol.web.rest

import com.lot.smartcossiol.BackendApp
import com.lot.smartcossiol.domain.Devices
import com.lot.smartcossiol.domain.enumeration.Type
import com.lot.smartcossiol.repository.DevicesRepository
import com.lot.smartcossiol.web.rest.errors.ExceptionTranslator
import javax.persistence.EntityManager
import kotlin.test.assertNotNull
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator

/**
 * Integration tests for the [DevicesResource] REST controller.
 *
 * @see DevicesResource
 */
@SpringBootTest(classes = [BackendApp::class])
class DevicesResourceIT {

    @Autowired
    private lateinit var devicesRepository: DevicesRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restDevicesMockMvc: MockMvc

    private lateinit var devices: Devices

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val devicesResource = DevicesResource(devicesRepository)
        this.restDevicesMockMvc = MockMvcBuilders.standaloneSetup(devicesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        devices = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createDevices() {
        val databaseSizeBeforeCreate = devicesRepository.findAll().size

        // Create the Devices
        restDevicesMockMvc.perform(
            post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(devices))
        ).andExpect(status().isCreated)

        // Validate the Devices in the database
        val devicesList = devicesRepository.findAll()
        assertThat(devicesList).hasSize(databaseSizeBeforeCreate + 1)
        val testDevices = devicesList[devicesList.size - 1]
        assertThat(testDevices.title).isEqualTo(DEFAULT_TITLE)
        assertThat(testDevices.type).isEqualTo(DEFAULT_TYPE)
        assertThat(testDevices.identification).isEqualTo(DEFAULT_IDENTIFICATION)
        assertThat(testDevices.active).isEqualTo(DEFAULT_ACTIVE)
    }

    @Test
    @Transactional
    fun createDevicesWithExistingId() {
        val databaseSizeBeforeCreate = devicesRepository.findAll().size

        // Create the Devices with an existing ID
        devices.id = 1L

        // An entity with an existing ID cannot be created, so this API call must fail
        restDevicesMockMvc.perform(
            post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(devices))
        ).andExpect(status().isBadRequest)

        // Validate the Devices in the database
        val devicesList = devicesRepository.findAll()
        assertThat(devicesList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    fun checkTitleIsRequired() {
        val databaseSizeBeforeTest = devicesRepository.findAll().size
        // set the field null
        devices.title = null

        // Create the Devices, which fails.

        restDevicesMockMvc.perform(
            post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(devices))
        ).andExpect(status().isBadRequest)

        val devicesList = devicesRepository.findAll()
        assertThat(devicesList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkTypeIsRequired() {
        val databaseSizeBeforeTest = devicesRepository.findAll().size
        // set the field null
        devices.type = null

        // Create the Devices, which fails.

        restDevicesMockMvc.perform(
            post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(devices))
        ).andExpect(status().isBadRequest)

        val devicesList = devicesRepository.findAll()
        assertThat(devicesList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllDevices() {
        // Initialize the database
        devicesRepository.saveAndFlush(devices)

        // Get all the devicesList
        restDevicesMockMvc.perform(get("/api/devices?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(devices.id?.toInt())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].identification").value(hasItem(DEFAULT_IDENTIFICATION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
    }

    @Test
    @Transactional
    fun getDevices() {
        // Initialize the database
        devicesRepository.saveAndFlush(devices)

        val id = devices.id
        assertNotNull(id)

        // Get the devices
        restDevicesMockMvc.perform(get("/api/devices/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.identification").value(DEFAULT_IDENTIFICATION))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE))
    }

    @Test
    @Transactional
    fun getNonExistingDevices() {
        // Get the devices
        restDevicesMockMvc.perform(get("/api/devices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun updateDevices() {
        // Initialize the database
        devicesRepository.saveAndFlush(devices)

        val databaseSizeBeforeUpdate = devicesRepository.findAll().size

        // Update the devices
        val id = devices.id
        assertNotNull(id)
        val updatedDevices = devicesRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedDevices are not directly saved in db
        em.detach(updatedDevices)
        updatedDevices.title = UPDATED_TITLE
        updatedDevices.type = UPDATED_TYPE
        updatedDevices.identification = UPDATED_IDENTIFICATION
        updatedDevices.active = UPDATED_ACTIVE

        restDevicesMockMvc.perform(
            put("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedDevices))
        ).andExpect(status().isOk)

        // Validate the Devices in the database
        val devicesList = devicesRepository.findAll()
        assertThat(devicesList).hasSize(databaseSizeBeforeUpdate)
        val testDevices = devicesList[devicesList.size - 1]
        assertThat(testDevices.title).isEqualTo(UPDATED_TITLE)
        assertThat(testDevices.type).isEqualTo(UPDATED_TYPE)
        assertThat(testDevices.identification).isEqualTo(UPDATED_IDENTIFICATION)
        assertThat(testDevices.active).isEqualTo(UPDATED_ACTIVE)
    }

    @Test
    @Transactional
    fun updateNonExistingDevices() {
        val databaseSizeBeforeUpdate = devicesRepository.findAll().size

        // Create the Devices

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDevicesMockMvc.perform(
            put("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(devices))
        ).andExpect(status().isBadRequest)

        // Validate the Devices in the database
        val devicesList = devicesRepository.findAll()
        assertThat(devicesList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteDevices() {
        // Initialize the database
        devicesRepository.saveAndFlush(devices)

        val databaseSizeBeforeDelete = devicesRepository.findAll().size

        val id = devices.id
        assertNotNull(id)

        // Delete the devices
        restDevicesMockMvc.perform(
            delete("/api/devices/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val devicesList = devicesRepository.findAll()
        assertThat(devicesList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_TITLE = "AAAAAAAAAA"
        private const val UPDATED_TITLE = "BBBBBBBBBB"

        private val DEFAULT_TYPE: Type = Type.SmartCossiol
        private val UPDATED_TYPE: Type = Type.SmartCossiol

        private const val DEFAULT_IDENTIFICATION = "AAAAAAAAAA"
        private const val UPDATED_IDENTIFICATION = "BBBBBBBBBB"

        private const val DEFAULT_ACTIVE: Boolean = false
        private const val UPDATED_ACTIVE: Boolean = true

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Devices {
            val devices = Devices(
                title = DEFAULT_TITLE,
                type = DEFAULT_TYPE,
                identification = DEFAULT_IDENTIFICATION,
                active = DEFAULT_ACTIVE
            )

            return devices
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Devices {
            val devices = Devices(
                title = UPDATED_TITLE,
                type = UPDATED_TYPE,
                identification = UPDATED_IDENTIFICATION,
                active = UPDATED_ACTIVE
            )

            return devices
        }
    }
}

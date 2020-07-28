package com.lot.smartcossiol.web.rest

import com.lot.smartcossiol.BackendApp
import com.lot.smartcossiol.domain.Measures
import com.lot.smartcossiol.repository.MeasuresRepository
import com.lot.smartcossiol.web.rest.errors.ExceptionTranslator
import java.time.Instant
import java.time.temporal.ChronoUnit
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
 * Integration tests for the [MeasuresResource] REST controller.
 *
 * @see MeasuresResource
 */
@SpringBootTest(classes = [BackendApp::class])
class MeasuresResourceIT {

    @Autowired
    private lateinit var measuresRepository: MeasuresRepository

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

    private lateinit var restMeasuresMockMvc: MockMvc

    private lateinit var measures: Measures

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val measuresResource = MeasuresResource(measuresRepository)
        this.restMeasuresMockMvc = MockMvcBuilders.standaloneSetup(measuresResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        measures = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createMeasures() {
        val databaseSizeBeforeCreate = measuresRepository.findAll().size

        // Create the Measures
        restMeasuresMockMvc.perform(
            post("/api/measures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(measures))
        ).andExpect(status().isCreated)

        // Validate the Measures in the database
        val measuresList = measuresRepository.findAll()
        assertThat(measuresList).hasSize(databaseSizeBeforeCreate + 1)
        val testMeasures = measuresList[measuresList.size - 1]
        assertThat(testMeasures.temp).isEqualTo(DEFAULT_TEMP)
        assertThat(testMeasures.soil).isEqualTo(DEFAULT_SOIL)
        assertThat(testMeasures.light).isEqualTo(DEFAULT_LIGHT)
        assertThat(testMeasures.insertAt).isEqualTo(DEFAULT_INSERT_AT)
    }

    @Test
    @Transactional
    fun createMeasuresWithExistingId() {
        val databaseSizeBeforeCreate = measuresRepository.findAll().size

        // Create the Measures with an existing ID
        measures.id = 1L

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeasuresMockMvc.perform(
            post("/api/measures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(measures))
        ).andExpect(status().isBadRequest)

        // Validate the Measures in the database
        val measuresList = measuresRepository.findAll()
        assertThat(measuresList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    fun checkTempIsRequired() {
        val databaseSizeBeforeTest = measuresRepository.findAll().size
        // set the field null
        measures.temp = null

        // Create the Measures, which fails.

        restMeasuresMockMvc.perform(
            post("/api/measures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(measures))
        ).andExpect(status().isBadRequest)

        val measuresList = measuresRepository.findAll()
        assertThat(measuresList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkSoilIsRequired() {
        val databaseSizeBeforeTest = measuresRepository.findAll().size
        // set the field null
        measures.soil = null

        // Create the Measures, which fails.

        restMeasuresMockMvc.perform(
            post("/api/measures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(measures))
        ).andExpect(status().isBadRequest)

        val measuresList = measuresRepository.findAll()
        assertThat(measuresList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkLightIsRequired() {
        val databaseSizeBeforeTest = measuresRepository.findAll().size
        // set the field null
        measures.light = null

        // Create the Measures, which fails.

        restMeasuresMockMvc.perform(
            post("/api/measures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(measures))
        ).andExpect(status().isBadRequest)

        val measuresList = measuresRepository.findAll()
        assertThat(measuresList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkInsertAtIsRequired() {
        val databaseSizeBeforeTest = measuresRepository.findAll().size
        // set the field null
        measures.insertAt = null

        // Create the Measures, which fails.

        restMeasuresMockMvc.perform(
            post("/api/measures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(measures))
        ).andExpect(status().isBadRequest)

        val measuresList = measuresRepository.findAll()
        assertThat(measuresList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllMeasures() {
        // Initialize the database
        measuresRepository.saveAndFlush(measures)

        // Get all the measuresList
        restMeasuresMockMvc.perform(get("/api/measures?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measures.id?.toInt())))
            .andExpect(jsonPath("$.[*].temp").value(hasItem(DEFAULT_TEMP)))
            .andExpect(jsonPath("$.[*].soil").value(hasItem(DEFAULT_SOIL)))
            .andExpect(jsonPath("$.[*].light").value(hasItem(DEFAULT_LIGHT)))
            .andExpect(jsonPath("$.[*].insertAt").value(hasItem(DEFAULT_INSERT_AT.toString())))
    }

    @Test
    @Transactional
    fun getMeasures() {
        // Initialize the database
        measuresRepository.saveAndFlush(measures)

        val id = measures.id
        assertNotNull(id)

        // Get the measures
        restMeasuresMockMvc.perform(get("/api/measures/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.temp").value(DEFAULT_TEMP))
            .andExpect(jsonPath("$.soil").value(DEFAULT_SOIL))
            .andExpect(jsonPath("$.light").value(DEFAULT_LIGHT))
            .andExpect(jsonPath("$.insertAt").value(DEFAULT_INSERT_AT.toString()))
    }

    @Test
    @Transactional
    fun getNonExistingMeasures() {
        // Get the measures
        restMeasuresMockMvc.perform(get("/api/measures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun updateMeasures() {
        // Initialize the database
        measuresRepository.saveAndFlush(measures)

        val databaseSizeBeforeUpdate = measuresRepository.findAll().size

        // Update the measures
        val id = measures.id
        assertNotNull(id)
        val updatedMeasures = measuresRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedMeasures are not directly saved in db
        em.detach(updatedMeasures)
        updatedMeasures.temp = UPDATED_TEMP
        updatedMeasures.soil = UPDATED_SOIL
        updatedMeasures.light = UPDATED_LIGHT
        updatedMeasures.insertAt = UPDATED_INSERT_AT

        restMeasuresMockMvc.perform(
            put("/api/measures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedMeasures))
        ).andExpect(status().isOk)

        // Validate the Measures in the database
        val measuresList = measuresRepository.findAll()
        assertThat(measuresList).hasSize(databaseSizeBeforeUpdate)
        val testMeasures = measuresList[measuresList.size - 1]
        assertThat(testMeasures.temp).isEqualTo(UPDATED_TEMP)
        assertThat(testMeasures.soil).isEqualTo(UPDATED_SOIL)
        assertThat(testMeasures.light).isEqualTo(UPDATED_LIGHT)
        assertThat(testMeasures.insertAt).isEqualTo(UPDATED_INSERT_AT)
    }

    @Test
    @Transactional
    fun updateNonExistingMeasures() {
        val databaseSizeBeforeUpdate = measuresRepository.findAll().size

        // Create the Measures

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasuresMockMvc.perform(
            put("/api/measures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(measures))
        ).andExpect(status().isBadRequest)

        // Validate the Measures in the database
        val measuresList = measuresRepository.findAll()
        assertThat(measuresList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteMeasures() {
        // Initialize the database
        measuresRepository.saveAndFlush(measures)

        val databaseSizeBeforeDelete = measuresRepository.findAll().size

        val id = measures.id
        assertNotNull(id)

        // Delete the measures
        restMeasuresMockMvc.perform(
            delete("/api/measures/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val measuresList = measuresRepository.findAll()
        assertThat(measuresList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_TEMP: Int = 1
        private const val UPDATED_TEMP: Int = 2

        private const val DEFAULT_SOIL: Int = 1
        private const val UPDATED_SOIL: Int = 2

        private const val DEFAULT_LIGHT: Int = 1
        private const val UPDATED_LIGHT: Int = 2

        private val DEFAULT_INSERT_AT: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_INSERT_AT: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Measures {
            val measures = Measures(
                temp = DEFAULT_TEMP,
                soil = DEFAULT_SOIL,
                light = DEFAULT_LIGHT,
                insertAt = DEFAULT_INSERT_AT
            )

            return measures
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Measures {
            val measures = Measures(
                temp = UPDATED_TEMP,
                soil = UPDATED_SOIL,
                light = UPDATED_LIGHT,
                insertAt = UPDATED_INSERT_AT
            )

            return measures
        }
    }
}

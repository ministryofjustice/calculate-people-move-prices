package uk.gov.justice.digital.hmpps.pecs.jpc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ResourceLoader
import org.springframework.jdbc.core.JdbcTemplate
import uk.gov.justice.digital.hmpps.pecs.jpc.config.*
import uk.gov.justice.digital.hmpps.pecs.jpc.reporting.MoveModelJdbcRepository
import uk.gov.justice.digital.hmpps.pecs.jpc.reporting.ReportImporter
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.sql.DataSource

@TestConfiguration
class TestConfig {

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    @Bean
    fun moveModelSelectRepository(@Qualifier("dataSource") dataSource: DataSource): MoveModelJdbcRepository{
        return MoveModelJdbcRepository(JdbcTemplate(dataSource))
    }

    @Bean
    fun clock(): Clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())

    @Bean
    fun timeSource(): TimeSource {
        return TimeSource { LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault())) }
    }

    @Bean
    fun dataSource(): DataSource {
        return DataSourceBuilder.create().url("jdbc:h2:mem:testdb").build()
    }

    @Bean
    fun locationsResourceProvider(): Schedule34LocationsProvider {
        return Schedule34LocationsProvider { resourceLoader.getResource("classpath:/spreadsheets/locations.xlsx").inputStream }
    }

    @Bean
    fun sercoPricesResourceProvider(): SercoPricesProvider {
        return SercoPricesProvider { resourceLoader.getResource("classpath:/spreadsheets/supplier_b_prices.xlsx").inputStream }
    }

    @Bean
    fun geoameyPricesResourceProvider(): GeoameyPricesProvider {
        return GeoameyPricesProvider { resourceLoader.getResource("classpath:/spreadsheets/supplier_a_prices.xlsx").inputStream }
    }

    @Bean
    fun reportingResourceProvider(): ReportingProvider {
        return ReportingProvider { resourceLoader.getResource("classpath:/reporting/$it").file.readText() }
    }

    @Bean
    fun reportImporter() = ReportImporter(reportingResourceProvider(), timeSource())

    @Bean
    fun jpcTemplateProvider(): JPCTemplateProvider {
        return JPCTemplateProvider { resourceLoader.getResource("classpath:/spreadsheets/JPC_template.xlsx").inputStream }
    }
}
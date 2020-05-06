package name.nepavel.coffeeinbot

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.MappedSchema
import org.jooq.conf.RenderMapping
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource


@EnableTransactionManagement
@SpringBootApplication
class CoffeeInBotApplication {
    @Bean
    fun dsl(datasource: DataSource, @Value("\${spring.datasource.hikari.schema}") schemaName: String): DSLContext {
        val settings = Settings()
                .withRenderMapping(RenderMapping()
                        .withSchemata(
                                MappedSchema()
                                        .withInput("public").withOutput(schemaName)
                        )
                )
        return DSL.using(datasource, SQLDialect.POSTGRES, settings)
    }

}

fun main(args: Array<String>) {
    runApplication<CoffeeInBotApplication>(*args)
}

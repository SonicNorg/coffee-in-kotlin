package name.nepavel.coffeeinbot

import com.opentable.db.postgres.embedded.EmbeddedPostgres
import name.nepavel.coffeeinbot.Public.PUBLIC
import name.nepavel.coffeeinbot.tables.CoffeeSort.COFFEE_SORT
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CoffeeInBotApplicationTests {
	companion object {
		lateinit var embeddedPostgres : EmbeddedPostgres
		@BeforeAll
		@JvmStatic
		fun init() {
			embeddedPostgres = EmbeddedPostgres.builder()
					.setPort(5433).start()
			embeddedPostgres.postgresDatabase.connection.use {
				it.createStatement().execute("CREATE DATABASE coffee")
			}
		}
		@AfterAll
		@JvmStatic
		fun close() {
			try {
				embeddedPostgres.close()
			} catch (e: Exception) {
				//ignore
			}
		}
	}

	@Autowired
	lateinit var dsl: DSLContext


	@Test
	fun contextLoads() {
	}

	@Test
	fun insert() {
		dsl.insertInto(COFFEE_SORT, COFFEE_SORT.NAME, COFFEE_SORT.DESCRIPTION)
				.values("Коста-Рика", "Отличная вкусняшка")
				.execute()
	}

}

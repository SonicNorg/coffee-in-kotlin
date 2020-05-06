package name.nepavel.coffeeinbot

import com.opentable.db.postgres.embedded.EmbeddedPostgres
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
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


	@Test
	fun contextLoads() {
	}

}

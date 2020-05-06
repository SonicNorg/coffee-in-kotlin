package name.nepavel.coffeeinbot

import name.nepavel.coffeeinbot.tables.daos.BotUserDao
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CoffeeInBotApplication {
	val userDto: BotUserDao = BotUserDao()

}

fun main(args: Array<String>) {
	runApplication<CoffeeInBotApplication>(*args)
}

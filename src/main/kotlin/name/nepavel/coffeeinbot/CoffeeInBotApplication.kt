package name.nepavel.coffeeinbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class CoffeeInBotApplication {


}

fun main(args: Array<String>) {
	runApplication<CoffeeInBotApplication>(*args)
}

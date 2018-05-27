package ca.usherbrooke.degel

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
@SpringBootTest
class DegelApplicationTests {

	@Test
	fun contextLoads() {
	}

}

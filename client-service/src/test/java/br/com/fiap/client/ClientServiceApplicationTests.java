package br.com.fiap.client;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ClientServiceApplication.class,
        properties = {
                "spring.autoconfigure.exclude=" +
                        "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
                        "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
        })
class ClientServiceApplicationTests {
}
package works.hop.naming;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NamingStrategyFactoryTest {

    @Test
    void register() {
        NamingStrategyFactory.register(new SnakeToCamelStrategy());
        NamingStrategyFactory.register(new SnakeToPascalStrategy());
        assertThat(NamingStrategyFactory.registry).hasSize(2);
    }

    @Test
    void forEach() {
        NamingStrategyFactory.register(new SnakeToCamelStrategy());
        NamingStrategyFactory.register(new SnakeToPascalStrategy());
        String snake_case = "first_name_only";
        String pascalCase = "FirstNameOnly";
        String camelCase = "firstNameOnly";
        NamingStrategyFactory.forEach((i, strategy) -> {
            if (i == 1) {
                assertThat(strategy.resolve(snake_case)).isEqualTo(pascalCase);
            }
            if (i == 2) {
                assertThat(strategy.resolve(snake_case)).isEqualTo(camelCase);
            }
        });
    }
}
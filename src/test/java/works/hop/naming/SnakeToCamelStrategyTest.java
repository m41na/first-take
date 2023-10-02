package works.hop.naming;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SnakeToCamelStrategyTest {

    @Test
    void verify_converting_snake_case_to_camel_case() {
        String columnName = "first_name_only";
        String expected = "firstNameOnly";
        assertThat(new SnakeToCamelStrategy().resolve(columnName)).isEqualTo(expected);

        String columnName2 = "FIRST_NAME_ONLY";
        assertThat(new SnakeToCamelStrategy().resolve(columnName2)).isEqualTo(expected);
    }

    @Test
    void verify_converting_camel_case_to_snake_case() {
        String columnName = "firstNameOnly";
        String expected = "first_name_only";
        assertThat(new SnakeToCamelStrategy().inverse(columnName)).isEqualTo(expected);
    }
}
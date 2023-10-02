package works.hop.naming;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SnakeToPascalStrategy implements NamingStrategy {

    @Override
    public String resolve(String name) {
        String[] parts = name.split("_");
        return IntStream.range(0, parts.length).mapToObj(i -> new Entry<>(i, parts[i]))
                .map(e -> Character.toUpperCase(e.value.charAt(0)) + e.value.substring(1).toLowerCase())
                .collect(Collectors.joining());
    }

    @Override
    public String inverse(String name) {
        return name.replaceAll("([A-Z])", "_$1").toLowerCase().replaceFirst("_", "");
    }
}

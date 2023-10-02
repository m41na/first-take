package works.hop.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EntityNodeFactory {

    private static final Map<Class<?>, Supplier<EntityNode>> registry = new HashMap<>();

    public static Supplier<EntityNode> template(Class<?> type) {
        return registry.get(type);
    }

    public static void register(Class<?> type, Supplier<EntityNode> supplier) {
        registry.putIfAbsent(type, supplier);
    }
}

package works.hop.json;

import jakarta.json.Json;
import jakarta.json.stream.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Stack;

public class JNodeParser {

    public static void main(String... args) throws IOException {
        NodeValue<?> node = new JNodeParser().parse();
        System.out.println(node.getValue());
    }

    public NodeValue<?> parse() throws IOException {
        Stack<NodeValue<?>> pipeline = new Stack<>();
        Stack<String> keyStack = new Stack<>();
        try (InputStream is = JNodeParser.class.getClassLoader().getResourceAsStream("sample.json");
             JsonParser parser = Json.createParser(is)) {
            while (parser.hasNext()) {
                final JsonParser.Event event = parser.next();
                switch (event) {
                    case KEY_NAME:
                        keyStack.push(parser.getString());
                        System.out.println(keyStack.peek());
                        break;
                    case VALUE_STRING: {
                        NodeValue<String> value = new NodeValue<>(parser.getString());
                        System.out.println(value.getValue(String.class));
                        if (pipeline.peek().is(JArray.class)) {
                            ((JArray) pipeline.peek().getValue()).add(value);
                        }
                        if (pipeline.peek().is(JObject.class)) {
                            ((JObject) pipeline.peek().getValue()).put(keyStack.pop(), value);
                        }
                        break;
                    }
                    case VALUE_NUMBER: {
                        NodeValue<BigDecimal> value = new NodeValue<>(parser.getBigDecimal());
                        System.out.println(value.getValue(BigDecimal.class));
                        if (pipeline.peek().is(JArray.class)) {
                            ((JArray) pipeline.peek().getValue()).add(value);
                        }
                        if (pipeline.peek().is(JObject.class)) {
                            ((JObject) pipeline.peek().getValue()).put(keyStack.pop(), value);
                        }
                        break;
                    }
                    case VALUE_TRUE: {
                        NodeValue<Boolean> value = new NodeValue<>(true);
                        System.out.println(value.getValue(Boolean.class));
                        if (pipeline.peek().is(JArray.class)) {
                            ((JArray) pipeline.peek().getValue()).add(value);
                        }
                        if (pipeline.peek().is(JObject.class)) {
                            ((JObject) pipeline.peek().getValue()).put(keyStack.pop(), value);
                        }
                        break;
                    }
                    case VALUE_FALSE: {
                        NodeValue<Boolean> value = new NodeValue<>(false);
                        System.out.println(value.getValue());
                        if (pipeline.peek().is(JArray.class)) {
                            ((JArray) pipeline.peek().getValue()).add(value);
                        }
                        if (pipeline.peek().is(JObject.class)) {
                            ((JObject) pipeline.peek().getValue()).put(keyStack.pop(), value);
                        }
                        break;
                    }
                    case START_OBJECT: {
                        System.out.println(event);
                        pipeline.push(new NodeValue<>(new JObject()));
                        break;
                    }
                    case START_ARRAY: {
                        System.out.println(event);
                        pipeline.push(new NodeValue<>(new JArray()));
                        break;
                    }
                    case END_OBJECT:
                    case END_ARRAY: {
                        NodeValue<?> value = pipeline.pop();
                        if (keyStack.isEmpty()) {
                            return value;
                        }
                        if (pipeline.peek().is(JArray.class)) {
                            ((JArray) pipeline.peek().getValue()).add(value);
                        }
                        if (pipeline.peek().is(JObject.class)) {
                            ((JObject) pipeline.peek().getValue()).put(keyStack.pop(), value);
                        }
                        System.out.println(event);
                        break;
                    }
                }
            }
            return null;
        }
    }

    public static class JObject extends LinkedHashMap<String, NodeValue<?>> {
    }

    public static class JArray extends LinkedList<NodeValue<?>> {
    }
}
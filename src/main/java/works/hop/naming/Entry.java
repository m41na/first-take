package works.hop.naming;

import lombok.Data;

@Data
public class Entry<T> {
    final int index;
    final T value;
}

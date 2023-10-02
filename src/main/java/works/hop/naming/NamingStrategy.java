package works.hop.naming;

public interface NamingStrategy {

    String resolve(String name);

    String inverse(String name);
}


package just.console.table;

@FunctionalInterface
public interface Function<T, R> {

    R apply(T t);

}

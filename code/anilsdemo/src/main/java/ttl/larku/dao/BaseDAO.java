package ttl.larku.dao;

import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * @param <T>
 * @author anil
 */
public interface BaseDAO<T> {

    public boolean update(T updateObject);

    public boolean delete(T deleteObject);

    public T create(T newObject);

    public T get(int id);

    public List<T> getAll();

    public default void deleteStore() {
        throw new UnsupportedOperationException("Needs implementing");
    };

    public default void createStore() {
        throw new UnsupportedOperationException("Needs implementing");
    };

    default public List<T> findBy(Predicate<T> pred) {
        List<T> x = getAll();
        List<T> result = getAll().stream()
                .filter(pred)
                .collect(toList());
        return result;
    }
}

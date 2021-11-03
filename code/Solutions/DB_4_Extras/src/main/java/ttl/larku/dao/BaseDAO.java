package ttl.larku.dao;

import ttl.larku.dao.jpahibernate.SearchWrapper;
import ttl.larku.domain.Track;

import java.util.List;

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
        throw new UnsupportedOperationException("Needs Implementing");
    }

    public default void createStore() {
        throw new UnsupportedOperationException("Needs Implementing");
    }

    public List<T> getByExample(T example);
    public List<T> getTracksByTitle(String title);

    public default List<Track> getByFlexiSearch(SearchWrapper searchWrapper) {
        throw new UnsupportedOperationException("Needs Implementing");
    }
}

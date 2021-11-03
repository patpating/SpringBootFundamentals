package ttl.larku.dao.jpahibernate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author whynot
 */
public class SearchWrapper {
    public final List<SearchSpec<?>> searchSpecs = new ArrayList<>();
    public final SearchSpec.Operation operation;

    public SearchWrapper(SearchSpec.Operation operation) {
        this.operation = operation;
    }

    public SearchWrapper(SearchSpec.Operation operation, SearchSpec<?> ... specs) {
        for(SearchSpec<?> spec : specs) {
            searchSpecs.add(spec);
        }
        this.operation = operation;
    }

    public static SearchWrapper or(SearchSpec<?> ... specs) {
       return new SearchWrapper(SearchSpec.Operation.Or, specs);
    }

    public static SearchWrapper and(SearchSpec<?> ... specs) {
        return new SearchWrapper(SearchSpec.Operation.And, specs);
    }

    public void add(SearchSpec<?> searchSpec) {
        searchSpecs.add(searchSpec);
    }


    public void clear() {
        searchSpecs.clear();
    }

    @Override
    public String toString() {
        String result = "SearchWrapper: operation=" + operation + "\n";
        for(SearchSpec<?> spec : searchSpecs) {
            result  += "\t" + spec + "\n";
        }
        return result;
    }
}

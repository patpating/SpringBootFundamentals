package ttl.larku.dao.jpahibernate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author whynot
 */
public class SearchSpec<T> {

    public enum Operation {
        And,
        Or
    }

    public enum SearchType {
        Equal(false) {
            @Override
            public <T> Predicate makeIt(CriteriaBuilder cb, Root<T> root, String propName, Object value) {
                return cb.equal(root.get(propName), value);
            }
        },
        NotEqual(false) {
            @Override
            public <T> Predicate makeIt(CriteriaBuilder cb, Root<T> root, String propName, Object value) {
                return cb.notEqual(root.get(propName), value);
            }
        },
        Greater(true) {
            @SuppressWarnings({"unchecked", "rawtypes"})
            public <T> Predicate makeIt(CriteriaBuilder cb, Root<T> root, String propName, Object value) {
                return cb.greaterThan(root.get(propName), (Comparable) value);
            }
        },
        GreaterEqual(true) {
            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            public <T> Predicate makeIt(CriteriaBuilder cb, Root<T> root, String propName, Object value) {
                return cb.greaterThanOrEqualTo(root.get(propName), (Comparable) value);
            }
        },
        Less(true){
            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            public <T> Predicate makeIt(CriteriaBuilder cb, Root<T> root, String propName, Object value) {
                return cb.lessThan(root.get(propName), (Comparable) value);
            }
        },
        LessEqual(true){
            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            public <T> Predicate makeIt(CriteriaBuilder cb, Root<T> root, String propName, Object value) {
                return cb.lessThanOrEqualTo(root.get(propName), (Comparable) value);
            }
        },
        ContainsString(false) {
            @Override
            public <T> Predicate makeIt(CriteriaBuilder cb, Root<T> root, String propName, Object value) {
                return cb.like(root.get(propName), "%" + value.toString() + "%");
            }
        },
        ContainsStringIC(false) {
            @Override
            public <T> Predicate makeIt(CriteriaBuilder cb, Root<T> root, String propName, Object value) {
                return cb.like(cb.lower(root.get(propName)), "%" + value.toString().toLowerCase() + "%");
            }
        };

        public boolean needsComparable;
        SearchType(boolean needsComparable) {
            this.needsComparable = needsComparable;
        }
        public abstract <T> Predicate makeIt(CriteriaBuilder cb, Root<T> root, String propName, Object value);

    }

    public final SearchType searchType;
    public final T value;
    public final String propName;


    public SearchSpec(SearchType searchType, String propName, T value) {
        this.searchType = searchType;
        this.propName = propName;
        this.value = value;
    }

    public <X> Predicate makePredicate(CriteriaBuilder cb, Root<X> root) {
        if (searchType.needsComparable && !(value instanceof Comparable)) {
            throw new RuntimeException(searchType + " can only be applied to Comparables");
        }
        return searchType.makeIt(cb, root, propName, value);
    }

    @Override
    public String toString() {
        return "SearchSpec{" +
                "searchType=" + searchType +
                ", propName='" + propName + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

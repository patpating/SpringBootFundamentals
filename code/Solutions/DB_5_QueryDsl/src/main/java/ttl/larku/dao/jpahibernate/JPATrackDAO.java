package ttl.larku.dao.jpahibernate;

import org.springframework.transaction.annotation.Transactional;
import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Track;
import ttl.larku.domain.Track_;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Transactional
public class JPATrackDAO implements BaseDAO<Track> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean update(Track updateObject) {
        em.merge(updateObject);
        return true;
    }

    @Override
    public boolean delete(Track deleteObject) {
        em.remove(deleteObject);
        return true;
    }

    @Override
    public Track create(Track newObject) {
        em.persist(newObject);
        return newObject;
    }

    @Override
    public Track get(int id) {
        return em.find(Track.class, id);
    }

    @Override
    public List<Track> getAll() {
        TypedQuery<Track> query = em.createQuery("Select t from Track t", Track.class);
        List<Track> tracks = query.getResultList();
        return tracks;
    }

    @Override
    public void deleteStore() {
        Query query = em.createQuery("Delete from Track");
        query.executeUpdate();
    }

    @Override
    public void createStore() {
    }

    @Override
    public List<Track> getTracksByTitle(String title) {
        TypedQuery<Track> query = em.createNamedQuery("Track.getByTitle", Track.class);
        List<Track> result = query.getResultList();
        return result;
    }

    /**
     * An attempt to make a general purpose Search Mechanism.  Takes a SearchWrapper which
     * contains a List of SearchSpecs and an operation which specifies how to combine the Specs.
     * SearchSpec encapsulates a SearchType (equals, LessThan, GreaterEqual etc.), the property
     * name that we are searching, and the value we are looking for.  The main job of this
     * function is to create the list of javax.persistence.criteria.Predicate that define the
     * search.  Most of the work is actually done in the SearchSpec class and the SearchType enum.
     * <p>
     * This is very much like the Spring Specification mechanism.  The "Specifications" are
     * built in the SearchSpec.
     *
     * @param searchWrapper The Container for the list of SearchSpecs and the operation for the search.
     * @return A (possibly empty) List of Tracks
     */
    public List<Track> getByFlexiSearch(SearchWrapper searchWrapper) {
        //Get the builder
        CriteriaBuilder builder = em.getCriteriaBuilder();
        //Create a query that will return Tracks
        CriteriaQuery<Track> cq = builder.createQuery(Track.class);

        //Tracks is also going to be the (only) root entity we will
        //be searching from.  This need not always be the same as the
        //type returned from the query.  This is the 'From' clause.
        Root<Track> queryRoot = cq.from(Track.class);

        //We are going to be selecting Tracks.
        cq.select(queryRoot).distinct(true);

        //Now we go and make a list of Predicates for the where clause.
        List<javax.persistence.criteria.Predicate> preds = new ArrayList<>();
        for (SearchSpec<?> searchSpec : searchWrapper.searchSpecs) {
            //All the work is being done in the SearchSpec class.
            preds.add(searchSpec.makePredicate(builder, queryRoot));
        }

        Predicate finalPredicate;
        //Combine the predicates based on the operation
        if (searchWrapper.operation == SearchSpec.Operation.Or) {
            finalPredicate = builder.or(preds.toArray(new javax.persistence.criteria.Predicate[0]));
        } else {
            finalPredicate = builder.and(preds.toArray(new javax.persistence.criteria.Predicate[0]));
        }

        //And set the result as the where clause of our query.
        cq.where(finalPredicate);

        List<Track> result = em.createQuery(cq).getResultList();

        return result;
    }


    /**
     * Use the Criteria API to create our query.  Code is
     * almost as ugly as building a query String.
     * Here we are using the JPA Metamodel.  It gets generated
     * by the Hibernate JPA model generator.  See pom.xml for
     * configuration.  Output is Track_.java in target/generated-sources.
     * <p>
     * We can use it here to give us type safety when accessing properties
     * of the Entity.
     *
     * @param example
     * @return
     */
    @Override
    public List<Track> getByExample(Track example) {

        //Get the builder
        CriteriaBuilder builder = em.getCriteriaBuilder();
        //Create a query that will return Tracks
        CriteriaQuery<Track> cq = builder.createQuery(Track.class);

        //Tracks is also going to be the (only) root entity we will
        //be searching from.  This need not always be the same as the
        //type returned from the query.  This is the 'From' clause.
        Root<Track> queryRoot = cq.from(Track.class);

        //We are going to be selecting Tracks.
        cq.select(queryRoot).distinct(true);

        //Build up a List of javax.persistence.criteria.Predicate objects,
        //based on what is not null in the example Student.
        List<javax.persistence.criteria.Predicate> preds = new ArrayList<>();
        if (example.getTitle() != null) {
            //we are doing a 'like' comparison, with the lower case entity title to lower case example title.
            //And using the Metamodel to refer to the properties for compile time type safety.
            //preds.add(builder.like(builder.lower(queryRoot.get("id")), "%" + example.getTitle().toLowerCase() + "%"));
            preds.add(builder.like(builder.lower(queryRoot.get(Track_.title)), "%" + example.getTitle().toLowerCase() + "%"));
        }
        if (example.getArtist() != null) {
            preds.add(builder.like(builder.lower(queryRoot.get(Track_.artist)), "%" + example.getArtist().toLowerCase() + "%"));
        }
        if (example.getAlbum() != null) {
            preds.add(builder.like(builder.lower(queryRoot.get(Track_.album)), "%" + example.getAlbum().toLowerCase() + "%"));
        }

        //Now 'or' them together.
        Predicate finalPred = builder.or(preds.toArray(new javax.persistence.criteria.Predicate[0]));

        //And set them as the where clause of our query.
        cq.where(finalPred);

        List<Track> result = em.createQuery(cq).getResultList();

        return result;
    }

    /**
     * Use the Criteria API to create our query.  Code is
     * almost as ugly as building a query String.  And only
     * half type safe here because we are not using a meta-model.
     *
     * @param example
     * @return
     */
    public List<Track> getByExampleButWithoutMetaModel(Track example) {

        //Get the builder
        CriteriaBuilder builder = em.getCriteriaBuilder();
        //Create a query that will return Tracks
        CriteriaQuery<Track> cq = builder.createQuery(Track.class);

        //Tracks is also going to be the (only) root entity we will
        //be searching from.  This need not always be the same as the
        //type returned from the query.  This is the 'From' clause.
        Root<Track> queryRoot = cq.from(Track.class);

        //We are going to be selecting Tracks.
        cq.select(queryRoot).distinct(true);

        //Build up a List of javax.persistence.criteria.Predicate objects,
        //based on what is not null in the example Student.
        List<javax.persistence.criteria.Predicate> preds = new ArrayList<>();
        if (example.getTitle() != null) {
            //we are doing a 'like' comparison, with the lower case entity title to lower case example title.
            //But purposely asking for "id" instead of title.  This will throw exception at query creation at run time.
            //MetaModel variation above will catch this error at compile time.
            preds.add(builder.like(builder.lower(queryRoot.get("id")), "%" + example.getTitle().toLowerCase() + "%"));
        }
        if (example.getArtist() != null) {
            preds.add(builder.like(builder.lower(queryRoot.get("artist")), "%" + example.getArtist().toLowerCase() + "%"));
        }
        if (example.getAlbum() != null) {
            preds.add(builder.like(builder.lower(queryRoot.get("album")), "%" + example.getAlbum().toLowerCase() + "%"));
        }

        //Now 'or' them together.
        Predicate finalPred = builder.or(preds.toArray(new javax.persistence.criteria.Predicate[0]));

        //And set them as the where clause of our query.
        cq.where(finalPred);

        List<Track> result = em.createQuery(cq).getResultList();

        return result;
    }

    /**
     * Clumsy way of doing this, by creating a query String on the fly.
     * See this class in DB_4_Extras for an example of using the Criteria
     * query.  Which is still ugly, but probably better.
     *
     * @param example
     * @return
     */
    public List<Track> getByExampleClumsy(Track example) {
        String rootQuery = "select t from Track t where ";
        StringBuilder builder = new StringBuilder(rootQuery);
        StringBuilder qb = new StringBuilder();
        if (example.getTitle() != null) {
            //we are doing a 'like' comparison, with the lower case entity title to lower case example title.
            qb.append(qb.length() == 0 ? " " : " or ");
            qb.append(" LOWER(t.title) like '%").append(example.getTitle().toLowerCase()).append("%'");
        }
        if (example.getArtist() != null) {
            qb.append(qb.length() == 0 ? " " : " or ");
            qb.append(" LOWER(t.artist) like '%").append(example.getArtist().toLowerCase()).append("%'");
        }
        if (example.getAlbum() != null) {
            qb.append(qb.length() == 0 ? " " : " or ");
            qb.append(" LOWER(t.album) like '%").append(example.getAlbum().toLowerCase()).append("%'");
        }
        String finalQuery = builder.append(qb).toString();
        TypedQuery<Track> query = em.createQuery(finalQuery, Track.class);
        List<Track> result = query.getResultList();

        return result;
    }

}

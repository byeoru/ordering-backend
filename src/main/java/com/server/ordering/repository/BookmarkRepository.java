package com.server.ordering.repository;

import com.server.ordering.domain.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookmarkRepository {

    private final EntityManager em;

    public void save(Bookmark bookmark) {
        em.persist(bookmark);
    }

    public void remove(Long bookmarkId) {
        Bookmark bookmark = em.find(Bookmark.class, bookmarkId);
        em.remove(bookmark);
    }

    public void findOneByCustomerIdAndRestaurantId(Long customerId, Long restaurantId) throws PersistenceException {
        em.createQuery("select m from Bookmark m where m.customer.id =:customerId and m.restaurant.id =:restaurantId", Bookmark.class)
                .setParameter("customerId", customerId)
                .setParameter("restaurantId", restaurantId)
                .getSingleResult();
    }

    public List<Bookmark> findAllWithRestWithRepresentativeByCustomerId(Long customerId) {
        return em.createQuery("select distinct m from Bookmark m" +
                        " left join fetch m.restaurant r" +
                        " left join fetch r.representativeMenus where m.customer.id =:customerId", Bookmark.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }
}

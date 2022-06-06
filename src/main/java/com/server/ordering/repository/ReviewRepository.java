package com.server.ordering.repository;

import com.server.ordering.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {

    private final EntityManager em;

    public void save(Review review) {
        em.persist(review);
    }

    public Review findOne(Long reviewId) {
        return em.find(Review.class, reviewId);
    }

    public List<Review> findAllWithOrderWithCustomerByRestaurantId(Long restaurantId) {
        return em.createQuery("select distinct m from Review m" +
                        " left join fetch m.order o" +
                        " left join fetch o.customer where m.restaurant.id =:restaurantId order by m.id desc ", Review.class)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
    }

    public void remove(Review review) {
        em.remove(review);
    }
}

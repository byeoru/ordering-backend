package com.server.ordering.repository;

import com.server.ordering.domain.RepresentativeMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RepresentativeMenuRepository {

    private final EntityManager em;

    public void save(RepresentativeMenu representativeMenu) {
        em.persist(representativeMenu);
    }

    public void remove(Long id) {
        RepresentativeMenu representativeMenu = em.find(RepresentativeMenu.class, id);
        em.remove(representativeMenu);
    }

    public RepresentativeMenu findOneByRestaurantIdAndFoodId(Long restaurantId, Long foodId) throws PersistenceException {
        return em.createQuery("select m from RepresentativeMenu m where m.food.id =:foodId and m.restaurant.id =: restaurantId", RepresentativeMenu.class)
                .setParameter("foodId", foodId)
                .setParameter("restaurantId", restaurantId)
                .getSingleResult();
    }

    public List<RepresentativeMenu> findAllByRestaurantId(Long restaurantId) {
        return em.createQuery("select distinct m from RepresentativeMenu m where m.restaurant.id =:restaurantId", RepresentativeMenu.class)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
    }
}

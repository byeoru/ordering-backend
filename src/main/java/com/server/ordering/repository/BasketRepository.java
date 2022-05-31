package com.server.ordering.repository;

import com.server.ordering.domain.Basket;
import com.server.ordering.domain.dto.request.BasketPutDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BasketRepository {

    private final EntityManager em;
    private final JdbcTemplate jdbcTemplate;

    public void save(Basket basket) {
        em.persist(basket);
    }

    public List<Basket> findAllWithFoodByCustomerId(Long customerId) {
        return em.createQuery("select distinct m from Basket m left join fetch m.food where m.customer.id =:customerId", Basket.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }

    public Basket findOneByCustomerIdAndFoodId(Long customerId, Long foodId) throws PersistenceException {
        return em.createQuery("select m from Basket m where m.customer.id =:customerId and m.food.id =:foodId", Basket.class)
                .setParameter("customerId", customerId)
                .setParameter("foodId", foodId)
                .getSingleResult();
    }

    public void putCount(Long customerId, List<BasketPutDto> basketPutDtos) {

        // Query builder
        StringBuilder caseQueryBuilder = new StringBuilder(30 * basketPutDtos.size());

        // list 요소 수만큼 쿼리를 추가
        basketPutDtos.forEach(dto ->
                caseQueryBuilder.append(String.format(" when %d then %d", dto.getBasketId(), dto.getCount())));

        caseQueryBuilder.append(" else m.count end");

        // 한 번의 Query로 여러 음식의 count를 UPDATE
        em.createQuery("update Basket m set m.count = case m.id"
                        + caseQueryBuilder
                        + " where m.customer.id =:customerId")
                .setParameter("customerId", customerId)
                .executeUpdate();
    }

    public void remove(Long basketId) {
        Basket basket = em.find(Basket.class, basketId);
        em.remove(basket);
    }

    public void removeAll(Long customerId) {
        em.createQuery("delete from Basket m where m.customer.id =:id")
                .setParameter("id", customerId)
                .executeUpdate();
    }

    public int getBasketCount(Long customerId) {
        Map<String, Object> result = jdbcTemplate.queryForMap("select ifnull(sum(count), 0) as basket_count from basket where customer_id=?;", customerId);
        return Integer.parseInt(String.valueOf(result.get("basket_count")));
    }
}

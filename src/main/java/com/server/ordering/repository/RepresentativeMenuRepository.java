package com.server.ordering.repository;

import com.server.ordering.domain.RepresentativeMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class RepresentativeMenuRepository {

    private final EntityManager em;

    public void save(RepresentativeMenu representativeMenu) {
        em.persist(representativeMenu);
    }

    public void remove(String id) {
        RepresentativeMenu representativeMenu = em.find(RepresentativeMenu.class, id);
        em.remove(representativeMenu);
    }
}

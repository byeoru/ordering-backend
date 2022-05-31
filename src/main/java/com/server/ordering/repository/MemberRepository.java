package com.server.ordering.repository;

import com.server.ordering.domain.member.MemberBase;

public interface MemberRepository<T extends MemberBase> {

    void save(T member);
    T findOne(Long id);
    T findById(String email);
    void remove(Long id);
}

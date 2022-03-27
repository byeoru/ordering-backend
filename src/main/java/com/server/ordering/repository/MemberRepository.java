package com.server.ordering.repository;

import com.server.ordering.domain.member.MemberBase;

public interface MemberRepository<T extends MemberBase> {

    void save(T member);
    T findOne(Long id);
    T findById(String email);
    T findByIdAndPassword(String email, String password);
    void remove(Long id);
}

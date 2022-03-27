package com.server.ordering.service;

import com.server.ordering.domain.member.MemberBase;

import java.util.Optional;

public interface MemberService<T extends MemberBase> {

    Optional<Long> signUp(T customer);
    Optional<T> signIn(String email, String password);
    boolean isIdDuplicated(String email);
    void deleteAccount(Long id);
}

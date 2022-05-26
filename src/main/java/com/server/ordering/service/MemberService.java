package com.server.ordering.service;

import com.server.ordering.domain.dto.request.SignInDto;
import com.server.ordering.domain.member.MemberBase;

import java.util.Optional;

public interface MemberService<T extends MemberBase> {

    Optional<Long> signUp(T customer);
    Optional<T> signIn(SignInDto signInDto);
    boolean isIdDuplicated(String signInId);
    void deleteAccount(Long id);
}

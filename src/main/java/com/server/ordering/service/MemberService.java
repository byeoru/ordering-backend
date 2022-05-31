package com.server.ordering.service;

import com.server.ordering.domain.dto.request.SignInDto;
import com.server.ordering.domain.member.MemberBase;

import java.util.Optional;

public interface MemberService<T extends MemberBase> {

    Long signUp(Object signUpDto);
    T signIn(SignInDto signInDto);
    void signOut(Long id);
    boolean isIdDuplicated(String signInId);
    void deleteAccount(Long id);
}

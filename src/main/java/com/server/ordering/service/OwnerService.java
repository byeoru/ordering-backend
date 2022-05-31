package com.server.ordering.service;

import com.server.ordering.domain.MemberType;
import com.server.ordering.domain.PhoneNumber;
import com.server.ordering.domain.dto.request.OwnerSignUpDto;
import com.server.ordering.domain.dto.request.PasswordChangeDto;
import com.server.ordering.domain.dto.request.SignInDto;
import com.server.ordering.domain.member.Owner;
import com.server.ordering.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OwnerService implements MemberService<Owner> {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 점주 회원가입
     */
    @Transactional
    @Override
    public Long signUp(Object signUpDto) throws PersistenceException {
        OwnerSignUpDto ownerSignUpDto = (OwnerSignUpDto) signUpDto;
        PhoneNumber phoneNumber = new PhoneNumber(ownerSignUpDto.getPhoneNumber(), MemberType.OWNER);
        String encodedPassword = passwordEncoder.encode(ownerSignUpDto.getPassword());
        Owner owner = new Owner(ownerSignUpDto.getSignInId(), encodedPassword, phoneNumber);
        ownerRepository.save(owner);
        return owner.getId();
    }

    /**
     * 점주 로그인
     */
    @Transactional
    @Override
    public Owner signIn(SignInDto signInDto) throws PersistenceException {
        try {
            Owner owner = ownerRepository.findOneWithRestaurantById(signInDto.getSignInId());
            boolean bMatch = passwordEncoder.matches(signInDto.getPassword(), owner.getPassword());

            if (!bMatch) {
                return null;
            }

            owner.getRestaurant().putFirebaseToken(signInDto.getFirebaseToken());
            return owner;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    @Override
    public void signOut(Long id) {
        Owner owner = ownerRepository.findOneWithRestaurant(id);
        owner.getRestaurant().clearFirebaseToken();
    }


    /**
     * 점주 회원가입할 때 email 중복 검증
     */
    @Override
    public boolean isIdDuplicated(String signInId) throws PersistenceException {
        try {
            ownerRepository.findById(signInId);
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     * 점주 휴대폰번호 변경
     */
    @Transactional
    public void putPhoneNumber(Long ownerId, String phoneNumber) {
        Owner owner = ownerRepository.findOneWithPhoneNumber(ownerId);
        owner.getPhoneNumber().putPhoneNumber(phoneNumber);
    }

    /**
     * 점주 비밀번호 변경
     */
    @Transactional
    public Boolean putPassword(Long ownerId, PasswordChangeDto dto) {
        Owner owner = ownerRepository.findOne(ownerId);
        if (passwordEncoder.matches(dto.getCurrentPassword(), owner.getPassword())) {
            String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
            owner.putPassword(encodedNewPassword);
            return true;
        }
        return false;
    }

    /**
     * 점주 회원탈퇴
     */
    @Transactional
    @Override
    public void deleteAccount(Long id) {
        ownerRepository.remove(id);
    }
}

package com.server.ordering.service;

import com.server.ordering.domain.MemberType;
import com.server.ordering.domain.PhoneNumber;
import com.server.ordering.domain.member.Owner;
import com.server.ordering.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OwnerService implements MemberService<Owner> {

    private final OwnerRepository ownerRepository;

    /**
     * 점주 회원가입
     * @return 가입한 점주 ID 반환
     */
    @Transactional
    @Override
    public Optional<Long> signUp(Owner customer) throws PersistenceException {
        ownerRepository.save(customer);
        return Optional.of(customer.getId());
    }

    /**
     * 점주 로그인
     * @return 로그인 성공 시 owner, 실패 시 null을 Optional로 반환
     */
    @Override
    public Optional<Owner> signIn(String signInId, String password) throws PersistenceException {
        try {
            Owner owner = ownerRepository.findOneWithRestaurantByIdAndPassword(signInId, password);
            return Optional.of(owner);
        } catch (NoResultException e) {
            return Optional.empty();
        }
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
        PhoneNumber number = new PhoneNumber(phoneNumber, MemberType.OWNER);
        owner.putPhoneNumber(number);
    }

    /**
     * 점주 비밀번호 변경
     */
    @Transactional
    public void putPassword(Long ownerId, String password) {
        Owner owner = ownerRepository.findOne(ownerId);
        owner.putPassword(password);
    }

    public Owner findOwner(Long id) {
        return ownerRepository.findOne(id);
    }

    /**
     * 점주 회원탈퇴
     */
    @Transactional
    @Override
    public void deleteAccount(Long id) {
        ownerRepository.remove(id);
    }

    public List<Owner> findAllOwners() {
        return ownerRepository.findAll();
    }
}

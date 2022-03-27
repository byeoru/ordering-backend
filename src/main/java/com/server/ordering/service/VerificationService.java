package com.server.ordering.service;

import com.server.ordering.Properties;
import com.server.ordering.domain.MemberType;
import com.server.ordering.domain.PhoneNumber;
import com.server.ordering.repository.PhoneNumberRepository;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VerificationService {

    private final PhoneNumberRepository phoneNumberRepository;
    private final Properties pt;

    @Transactional
    public void registerPhoneNumber(PhoneNumber phoneNumber) {
        phoneNumberRepository.save(phoneNumber);
    }

    public boolean isPhoneNumberDuplicated(MemberType memberType, String phoneNumber) throws PersistenceException {
        try {
            phoneNumberRepository.findPhoneNumber(memberType, phoneNumber);
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public void sendCode(String phoneNum) {
        Twilio.init(pt.getAccount_sid(), pt.getAuth_token());
        Verification.creator(pt.getPath_service_sid(), "+82" + phoneNum, "sms")
                .setLocale("ko")
                .create();
    }

    public boolean checkCode(String totalNum, String verificationCode) {
        Twilio.init(pt.getAccount_sid(), pt.getAuth_token());
        VerificationCheck verificationCheck = VerificationCheck.creator(
                        pt.getPath_service_sid(),
                        verificationCode)
                .setTo(totalNum).create();

        return verificationCheck.getValid();
    }
}

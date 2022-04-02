package com.server.ordering;

import com.server.ordering.domain.MemberType;
import com.server.ordering.domain.PhoneNumber;
import com.server.ordering.domain.member.Owner;
import com.server.ordering.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DbInit {

    private final OwnerService ownerService;

    @PostConstruct
    public void init() {
        Owner byeongGyu = new Owner("byeoru", "1234", new PhoneNumber("12343785", MemberType.OWNER));
        ownerService.signUp(byeongGyu);

        Owner seongGyu = new Owner("asdasd", "asdasd", new PhoneNumber("4326986", MemberType.OWNER));
        ownerService.signUp(seongGyu);

        Owner minju = new Owner("minju", "minjuworld", new PhoneNumber("57896239", MemberType.OWNER));
        ownerService.signUp(minju);
    }
}

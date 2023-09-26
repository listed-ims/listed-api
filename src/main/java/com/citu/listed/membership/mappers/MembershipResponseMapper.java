package com.citu.listed.membership.mappers;

import com.citu.listed.membership.Membership;
import com.citu.listed.membership.dtos.MembershipResponse;
import com.citu.listed.permission.Permission;
import com.citu.listed.user.mappers.UserResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MembershipResponseMapper implements Function<Membership, MembershipResponse> {

    public final UserResponseMapper userResponseMapper;

    @Override
    public MembershipResponse apply(Membership membership){
        return new MembershipResponse(
                userResponseMapper.apply(membership.getUser()),
                membership.getPermissions()
                        .stream()
                        .map(Permission::getUserPermission)
                        .collect(Collectors.toSet()),
                membership.getMembershipStatus()
        );
    }
}

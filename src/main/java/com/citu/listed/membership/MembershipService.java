package com.citu.listed.membership;

import com.citu.listed.membership.dtos.MembershipRequest;
import com.citu.listed.membership.dtos.MembershipResponse;
import com.citu.listed.membership.enums.MembershipStatus;
import com.citu.listed.permission.enums.UserPermissions;

import java.util.List;
import java.util.Set;

public interface MembershipService {
    MembershipResponse addCollaborator(String token, MembershipRequest membership);
    List<MembershipResponse> getCollaborators(
            Integer storeId,
            MembershipStatus membershipStatus,
            Integer userId,
            int pageNumber,
            int pageSize
    );
    MembershipResponse getCollaborator(Integer membershipId);
    MembershipResponse updateCollaborator(
            Integer id,
            Set<UserPermissions> permissions,
            MembershipStatus membershipStatus
    );
}

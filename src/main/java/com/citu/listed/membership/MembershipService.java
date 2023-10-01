package com.citu.listed.membership;

import com.citu.listed.membership.dtos.MembershipRequest;
import com.citu.listed.membership.dtos.MembershipResponse;
import com.citu.listed.membership.enums.MembershipStatus;

import java.util.List;

public interface MembershipService {
    MembershipResponse addCollaborator(MembershipRequest membership);
    List<MembershipResponse> getCollaborators(
            Integer storeId,
            MembershipStatus membershipStatus,
            Integer userId,
            int pageNumber,
            int pageSize
    );
    MembershipResponse getCollaborator(Integer membershipId);
}

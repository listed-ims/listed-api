package com.citu.listed.membership;

import com.citu.listed.membership.dtos.MembershipRequest;
import com.citu.listed.membership.dtos.MembershipResponse;

public interface MembershipService {
    MembershipResponse addCollaborator(Integer userId, Integer storeId, MembershipRequest membership);
}

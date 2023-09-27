package com.citu.listed.membership.dtos;

import com.citu.listed.membership.enums.MembershipStatus;
import com.citu.listed.permission.enums.UserPermissions;
import com.citu.listed.user.dtos.UserResponse;

import java.util.Set;

public record MembershipResponse (
  UserResponse user,
  Set<UserPermissions> permissions,
  MembershipStatus membershipStatus
){}

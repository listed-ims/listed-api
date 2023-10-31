package com.citu.listed.membership;

import com.citu.listed.membership.dtos.MembershipRequest;
import com.citu.listed.membership.dtos.MembershipResponse;
import com.citu.listed.membership.enums.MembershipStatus;
import com.citu.listed.permission.enums.UserPermissions;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "Collaborator")
@RestController
@CrossOrigin
@RequestMapping("/listed/api/collaborators")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PreAuthorize("@MethodSecurity.hasPermission('ADD_COLLABORATOR')")
    @PostMapping("")
    public ResponseEntity<MembershipResponse> addCollaborator(
            @RequestHeader HttpHeaders headers,
            @RequestBody @Valid MembershipRequest membership
    ) {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION).substring(7);
        return new ResponseEntity<>(membershipService.addCollaborator(token, membership), HttpStatus.CREATED);
    }

    @PreAuthorize("@MethodSecurity.hasPermission('VIEW_COLLABORATORS')")
    @GetMapping("")
    public ResponseEntity<List<MembershipResponse>> getCollaborators(
            @RequestParam Integer storeId,
            @RequestParam(defaultValue = "") MembershipStatus membershipStatus,
            @RequestParam(defaultValue = "") Integer userId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return new ResponseEntity<>(
                membershipService.getCollaborators(
                        storeId,
                        membershipStatus,
                        userId,
                        pageNumber,
                        pageSize
                ), HttpStatus.OK
        );
    }

    @PreAuthorize("@MethodSecurity.hasPermission('VIEW_COLLABORATOR_DETAILS')")
    @GetMapping("/{id}")
    public ResponseEntity<MembershipResponse> getCollaborator(@PathVariable Integer id) {
        return new ResponseEntity<>(membershipService.getCollaborator(id), HttpStatus.OK);
    }

    @PreAuthorize("@MethodSecurity.hasAnyPermission(" +
            "'UPDATE_COLLABORATOR', " +
            "'DELETE_COLLABORATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<MembershipResponse> updateCollaborator(
            @RequestHeader HttpHeaders headers,
            @PathVariable Integer id,
            @RequestBody(required = false) Set<UserPermissions> userPermissions,
            @RequestParam(defaultValue = "") MembershipStatus membershipStatus
    ) {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION).substring(7);
        return new ResponseEntity<>(
                membershipService.updateCollaborator(token, id, userPermissions, membershipStatus),
                HttpStatus.OK
        );
    }
}

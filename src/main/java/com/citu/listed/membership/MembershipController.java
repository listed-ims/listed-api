package com.citu.listed.membership;

import com.citu.listed.membership.dtos.MembershipRequest;
import com.citu.listed.membership.dtos.MembershipResponse;
import com.citu.listed.membership.enums.MembershipStatus;
import com.citu.listed.permission.enums.UserPermissions;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("")
    public ResponseEntity<MembershipResponse> addCollaborator(
            @RequestBody @Valid MembershipRequest membership
    ){
        return new ResponseEntity<>(membershipService.addCollaborator(membership), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<MembershipResponse>> getCollaborators(
            @RequestParam Integer storeId,
            @RequestParam(defaultValue = "") MembershipStatus membershipStatus,
            @RequestParam(defaultValue = "") Integer userId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
            ){
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

    @GetMapping("/{id}")
    public ResponseEntity<MembershipResponse> getCollaborator(@PathVariable Integer id){
        return new ResponseEntity<>(membershipService.getCollaborator(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembershipResponse> updateCollaborator(
            @PathVariable Integer id,
            @RequestBody(required = false) Set<UserPermissions> userPermissions,
            @RequestParam(defaultValue = "") MembershipStatus membershipStatus
    ){
        return new ResponseEntity<>(
                membershipService.updateCollaborator(id, userPermissions, membershipStatus),
                HttpStatus.OK
        );
    }
}

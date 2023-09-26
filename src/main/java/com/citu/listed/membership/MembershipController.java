package com.citu.listed.membership;

import com.citu.listed.membership.dtos.MembershipRequest;
import com.citu.listed.membership.dtos.MembershipResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Collaborator")
@RestController
@CrossOrigin
@RequestMapping("/listed/api/collaborators")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("")
    public ResponseEntity<MembershipResponse> addCollaborator(
            @RequestParam Integer userId,
            @RequestParam Integer storeId,
            @RequestBody @Valid MembershipRequest membership
    ){
        return new ResponseEntity<>(membershipService.addCollaborator(userId, storeId, membership), HttpStatus.CREATED);
    }
}

package com.citu.listed.notification;

import com.citu.listed.notification.enums.NotificationStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Notification")
@RestController
@CrossOrigin
@RequestMapping("/listed/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    @GetMapping("")
    public ResponseEntity<Object> getNotifications(
            @RequestHeader HttpHeaders headers,
            @RequestParam(defaultValue = "") NotificationStatus status,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {

        String token = headers.getFirst(HttpHeaders.AUTHORIZATION).substring(7);

        return new ResponseEntity<>(
                notificationService.getNotifications(token, status, pageNumber, pageSize),
                HttpStatus.OK);
    }


}

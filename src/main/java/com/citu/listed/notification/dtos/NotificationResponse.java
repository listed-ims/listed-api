package com.citu.listed.notification.dtos;

import com.citu.listed.notification.enums.NotificationStatus;
import com.citu.listed.notification.enums.NotificationType;
import com.citu.listed.user.dtos.UserResponse;
import java.time.LocalDateTime;

public record NotificationResponse(

        Integer id,
        UserResponse sender,
        String metaData,
        NotificationStatus status,
        NotificationType type,
        LocalDateTime dateCreated

) {
}

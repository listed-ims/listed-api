package com.citu.listed.notification.mappers;

import com.citu.listed.notification.NotificationBroadcast;
import com.citu.listed.notification.dtos.NotificationResponse;
import com.citu.listed.user.mappers.UserResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class NotificationResponseMapper implements Function<NotificationBroadcast, NotificationResponse> {

    private final UserResponseMapper userResponseMapper;

    @Override
    public NotificationResponse apply(NotificationBroadcast notificationBroadcast) {

        return new NotificationResponse(
                notificationBroadcast.getId(),
                notificationBroadcast.getNotification().getSender()==null?null:userResponseMapper.apply(notificationBroadcast.getNotification().getSender()),
                notificationBroadcast.getNotification().getMetaData(),
                notificationBroadcast.getNotificationStatus(),
                notificationBroadcast.getNotification().getNotificationType(),
                notificationBroadcast.getNotification().getDateCreated()
        );
    }
}

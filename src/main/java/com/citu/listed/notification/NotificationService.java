package com.citu.listed.notification;

import com.citu.listed.incoming.Incoming;
import com.citu.listed.membership.Membership;
import com.citu.listed.notification.dtos.NotificationResponse;
import com.citu.listed.notification.enums.NotificationStatus;
import com.citu.listed.notification.enums.NotificationType;
import com.citu.listed.product.Product;
import com.citu.listed.user.User;

import java.util.List;

public interface NotificationService {

        void addNewNotification(Membership membership, Product product, Incoming incoming, User user, NotificationType notificationType);
        List<NotificationResponse> getNotifications(String token, NotificationStatus status, int pageNumber, int pageSize);
        NotificationResponse updateNotification(Integer notificationBroadcastId);

}

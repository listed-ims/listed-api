package com.citu.listed.notification;

import com.citu.listed.membership.Membership;
import com.citu.listed.notification.enums.NotificationType;
import com.citu.listed.product.Product;
import com.citu.listed.user.User;

public interface NotificationService {

        void addNewNotification(Membership membership, Product product, User user, NotificationType notificationType);
}

package com.citu.listed.notification;

import com.citu.listed.notification.enums.NotificationStatus;
import com.citu.listed.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationBroadcastRepository extends JpaRepository<NotificationBroadcast, Integer> {

    List<NotificationBroadcast> findByReceiverAndNotificationStatusOrderByNotificationDateCreatedDesc(User receiver, NotificationStatus status, Pageable pageable);
    List<NotificationBroadcast> findByReceiverOrderByNotificationDateCreatedDesc(User receiver, Pageable pageable);


}

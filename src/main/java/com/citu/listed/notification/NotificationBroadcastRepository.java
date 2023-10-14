package com.citu.listed.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationBroadcastRepository extends JpaRepository<NotificationBroadcast, Integer> {

}

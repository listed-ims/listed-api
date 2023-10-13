package com.citu.listed.notification;


import com.citu.listed.notification.enums.NotificationStatus;
import com.citu.listed.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notification_broadcast")
@Getter
@Setter
@NoArgsConstructor
public class NotificationBroadcast {


    @Id
    @GeneratedValue
    private Integer id;

    @Column
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;

    @Builder
    public NotificationBroadcast(NotificationStatus notificationStatus,
                        User receiver, Notification notification){

        this.notificationStatus = notificationStatus;
        this.receiver = receiver;
        this.notification = notification;

    }


}

package com.citu.listed.notification;



import com.citu.listed.notification.enums.NotificationType;
import com.citu.listed.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;

    @Column(columnDefinition = "JSON")
    private String metaData;

    @Column
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column
    private LocalDateTime dateCreated;


    @Builder
    public Notification(User sender, String metaData,
                        NotificationType notificationType, LocalDateTime dateCreated){

        this.sender = sender;
        this.metaData = metaData;
        this.notificationType = notificationType;
        this.dateCreated = dateCreated;


    }
}


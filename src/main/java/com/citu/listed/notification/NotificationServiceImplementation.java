package com.citu.listed.notification;

import com.citu.listed.incoming.Incoming;
import com.citu.listed.incoming.IncomingRepository;
import com.citu.listed.membership.Membership;
import com.citu.listed.membership.enums.MembershipStatus;
import com.citu.listed.notification.dtos.NotificationResponse;
import com.citu.listed.notification.enums.NotificationStatus;
import com.citu.listed.notification.enums.NotificationType;
import com.citu.listed.notification.mappers.NotificationResponseMapper;
import com.citu.listed.permission.enums.UserPermissions;
import com.citu.listed.product.Product;
import com.citu.listed.user.User;
import com.citu.listed.user.UserRepository;
import com.citu.listed.user.config.JwtService;
import com.citu.listed.user.mappers.UserResponseMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImplementation implements NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final IncomingRepository incomingRepository;
    private final NotificationBroadcastRepository notificationBroadcastRepository;
    private final UserResponseMapper userResponseMapper;
    private final NotificationResponseMapper notificationResponseMapper;
    private final JwtService jwtService;


    @Transactional
    @Override
    public void addNewNotification(Membership membership, Product product, User user, NotificationType notificationType) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();

        try {
            if(notificationType == NotificationType.LOW_STOCK){
                objectNode.put("product", mapper.writeValueAsString(product));
                objectNode.put("quantity", incomingRepository.getTotalQuantityByProductId(product.getId()) );
                objectNode.put("store", mapper.writeValueAsString(product.getStore()));

            } else if (notificationType == NotificationType.EXPIRATION) {
                Incoming incoming = incomingRepository.findFirstByActualQuantityGreaterThanAndProductIdOrderByExpirationDateDesc(0.0, product.getId()).orElseThrow();
                objectNode.put("product", mapper.writeValueAsString(product));
                objectNode.put("quantity", product.getThreshold());
                objectNode.put("expirationDate", mapper.writeValueAsString(incoming.getExpirationDate()));
                objectNode.put("store", mapper.writeValueAsString(product.getStore()));

            } else if (notificationType == NotificationType.COLLABORATOR_REMOVAL) {
                objectNode.put("sender", mapper.writeValueAsString(userResponseMapper.apply(user)));
                objectNode.put("recipient", mapper.writeValueAsString(userResponseMapper.apply(membership.getUser())));
                objectNode.put("store", mapper.writeValueAsString(membership.getStore()));

                
            } else {
                objectNode.put("sender", mapper.writeValueAsString(userResponseMapper.apply(user)));
                objectNode.put("invitee", mapper.writeValueAsString(userResponseMapper.apply(membership.getUser())));
                objectNode.put("store", mapper.writeValueAsString(membership.getStore()));
                objectNode.put("status", mapper.writeValueAsString(membership.getMembershipStatus()));
            }

            Notification notification = notificationRepository.save(Notification.builder()
                    .sender(user)
                    .metaData(objectNode.toString())
                    .notificationType(notificationType)
                    .dateCreated(LocalDateTime.now())
                    .build());

            List<User> recipients;

            if (notificationType == NotificationType.LOW_STOCK || notificationType == NotificationType.EXPIRATION) {
                recipients = getRecipientsForProduct(product);

            } else {
                recipients = getRecipientsForMembership(membership, user, notificationType);

            }

            for (User recipient: recipients) {
            NotificationBroadcast broadcast = NotificationBroadcast.builder()
                    .notification(notification)
                    .receiver(recipient).notificationStatus(NotificationStatus.UNREAD)
                    .build();

            notificationBroadcastRepository.save(broadcast);
        }

        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
    }


    @Override
    public List<NotificationResponse> getNotifications(String token, NotificationStatus status, int pageNumber, int pageSize) {

        User receiver = userRepository.findByUsername(jwtService.extractUsername(token))
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        List<NotificationBroadcast> notifications;

        if (status == null) {
            notifications = notificationBroadcastRepository.findByReceiverOrderByNotificationDateCreatedDesc(receiver, pageable);

        } else {
            notifications = notificationBroadcastRepository.findByReceiverAndNotificationStatusOrderByNotificationDateCreatedDesc(receiver, status, pageable);
        }

        return notifications.stream()
                .map(notificationResponseMapper)
                .collect(Collectors.toList());

    }


    @Override
    public NotificationResponse updateNotification(Integer notificationBroadcastId) {
        NotificationBroadcast notificationBroadcast = notificationBroadcastRepository.findById(notificationBroadcastId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notificationBroadcast.setNotificationStatus(NotificationStatus.READ);
        notificationBroadcastRepository.save(notificationBroadcast);

        return notificationResponseMapper.apply(notificationBroadcast);
    }



    private List<User> getRecipientsForMembership(Membership membership, User user, NotificationType type) {
        List<User> recipients = new ArrayList<>();

        if (type == NotificationType.INVITE_REPLY){
            recipients.add(membership.getSender());

        } else {
            recipients.add(membership.getUser());
        }

        User owner = userRepository.findByMemberships_StoreAndMemberships_Permissions_UserPermission(membership.getStore(), UserPermissions.OWNER);

        if(!owner.getId().equals(user.getId())){
            recipients.add(owner);
        }

        return recipients;
    }
    private List<User> getRecipientsForProduct(Product product) {
        List<User> recipients = userRepository.findByMemberships_StoreAndMemberships_Permissions_UserPermissionAndMemberships_MembershipStatus(product.getStore(), UserPermissions.VIEW_PRODUCT_DETAILS, MembershipStatus.ACTIVE);
        recipients.add(userRepository.findByMemberships_StoreAndMemberships_Permissions_UserPermission(product.getStore(), UserPermissions.OWNER));

        return recipients;
    }
}

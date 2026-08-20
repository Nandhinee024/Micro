package com.socialmedia.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "privacy_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivacySetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_visibility")
    @Builder.Default
    private Visibility profileVisibility = Visibility.PUBLIC;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_visibility")
    @Builder.Default
    private Visibility postVisibility = Visibility.PUBLIC;

    @Enumerated(EnumType.STRING)
    @Column(name = "friend_list_visibility")
    @Builder.Default
    private Visibility friendListVisibility = Visibility.FRIENDS;

    @Column(name = "allow_messages_from_strangers")
    @Builder.Default
    private boolean allowMessagesFromStrangers = false;

    @Column(name = "show_online_status")
    @Builder.Default
    private boolean showOnlineStatus = true;

    @Column(name = "allow_tagging")
    @Builder.Default
    private boolean allowTagging = true;

    @Column(name = "show_in_search")
    @Builder.Default
    private boolean showInSearch = true;

    public enum Visibility {
        PUBLIC, FRIENDS, ONLY_ME
    }
}

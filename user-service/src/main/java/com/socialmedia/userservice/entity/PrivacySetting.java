package com.socialmedia.userservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "privacy_settings")
public class PrivacySetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_visibility")
    private Visibility profileVisibility = Visibility.PUBLIC;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_visibility")
    private Visibility postVisibility = Visibility.PUBLIC;

    @Enumerated(EnumType.STRING)
    @Column(name = "friend_list_visibility")
    private Visibility friendListVisibility = Visibility.FRIENDS;

    @Column(name = "allow_messages_from_strangers")
    private boolean allowMessagesFromStrangers = false;

    @Column(name = "show_online_status")
    private boolean showOnlineStatus = true;

    @Column(name = "allow_tagging")
    private boolean allowTagging = true;

    @Column(name = "show_in_search")
    private boolean showInSearch = true;

    public PrivacySetting() {}

    public PrivacySetting(Long id, User user, Visibility profileVisibility, Visibility postVisibility, Visibility friendListVisibility, boolean allowMessagesFromStrangers, boolean showOnlineStatus, boolean allowTagging, boolean showInSearch) {
        this.id = id;
        this.user = user;
        this.profileVisibility = profileVisibility != null ? profileVisibility : Visibility.PUBLIC;
        this.postVisibility = postVisibility != null ? postVisibility : Visibility.PUBLIC;
        this.friendListVisibility = friendListVisibility != null ? friendListVisibility : Visibility.FRIENDS;
        this.allowMessagesFromStrangers = allowMessagesFromStrangers;
        this.showOnlineStatus = showOnlineStatus;
        this.allowTagging = allowTagging;
        this.showInSearch = showInSearch;
    }

    public static PrivacySettingBuilder builder() {
        return new PrivacySettingBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Visibility getProfileVisibility() { return profileVisibility; }
    public void setProfileVisibility(Visibility profileVisibility) { this.profileVisibility = profileVisibility; }
    public Visibility getPostVisibility() { return postVisibility; }
    public void setPostVisibility(Visibility postVisibility) { this.postVisibility = postVisibility; }
    public Visibility getFriendListVisibility() { return friendListVisibility; }
    public void setFriendListVisibility(Visibility friendListVisibility) { this.friendListVisibility = friendListVisibility; }
    public boolean isAllowMessagesFromStrangers() { return allowMessagesFromStrangers; }
    public void setAllowMessagesFromStrangers(boolean allowMessagesFromStrangers) { this.allowMessagesFromStrangers = allowMessagesFromStrangers; }
    public boolean isShowOnlineStatus() { return showOnlineStatus; }
    public void setShowOnlineStatus(boolean showOnlineStatus) { this.showOnlineStatus = showOnlineStatus; }
    public boolean isAllowTagging() { return allowTagging; }
    public void setAllowTagging(boolean allowTagging) { this.allowTagging = allowTagging; }
    public boolean isShowInSearch() { return showInSearch; }
    public void setShowInSearch(boolean showInSearch) { this.showInSearch = showInSearch; }

    public static class PrivacySettingBuilder {
        private Long id;
        private User user;
        private Visibility profileVisibility = Visibility.PUBLIC;
        private Visibility postVisibility = Visibility.PUBLIC;
        private Visibility friendListVisibility = Visibility.FRIENDS;
        private boolean allowMessagesFromStrangers = false;
        private boolean showOnlineStatus = true;
        private boolean allowTagging = true;
        private boolean showInSearch = true;

        public PrivacySettingBuilder id(Long id) { this.id = id; return this; }
        public PrivacySettingBuilder user(User user) { this.user = user; return this; }
        public PrivacySettingBuilder profileVisibility(Visibility profileVisibility) { this.profileVisibility = profileVisibility; return this; }
        public PrivacySettingBuilder postVisibility(Visibility postVisibility) { this.postVisibility = postVisibility; return this; }
        public PrivacySettingBuilder friendListVisibility(Visibility friendListVisibility) { this.friendListVisibility = friendListVisibility; return this; }
        public PrivacySettingBuilder allowMessagesFromStrangers(boolean allowMessagesFromStrangers) { this.allowMessagesFromStrangers = allowMessagesFromStrangers; return this; }
        public PrivacySettingBuilder showOnlineStatus(boolean showOnlineStatus) { this.showOnlineStatus = showOnlineStatus; return this; }
        public PrivacySettingBuilder allowTagging(boolean allowTagging) { this.allowTagging = allowTagging; return this; }
        public PrivacySettingBuilder showInSearch(boolean showInSearch) { this.showInSearch = showInSearch; return this; }

        public PrivacySetting build() {
            return new PrivacySetting(id, user, profileVisibility, postVisibility, friendListVisibility, allowMessagesFromStrangers, showOnlineStatus, allowTagging, showInSearch);
        }
    }

    public enum Visibility {
        PUBLIC, FRIENDS, ONLY_ME
    }
}

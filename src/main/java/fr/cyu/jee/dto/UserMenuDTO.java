package fr.cyu.jee.dto;

import fr.cyu.jee.model.User;
import fr.cyu.jee.model.UserType;

import java.util.Optional;

public class UserMenuDTO {

    private MenuType selectedMenu;

    private User selectedUser;

    private String emailSearch;

    private String userType;

    public MenuType getSelectedMenu() {
        return selectedMenu;
    }

    public void setSelectedMenu(MenuType selectedMenu) {
        this.selectedMenu = selectedMenu;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public Optional<User> getSelectedUserOptional() {
        return Optional.ofNullable(selectedUser);
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public Optional<String> getEmailSearchOptional() {
        return Optional.ofNullable(emailSearch);
    }

    public String getEmailSearch() {
        return emailSearch;
    }

    public void setEmailSearch(String emailSearch) {
        this.emailSearch = emailSearch;
    }

    public String getUserType() {
        return userType;
    }

    public Optional<UserType> getUserTypeOptional() {
        try {
            return Optional.of(UserType.valueOf(userType));
        } catch (IllegalArgumentException | NullPointerException e) {
            return Optional.empty();
        }
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean check(User user) {
        return getEmailSearchOptional().map(email -> user.getEmail().startsWith(email)).orElse(true)
                && getUserTypeOptional().map(type -> user.getUserType() == type).orElse(true);
    }

    public enum MenuType {
        ALL_MENU, DISPLAY_MENU, ADD_MENU, EDIT_MENU
    }
}
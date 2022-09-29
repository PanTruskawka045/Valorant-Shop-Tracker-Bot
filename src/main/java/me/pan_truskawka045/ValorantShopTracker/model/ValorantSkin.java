package me.pan_truskawka045.ValorantShopTracker.model;

public class ValorantSkin {

    public ValorantSkin(String displayName, String uuid, String displayVideo, String displayIcon) {
        this.displayName = displayName;
        this.uuid = uuid;
        this.displayVideo = displayVideo;
        this.displayIcon = displayIcon;
    }

    private final String displayName, uuid, displayVideo, displayIcon;


    public String getDisplayName() {
        return displayName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDisplayVideo() {
        return displayVideo;
    }

    public String getDisplayIcon() {
        return displayIcon;
    }

}

package me.pan_truskawka045.ValorantShopTracker.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.pan_truskawka045.AnnotationCore.annotation.file.Field;
import me.pan_truskawka045.AnnotationCore.annotation.file.Key;
import me.pan_truskawka045.AnnotationCore.annotation.file.StorageEntity;

@StorageEntity(folder = "shop-details")
@Getter
@Setter
@NoArgsConstructor
public class ShopDetails {

    @Key
    private long id;

    @Field
    private String login;

    @Field
    private String password;

    public ShopDetails(long id) {
        this.id = id;
    }

}

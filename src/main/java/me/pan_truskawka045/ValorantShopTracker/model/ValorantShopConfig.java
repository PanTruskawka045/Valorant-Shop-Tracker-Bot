package me.pan_truskawka045.ValorantShopTracker.model;

import lombok.Getter;
import me.pan_truskawka045.AnnotationCore.annotation.file.Entity;
import me.pan_truskawka045.AnnotationCore.annotation.file.Field;

@Entity
@Getter
public class ValorantShopConfig {

    @Field
    private String cipherKey;

}

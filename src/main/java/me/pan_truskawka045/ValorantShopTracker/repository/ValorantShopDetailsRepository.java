package me.pan_truskawka045.ValorantShopTracker.repository;

import me.pan_truskawka045.AnnotationCore.annotation.file.StorageRepository;
import me.pan_truskawka045.AnnotationCore.repository.file.FileRepository;
import me.pan_truskawka045.ValorantShopTracker.model.ShopDetails;
import net.dv8tion.jda.api.entities.Member;

@StorageRepository
public interface ValorantShopDetailsRepository extends FileRepository<ShopDetails, Member> {
}

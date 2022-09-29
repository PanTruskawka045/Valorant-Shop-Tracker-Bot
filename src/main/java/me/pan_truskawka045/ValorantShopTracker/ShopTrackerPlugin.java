package me.pan_truskawka045.ValorantShopTracker;

import me.pan_truskawka045.AnnotationCore.annotation.plugin.Plugin;
import me.pan_truskawka045.AnnotationCore.annotation.plugin.PluginInit;

@Plugin(name = "valorant-shop-tracker")
public class ShopTrackerPlugin {

    @PluginInit
    private void onEnable(){
        System.out.println("Valorant Shop Tracker Plugin enabled");
    }

}
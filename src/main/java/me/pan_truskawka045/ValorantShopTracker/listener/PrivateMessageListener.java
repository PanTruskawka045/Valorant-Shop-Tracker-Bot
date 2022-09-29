package me.pan_truskawka045.ValorantShopTracker.listener;

import lombok.SneakyThrows;
import me.pan_truskawka045.AnnotationCore.annotation.Autowired;
import me.pan_truskawka045.AnnotationCore.annotation.ContextParam;
import me.pan_truskawka045.AnnotationCore.annotation.event.EventHandler;
import me.pan_truskawka045.AnnotationCore.annotation.event.EventListener;
import me.pan_truskawka045.AnnotationCore.annotation.file.GlobalConfig;
import me.pan_truskawka045.AnnotationCore.event.Event;
import me.pan_truskawka045.ValorantShopTracker.model.ShopDetails;
import me.pan_truskawka045.ValorantShopTracker.model.ValorantShopConfig;
import me.pan_truskawka045.ValorantShopTracker.repository.ValorantShopDetailsRepository;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@EventListener
public class PrivateMessageListener {

    @Autowired
    private ValorantShopDetailsRepository valorantShopDetailsRepository;

    @GlobalConfig("valorant-shop")
    private ValorantShopConfig valorantShopConfig;

    @SneakyThrows
    @EventHandler(event = Event.PRIVATE_MESSAGE_RECEIVED)
    private void onPrivateMessageReceived(@ContextParam User user, @ContextParam Message message, @ContextParam PrivateChannel channel){
        if(user.isBot()) return;
        String content = message.getContentRaw();
        if (content.startsWith("dodaj-konto")) {
            String[] args = content.split(" ");
            if (args.length >= 3) {
                String account = args[1];
                String password = args[2];
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(valorantShopConfig.getCipherKey().getBytes(), "AES"));
                byte[] encrypted = cipher.doFinal(password.getBytes());
                ShopDetails shopDetails = valorantShopDetailsRepository.findById(user.getId()).orElse(new ShopDetails(user.getIdLong()));
                shopDetails.setLogin(account);
                shopDetails.setPassword(new String(Base64.getEncoder().encode(encrypted)));
                valorantShopDetailsRepository.save(shopDetails);
                channel.sendMessage("Konto zostało dodane!").queue();
            } else {
                channel.sendMessage("`dodaj-konto <username> <password>`").queue();
            }
        } else {
            channel.sendMessage("`dodaj-konto <username> <password>`").queue();
        }
    }


}

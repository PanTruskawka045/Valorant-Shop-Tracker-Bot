package me.pan_truskawka045.ValorantShopTracker.commands;

import lombok.SneakyThrows;
import me.pan_truskawka045.AnnotationCore.annotation.Autowired;
import me.pan_truskawka045.AnnotationCore.annotation.ContextParam;
import me.pan_truskawka045.AnnotationCore.annotation.command.Command;
import me.pan_truskawka045.AnnotationCore.annotation.command.CommandHandler;
import me.pan_truskawka045.AnnotationCore.annotation.file.GlobalConfig;
import me.pan_truskawka045.ValorantShopTracker.impl.SimpleCookieJar;
import me.pan_truskawka045.ValorantShopTracker.model.ShopDetails;
import me.pan_truskawka045.ValorantShopTracker.model.ValorantShopConfig;
import me.pan_truskawka045.ValorantShopTracker.model.ValorantSkin;
import me.pan_truskawka045.ValorantShopTracker.repository.SkinRepository;
import me.pan_truskawka045.ValorantShopTracker.repository.ValorantShopDetailsRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import okhttp3.*;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@CommandHandler
public class ShopCommand {

    @Autowired
    private SkinRepository skinRepository;

    @Autowired
    private ValorantShopDetailsRepository valorantShopDetailsRepository;

    @GlobalConfig("valorant-shop")
    private ValorantShopConfig valorantShopConfig;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String region = "eu";

    @SneakyThrows
    @Command(name="shop", aliases = {"sklep"})
    private void shopCommand(@ContextParam Member member, @ContextParam TextChannel textChannel){
        ShopDetails shopDetails = valorantShopDetailsRepository.findById(member).orElse(null);
        if(shopDetails == null){
            textChannel.sendMessage("Nie ustawiono loginu i hasła").queue();
            return;
        }

        String username = shopDetails.getLogin();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(valorantShopConfig.getCipherKey().getBytes(), "AES"));
        String password = new String(cipher.doFinal(Base64.getDecoder().decode(shopDetails.getPassword())));


        SimpleCookieJar cookieJar = new SimpleCookieJar();
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(cookieJar).build();

        RequestBody login1Body = RequestBody.create(JSON, "{\"client_id\":\"play-valorant-web-prod\",\"nonce\": \"1\",\"redirect_uri\":\"https://playvalorant.com/opt_in\",\"response_type\":\"token id_token\"}");

        Request login1 = new Request.Builder()
                .url("https://auth.riotgames.com/api/v1/authorization").post(login1Body).build();

        RequestBody login2Body = RequestBody.create(JSON, "{" +
                "    \"type\": \"auth\"," +
                "    \"username\": \"" + username + "\"," +
                "    \"password\": \"" + password + "\"" +
                "}");
        Request login2 = new Request.Builder()
                .url("https://auth.riotgames.com/api/v1/authorization").put(login2Body).build();

        //WTF
        client.newCall(login2).execute().close();
        client.newCall(login1).execute().close();
        Response login2Response = client.newCall(login2).execute();


        JSONObject login2JsonResponse = new JSONObject(login2Response.body().string());

        login2Response.close();

        String uri = login2JsonResponse.getJSONObject("response").getJSONObject("parameters").getString("uri");
        String tokenCut1 = uri.substring(uri.indexOf("access_token=") + "access_token=".length());
        String token = tokenCut1.substring(0, tokenCut1.indexOf("&"));

        Headers headers = new Headers.Builder().add("Authorization", "Bearer " + token).build();
        RequestBody emptyRequestBody = RequestBody.create(JSON, "");

        Request entitlementsTokenRequest = new Request.Builder()
                .url("https://entitlements.auth.riotgames.com/api/token/v1").post(emptyRequestBody).headers(headers).build();

        Request userInfoRequest = new Request.Builder()
                .url("https://auth.riotgames.com/userinfo").post(emptyRequestBody).headers(headers).build();

        Response entitlementsTokenResponse = client.newCall(entitlementsTokenRequest).execute();

        JSONObject entitlementsTokenJsonResponse = new JSONObject(entitlementsTokenResponse.body().string());
        String entitlementsToken = entitlementsTokenJsonResponse.getString("entitlements_token");

        entitlementsTokenResponse.close();

        Response userInfoResponse = client.newCall(userInfoRequest).execute();


        JSONObject subJsonResponse = new JSONObject(userInfoResponse.body().string());

        userInfoResponse.close();

        String userId = subJsonResponse.getString("sub");

        Headers headers2 = new Headers.Builder()
                .add("Authorization", "Bearer " + token)
                .add("X-Riot-Entitlements-JWT", entitlementsToken)
                .build();



        Request skinsRequest = new Request.Builder()
                .url("https://pd." + region + ".a.pvp.net/store/v2/storefront/" + userId).get().headers(headers2).build();

        Response skinsResponse = client.newCall(skinsRequest).execute();

        JSONObject skinsJsonResponse = new JSONObject(skinsResponse.body().string());

        String[] dailySkins = skinsJsonResponse.getJSONObject("SkinsPanelLayout").getJSONArray("SingleItemOffers").toList().stream().map(Object::toString).toArray(String[]::new);

        List<MessageEmbed> embedsToSend = new ArrayList<>();



        for (String dailySkin : dailySkins) {
            ValorantSkin skin = skinRepository.getSkins().get(dailySkin);
            embedsToSend.add(new EmbedBuilder().setTitle(skin.getDisplayName())
                    .setImage(skin.getDisplayIcon())
                    .setColor(member.getColorRaw()).build());
        }
        textChannel.sendMessageEmbeds(embedsToSend).queue();

    }
}

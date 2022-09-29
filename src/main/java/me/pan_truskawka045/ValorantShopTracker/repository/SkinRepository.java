package me.pan_truskawka045.ValorantShopTracker.repository;

import lombok.Getter;
import lombok.SneakyThrows;
import me.pan_truskawka045.AnnotationCore.annotation.Service;
import me.pan_truskawka045.ValorantShopTracker.model.ValorantSkin;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Service
public class SkinRepository {

    @Getter
    private Map<String, ValorantSkin> skins = new HashMap<>();

    @SneakyThrows
    public SkinRepository(){
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url("https://valorant-api.com/v1/weapons/skinlevels").build();
        Response response = client.newCall(request).execute();
        JSONObject object = new JSONObject(response.body().string());
        object.getJSONArray("data").forEach(skinObj -> {
            JSONObject jsonData = new JSONObject(skinObj.toString());
            skins.put(jsonData.getString("uuid"), new ValorantSkin(
                    jsonData.get("displayName") == null ? null : jsonData.get("displayName").toString(),
                    jsonData.getString("uuid"),
                    jsonData.get("streamedVideo") == null ? null : jsonData.get("streamedVideo").toString(),
                    jsonData.get("displayIcon") == null ? null : jsonData.get("displayIcon").toString()
            ));
        });
        response.close();
    }

}

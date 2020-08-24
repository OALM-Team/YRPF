package fr.yuki.yrpf.world;

import com.google.gson.Gson;
import fr.yuki.yrpf.net.payload.SetImageWUIPayload;
import fr.yuki.yrpf.net.payload.SetProgressWUIPayload;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

public class WImageContainerWUI extends WorldUI {
    private String imageUrl = "";

    public WImageContainerWUI(Vector position, Vector rotation, int width, int height, String uiType) {
        super(position, rotation, width, height, uiType);
    }

    public void setImageUrl(String url) {
        if(url.startsWith("http")) {
            this.imageUrl = url;
        } else {
            this.imageUrl = "../../../../" + url;
        }
        this.syncAll();
    }

    @Override
    public void sync(Player player) {
        this.dispatchToPlayerUI(player, new Gson().toJson(new SetImageWUIPayload(this.imageUrl)));
    }
}

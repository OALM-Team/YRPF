package fr.yuki.yrpf.world;

import com.google.gson.Gson;
import fr.yuki.yrpf.net.payload.AddGameMapMarkerPayload;
import fr.yuki.yrpf.net.payload.SetProgressWUIPayload;
import lombok.Getter;
import lombok.Setter;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

public class WProgressBarWUI extends WorldUI {
    private int progress;

    public WProgressBarWUI(Vector position, Vector rotation, int width, int height, String uiType) {
        super(position, rotation, width, height, uiType);
        this.progress = 0;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        this.syncAll();
    }

    @Override
    public void sync(Player player) {
        this.dispatchToPlayerUI(player, new Gson().toJson(new SetProgressWUIPayload(this.progress)));
    }
}

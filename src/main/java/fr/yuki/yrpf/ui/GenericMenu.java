package fr.yuki.yrpf.ui;

import com.google.gson.Gson;
import fr.yuki.yrpf.manager.UIStateManager;
import fr.yuki.yrpf.net.payload.AddGenericMenuItemPayload;
import fr.yuki.yrpf.net.payload.ClearGenericMenuPayload;
import lombok.Getter;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;

public class GenericMenu {
    @Getter
    private Player player;
    private ArrayList<GenericMenuItem> items;

    public GenericMenu(Player player) {
        this.player = player;
        this.items = new ArrayList<>();
    }

    public void show() {
        if(!UIStateManager.handleUIToogle(player, "genericMenu"))
            UIStateManager.handleUIToogle(player, "genericMenu");
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new ClearGenericMenuPayload()));
        for(GenericMenuItem menuItem : this.items) {
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson
                    (new AddGenericMenuItemPayload(menuItem.getText(), menuItem.getAction())));
        }
    }

    public void addCloseItem() {
        this.getItems().add(new GenericMenuItem("Fermer", "window.CallEvent(\"RemoteCallInterface\", \"GenericMenu:Dismiss\");"));
    }

    public void hide() {
        if(UIStateManager.handleUIToogle(player, "genericMenu"))
            UIStateManager.handleUIToogle(player, "genericMenu");
    }

    public ArrayList<GenericMenuItem> getItems() {
        return items;
    }
}

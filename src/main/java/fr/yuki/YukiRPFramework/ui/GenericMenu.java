package fr.yuki.YukiRPFramework.ui;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.manager.UIStateManager;
import fr.yuki.YukiRPFramework.net.payload.AddGenericMenuItemPayload;
import fr.yuki.YukiRPFramework.net.payload.ClearGenericMenuPayload;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;

public class GenericMenu {
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

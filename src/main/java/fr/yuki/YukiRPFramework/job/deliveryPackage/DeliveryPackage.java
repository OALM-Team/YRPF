package fr.yuki.YukiRPFramework.job.deliveryPackage;

import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.manager.ModdingManager;
import fr.yuki.YukiRPFramework.manager.UIStateManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.ui.UIState;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.WorldObject;

public abstract class DeliveryPackage {
    private Player player;
    protected Vector position;
    protected Vector rotation;
    protected WorldObject worldObject;

    public DeliveryPackage(Player player, Vector position, Vector rotation) {
        this.player = player;
        this.position = position;
        this.rotation = rotation;
    }

    public abstract Vector getPackageScale();
    public abstract int getModelId();
    public abstract int getTimeForDelivery();
    public abstract void onDelivered();

    public void spawn() {
        Account account = WorldManager.getPlayerAccount(this.player);
        UIStateManager.sendNotification(this.player, ToastTypeEnum.SUCCESS,
                I18n.t(account.getLang(), "toast.shipping.time_before_shipped",
                        String.valueOf(Math.floor(this.getTimeForDelivery() / 1000)) + "s"));
        this.worldObject = Onset.getServer().createObject(this.position, this.getModelId());
        if(ModdingManager.isCustomModelId(this.getModelId())) ModdingManager.assignCustomModel(this.worldObject, this.getModelId());
        this.worldObject.setRotation(this.rotation);
        this.worldObject.setProperty("no_collision", 1, true);

        Onset.delay(this.getTimeForDelivery(), () -> {
            Onset.print("Delivery shipped");
            if(WorldManager.findPlayerByAccountId(account.getId()) != null) {
                UIStateManager.sendNotification(WorldManager.findPlayerByAccountId(account.getId()),
                        ToastTypeEnum.SUCCESS,
                        I18n.t(account.getLang(), "toast.shipping.shipped"));
            }
            this.worldObject.destroy();
            this.onDelivered();
        });
    }
}

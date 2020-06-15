package fr.yuki.YukiRPFramework.character;

import fr.yuki.YukiRPFramework.dao.AccountDAO;
import fr.yuki.YukiRPFramework.job.ObjectPlacementInstance;
import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import fr.yuki.YukiRPFramework.manager.ModdingManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.modding.Line3D;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.model.Bag;
import fr.yuki.YukiRPFramework.model.Mask;
import fr.yuki.YukiRPFramework.phone.PhoneCall;
import fr.yuki.YukiRPFramework.ui.UIState;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.WorldObject;

import java.sql.SQLException;
import java.util.ArrayList;

public class CharacterState {
    private WearableWorldObject wearableWorldObject;
    private boolean isDead = false;
    private boolean firstSpawn = true;
    private boolean hasUIReady = false;
    private ObjectPlacementInstance currentObjectPlacementInstance = null;
    private boolean cuffed = false;
    private UIState uiState = null;
    private Line3D currentDisplayedLine3D = null;
    private ArrayList<Vector> lastLocationsRequest = new ArrayList<>();
    private Mask currentMask = null;
    private WorldObject maskWorldObject = null;
    private Bag currentBag = null;
    private WorldObject bagWorldObject = null;
    private PhoneCall currentPhoneCall = null;

    public CharacterState() {
        this.uiState = new UIState();
    }

    public WearableWorldObject getWearableWorldObject() {
        return wearableWorldObject;
    }

    public void setWearableWorldObject(Player player, WearableWorldObject wearableWorldObject) {
        this.wearableWorldObject = wearableWorldObject;
        if(this.wearableWorldObject != null) {
            player.setProperty("wearId", this.wearableWorldObject.getUuid(), true);
        } else {
            player.setProperty("wearId", "", true);
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isFirstSpawn() {
        return firstSpawn;
    }

    public void setFirstSpawn(boolean firstSpawn) {
        this.firstSpawn = firstSpawn;
    }

    public ObjectPlacementInstance getCurrentObjectPlacementInstance() {
        return currentObjectPlacementInstance;
    }

    public void setCurrentObjectPlacementInstance(ObjectPlacementInstance currentObjectPlacementInstance) {
        this.currentObjectPlacementInstance = currentObjectPlacementInstance;
    }

    public boolean isCuffed() {
        return cuffed;
    }

    public void setCuffed(boolean cuffed) {
        this.cuffed = cuffed;
    }

    public boolean canInteract() {
        if(this.isDead) return false;
        if(this.cuffed) return false;
        if(this.getCurrentObjectPlacementInstance() != null) return false;
        return true;
    }

    public UIState getUiState() {
        return uiState;
    }

    public Line3D getCurrentDisplayedLine3D() {
        return currentDisplayedLine3D;
    }

    public void setCurrentDisplayedLine3D(Line3D currentDisplayedLine3D) {
        this.currentDisplayedLine3D = currentDisplayedLine3D;
    }

    public ArrayList<Vector> getLastLocationsRequest() {
        return lastLocationsRequest;
    }

    public void setLastLocationsRequest(ArrayList<Vector> lastLocationsRequest) {
        this.lastLocationsRequest = lastLocationsRequest;
    }

    public void attachMask(Mask mask, Player player) {
        if(this.maskWorldObject != null) {
            this.maskWorldObject.destroy();
        }
        this.currentMask = mask;
        Onset.print("Attach mask id: " + mask.getModelId());
        this.maskWorldObject = Onset.getServer().createObject(new Vector(0, 0, 0), mask.getModelId());
        this.maskWorldObject.attach(player, new Vector(mask.getX(), mask.getY(), mask.getZ()), new Vector(mask.getRx(), mask.getRy(), mask.getRz()), mask.getSocket());
        if(ModdingManager.isCustomModelId(mask.getModelId())) ModdingManager.assignCustomModel(this.maskWorldObject, mask.getModelId());
        this.maskWorldObject.setScale(new Vector(mask.getSx(), mask.getSy(), mask.getSz()));
    }

    public void unattachMask() {
        if(this.maskWorldObject != null) {
            this.maskWorldObject.destroy();
        }
        this.currentMask = null;
    }

    public void attachBag(Bag bag, Player player) {
        if(this.bagWorldObject != null) {
            this.bagWorldObject.destroy();
        }
        Account account = WorldManager.getPlayerAccount(player);
        this.currentBag = bag;
        Onset.print("Attach bag id: " + bag.getModelId());
        this.bagWorldObject = Onset.getServer().createObject(new Vector(0, 0, 0), bag.getModelId());
        this.bagWorldObject.attach(player, new Vector(bag.getX(), bag.getY(), bag.getZ()), new Vector(bag.getRx(), bag.getRy(), bag.getRz()), bag.getSocket());
        if(ModdingManager.isCustomModelId(bag.getModelId())) ModdingManager.assignCustomModel(this.bagWorldObject, bag.getModelId());
        this.bagWorldObject.setScale(new Vector(bag.getSx(), bag.getSy(), bag.getSz()));
        account.setBagId(this.currentBag.getModelId());
        try {
            AccountDAO.updateAccount(account, null);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void unattachBag(Player player) {
        if(this.bagWorldObject != null) {
            this.bagWorldObject.destroy();
        }
        Account account = WorldManager.getPlayerAccount(player);
        this.currentBag = null;
        account.setBagId(-1);
        try {
            AccountDAO.updateAccount(account, null);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Mask getCurrentMask() {
        return currentMask;
    }

    public void setCurrentMask(Mask currentMask) {
        this.currentMask = currentMask;
    }

    public Bag getCurrentBag() {
        return currentBag;
    }

    public void setCurrentBag(Bag currentBag) {
        this.currentBag = currentBag;
    }

    public boolean isHasUIReady() {
        return hasUIReady;
    }

    public void setHasUIReady(boolean hasUIReady) {
        this.hasUIReady = hasUIReady;
    }

    public PhoneCall getCurrentPhoneCall() {
        return currentPhoneCall;
    }

    public void setCurrentPhoneCall(PhoneCall currentPhoneCall) {
        this.currentPhoneCall = currentPhoneCall;
    }
}

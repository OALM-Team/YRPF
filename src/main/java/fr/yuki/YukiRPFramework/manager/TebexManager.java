package fr.yuki.YukiRPFramework.manager;

import fr.yuki.YukiRPFramework.model.ItemTemplate;
import fr.yuki.YukiRPFramework.tebex.TebexAPI;
import fr.yuki.YukiRPFramework.tebex.responses.Command;
import fr.yuki.YukiRPFramework.tebex.responses.TebexCommandQueue;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

import java.io.IOException;

public class TebexManager {
    public static TebexAPI tebexAPI;

    public static void init() throws Exception {
        if(WorldManager.getServerConfig().getTebexSecretKey().equals("")) {
            Onset.print("TebexAPI not configured, you need to put your secret key in yrpf/server_config.json");
            return;
        }
        tebexAPI = new TebexAPI(WorldManager.getServerConfig().getTebexSecretKey());
        Onset.print("TebexAPI initialized");

        Onset.timer(10000, () -> {
            processCommands();
        });
    }

    private static void processCommands() {
        try {
            TebexCommandQueue commandQueue = tebexAPI.getCommandQueues();
            if(commandQueue == null) return;
            for(Command command : commandQueue.getCommands()) {
                Onset.print("Process command id: " + command.getID());
                Onset.print("Command parameters: " + command.getCommand());

                String commandType = command.getCommand().split(" ")[0];
                boolean isSuccess = false;
                switch (commandType) {
                    case "purchase_item":
                        isSuccess = onPurchaseItem(command);
                        break;
                }

                if(isSuccess) {
                    Onset.print("Purchase success, now delete it from Tebex queue");
                    tebexAPI.deleteCommand(command);
                } else {
                    Onset.print("Purchase failed, we keep it in queue");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Onset.print("Can't process tebex commands");
        }
    }

    private static boolean onPurchaseItem(Command command) {
        int itemId = Integer.parseInt(command.getCommand().split(" ")[1]);
        int quantity = Integer.parseInt(command.getCommand().split(" ")[2]);
        Player player = WorldManager.findPlayerBySteamId(command.getPlayer().getUUID());
        if(player == null) return false;
        if(CharacterManager.getCharacterStateByPlayer(player) == null) return false;
        if(!CharacterManager.getCharacterStateByPlayer(player).isHasUIReady()) return false;

        ItemTemplate itemTemplate = InventoryManager.getItemTemplates().get(itemId);
        InventoryManager.addItemToPlayer(player, String.valueOf(itemTemplate.getId()), quantity, false);

        return true;
    }
}

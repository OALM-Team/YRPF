package fr.yuki.yrpf.luaapi.job;

import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.manager.CharacterManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class GetWearedObjectEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Player player = Onset.getPlayer(Integer.parseInt(objects[0].toString()));
        if(player == null) return -1;
        Account account = WorldManager.getPlayerAccount(player);
        if(account == null) return -1;
        CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);
        if(characterState.getWearableWorldObject() == null) return -1;
        return characterState.getWearableWorldObject().getModelId();
    }
}

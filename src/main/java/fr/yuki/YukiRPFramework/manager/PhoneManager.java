package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.dao.PhoneContactDAO;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.model.PhoneContact;
import fr.yuki.YukiRPFramework.net.payload.*;
import fr.yuki.YukiRPFramework.phone.PhoneCall;
import fr.yuki.YukiRPFramework.phone.PhoneMessage;
import fr.yuki.YukiRPFramework.utils.Basic;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class PhoneManager {
    private static ArrayList<PhoneContact> phoneContacts;
    private static ArrayList<PhoneMessage> phoneMessages;

    public static void init() throws SQLException {
        phoneContacts = PhoneContactDAO.loadPhoneContacts();
        Onset.print("Loaded " + phoneContacts.size() + " phone contact(s) from the database");

        phoneMessages = new ArrayList<>();
    }

    public static String generateRandomPhoneNumber() {
        String phoneNumber = "07";
        for(int i = 0; i < 8; i++) {
            phoneNumber += String.valueOf(Basic.randomNumber(1, 9));
        }
        return phoneNumber;
    }

    public static void handleRequestPhoneContacts(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        ArrayList<PhoneContact> contacts = new ArrayList<>(phoneContacts.stream().filter(x -> x.getAccountId() == account.getId()).collect(Collectors.toList()));
        for(PhoneContact contact : contacts) {
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddClientContactPayload(contact.getId(), contact.getName(), contact.getNumber())));
        }
    }

    public static void handleAddPhoneContact(Player player, PhoneAddContactPayload payload) throws SQLException {
        if(payload.getName().trim().equals("")|| payload.getNumber().trim() == "") return;
        Account account = WorldManager.getPlayerAccount(player);
        ArrayList<PhoneContact> contacts = new ArrayList<>(phoneContacts.stream().filter(x -> x.getAccountId() == account.getId()).collect(Collectors.toList()));
        if(contacts.size() >= 30) return; // Too much contact
        PhoneContact phoneContact = new PhoneContact();
        phoneContact.setName(payload.getName());
        phoneContact.setNumber(payload.getNumber());
        phoneContact.setAccountId(account.getId());
        PhoneContactDAO.insertPhoneContact(phoneContact);
        phoneContacts.add(phoneContact);
        Onset.print("Phone contact created");
    }

    public static void handleRequestSendMessage(Player player, RequestPhoneSendMessagePayload payload) {
        if(payload.getMessage().trim().equals("") || payload.getNumber().trim().equals("")) return;
        Account account = WorldManager.getPlayerAccount(player);
        PhoneMessage phoneMessage = new PhoneMessage();
        phoneMessage.setFromNumber(account.getPhoneNumber());
        phoneMessage.setToNumber(payload.getNumber());
        phoneMessage.setMessage(payload.getMessage());
        phoneMessage.setMessageType(1); // Text message
        phoneMessages.add(phoneMessage);

        // Send message to players
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddPhoneMessagePayload
                (phoneMessage.getMessageType(), phoneMessage.getFromNumber(), phoneMessage.getMessage())));
        Player otherPlayer = WorldManager.getPlayerByPhoneNumber(phoneMessage.getToNumber());
        if(otherPlayer != null) {
            Account otherAccount = WorldManager.getPlayerAccount(otherPlayer);
            otherPlayer.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddPhoneMessagePayload
                    (phoneMessage.getMessageType(), phoneMessage.getFromNumber(), phoneMessage.getMessage())));
            SoundManager.playSound3D("sounds/notif_1.mp3", otherPlayer.getLocation(), 350, 2);
            UIStateManager.sendNotification(otherPlayer, ToastTypeEnum.SUCCESS, I18n.t(otherAccount.getLang(),
                    "toast.phone.you_have_received_message", account.getPhoneNumber()));
        }
    }

    public static ArrayList<PhoneMessage> getConversation(String number1, String number2) {
        ArrayList<PhoneMessage> messages = new ArrayList<>();
        for(PhoneMessage phoneMessage : phoneMessages) {
            if(phoneMessage.getFromNumber().equals(number1)) {
                if(phoneMessage.getToNumber().equals(number2)) {
                    messages.add(phoneMessage);
                    continue;
                }
            }

            if(phoneMessage.getFromNumber().equals(number2)) {
                if(phoneMessage.getToNumber().equals(number1)) {
                    messages.add(phoneMessage);
                    continue;
                }
            }
        }
        return messages;
    }

    public static void handleRequestConversation(Player player, String number) {
        Account account = WorldManager.getPlayerAccount(player);
        ArrayList<PhoneMessage> messages = getConversation(account.getPhoneNumber(), number);

        for(PhoneMessage phoneMessage : messages) {
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddPhoneMessagePayload
                    (phoneMessage.getMessageType(), phoneMessage.getFromNumber(), phoneMessage.getMessage())));
        }
    }

    public static void handleRequestConversationsList(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        ArrayList<PhoneMessage> phoneMessages = getConversationsWithNumber(account.getPhoneNumber());
        HashMap<String, ArrayList<PhoneMessage>> uniqueConversation = new HashMap<>();

        // Make unique conversation
        for(PhoneMessage phoneMessage : phoneMessages) {
            if(phoneMessage.getFromNumber().equals(account.getPhoneNumber())) {
                if(!uniqueConversation.containsKey(phoneMessage.getToNumber())) {
                    uniqueConversation.put(phoneMessage.getToNumber(), new ArrayList<>());
                    uniqueConversation.get(phoneMessage.getToNumber()).add(phoneMessage);
                }
                else {
                    uniqueConversation.get(phoneMessage.getToNumber()).add(phoneMessage);
                }
            }

            if(phoneMessage.getToNumber().equals(account.getPhoneNumber())) {
                if(!uniqueConversation.containsKey(phoneMessage.getFromNumber())) {
                    uniqueConversation.put(phoneMessage.getFromNumber(), new ArrayList<>());
                    uniqueConversation.get(phoneMessage.getFromNumber()).add(phoneMessage);
                }
                else {
                    uniqueConversation.get(phoneMessage.getFromNumber()).add(phoneMessage);
                }
            }
        }

        for(Map.Entry<String, ArrayList<PhoneMessage>> conversations : uniqueConversation.entrySet()) {
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddPhoneConversationPayload(
                    conversations.getKey(), conversations.getValue().get(conversations.getValue().size() - 1).getMessage()
            )));
        }
    }

    public static void handleRequestCall(Player player, String phoneNumber) {
        Onset.print("Request call to " + phoneNumber);
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getCurrentPhoneCall() != null){
            Onset.print("Caller already in call");
            return;
        }
        Player receiver = WorldManager.getPlayerByPhoneNumber(phoneNumber);
        if(receiver == null) {
            Onset.print("Can't find the receiver");
            return;
        }
        CharacterState receiverState = CharacterManager.getCharacterStateByPlayer(receiver);
        if(receiverState.getCurrentPhoneCall() != null) {
            Onset.print("Receiver already in call");
            return;
        }

        PhoneCall phoneCall = new PhoneCall(player, receiver);
        state.setCurrentPhoneCall(phoneCall);
        receiverState.setCurrentPhoneCall(phoneCall);
        phoneCall.displayCall();
        Onset.print("Call begin");
    }

    public static void handleCallAnswer(Player player) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getCurrentPhoneCall() == null){
            Onset.print("Can't answer because there is no call");
            return;
        }
        state.getCurrentPhoneCall().begin();
    }

    public static void handleCallEnd(Player player) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getCurrentPhoneCall() == null){
            Onset.print("Can't answer because there is no call");
            return;
        }
        state.getCurrentPhoneCall().end();
    }

    public static ArrayList<PhoneMessage> getConversationsWithNumber(String number) {
        ArrayList<PhoneMessage> messages = new ArrayList<>();
        for(PhoneMessage phoneMessage : phoneMessages) {
            if(phoneMessage.getToNumber().equals(number)) {
                messages.add(phoneMessage);
                continue;
            }

            if(phoneMessage.getFromNumber().equals(number)) {
                messages.add(phoneMessage);
                continue;
            }
        }
        return messages;
    }
}

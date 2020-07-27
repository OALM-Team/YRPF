package fr.yuki.yrpf.manager;

import com.google.gson.Gson;
import eu.bebendorf.ajorm.Repo;
import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.character.CharacterToolAnimation;
import fr.yuki.yrpf.enums.JobEnum;
import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.i18n.I18n;
import fr.yuki.yrpf.model.Account;
import fr.yuki.yrpf.model.PhoneContact;
import fr.yuki.yrpf.net.payload.*;
import fr.yuki.yrpf.phone.PhoneCall;
import fr.yuki.yrpf.phone.PhoneMessage;
import fr.yuki.yrpf.phone.UrgencyPhoneMessage;
import fr.yuki.yrpf.utils.Basic;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class PhoneManager {
    private static List<PhoneContact> phoneContacts;
    private static List<PhoneMessage> phoneMessages;
    private static Map<String, ArrayList<UrgencyPhoneMessage>> urgencyMessages;

    private static int urgencyPhoneMessageCurrentId = 1;

    public static void init() throws SQLException {
        phoneContacts = Repo.get(PhoneContact.class).all();
        Onset.print("Loaded " + phoneContacts.size() + " phone contact(s) from the database");

        phoneMessages = new ArrayList<>();

        // init urgency
        urgencyMessages = new HashMap<>();
        urgencyMessages.put("police", new ArrayList<>());
        urgencyMessages.put("hospital", new ArrayList<>());
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
        phoneContact.save();
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
        attachPhone(state.getCurrentPhoneCall().getCaller());
        attachPhone(state.getCurrentPhoneCall().getReceiver());
    }

    public static void handleCallEnd(Player player) {
        Onset.print("Request end call");
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getCurrentPhoneCall() == null){
            Onset.print("Can't answer because there is no call");
            return;
        }
        state.getCurrentPhoneCall().end();
    }

    public static void handleAttachPhone(Player player, String phoneState) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(!state.canInteract()) return;
        if(phoneState.equals("true")) {
            attachPhone(player);
        } else {
            if(state.getCurrentPhoneCall() == null) {
                player.setAnimation(Animation.PHONE_PUTAWAY);
                state.getCurrentPhoneAttached().unAttach();
                state.setCurrentPhoneAttached(null);
            }
        }
    }

    public static void handleUrgencyRequest(Player player, UrgencyRequestPayload payload) {
        Account account = WorldManager.getPlayerAccount(player);
        UrgencyPhoneMessage urgencyPhoneMessage = new UrgencyPhoneMessage();
        urgencyPhoneMessage.setId(urgencyPhoneMessageCurrentId++);
        urgencyPhoneMessage.setFromNumber(account.getPhoneNumber());
        urgencyPhoneMessage.setToNumber(payload.getService());
        urgencyPhoneMessage.setMessage(payload.getText());
        urgencyPhoneMessage.setMessageType(1);
        urgencyPhoneMessage.setService(payload.getService().toLowerCase());
        urgencyPhoneMessage.setPosition(player.getLocation());
        urgencyMessages.get(payload.getService()).add(urgencyPhoneMessage);
        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, "Votre demande est désormais en attente");

        // Display messages
        JobEnum jobEnum = JobEnum.POLICE;
        Onset.print("Add urgency for service: " + payload.getService().toLowerCase());
        switch (payload.getService().toLowerCase()) {
            case "police":
                jobEnum = JobEnum.POLICE;
                break;

            case "hospital":
                jobEnum = JobEnum.EMS;
                break;
        }
        ArrayList<Player> whitelistedPlayers = JobManager.getWhitelistedPlayersForJob(jobEnum);
        for(Player wPlayer : whitelistedPlayers) {
            try {
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddPhoneUrgencyPayload(
                        urgencyPhoneMessage.getId(), urgencyPhoneMessage.getFromNumber(), urgencyPhoneMessage.getMessage(),
                        urgencyPhoneMessage.getService(),
                        urgencyPhoneMessage.getPosition().getX(), urgencyPhoneMessage.getPosition().getY(), urgencyPhoneMessage.getPosition().getZ()
                )));
                UIStateManager.sendNotification(wPlayer, ToastTypeEnum.SUCCESS, "Vous avez reçu une nouvelle urgence");
                SoundManager.playSound3D("sounds/notif_1.mp3", wPlayer.getLocation(), 350, 2);
            } catch (Exception ex) {
                Onset.print("Can't send urgency: " + ex.toString());
            }
        }
    }

    public static void attachPhone(Player player) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getCurrentPhoneAttached() != null) return;
        CharacterToolAnimation toolAnimation = new CharacterToolAnimation(50011, new Vector(-10,4,5),
                new Vector(180,-40,90), new Vector(0.02, 0.02, 0.02), "hand_r");
        player.setAnimation(Animation.PHONE_HOLD);
        state.setCurrentPhoneAttached(toolAnimation);
        toolAnimation.attach(player);
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

    private static void refreshUrgencyMessages(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        boolean isPolice = AccountManager.getAccountJobWhitelists().stream()
                .filter(x -> x.getAccountId() == account.getId() && x.getJobId().equals(JobEnum.POLICE.name()))
                .findFirst().orElse(null) != null;
        boolean isEMS = AccountManager.getAccountJobWhitelists().stream()
                .filter(x -> x.getAccountId() == account.getId() && x.getJobId().equals(JobEnum.EMS.name()))
                .findFirst().orElse(null) != null;
        ArrayList<UrgencyPhoneMessage> urgencyPhoneMessages = new ArrayList<>();

        // Check whitelists
        if(isPolice) urgencyPhoneMessages.addAll(urgencyMessages.get("police"));
        if(isEMS) urgencyPhoneMessages.addAll(urgencyMessages.get("hospital"));

        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new ClearPhoneUrgencyPayload()));
        Collections.reverse(urgencyPhoneMessages); // Reverse to get the newest before
        for(UrgencyPhoneMessage urgencyPhoneMessage : urgencyPhoneMessages) {
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddPhoneUrgencyPayload(
                    urgencyPhoneMessage.getId(), urgencyPhoneMessage.getFromNumber(), urgencyPhoneMessage.getMessage(),
                    urgencyPhoneMessage.getService(),
                    urgencyPhoneMessage.getPosition().getX(), urgencyPhoneMessage.getPosition().getY(), urgencyPhoneMessage.getPosition().getZ()
            )));
        }
    }

    public static void handleUrgencyListRequest(Player player) {
        refreshUrgencyMessages(player);
    }

    public static void handleUrgencySolveRequest(Player player, SolveUrgencyPayload payload) {
        UrgencyPhoneMessage phoneMessage = urgencyMessages.get(payload.getService()).stream()
                .filter(x -> x.getId() == payload.getId()).findFirst().orElse(null);
        if(phoneMessage == null) return;
        urgencyMessages.get(payload.getService()).remove(phoneMessage);
        refreshUrgencyMessages(player);
        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, "Urgence clôturée");
    }

    public static void handleDeletePhoneContact(Player player, String number) {
        Account account = WorldManager.getPlayerAccount(player);
        PhoneContact contact = phoneContacts.stream().filter(x -> x.getAccountId() == account.getId()
                && x.getNumber().equals(number)).findFirst().orElse(null);
        if(contact == null) return;
        phoneContacts.remove(contact);
    }
}

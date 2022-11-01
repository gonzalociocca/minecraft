package me.gonzalociocca.minelevel.core.server;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.database.DatabaseListener;
import me.gonzalociocca.minelevel.core.listeners.*;
import me.gonzalociocca.minelevel.core.misc.Variable;

/**
 * Created by noname on 22/4/2017.
 */
public abstract class ServerBase {
    public BankListener bankListener;
    public AutoPrivateListener autoPrivateListener;
    public ChatListener chatListener;
    public DatabaseListener databaseListener;
    public FactionsEventListener factionsEventListener;
    public FactionsListener factionsListener;
    public FactionsScoreboardListener factionsScoreboardListener;
    public MobListener mobListener;
    public CommandListener commandListener;
    public RexListener rexListener;
    public LobbyListener lobbyListener;

    public ServerBase(Main main){

    }

    public ServerBase(Main main, boolean isAutoPrivate, boolean isBank, boolean isChat, boolean isDatabase, boolean isFactionsEvent, boolean isFactions, boolean isFactionsScoreboard, boolean isMob, boolean isCommand, boolean isRex, boolean isLobby){
        if(isAutoPrivate) {
            autoPrivateListener = new AutoPrivateListener(main);
        }
        if(isBank) {
            bankListener = new BankListener(main);
        }
        if(isChat) {
            chatListener = new ChatListener(main);
        }
        if(isDatabase) {
            databaseListener = new DatabaseListener(main, Variable.address, Variable.port, Variable.db, Variable.user, Variable.password);
        }
        if(isFactionsEvent) {
            factionsEventListener = new FactionsEventListener(main);
        }
        if(isFactions) {
            factionsListener = new FactionsListener(main);
        }
        if(isFactionsScoreboard) {
            factionsScoreboardListener = new FactionsScoreboardListener(main);
        }
        if(isMob) {
            mobListener = new MobListener(main);
        }
        if(isCommand) {
            commandListener = new CommandListener(main);
        }
        if(isRex){
            rexListener = new RexListener(main);
        }
        if(isLobby){
            lobbyListener = new LobbyListener(main);
        }
    }

}

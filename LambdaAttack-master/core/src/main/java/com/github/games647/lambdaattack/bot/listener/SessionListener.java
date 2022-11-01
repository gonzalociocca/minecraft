package com.github.games647.lambdaattack.bot.listener;

import com.github.games647.lambdaattack.LambdaAttack;
import com.github.games647.lambdaattack.bot.Bot;

import java.util.logging.Level;

import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.packetlib.event.session.DisconnectedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;

public abstract class SessionListener extends SessionAdapter {

    protected final Bot owner;

    public SessionListener(Bot owner) {
        this.owner = owner;
    }

    @Override
    public void disconnected(DisconnectedEvent disconnectedEvent) {
     //   String reason = disconnectedEvent.getReason();
   //     owner.getLogger().log(Level.INFO, "Disconnected: {0}", reason);
    }

    public void onJoin(GameProfile profile) {
        if (LambdaAttack.getInstance().isAutoRegister()) {
       //     String password = LambdaAttack.PROJECT_NAME;
       //     owner.sendMessage(Bot.COMMAND_IDENTIFIER + "register " + password + password);
       //     owner.sendMessage(Bot.COMMAND_IDENTIFIER + "login " + password);
        }
    }
}

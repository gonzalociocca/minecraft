//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.spigotmc;

import net.minecraft.server.v1_8_R3.MinecraftServer;

public class AsyncCatcher {
    public static boolean enabled = true;

    public AsyncCatcher() {
    }

    public static void catchOp(String reason) {
        /*
        if(enabled && Thread.currentThread() != MinecraftServer.getServer().primaryThread) {
            throw new IllegalStateException("Asynchronous " + reason + "!");
        }*/
    }
}

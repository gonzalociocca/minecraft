# BungeePluginRestarter
This plugin gives you the ability to restart plugins in your proxy without using /end.

## Usage
If you aren't a developer, you can use `/restartplugin <Plugin Name>` to restart a plugin. You will need the permission `bpr.restart`.

If you are a developer, here's how simple is to use this API:

First import the project with Maven:

As I don't yet have a Maven repo, you will need to `git clone` this project and run `mvn install`.

```xml
<dependency>
    <groupId>me.hugmanrique</groupId>
    <artifactId>bungee-plugin-restarter</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

And then you can use the API! Here's how it works:

```java
public class MyAwesomePlugin extends net.md_5.bungee.api.plugin.Plugin {
    public void restartMe(Plugin plugin){
        me.hugmanrique.bpr.data.PluginRestart pluginRestart = new PluginRestart(this);
        
        boolean success = pluginRestart.run();
        getLogger().log(java.util.logging.Level.INFO, "Plugin reloaded? {0}", success);
    }   
}
```

You can also listen for `PlayerRestartJoinEvent` and `PlayerRestartQuitEvent` if your plugin will be restarted to save player data (Do it synchronously!). You sould check for `event#shouldFire` to see if your plugin is the one that is being restarted or if it's another.

## How it works?
You can go to the [PluginRestart](src/main/java/me/hugmanrique/bpr/data/PluginRestart.java) class and start digging in it.
Basically, a custom `PlayerRestartQuitEvent` is fired if you have specified the `RestartOption.FIRE_QUIT_EVENT` in the constructor of `PluginRestart`.

You should check for the `PlayerRestartQuitEvent#shouldFire` method if you are restarting multiple plugins.

Once the event is fired, the plugin will start finding the actual `plugin.yml` of the *loaded* plugin so the `plugin.yml` won't be updated unless the hole proxy is restarted.
Then, we use reflection to create the plugin, replace it in the `PluginManager`'s plugins array and fire the `onLoad` and `onEnable` methods.

## Note:
I have not tested this plugin in any kind of Proxy. Reports of bugs are very appreciated.
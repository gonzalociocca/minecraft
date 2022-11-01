package server.command;

/*
public class CommandKit {

    /* Display
    * Rarity
    * Cost
    * ItemList
    * DescriptionList */

    /**
     * GameServer classeditor Menu > Select >
     * Open Kit Group Menu > Select > Create Kit Menu |
     * Open Kit Menu > Select Display, Rarity, Cost, ItemList, DescriptionList
     */
/*
    public static boolean onCommand(GameAPI Manager, CommandSender sender, Command command, String label, String[] args) {
        if (command.getId().equalsIgnoreCase("kit")) {


            return true;
        }
        return false;
    }

    public static void initialize(GameAPI Manager) {
        createGenerator(Manager);
    }

    public static void createGenerator(GameAPI Manager) {
        MenuAPI.MenuGenerator menuGenerator = new MenuAPI.MenuGenerator() {

            @Override
            public boolean onCommand(String[] args) {
                return args.length == 2 && args[0].equalsIgnoreCase("/kit") && args[1].equalsIgnoreCase("edit");
            }

            @Override
            public Menu createMenu(GameAPI manager, Player player, Menu.OpenReason openReason) {
                Menu toreturn = new MenuAPI.PlayerMenu(manager, player, "&aSeleccionar Modalidad", openReason);
                for (final InstanceData gameType : InstanceData.values()) {
                    final KitData kitData = manager.getGameDataManager().getOrCreateContainer(gameType).getOrCreateKitData();

                    toreturn.add(Code.makeItemStack(Material.PAPER, gameType.getShortName()), new Menu.ClickCallableMenuOpen(
                            new MenuAPI.MenuGenerator() {
                                @Override
                                public Menu createMenu(GameAPI manager, Player player, Menu.OpenReason openReason) {
                                    Menu toreturn = new MenuAPI.PlayerMenu(manager, player, "&aSeleccionar Grupo", openReason);
                                    toreturn.add(Code.makeItemStack(Material.NETHER_STAR, UtilMsg.Green + "Crear Grupo"), new Menu.ClickCallable() {
                                        @Override
                                        public void onClick(GameAPI manager, Player player, Menu menu, ItemStack itemStack, InventoryClickEvent event) {
                                            super.onClick(manager, player, menu, itemStack, event);
                                            player.closeInventory();
                                            player.sendMessage(UtilMsg.Green + "&aEscribe el nombre de grupo a crear en el chat.");
                                            manager.getMenuManager().addChatCallable(player.getId(), new MenuAPI.ChatCallable() {

                                                @Override
                                                public boolean onChat(Player player, String message) {
                                                    kitData.createGroup(message);
                                                    return true;
                                                }
                                            });
                                            
                                        }
                                    });

                                    for (final Map.Entry<String, List<KitInfo>> entry : kitData.getMap().entrySet()) {
                                        toreturn.add(Code.makeItemStack(Material.BOOK, entry.getKey()), new Menu.ClickCallableMenuOpen(new MenuAPI.MenuGenerator() {
                                            @Override
                                            public Menu createMenu(GameAPI manager, Player player, Menu.OpenReason openReason) {
                                                Menu toreturn = new MenuAPI.PlayerMenu(manager, player, "&aSeleccionar Kit", openReason);
                                                toreturn.add(Code.makeItemStack(Material.BARRIER, UtilMsg.Red + "Borrar Grupo"), new Menu.ClickCallable() {
                                                    @Override
                                                    public void onClick(GameAPI manager, Player player, Menu menu, ItemStack itemStack, InventoryClickEvent event) {
                                                        super.onClick(manager, player, menu, itemStack, event);
                                                        player.closeInventory();
                                                        kitData.getMap().remove(entry.getKey());
                                                    }
                                                });

                                                toreturn.add(Code.makeItemStack(Material.SLIME_BLOCK, UtilMsg.Green + "Crear Kit"), new Menu.ClickCallable() {
                                                    @Override
                                                    public void onClick(final GameAPI manager, Player player, final Menu menu, ItemStack itemStack, InventoryClickEvent event) {
                                                        super.onClick(manager, player, menu, itemStack, event);
                                                                kitData.createKit(entry.getKey());
                                                                player.closeInventory();
                                                    }
                                                });
                                                for (final KitInfo kitInfo : entry.getValue()) {
                                                    toreturn.add(Code.makeItemStack(Material.DIAMOND_CHESTPLATE, kitInfo.getDisplay()+"("+kitInfo.getPosition()+")"), new Menu.ClickCallableMenuOpen(new MenuAPI.MenuGenerator() {
                                                        @Override
                                                        public Menu createMenu(GameAPI manager, Player player, Menu.OpenReason openReason) {
                                                            Menu toreturn = new MenuAPI.PlayerMenu(manager, player, "&aSeleccionar Opcion", openReason);
                                                            /* Rareza, Display, Costo, Remover, Descripcion, Items */
/*
                                                            toreturn.add(Code.makeItemStack(Material.BLAZE_POWDER, UtilMsg.Red+"Rareza: "+ UtilMsg.White+kitInfo.getRarity()), new Menu.ClickCallable(){
                                                                @Override
                                                                public void onClick(final GameAPI manager, Player player, Menu menu, ItemStack itemStack, InventoryClickEvent event) {
                                                                    super.onClick(manager, player, menu, itemStack, event);
                                                                    player.closeInventory();
                                                                    manager.getMenuManager().addChatCallable(player.getName(), new MenuAPI.ChatCallable() {
                                                                        @Override
                                                                        public boolean onChat(Player player, String message) {
                                                                            try{
                                                                                kitInfo.setRarity(Integer.parseInt(message));
                                                                            }catch (Exception e){
                                                                                e.printStackTrace();
                                                                            }

                                                                            createMenu(manager, player, Menu.OpenReason.Menu).openFor(manager, player, Menu.OpenReason.Menu);

                                                                            return true;
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                            return toreturn;
                                                        }
                                                    }));
                                                }
                                                return toreturn;
                                            }
                                        }));
                                    }
                                    return toreturn;
                                }
                            }
                    ));
                }
                return toreturn;
            }
        };


        Manager.getMenuManager().addGenerator(menuGenerator);
    }

}
*/
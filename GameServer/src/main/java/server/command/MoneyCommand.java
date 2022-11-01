package server.command;

/**
 * Created by noname on 26/4/2017.
 */
/*
public class CommandMoney {

    public static boolean onCommand(GameAPI Manager, CommandSender sender, Command command, String label, String[] args) {
        if(command.getId().equalsIgnoreCase("money")){
            if(args.length == 0){
                User pd = Manager.getPlayerData(sender.getId());
                Money money = pd.getDataManager().getMoney();
                for(InstanceData classeditor : InstanceData.values()){
                    sender.sendMessage(UtilMsg.Green+"Tienes $"+money.getValue(classeditor)+" creditos.");
                }
            } else {
                String arg1 = args[0];
                if(arg1.equalsIgnoreCase("set") && args.length >= 3 && sender.isOp()){
                    String playerName = args[1];
                    double amount = Double.parseDouble(args[2]);
                    User otherData = Manager.getPlayerData(playerName);
                    Money money = otherData.getDataManager().getMoney();

                    for(InstanceData classeditor : InstanceData.values()){
                        money.setValue(classeditor, amount);
                    }
                    sender.sendMessage(UtilMsg.Green+"$"+amount+" enviados a "+playerName);
                }
            }
            return true;
        }
        return false;
    }
    }
*/
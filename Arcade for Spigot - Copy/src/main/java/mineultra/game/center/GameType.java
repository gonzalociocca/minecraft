package mineultra.game.center;

public enum GameType
{
    DTN("DTN", 0, "Destroy the Nexus"),
    SWTeams("SWTeams", 1, "SkyWars Teams"),
    Smash("Smash", 2, "Smash"),
    BlockHunt("BlockHunt",3,"BlockHunt"),
    Quiver("Quiver",4,"One in the Quiver"),
    Spleef("Spleef",5,"SuperSpleef"),
    ZombieSurvival("ZombieSurvival",6,"Zombie Survival"),
    SkyWars("SkyWars",7,"SkyWars"),
    BuildBattle("BuildBattle",8,"Build Battle"),
    TurboRacers("TurboRacers",9,"Turbo Racers");
    
    String _name;
    String _lobbyName;
    
    private GameType(final String s, final int n, final String name) {
        this._name = name;
        this._lobbyName = name;
    }
    
    private GameType(final String s, final int n, final String name, final String lobbyName) {
        this._name = name;
        this._lobbyName = lobbyName;
    }
    
    public String GetName() {
        return this._name;
    }
    
    public String GetLobbyName() {
        return this._lobbyName;
    }
}

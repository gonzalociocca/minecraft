package mineultra.game.center;

public enum GameType
{
    BuildBattle("BuildBattle",0,"Build Battle"),
    TurboRacers("TurboRacers",1,"Turbo Racers");
    
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

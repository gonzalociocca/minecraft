package server.instance.misc;

import server.common.Code;

public enum GameState {
    Loading(Code.Color("&cCargando")),
    Recruit(Code.Color("&aReclutando")),
    Prepare(Code.Color("&eEmpezando")),
    Live(Code.Color("&cEn juego")),
    End(Code.Color("&4Terminando")),
    Dead(Code.Color("&4Reiniciando"));
    String _display;

    GameState(String display) {
        _display = display;
    }

    public String getDisplay() {
        return _display;
    }
}

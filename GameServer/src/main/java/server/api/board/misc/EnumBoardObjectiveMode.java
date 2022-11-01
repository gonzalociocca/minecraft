package server.api.board.misc;

public enum EnumBoardObjectiveMode {
    Create, Remove, Update;

    public static EnumBoardObjectiveMode getById(int id){
        return values()[id];
    }
}

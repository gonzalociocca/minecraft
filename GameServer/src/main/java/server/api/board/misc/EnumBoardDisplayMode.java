package server.api.board.misc;

public enum EnumBoardDisplayMode {
    List, Sidebar, BelowName;

    public static EnumBoardDisplayMode getById(int id){
        return values()[id];
    }
}

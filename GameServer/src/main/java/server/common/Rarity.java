package server.common;

public enum Rarity {
    Normal(1, Code.Color("&a")),
    Raro(2, Code.Color("&9")),
    Epico(3, Code.Color("&d")),
    Legendario(4, Code.Color("&6"));

    private final int _rarity;
    private final String _color;

    Rarity(int id, String color) {
        _rarity = id;
        _color = color;
    }

    public int getID() {
        return _rarity;
    }

    public String getColor(){
        return _color;
    }

    public static Rarity of(Integer id){
        for(Rarity rarity : Rarity.values()){
            if(rarity.getID() == id){
                return rarity;
            }
        }
        return Rarity.Normal;
    }
}
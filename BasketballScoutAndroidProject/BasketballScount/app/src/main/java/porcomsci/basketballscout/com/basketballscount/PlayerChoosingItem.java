package porcomsci.basketballscout.com.basketballscount;

/**
 * Created by RunFucker on 5/4/2015.
 */
public class PlayerChoosingItem {
    private String playerName;
    private String playerNumber;

    public PlayerChoosingItem(String playerName, String playerNumber) {
        super();
        this.playerName = playerName;
        this.playerNumber = playerNumber;
    }

    String getPlayerName(){
        return this.playerName;
    }

    String playerNumber(){
        return this.playerNumber;
    }

}

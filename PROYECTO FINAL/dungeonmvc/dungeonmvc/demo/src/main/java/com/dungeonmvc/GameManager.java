package com.dungeonmvc;

import java.util.ArrayList;
import java.util.Random;

import com.dungeonmvc.models.*;
import com.dungeonmvc.models.Board.Direction;
import com.dungeonmvc.utils.Vector2;

public class GameManager {

    
        /*int num;
        Random random = new Random();
        boolean mapa1[][] = {
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, true, true, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, true, false, false, true, false, true, true, true, true, true, true, false, true, true},
            {true, false, true, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, true, false, true, true, true, true, true, true, false, true, true},
            {true, false, false, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, false, false, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, true, false, true, true, true, true, true, true, false, true, true},
            {true, false, true, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, true, false, true, true, true, true, true, true, false, true, true},
            {true, false, false, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}
        };

        boolean mapa2[][] = {
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, false, false, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, true, false, true, true, true, true, true, true, false, true, true},
            {true, false, true, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, true, false, true, true, true, true, true, true, false, true, true},
            {true, false, false, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, false, false, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, true, false, true, true, true, true, true, true, false, true, true},
            {true, false, true, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, true, false, true, true, true, true, true, true, false, true, true},
            {true, false, false, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}
        };

        ArrayList<boolean[][]> mapas = new ArrayList<>();*/

        

        ArrayList<Personaje> monigotes = new ArrayList<>();

    
    private static GameManager instance;
    
    Player player;
    Enemigo enemigo;
    Board board;
    
    private GameManager(){

    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }


    public Player getPlayer() {
        return this.player;
    }

    public Enemigo getEnemigo(){
        return this.enemigo;
    }

    public Board getBoard() {
        return this.board;
    }

    public void newTurn(Direction direction){
        board.move(player, direction);
    }

    public void testGame(){
        

        player = new Player(new Vector2(0, 0),"player", "Paladin",34,65,45,47,"portrait", "item7", "item6");
        player.getInventory().addItem("item1");
        player.getInventory().addItem("item2");
        player.getInventory().addItem("item3");
        player.getInventory().addItem("item4");
        player.getInventory().addItem("item5");

        /*enemigo = new Enemigo(new Vector2(4, 4), "enemigo","Drako", 35, 12, 29, 34, "portrait" ,45);*/

        monigotes.add(player);
        monigotes.add(enemigo = new Enemigo(new Vector2(4, 4), "enemigo","Drako", 35, 12, 29, 34, "portrait" ,45));
        /*mapas.add(mapa1);
        mapas.add(mapa2);
        num = random.nextInt(0,mapas.size());*/

        /*board = new Board(mapas.get(num).length,"floor","wall");
        for (int i = 0; i < mapas.get(num).length; i++) {
            for (int j=0;j < mapas.get(num).length;j++){
                board.newCell(new Vector2(i, j), mapas.get(num)[i][j]);
            }
        }*/

        for(int i = 0; i < monigotes.size(); i++){
            monigotes.get(i);
        }
        
        boolean boardMatrix[][] = {
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, false, false, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, true, false, true, true, true, true, true, true, false, true, true},
            {true, false, true, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, true, false, true, true, true, true, true, true, false, true, true},
            {true, false, false, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, false, false, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, true, false, true, true, true, true, true, true, false, true, true},
            {true, false, true, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, true, false, true, true, true, true, true, true, false, true, true},
            {true, false, false, false, true, false, false, false, false, false, false, false, false, false, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}
        };

        board = new Board(boardMatrix.length,"floor","wall");
        for (int i = 0; i < boardMatrix.length; i++) {
            for (int j=0;j < boardMatrix[0].length;j++){
                board.newCell(new Vector2(i, j), boardMatrix[i][j]);
            }
        }
    }
}
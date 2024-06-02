package com.dungeonmvc.models;

import java.util.Random;

import com.dungeonmvc.controllers.BoardViewController;
import com.dungeonmvc.interfaces.Interactuable;
import com.dungeonmvc.utils.Vector2;

public class Objetos implements Interactuable{
    private String imagen;
    private String nombre;
    private int vida;
    private int fuerza;
    private Vector2 position;
    private int saludCofre;
    private int defensa;
    Board board;
    BoardViewController boardViewController;

    

    public Objetos(String imagen, String nombre, int vida, int fuerza, Vector2 position, int saludCofre, int defensa, Board board, BoardViewController boardViewController){
        this.imagen = imagen;
        this.nombre = nombre;
        this.vida = vida;
        this.fuerza = fuerza;
        this.position = position;
        this.saludCofre = saludCofre;
        this.defensa = defensa;
        this.board = board;
        this.boardViewController = boardViewController;
    }

    public String getImagen(){
        return imagen;
    }

    public String getNombre(){
        return nombre;
    }

    public int getVida(){
        return vida;
    }

    public int getFuerza(){
        return fuerza;
    }

    public Vector2 getPosition(){
        return position;
    }

    public int getSaludCofre(){
        return saludCofre;
    }

    public void setSaludCofre(int saludCofre){
        this.saludCofre = saludCofre;
    }

    public int getDefensa(){
        return defensa;
    }

    public void interactuar(Interactuable interactuable){
        
        Random random = new Random();
        int num = random.nextInt(3);
        //Hacemos un cast para convertir el parametreo interactuable en player, esto evitara un error de this.player is null
        Player player = (Player) interactuable;

        if(num == 0){
            int fuerza = this.getFuerza();
            int fuerzaTotal = player.getFuerza() + fuerza;
            player.setFuerza(fuerzaTotal);
            System.out.println("FUERZA EXTRA!!");
        }else if(num == 1){
            int vida = this.getVida();
            int vidaTotal = player.getPuntosVida() + vida;
            player.setPuntosVida(vidaTotal);
            System.out.println("VIDA EXTRA!!");
        }else{
            int defensa = this.getDefensa();
            int defensaTotal = player.getDefensa() + defensa;
            player.setDefensa(defensaTotal);
            System.out.println("DEFENSA EXTRA!!");
        }

        this.setSaludCofre(this.getSaludCofre() - player.getFuerza());

        if(this.saludCofre <= 0){
            board.eliminarCofre(this);
            //boardViewController.eliminarImagenCofre(this);
        }

        board.notifyObservers();
        player.notifyObservers();
    }

    
}

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
        //Guardamos en la variable num un numero al azar del 0 al 2
        int num = random.nextInt(3);
        //Hacemos un cast para convertir el parametreo interactuable en player, esto evitara un error de this.player is null
        Player player = (Player) interactuable;

        
        if(num == 0){
            //Guardamos en fuerza, la fuerza pasada por parametro que se le añadira al player 
            int fuerza = this.getFuerza();
            //Guardamos en fuerza total, el resultado de la fuerza del personaje mas la añadida
            int fuerzaTotal = player.getFuerza() + fuerza;
            //Modificamos la fuerza del personaje con el valor de la variable fuerzaTotal
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

        //Modificamos la salud del cofre a 0 cuando un jugador haya interactuado, para despues poder eliminar la imagen
        this.setSaludCofre(this.getSaludCofre() - player.getFuerza());

        //Una vez que hayamos interactuado con el cofre, la salud se pondra a 0 y llamara al metodo eliminarCofre, eeste
        //eliminara el objeto del array list
        //Tambien llamara al metodo eliminarImagenCofre para eliminar la imagen del tablero
        if(this.saludCofre <= 0){
            if(boardViewController != null){
                boardViewController.eliminarImagenCofre(this);
            }else{
                System.out.println("boardViewController is null");
            }
            board.eliminarCofre(this);
            System.out.println("COFRE ABIERTO");
            
        }

        board.notifyObservers();
        player.notifyObservers();
    }

    
}

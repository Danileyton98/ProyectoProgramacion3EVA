package com.dungeonmvc.models;

import java.util.ArrayList;

import com.dungeonmvc.GameManager;
import com.dungeonmvc.interfaces.Interactuable;
import com.dungeonmvc.interfaces.Observer;
import com.dungeonmvc.models.Board.Direction;
import com.dungeonmvc.utils.Vector2;

public class Player extends Personaje implements Interactuable{
    ArrayList<Observer> observers;
    String leftHand;
    String rightHand;
    Inventory inventory;
    boolean eliminado = false;



    public Player(Vector2 position,String image, String name, int puntosVida, int fuerza, int defensa, int velocidad, String portrait, Board board, String leftHand, String rightHand) {
        super(position,image,name,puntosVida,fuerza,defensa,velocidad,portrait,board);
        observers = new ArrayList<>();
        this.leftHand = leftHand;
        this.rightHand = rightHand;
        this.inventory = new Inventory();
    }

    public void suscribe(Observer observer){
        observers.add(observer);
    }

    public void unsuscribe(Observer observer){
        observers.remove(observer);
    }

    public void notifyObservers(){
        observers.forEach(x -> x.onChange());
    }

    public String getLeftHand() {
        return this.leftHand;
    }

    public void setLeftHand(String leftHand) {
        this.leftHand = leftHand;
        notifyObservers();
    }

    public String getRightHand() {
        return this.rightHand;
    }

    public void setRightHand(String rightHand) {
        this.rightHand = rightHand;
        notifyObservers();
    }

    public Inventory getInventory(){
        return this.inventory;
    }

    public void eliminar(){
        eliminado = true;
    }

    public boolean estaEliminado(){
        return eliminado;
    }

    public ArrayList<Observer> getObservers() {
        return this.observers;
    }

    public void setObservers(ArrayList<Observer> observers) {
        this.observers = observers;
    }

    // Comprobamos que el destino del jugador es mayor o igual a 0, esto evita que el personaje se salga del tablero
    // En segundo lugar comprobamos si el destino del jugador es suelo, esto evitara que el jugador se posicione en una casilla de pared
    //y si esta ocupada por otro jugador, esto evitara que se junten dos monigotes en la misma casilla
    public void move(Direction direction){
        Vector2 destino = board.getDestination(this.position, direction);
        if (destino.getX() >= 0 && destino.getX() < board.getSize() &&
            destino.getY() >= 0 && destino.getY() < board.getSize()){
            if(board.isFloor(destino) && !board.getCell(destino).ocupada()){
                //Liberamos la celda anterior en la que el jugador se encontraba poniendola a null
                board.getCell(this.position).setInteractuable(null);
                //Se establece la nueva posicion del jugador pasandola como argumento del metodo
                this.setPosition(destino);
                //Volvemos a convertir en interactuable la celda en la que se encuentra el jugador pasando this como argumento 
                board.getCell(destino).setInteractuable(this);
            }
        }
        //Llamamos al metodo notifyObservers de la clase board
        board.notifyObservers();
    }

    public void enemigosAdyacentes(){
        //Guardamos en la variable direccion las diferentes direcciones que el player podría tomar
        Vector2[] direccion = {
            new Vector2(0,1), new Vector2(1,0),
            new Vector2(0,-1), new Vector2(-1,0)
        };

        //Este bucle itera sobre las distintas posiciones adyacentes del jugador asegurandose de que la siguiente posicion a tomar por el
        //jugador sea valida
        for(Vector2 dir : direccion){
            Vector2 posAdyacente = new Vector2(position.getX() + dir.getX(), position.getY() + dir.getY());
            if(posAdyacente.getX() >= 0 && posAdyacente.getX() < board.getSize() &&
               posAdyacente.getY() >= 0 && posAdyacente.getY() < board.getSize()){
                //Una vez verificado, guarda en celdaAdyacente la posicion, y seguidamente comprueba si la celda adyacente esta ocupada y si es 
                //instancia de enemigo, si es asi, el siguiente metodo(interactuar) devolveria true
                Cell celdaAdyacente = board.getCell(posAdyacente);
                if(celdaAdyacente.ocupada() && celdaAdyacente.getInteractuable() instanceof Enemigo){
                    interactuar(celdaAdyacente.getInteractuable());
                }
            }
        }

        
    }

    @Override
    public void interactuar(Interactuable o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'interactuar'");
    }

    
}

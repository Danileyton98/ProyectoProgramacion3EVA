package com.dungeonmvc.models;

import java.util.ArrayList;
import java.util.Random;

import com.dungeonmvc.GameManager;
import com.dungeonmvc.interfaces.Observer;
import com.dungeonmvc.models.Board.Direction;
import com.dungeonmvc.utils.Vector2;

public class Enemigo extends Personaje{
    
    
    int percepcion;
    ArrayList<Observer> observers;
    
    public Enemigo(Vector2 position,String image, String name, int puntosVida, int fuerza, int defensa, int velocidad, String portrait, Board board, int percepcion){
        super(position,image,name,puntosVida,fuerza,defensa,velocidad,portrait,board);
        this.percepcion = percepcion;
        observers = new ArrayList<>();
    }

    public int getPercepcion(){
        return percepcion;
    }

    public void setPercepcion(int percepcion){
        this.percepcion = percepcion;
    }

    public void suscribe(Observer observer){
        observers.add(observer);
    }

    public void unsuscribe(Observer observer){
        observers.remove(observer);
    }

    public Vector2 randomEnemigo(){
        Random random = new Random();
        int pos;
        pos = random.nextInt(4);
        int destinoX = position.getX();
        int destinoY = position.getY();
        switch (pos) {
            case 0:
                destinoY--;
            break;

            case 1:
                destinoX++;
            break;

            case 2:
                destinoY++;
                break;

            case 3:
                destinoX--;
                break;

            default:
                break;
        }
        return new Vector2(destinoX, destinoY);
    }

    //Calcula la posicion del jugador con la del enemigo para mover al enemigo a una nueva coordenada persiguiendo al jugador
    public Vector2 directionEnemigo(Vector2 objetivo){
        //Almacenamos en las variables x,y la posicion del enemigo
        int posEnemigoX = position.getX();
        int posEnemigoY = position.getY();

        //Almacenamos en las variables objetivoX y objetivoY la posicion del objetivo que en este caso sería el player
        int objetivoX = objetivo.getX();
        int objetivoY = objetivo.getY();

        /*Con las variables moveX y moveY determina en que posicion debe moverse el enemigo
        Si devuelve 1 el primer valor es mayor que el segundo valor
        Si devuelve -1 el primer valor es menor que el segundo valor
        Si devuelve 0 son iguales*/
        int moveX = Integer.compare(objetivoX, posEnemigoX);
        int moveY = Integer.compare(objetivoY, posEnemigoY);

        /*Por ultimo se suma la variable x con la variable moveX y la variable y con la variable moveY para obtener las nuevas coordenadas
        hacia el jugador*/
        //Por lo tanto si moveX es positivo, significaría que el player estaria a la derecha del enemigo y se sumaria a la coordenada moveX
        return new Vector2(posEnemigoX + moveX, posEnemigoY + moveY);
    }

    
    public void moveEnemigo(Direction direction){
        Vector2 destino = randomEnemigo();
        //Almacenamos la posicion del jugador en la variable positionPlayer
        Vector2 positionPlayer = GameManager.getInstance().getPlayer().getPosition();
        //Almacenamos la distancia en la variable distancia Player con el metodo distancia de la clase Vector2
        double distanciaPlayer = position.distance(positionPlayer);
        
        if(distanciaPlayer <= percepcion){
            destino = directionEnemigo(positionPlayer);
        }else{
            destino = randomEnemigo();
        }
        // Comprobamos que el destino del jugador es mayor o igual a 0, esto evita que el personaje se salga del tablero
        // En segundo lugar comprobamos si el destino del jugador es suelo, esto evitara que el jugador se posicione en una casilla de pared
        if (destino.getX() >= 0 && destino.getX() < board.getSize() && destino.getY() >= 0 && destino.getY() < board.getSize()){
            if(board.isFloor(destino)){
                this.setPosition(destino);
            }
        }
        //Llamamos al metodo notifyObservers de la clase board
        board.notifyObservers();
    }
}

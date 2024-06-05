package com.dungeonmvc.models;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.dungeonmvc.GameManager;
import com.dungeonmvc.controllers.BoardViewController;
import com.dungeonmvc.interfaces.Interactuable;
import com.dungeonmvc.interfaces.Observer;
import com.dungeonmvc.models.Board.Direction;
import com.dungeonmvc.utils.DiceRoll.Dice;
import com.dungeonmvc.utils.Vector2;

import javafx.application.Platform;

public class Enemigo extends Personaje implements Interactuable {

    int percepcion;
    ArrayList<Observer> observers;
    Player player;
    BoardViewController boardViewController;
    ArrayList<Habilidades> habilidades = new ArrayList<>();
    int diceQuantity;
    Dice damage;

    public Enemigo(Vector2 position, String image, String name, int puntosVida, int fuerza, int defensa, int velocidad,
        String portrait, Board board, HashMap<Habilidades, Resistencias> resistencias, int diceQuantity,
        Dice damage, int percepcion, BoardViewController boardViewController, ArrayList<Habilidades> habilidades) {
        super(position, image, name, puntosVida, fuerza, defensa, velocidad, portrait, board, resistencias);
        this.percepcion = percepcion;
        observers = new ArrayList<>();
        this.boardViewController = boardViewController;
        this.habilidades = habilidades;
        this.diceQuantity = diceQuantity;
        this.damage = damage;
    }

    public int getPercepcion() {
        return percepcion;
    }

    public void setPercepcion(int percepcion) {
        this.percepcion = percepcion;
    }

    public void suscribe(Observer observer) {
        observers.add(observer);
    }

    public void unsuscribe(Observer observer) {
        observers.remove(observer);
    }

    public BoardViewController getBoardViewController(){
        return boardViewController;
    }

    public void setBoardViewController(BoardViewController boardViewController) {
        this.boardViewController = boardViewController;
    }

    public ArrayList<Habilidades> getHabilidades() {
        return habilidades;
    }

    public int getDiceQuantity() {
        return diceQuantity;
    }

    public Dice getDamage() {
        return damage;
    }

    
    public Vector2 randomEnemigo() {
        Random random = new Random();
        int pos;
        //Saca un numero al azar (0-3) y lo guarda en la variable pos, depende del numero que saque,
        //el enemigo se movera a una casilla u otra
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

    // Calcula la posicion del jugador con la del enemigo para mover al enemigo a
    // una nueva coordenada persiguiendo al jugador
    public Vector2 directionEnemigo(Vector2 objetivo) {
        // Almacenamos en las variables x,y la posicion del enemigo
        int posEnemigoX = position.getX();
        int posEnemigoY = position.getY();

        // Almacenamos en las variables objetivoX y objetivoY la posicion del objetivo
        // que en este caso sería el player
        int objetivoX = objetivo.getX();
        int objetivoY = objetivo.getY();

        /*
         * Con las variables moveX y moveY determina en que posicion debe moverse el
         * enemigo
         * Si devuelve 1 el primer valor es mayor que el segundo valor
         * Si devuelve -1 el primer valor es menor que el segundo valor
         * Si devuelve 0 son iguales
         */
        int moveX = Integer.compare(objetivoX, posEnemigoX);
        int moveY = Integer.compare(objetivoY, posEnemigoY);

        /*
         * Por ultimo se suma la variable x con la variable moveX y la variable "y" con la
         * variable moveY para obtener las nuevas coordenadas
         * hacia el jugador
         */
        // Por lo tanto si moveX es positivo, significaría que el player estaria a la
        // derecha del enemigo y se sumaria a la coordenada moveX
        return new Vector2(posEnemigoX + moveX, posEnemigoY + moveY);
    }

    public void moveEnemigo(Direction direction) {
        Vector2 destino = randomEnemigo();
        // Almacenamos la posicion del jugador en la variable positionPlayer
        Vector2 positionPlayer = GameManager.getInstance().getPlayer().getPosition();
        // Almacenamos la distancia en la variable distancia Player con el metodo
        // distancia de la clase Vector2
        double distanciaPlayer = position.distance(positionPlayer);

        if (distanciaPlayer <= percepcion) {
            destino = directionEnemigo(positionPlayer);
        } else {
            destino = randomEnemigo();
        }
        // Comprobamos que el destino del enemigo es mayor o igual a 0, esto evita que
        // el enemigo se salga del tablero
        // En segundo lugar comprobamos si el destino del enemigo es suelo, esto evitara
        // que el enemigo se posicione en una casilla de pared
        // y si esta ocupada por otro jugador, esto evitara que se junten dos monigotes
        // en la misma casilla
        if (destino.getX() >= 0 && destino.getX() < board.getSize()
                && destino.getY() >= 0 && destino.getY() < board.getSize()) {
            if (board.isFloor(destino) && !board.getCell(destino).ocupada()) {
                // Liberamos la celda en la que estaba el enemigo poniendo como argumento null
                board.getCell(this.position).setInteractuable(null);
                // Se establece la nueva posicion
                this.setPosition(destino);
                // Se activa la casilla actual en la que el enemigo se encuentra para
                // convertirla en interactuable
                board.getCell(destino).setInteractuable(this);
            }
        }
        // Llamamos al metodo notifyObservers de la clase board
        board.notifyObservers();
    }

    @Override
    public void interactuar(Interactuable interactuable) {

        Player player = (Player) interactuable;
        Random random = new Random();
        int numCaras = 0;
        int contador = 0;
        int dadoApuntar;
        int apuntar;
        int porcentajeDano;
        int totalDano;
        int diferenciaDano;
        int totalDados = 0;
        int dado;
        //Guardamos la cantidad de dados que tiene arma del player en la mano izquierda
        int cantDadosLeft = player.getLeftHand().getDiceQuantity();
        //Guardamos la cantidad de dados que tiene arma del player en la mano derecha
        int cantDadosRight = player.getRightHand().getDiceQuantity();
        //Guardamos la cantidad de dados que tiene el enemigo
        int cantDadosEnemigo = this.getDiceQuantity();
        
        int posPlayer = GameManager.getInstance().getMonigotes().indexOf(player);
        int posEnemigo = GameManager.getInstance().getMonigotes().indexOf(this);

        if (posPlayer > posEnemigo) {

            // ################# ATACA PLAYER CON LA MANO IZQUIERDA ################
            // Guardamos en la variable dadoPlayer el numero de caras que tiene el dado del
            // jugador
            Dice dadoPlayer = player.getLeftHand().getDice();
            // Damos valor a la variable numCaras con el numero de caras del dado
            if (dadoPlayer == Dice.d4) {
                numCaras = 4;
            } else if (dadoPlayer == Dice.d6) {
                numCaras = 6;
            } else if (dadoPlayer == Dice.d8) {
                numCaras = 8;
            } else if (dadoPlayer == Dice.d10) {
                numCaras = 10;
            } else if (dadoPlayer == Dice.d12) {
                numCaras = 12;
            } else if (dadoPlayer == Dice.d20) {
                numCaras = 20;
            }

            // Calculamos la punteria tirando un dado de 20 caras
            dadoApuntar = random.nextInt(20);
            // Sumanos el resultado de apuntar con la fuerza del jugador
            apuntar = dadoApuntar + player.getFuerza();
            // Bucle while para tirar la cantidad de dados que tenga el jugador
            while (contador < cantDadosLeft) {
                // Lanzamos el dado de jugador con un reultado al azar
                dado = random.nextInt(numCaras);
                // Acumulamos el valor del lanzamiento de los dados en el caso de que sea mas de
                // uno
                totalDados = totalDados + dado;
                contador++;
            }

            // Aqui he pensado en que el arma utilizada use solo una habilidad al azar, por
            // eso guardo en una variable
            // un numero al azar dentro del largo de la lista de habilidades
            int habPlayer = random.nextInt(player.getLeftHand().getHabilidades().size());
            // Guardamos en habilidadPlayer una habilidad al azar sacando el indice de la
            // anterior variable(habPlayer)
            Habilidades habilidadPlayer = player.getLeftHand().getHabilidades().get(habPlayer);
            // Aqui comprobamos si en el mapa de resistencias de enemigo existe un valor de
            // resistencia con la clave de habilidad que le hemos pasado
            Resistencias resistenciaEnemigo = this.getResistencias().get(habilidadPlayer);
            if (resistenciaEnemigo != null) {
                if (resistenciaEnemigo.equals(Resistencias.ABSORVENTE)) {
                    System.out.println("Absorvente");
                    // En porcentajeDaño, sumamos el total sacado de los dados mas el total de
                    // apuntar
                    porcentajeDano = totalDados + apuntar;
                    // Almacenamos el daño total
                    totalDano = porcentajeDano + this.getPuntosVida();
                    // Actualizamos los puntos de vida del enemigo
                    this.setPuntosVida(totalDano);
                } else if (resistenciaEnemigo.equals(Resistencias.FRAGIL)) {
                    System.out.println("Fragil");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano * 4;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaEnemigo.equals(Resistencias.INMUNE)) {
                    System.out.println("Inmune");
                } else if (resistenciaEnemigo.equals(Resistencias.IRROMPIBLE)) {
                    System.out.println("Irrompible");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano / 4;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaEnemigo.equals(Resistencias.LETAL)) {
                    System.out.println("Letal");
                    this.setPuntosVida(0);
                } else if (resistenciaEnemigo.equals(Resistencias.RESISTENTE)) {
                    System.out.println("Resistente");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano / 2;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaEnemigo.equals(Resistencias.VULNERABLE)) {
                    System.out.println("Vulnerable");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano * 2;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                }
            } else {
                System.out.println("Neutral");
                porcentajeDano = totalDados + apuntar;
                totalDano = porcentajeDano;
                if (totalDano > this.getDefensa()) {
                    diferenciaDano = totalDano - this.getDefensa();
                    this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                }
            }

            // ################# ATACA PLAYER CON LA MANO DERECHA ################
            dadoPlayer = player.getRightHand().getDice();

            if (dadoPlayer == Dice.d4) {
                numCaras = 4;
            } else if (dadoPlayer == Dice.d6) {
                numCaras = 6;
            } else if (dadoPlayer == Dice.d8) {
                numCaras = 8;
            } else if (dadoPlayer == Dice.d10) {
                numCaras = 10;
            } else if (dadoPlayer == Dice.d12) {
                numCaras = 12;
            } else if (dadoPlayer == Dice.d20) {
                numCaras = 20;
            }

            dadoApuntar = random.nextInt(20);
            apuntar = dadoApuntar + player.getFuerza();
            while (contador < cantDadosRight) {
                dado = random.nextInt(numCaras);
                totalDados = totalDados + dado;
                contador++;
            }

            habPlayer = random.nextInt(player.getRightHand().getHabilidades().size());
            habilidadPlayer = player.getRightHand().getHabilidades().get(habPlayer);
            resistenciaEnemigo = this.getResistencias().get(habilidadPlayer);
            if (resistenciaEnemigo != null) {
                if (resistenciaEnemigo.equals(Resistencias.ABSORVENTE)) {
                    System.out.println("Absorvente");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano + this.getPuntosVida();
                    this.setPuntosVida(totalDano);
                } else if (resistenciaEnemigo.equals(Resistencias.FRAGIL)) {
                    System.out.println("Fragil");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano * 4;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaEnemigo.equals(Resistencias.INMUNE)) {
                    System.out.println("Inmune");
                } else if (resistenciaEnemigo.equals(Resistencias.IRROMPIBLE)) {
                    System.out.println("Irrompible");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano / 4;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaEnemigo.equals(Resistencias.LETAL)) {
                    System.out.println("Letal");
                    this.setPuntosVida(0);
                } else if (resistenciaEnemigo.equals(Resistencias.RESISTENTE)) {
                    System.out.println("Resistente");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano / 2;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaEnemigo.equals(Resistencias.VULNERABLE)) {
                    System.out.println("Vulnerable");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano * 2;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                }
            } else {
                System.out.println("Neutral");
                porcentajeDano = totalDados + apuntar;
                totalDano = porcentajeDano;
                if (totalDano > this.getDefensa()) {
                    diferenciaDano = totalDano - this.getDefensa();
                    this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                }
            }

            // ################# ATACA ENEMIGO ################

            Dice dadoEnemigo = this.getDamage();

            if (dadoEnemigo == Dice.d4) {
                numCaras = 4;
            } else if (dadoEnemigo == Dice.d6) {
                numCaras = 6;
            } else if (dadoEnemigo == Dice.d8) {
                numCaras = 8;
            } else if (dadoEnemigo == Dice.d10) {
                numCaras = 10;
            } else if (dadoEnemigo == Dice.d12) {
                numCaras = 12;
            } else if (dadoEnemigo == Dice.d20) {
                numCaras = 20;
            }

            dadoApuntar = random.nextInt(20);
            apuntar = dadoApuntar + this.getFuerza();
            while (contador < cantDadosEnemigo) {
                dado = random.nextInt(numCaras);
                totalDados = totalDados + dado;
                contador++;
            }

            int habEnemigo = random.nextInt(this.getHabilidades().size());
            Habilidades habilidadEnemigo = this.getHabilidades().get(habEnemigo);
            Resistencias resistenciaPlayer = player.getResistencias().get(habilidadEnemigo);
            if (resistenciaPlayer != null) {
                if (resistenciaPlayer.equals(Resistencias.ABSORVENTE)) {
                    System.out.println("Absorvente");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano + player.getPuntosVida();
                    player.setPuntosVida(totalDano);
                } else if (resistenciaPlayer.equals(Resistencias.FRAGIL)) {
                    System.out.println("Fragil");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano * 4;
                    if (totalDano > player.getDefensa()) {
                        diferenciaDano = totalDano - player.getDefensa();
                        player.setPuntosVida(player.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaPlayer.equals(Resistencias.INMUNE)) {
                    System.out.println("Inmune");
                } else if (resistenciaPlayer.equals(Resistencias.IRROMPIBLE)) {
                    System.out.println("Irrompible");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano / 4;
                    if (totalDano > player.getDefensa()) {
                        diferenciaDano = totalDano - player.getDefensa();
                        player.setPuntosVida(player.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaPlayer.equals(Resistencias.LETAL)) {
                    System.out.println("Letal");
                    player.setPuntosVida(0);
                } else if (resistenciaPlayer.equals(Resistencias.RESISTENTE)) {
                    System.out.println("Resistente");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano / 2;
                    if (totalDano > player.getDefensa()) {
                        diferenciaDano = totalDano - player.getDefensa();
                        player.setPuntosVida(player.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaPlayer.equals(Resistencias.VULNERABLE)) {
                    System.out.println("Vulnerable");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano * 2;
                    if (totalDano > player.getDefensa()) {
                        diferenciaDano = totalDano - player.getDefensa();
                        player.setPuntosVida(player.getPuntosVida() - diferenciaDano);
                    }
                }
            } else {
                System.out.println("Neutral");
                porcentajeDano = totalDados + apuntar;
                totalDano = porcentajeDano;
                if (totalDano > player.getDefensa()) {
                    diferenciaDano = totalDano - player.getDefensa();
                    player.setPuntosVida(player.getPuntosVida() - diferenciaDano);
                }
            }
            System.out.println("Puntos de vida: " + this.getPuntosVida());
        } else {
            // ################# ATACA ENEMIGO ################

            Dice dadoEnemigo = this.getDamage();

            if (dadoEnemigo == Dice.d4) {
                numCaras = 4;
            } else if (dadoEnemigo == Dice.d6) {
                numCaras = 6;
            } else if (dadoEnemigo == Dice.d8) {
                numCaras = 8;
            } else if (dadoEnemigo == Dice.d10) {
                numCaras = 10;
            } else if (dadoEnemigo == Dice.d12) {
                numCaras = 12;
            } else if (dadoEnemigo == Dice.d20) {
                numCaras = 20;
            }

            dadoApuntar = random.nextInt(20);
            apuntar = dadoApuntar + this.getFuerza();
            while (contador < cantDadosEnemigo) {
                dado = random.nextInt(numCaras);
                totalDados = totalDados + dado;
                contador++;
            }

            int habEnemigo = random.nextInt(this.getHabilidades().size());
            Habilidades habilidadEnemigo = this.getHabilidades().get(habEnemigo);
            Resistencias resistenciaPlayer = player.getResistencias().get(habilidadEnemigo);
            if (resistenciaPlayer != null) {
                if (resistenciaPlayer.equals(Resistencias.ABSORVENTE)) {
                    System.out.println("Absorvente");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano + player.getPuntosVida();
                    player.setPuntosVida(totalDano);
                } else if (resistenciaPlayer.equals(Resistencias.FRAGIL)) {
                    System.out.println("Fragil");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano * 4;
                    if (totalDano > player.getDefensa()) {
                        diferenciaDano = totalDano - player.getDefensa();
                        player.setPuntosVida(player.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaPlayer.equals(Resistencias.INMUNE)) {
                    System.out.println("Inmune");
                } else if (resistenciaPlayer.equals(Resistencias.IRROMPIBLE)) {
                    System.out.println("Irrompible");
                    porcentajeDano = (totalDados * apuntar) / 100;
                    totalDano = porcentajeDano / 4;
                    if (totalDano > player.getDefensa()) {
                        diferenciaDano = totalDano - player.getDefensa();
                        player.setPuntosVida(player.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaPlayer.equals(Resistencias.LETAL)) {
                    System.out.println("Letal");
                    player.setPuntosVida(0);
                } else if (resistenciaPlayer.equals(Resistencias.RESISTENTE)) {
                    System.out.println("Resistente");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano / 2;
                    if (totalDano > player.getDefensa()) {
                        diferenciaDano = totalDano - player.getDefensa();
                        player.setPuntosVida(player.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaPlayer.equals(Resistencias.VULNERABLE)) {
                    System.out.println("Vulnerable");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano * 2;
                    if (totalDano > player.getDefensa()) {
                        diferenciaDano = totalDano - player.getDefensa();
                        player.setPuntosVida(player.getPuntosVida() - diferenciaDano);
                    }
                }
            } else {
                System.out.println("Neutral");
                porcentajeDano = totalDados + apuntar;
                totalDano = porcentajeDano;
                if (totalDano > player.getDefensa()) {
                    diferenciaDano = totalDano - player.getDefensa();
                    player.setPuntosVida(player.getPuntosVida() - diferenciaDano);
                }
            }
            // ################# ATACA PLAYER CON LA MANO IZQUIERDA ################
            Dice dadoPlayer = player.getLeftHand().getDice();

            if (dadoPlayer == Dice.d4) {
                numCaras = 4;
            } else if (dadoPlayer == Dice.d6) {
                numCaras = 6;
            } else if (dadoPlayer == Dice.d8) {
                numCaras = 8;
            } else if (dadoPlayer == Dice.d10) {
                numCaras = 10;
            } else if (dadoPlayer == Dice.d12) {
                numCaras = 12;
            } else if (dadoPlayer == Dice.d20) {
                numCaras = 20;
            }

            dadoApuntar = random.nextInt(20);
            apuntar = dadoApuntar + player.getFuerza();
            while (contador < cantDadosLeft) {
                dado = random.nextInt(numCaras);
                totalDados = totalDados + dado;
                contador++;
            }

            int habPlayer = random.nextInt(player.getLeftHand().getHabilidades().size());
            Habilidades habilidadPlayer = player.getLeftHand().getHabilidades().get(habPlayer);
            Resistencias resistenciaEnemigo = this.getResistencias().get(habilidadPlayer);
            if (resistenciaEnemigo != null) {
                if (resistenciaEnemigo.equals(Resistencias.ABSORVENTE)) {
                    System.out.println("Absorvente");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano + this.getPuntosVida();
                    this.setPuntosVida(totalDano);
                } else if (resistenciaEnemigo.equals(Resistencias.FRAGIL)) {
                    System.out.println("Fragil");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano * 4;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaEnemigo.equals(Resistencias.INMUNE)) {
                    System.out.println("Inmune");
                } else if (resistenciaEnemigo.equals(Resistencias.IRROMPIBLE)) {
                    System.out.println("Irrompible");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano / 4;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaEnemigo.equals(Resistencias.LETAL)) {
                    System.out.println("Letal");
                    this.setPuntosVida(0);
                } else if (resistenciaEnemigo.equals(Resistencias.RESISTENTE)) {
                    System.out.println("Resistente");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano / 2;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaEnemigo.equals(Resistencias.VULNERABLE)) {
                    System.out.println("Vulnerable");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano * 2;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                }
            } else {
                System.out.println("Neutral");
                porcentajeDano = totalDados + apuntar;
                totalDano = porcentajeDano;
                if (totalDano > this.getDefensa()) {
                    diferenciaDano = totalDano - this.getDefensa();
                    this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                }
            }

            // ################# ATACA PLAYER CON LA MANO DERECHA ################
            dadoPlayer = player.getRightHand().getDice();

            if (dadoPlayer == Dice.d4) {
                numCaras = 4;
            } else if (dadoPlayer == Dice.d6) {
                numCaras = 6;
            } else if (dadoPlayer == Dice.d8) {
                numCaras = 8;
            } else if (dadoPlayer == Dice.d10) {
                numCaras = 10;
            } else if (dadoPlayer == Dice.d12) {
                numCaras = 12;
            } else if (dadoPlayer == Dice.d20) {
                numCaras = 20;
            }

            dadoApuntar = random.nextInt(20);
            apuntar = dadoApuntar + player.getFuerza();
            while (contador < cantDadosRight) {
                dado = random.nextInt(numCaras);
                totalDados = totalDados + dado;
                contador++;
            }

            habPlayer = random.nextInt(player.getRightHand().getHabilidades().size());
            habilidadPlayer = player.getRightHand().getHabilidades().get(habPlayer);
            resistenciaEnemigo = this.getResistencias().get(habilidadPlayer);
            if (resistenciaEnemigo != null) {
                if (resistenciaEnemigo.equals(Resistencias.ABSORVENTE)) {
                    System.out.println("Absorvente");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano + this.getPuntosVida();
                    this.setPuntosVida(totalDano);
                } else if (resistenciaEnemigo.equals(Resistencias.FRAGIL)) {
                    System.out.println("Fragil");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano * 4;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaEnemigo.equals(Resistencias.INMUNE)) {
                    System.out.println("Inmune");
                } else if (resistenciaEnemigo.equals(Resistencias.IRROMPIBLE)) {
                    System.out.println("Irrompible");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano / 4;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaEnemigo.equals(Resistencias.LETAL)) {
                    System.out.println("Letal");
                    this.setPuntosVida(0);
                } else if (resistenciaEnemigo.equals(Resistencias.RESISTENTE)) {
                    System.out.println("Resistente");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano / 2;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                } else if (resistenciaEnemigo.equals(Resistencias.VULNERABLE)) {
                    System.out.println("Vulnerable");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano * 2;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                } else {
                    System.out.println("Neutral");
                    porcentajeDano = totalDados + apuntar;
                    totalDano = porcentajeDano;
                    if (totalDano > this.getDefensa()) {
                        diferenciaDano = totalDano - this.getDefensa();
                        this.setPuntosVida(this.getPuntosVida() - diferenciaDano);
                    }
                }

            }
            System.out.println("SALUD ENEMIGO: " + this.getPuntosVida());
        }

        //Si la salud del player llega a 0 o menos, el juego se cerraria
        if (player.getPuntosVida() <= 0) {
            Platform.exit();
            System.out.println("ELIMINADO");
        }

        // Notificamos a Observers de la clase player para actualizar los puntos de vida
        player.notifyObservers();
    }
}
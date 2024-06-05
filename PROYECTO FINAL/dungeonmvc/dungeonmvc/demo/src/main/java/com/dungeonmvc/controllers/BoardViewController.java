package com.dungeonmvc.controllers;

import java.util.ConcurrentModificationException;
import java.util.HashMap;

import com.dungeonmvc.App;
import com.dungeonmvc.GameManager;
import com.dungeonmvc.interfaces.Observer;
import com.dungeonmvc.models.Board;
import com.dungeonmvc.models.Enemigo;
import com.dungeonmvc.models.Objetos;
import com.dungeonmvc.models.Personaje;
import com.dungeonmvc.utils.Vector2;
import com.dungeonmvc.utils.Vector2Double;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class BoardViewController implements Observer{
    @FXML
    Pane pane;
    @FXML
    GridPane grid;

    private Board board;
    private double cellSize;
    private double boardSize;
 
    private ImageView personajeImg;
    private ImageView potenciadorImg;
    
    HashMap<Personaje,ImageView> cargarImagenPersonaje = new HashMap<>();
    HashMap<Objetos,ImageView> cargarImagenCofre = new HashMap<>();

    @FXML
    public void initialize() {
        System.out.println("Board controller loaded");
        board = GameManager.getInstance().getBoard();
        board.suscribe(this);
    }

    public void setUp(){
        if(board == null){
            System.out.println("Board is not initialized");
        }
        int cellNumber = board.getSize();
        cellSize=boardSize/cellNumber;
        System.out.println(cellSize);
        for (int i = 0; i < cellNumber; i++) {
            grid.addRow(i);
            grid.addColumn(i);
        }
        

        for (int row = 0; row < cellNumber; row++) {
            for (int col = 0; col < cellNumber; col++) {
                ImageView boardImg = new ImageView();
                boardImg.setFitWidth(cellSize);
                boardImg.setFitHeight(cellSize);
                boardImg.setSmooth(false);
                if (board.isFloor(row, col)) {
                    boardImg.setImage(new Image(App.class.getResource("images/"+board.getFloorImage()+".png").toExternalForm(),cellSize,cellSize,true,false));
                } else {
                    boardImg.setImage(new Image(App.class.getResource("images/"+board.getWallImage()+".png").toExternalForm(),cellSize,cellSize,true,false));
                }

                grid.add(boardImg, row, col);
            }
        }

        //Asociamos cada personaje con su imagen
        for (Personaje personaje : GameManager.getInstance().getMonigotes()){
            personajeImg = new ImageView();
            personajeImg.setFitWidth(cellSize);
            personajeImg.setFitHeight(cellSize);
            personajeImg.setImage(new Image(App.class.getResource("images/"+personaje.getImage()+".png").toExternalForm(),cellSize,cellSize,true,false));
            personajeImg.setSmooth(false);
            pane.getChildren().add(personajeImg);
            cargarImagenPersonaje.put(personaje, personajeImg);
        }

        for(Objetos objeto : GameManager.getInstance().getPotenciadores()){
            potenciadorImg = new ImageView();
            potenciadorImg.setFitWidth(cellSize);
            potenciadorImg.setFitHeight(cellSize);
            potenciadorImg.setImage(new Image(App.class.getResource("images/"+objeto.getImagen()+".png").toExternalForm(),cellSize,cellSize,true,false));
            potenciadorImg.setSmooth(false);
            pane.getChildren().add(potenciadorImg);
            cargarImagenCofre.put(objeto, potenciadorImg);
        }
        
        onChange();
    }

    //Metodo de actualización de imagenes para cuando haya cambios en el juego
    @Override
    public void onChange() {

        try{
            for(Personaje personaje : GameManager.getInstance().getMonigotes()){
                ImageView imageView = cargarImagenPersonaje.get(personaje);
    
                if(imageView != null){
                    Vector2Double newPos = matrixToInterface(personaje.getPosition());
                    //System.out.println(newPos);
                    imageView.setLayoutX(newPos.getX());
                    imageView.setLayoutY(newPos.getY());
                }
    
                //Si la salud del enemigo llega a 0 o menos, entraria en el if y supuestamente llamaria al metodo eliminarImagen y eliminarEnemigo
                if(personaje instanceof Enemigo && personaje.getPuntosVida()<=0){
                    eliminarImagen((Enemigo) personaje);
                    board.getCell(personaje.getPosition()).setInteractuable(null);
                    board.eliminarEnemigo((Enemigo) personaje);
                    
                }
                
            }
    
    
            for(Objetos objeto : GameManager.getInstance().getPotenciadores()){
                ImageView imageView = cargarImagenCofre.get(objeto);
                Vector2Double pos = matrixToInterface(objeto.getPosition());
                imageView.setLayoutX(pos.getX());
                imageView.setLayoutY(pos.getY());
    
                //Una vez que hayamos interactuado con el cofre, la salud se pondra a 0 y llamara al metodo eliminarCofre, eeste
                //eliminara el objeto del array list
                //Tambien llamara al metodo eliminarImagenCofre para eliminar la imagen del tablero
                if(objeto instanceof Objetos && objeto.getSaludCofre() <= 0){
                    eliminarImagenCofre((Objetos) objeto);
                    board.getCell(objeto.getPosition()).setInteractuable(null);
                    board.eliminarCofre((Objetos)objeto);
                }
        }
        }catch(ConcurrentModificationException e){
            System.out.println("SIUU!!");
        }

        
}

    @Override
    public void onChange(String... args) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onChange'");
    }
    
    private Vector2Double matrixToInterface(Vector2 coord){
        return new Vector2Double(cellSize*coord.getX(),cellSize*coord.getY());
    }

    public void setBoardSize(double boardSize){
        this.boardSize=boardSize;
    }


    public void eliminarImagen(Enemigo enemigo){
        //Guardamos en imageView la imagen del enemigo pasada por parametro
        ImageView imageView = cargarImagenPersonaje.get(enemigo);
        //Con el metodo remove, eliminamos la imagen del tablero
        pane.getChildren().remove(imageView);
        //Eliminamos al enemigo del HashMap de personajes
        cargarImagenPersonaje.remove(enemigo);
    }

    public void eliminarImagenCofre(Objetos objeto){
        ImageView imageView = cargarImagenCofre.get(objeto);
        pane.getChildren().remove(imageView);
        cargarImagenCofre.remove(objeto);
    }
}
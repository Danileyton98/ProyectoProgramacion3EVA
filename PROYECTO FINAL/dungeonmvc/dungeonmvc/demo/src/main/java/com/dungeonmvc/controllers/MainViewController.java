package com.dungeonmvc.controllers;

import java.io.IOException;
import com.dungeonmvc.App;
import com.dungeonmvc.GameManager;
import com.dungeonmvc.models.Board.Direction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class MainViewController {
    @FXML
    Button restartButton;
    @FXML
    Pane inventoryPane;
    @FXML
    Pane playerPane;
    @FXML
    Pane boardPane;

    @FXML
    private void initialize() {
        FXMLLoader boardLoader = new FXMLLoader(App.class.getResource("boardView.fxml"));
        FXMLLoader inventoryLoader = new FXMLLoader(App.class.getResource("inventoryView.fxml"));
        FXMLLoader playerLoader = new FXMLLoader(App.class.getResource("playerView.fxml"));
        Pane boardView, inventoryView, playerView;
        try {
            boardView = boardLoader.load();
            inventoryView = inventoryLoader.load();
            playerView = playerLoader.load();

            // Agregar la vista del tablero al panel correspondiente
            boardPane.getChildren().add(boardView);
            inventoryPane.getChildren().add(inventoryView);
            playerPane.getChildren().add(playerView);

            // Ajustar el tamaño de la vista del tablero para que se ajuste al panel
            boardView.prefWidthProperty().bind(boardPane.widthProperty());
            boardView.prefHeightProperty().bind(boardPane.heightProperty());
            BoardViewController bvc = boardLoader.getController();
            double boardSize = boardPane.getPrefHeight();
            bvc.setBoardSize(boardSize);
            bvc.setUp();
        } catch (IOException e) {
            e.printStackTrace();
        }

        restartButton.setOnAction(actionEvent -> {
            System.out.println("Restart button pressed");
        });

        boardPane.setOnMouseClicked(eventHandler -> {
            boardPane.requestFocus();
        });

        boardPane.setOnKeyPressed(event -> {
            Direction direction = null;
            if (event.getCode() == KeyCode.UP) {
                
                direction = Direction.UP;
            }if (event.getCode() == KeyCode.DOWN) {
                
                direction = Direction.DOWN;
            }if (event.getCode() == KeyCode.LEFT) {
                
                direction = Direction.LEFT;
            }if (event.getCode() == KeyCode.RIGHT) {
                
                direction = Direction.RIGHT;
            }
            GameManager.getInstance().newTurn(direction);
        });
    }

}

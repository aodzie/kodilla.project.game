package com.kodilla.project.game;

import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

    public class Board extends Parent {
        private VBox rows = new VBox();
        private boolean enemy = false;
        public int shipsNumber = 5;
        private Image imagex = new Image("file:src/main/resources/imagex.png");
        private Image dot = new Image("file:src/main/resources/dot.png");

        public Board(boolean enemy, EventHandler<? super MouseEvent> handler) {
            this.enemy = enemy;
            for (int y = 0; y < 10; y++) {
                HBox row = new HBox();
                for (int x = 0; x < 10; x++) {
                    Cell c = new Cell(x, y, this);
                    c.setOnMouseClicked(handler);
                    row.getChildren().add(c);
                }
                rows.getChildren().add(row);
            }
            getChildren().add(rows);
        }

        public boolean placeShip(Ship ship, int x, int y) {
            if (canPlaceShip(ship, x, y)) {
                int length = ship.type;
                for (int i = y; i < y + length; i++) {
                    Cell cell = getCell(x, i);
                    cell.ship = ship;
                    if (!enemy) {
                        cell.setFill(Color.WHITE);
                        cell.setStroke(Color.BLACK);
                    }
                } return true;
            } return false;
        }

        public Cell getCell(int x, int y) {
            return (Cell) ((HBox) rows.getChildren().get(y)).getChildren().get(x);
        }

        private Cell[] getNeighbors(int x, int y) {
            Point2D[] points = new Point2D[]{
                    new Point2D(x - 1, y),
                    new Point2D(x + 1, y),
                    new Point2D(x, y - 1),
                    new Point2D(x, y + 1)
            };

            List<Cell> neighbors = new ArrayList<Cell>();

            for (Point2D p : points) {
                if (isValidPoint(p)) {
                    neighbors.add(getCell((int) p.getX(), (int) p.getY()));
                }
            } return neighbors.toArray(new Cell[0]);
        }

        private boolean canPlaceShip(Ship ship, int x, int y) {
            int length = ship.type;

            for (int i = y; i < y + length; i++) {
                if (!isValidPoint(x, i))
                    return false;

                Cell cell = getCell(x, i);
                if (cell.ship != null)
                    return false;

                for (Cell neighbor : getNeighbors(x, i)) {
                    if (!isValidPoint(x, i))
                        return false;

                    if (neighbor.ship != null)
                        return false;
                }
            } return true;
        }

        private boolean isValidPoint(Point2D point) {
            return isValidPoint(point.getX(), point.getY());
        }

        private boolean isValidPoint(double x, double y) {
            return x >= 0 && x < 10 && y >= 0 && y < 10;
        }

        public class Cell extends Rectangle {
            public int x, y;
            public Ship ship = null;
            public boolean wasShot = false;

            private Board board;

            public Cell(int x, int y, Board board) {
                super(30, 30);
                this.x = x;
                this.y = y;
                this.board = board;
                setFill(Color.GRAY);
                setStroke(Color.BLACK);
            }

            public boolean shoot() {
                wasShot = true;
                setFill(new ImagePattern(dot, 0, 0, 1, 1, true));

                if (ship != null) {
                    ship.shoot();
                    setFill(new ImagePattern(imagex, 0, 0, 1, 1, true));
                    if (!ship.isAlive()) {
                        board.shipsNumber--;
                    } return true;
                } return false;
            }
        }
    }
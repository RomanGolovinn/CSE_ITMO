package io.ui.gui.utils;

import javafx.animation.ScaleTransition;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import models.Flat;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MapVisualizer {

    private final Pane mapPane;
    private final TableView<Flat> table;
    private final Runnable onUpdateRequested;
    private final Map<Integer, Color> userColors = new HashMap<>();
    private final Random random = new Random();

    public MapVisualizer(Pane mapPane, TableView<Flat> table, Runnable onUpdateRequested) {
        this.mapPane = mapPane;
        this.table = table;
        this.onUpdateRequested = onUpdateRequested;
    }

    public void draw(Collection<Flat> flats) {
        double width = mapPane.getWidth();
        double height = mapPane.getHeight();

        if (width == 0 || height == 0) return;

        double centerX = width / 2;
        double centerY = height / 2;

        if (mapPane.getChildren().isEmpty()) {
            Line xAxis = new Line(0, centerY, width, centerY);
            Line yAxis = new Line(centerX, 0, centerX, height);
            xAxis.setStroke(Color.LIGHTGRAY);
            yAxis.setStroke(Color.LIGHTGRAY);
            mapPane.getChildren().addAll(xAxis, yAxis);
        } else {
            Line xAxis = (Line) mapPane.getChildren().get(0);
            Line yAxis = (Line) mapPane.getChildren().get(1);
            xAxis.setStartY(centerY);
            xAxis.setEndY(centerY);
            xAxis.setEndX(width);
            yAxis.setStartX(centerX);
            yAxis.setEndX(centerX);
            yAxis.setEndY(height);
        }

        Set<String> currentFlatsIds = new HashSet<>();

        for (Flat flat : flats) {
            String circleId = "flat_" + flat.getId();
            currentFlatsIds.add(circleId);

            double screenX = centerX + flat.getCoordinates().getX();
            double screenY = centerY - flat.getCoordinates().getY();
            double radius = Math.max(5, Math.sqrt(flat.getArea()) * 2);

            Circle existingCircle = (Circle) mapPane.lookup("#" + circleId);

            if (existingCircle != null) {
                existingCircle.setCenterX(screenX);
                existingCircle.setCenterY(screenY);
                existingCircle.setRadius(radius);
            } else {
                int ownerId = flat.getOwnerId();
                Color userColor = userColors.computeIfAbsent(ownerId, id ->
                        Color.color(random.nextDouble() * 0.8, random.nextDouble() * 0.8, random.nextDouble() * 0.8)
                );

                Circle circle = new Circle(screenX, screenY, radius, userColor);
                circle.setId(circleId);
                circle.setOpacity(0.8);
                circle.setStroke(Color.BLACK);
                circle.setStrokeWidth(1);

                Tooltip tooltip = new Tooltip(
                        "ID: " + flat.getId() + "\n" +
                                "Имя: " + flat.getName() + "\n" +
                                "Площадь: " + flat.getArea() + "\n" +
                                "Владелец: " + ownerId
                );
                Tooltip.install(circle, tooltip);

                circle.setOnMouseClicked(event -> {
                    table.getSelectionModel().select(flat);
                    onUpdateRequested.run();
                });

                ScaleTransition st = new ScaleTransition(Duration.millis(500), circle);
                st.setFromX(0);
                st.setFromY(0);
                st.setToX(1);
                st.setToY(1);
                st.play();

                mapPane.getChildren().add(circle);
            }
        }

        mapPane.getChildren().removeIf(node -> {
            if (node instanceof Circle) {
                return !currentFlatsIds.contains(node.getId());
            }
            return false;
        });
    }
}
package cz.diplomka.pivovarfe.util;

import javafx.beans.property.*;
import javafx.scene.control.TableColumn;

public class TableColumnFactory {

    public static <T> void configureIntegerColumn(TableColumn<T, Integer> column, java.util.function.Function<T, Integer> extractor) {
        column.setCellValueFactory(cellData -> new SimpleIntegerProperty(extractor.apply(cellData.getValue())).asObject());
    }

    public static <T> void configureDoubleColumn(TableColumn<T, Double> column, java.util.function.Function<T, Double> extractor) {
        column.setCellValueFactory(cellData -> new SimpleDoubleProperty(extractor.apply(cellData.getValue())).asObject());
    }

    public static <T> void configureBooleanColumn(TableColumn<T, Boolean> column, java.util.function.Function<T, Boolean> extractor) {
        column.setCellValueFactory(cellData -> new SimpleBooleanProperty(extractor.apply(cellData.getValue())).asObject());
    }

    public static <T> void configureStringColumn(TableColumn<T, String> column, java.util.function.Function<T, String> extractor) {
        column.setCellValueFactory(cellData -> new SimpleStringProperty(extractor.apply(cellData.getValue())));
    }
}


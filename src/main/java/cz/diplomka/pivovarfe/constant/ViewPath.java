package cz.diplomka.pivovarfe.constant;

public enum ViewPath {
    MAIN_VIEW("/cz/diplomka/pivovarfe/view/main-view.fxml"),
    RECIPE_DETAIL("/cz/diplomka/pivovarfe/view/recipe-detail.fxml"),
    HARDWARE("/cz/diplomka/pivovarfe/view/hardware-view.fxml"),
    RECIPE_LIST("/cz/diplomka/pivovarfe/view/recipe-list-view.fxml"),
    CREATE("/cz/diplomka/pivovarfe/view/create-view.fxml"),
    START("/cz/diplomka/pivovarfe/view/start-view.fxml");

    private final String path;

    ViewPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}


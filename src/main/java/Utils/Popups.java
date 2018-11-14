package Utils;

import javafx.scene.control.Alert;

/**
 * A class for easily creating Popups
 *
 * @author Leuschner
 */

public class Popups {

    /**
     * You shall not initialize this class
     */
    private Popups() {
        System.exit(1);
        //Das lassen wir uns nicht bieten!
    }

    /**
     *
     * @param alertType
     * @param msg
     * @param title
     */
    public static void createPopup(final Alert.AlertType alertType, final String msg, final String title) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

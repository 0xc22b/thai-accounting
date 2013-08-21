package gwt.client.ui;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;

public class CustomDoubleRenderer extends AbstractRenderer<Double> {
    private static CustomDoubleRenderer INSTANCE;

    /**
     * Returns the instance.
     */
    public static Renderer<Double> instance() {
      if (INSTANCE == null) {
        INSTANCE = new CustomDoubleRenderer();
      }
      return INSTANCE;
    }

    protected CustomDoubleRenderer() {
    }

    public String render(Double object) {
      if (object == null) {
        return "";
      }

      return NumberFormat.getFormat("#,##0.00").format(object);
    }
}

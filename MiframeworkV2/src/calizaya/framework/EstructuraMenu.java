package calizaya.framework;

import java.util.List;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.AsynchronousTextGUIThread;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Panels;
import com.googlecode.lanterna.gui2.SeparateTextGUIThread;
import com.googlecode.lanterna.gui2.Separator;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

public final class EstructuraMenu {

	private List<Action> acciones;
	private Action accionActual = null;

	public EstructuraMenu(List<Action> acciones) {
		this.acciones = acciones;
	}

	public void cargarMenu() {
		Screen screen = null;
		Label labelEscritura = new Label("");
		Button button = null;
		try {
			screen = new DefaultTerminalFactory().createScreen();
			screen.startScreen();

			MultiWindowTextGUI textGUI = createTextGUI(screen);
			textGUI.setBlockingIO(false);
			textGUI.setEOFWhenNoWindows(true);
			textGUI.isEOFWhenNoWindows();

			final BasicWindow window = new BasicWindow();
			Panel contentPane = new Panel(new BorderLayout());

			Panel panel = Panels.vertical(
					new Separator(Direction.HORIZONTAL)
							.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill)),
					new MultiColorComponent(), new Button("Salir", window::close));

			panel.addComponent(1, labelEscritura);

			button = new Button("Ejecutar", new Runnable() {
				@Override
				public void run() {
					accionActual.ejecutar();
				}
			});
			panel.addComponent(2, button);

			contentPane.addComponent(panel);
			window.setComponent(contentPane);
			MenuBar menubar = new MenuBar();
			window.setMenuBar(menubar);

			Menu menuFile = new Menu("Acciones");
			menubar.add(menuFile);
			// cargar acciones en el menu
			for (int i = 0; i < acciones.size(); i++) {
				Action accion = acciones.get(i);
				menuFile.add(new MenuItem(accion.nombreItemMenu(), new Runnable() {

					@Override
					public void run() {
						labelEscritura.setText(accion.descripcionItemMenu());
						accionActual = accion;
					}
				}));
			}
			menuFile.add(new MenuItem("Salir", window::close));

			textGUI.addWindow(window);

			AsynchronousTextGUIThread guiThread = (AsynchronousTextGUIThread) textGUI.getGUIThread();
			guiThread.start();

			guiThread.waitForStop();
			screen.stopScreen();
		} catch (Exception e) {
			labelEscritura.setText("ERROR");
		}

	}

	private static MultiWindowTextGUI createTextGUI(Screen screen) {
		return new MultiWindowTextGUI(new SeparateTextGUIThread.Factory(), screen);
	}

	private class MultiColorComponent extends AbstractComponent<MultiColorComponent> {
		@Override
		protected ComponentRenderer<MultiColorComponent> createDefaultRenderer() {
			return new ComponentRenderer<MultiColorComponent>() {
				@Override
				public TerminalSize getPreferredSize(MultiColorComponent component) {
					return new TerminalSize(85, 15);
				}

				@Override
				public void drawComponent(TextGUIGraphics graphics, MultiColorComponent component) {
					graphics.applyThemeStyle(getTheme().getDefaultDefinition().getNormal());
					graphics.fill(' ');
				}
			};
		}
	}

}

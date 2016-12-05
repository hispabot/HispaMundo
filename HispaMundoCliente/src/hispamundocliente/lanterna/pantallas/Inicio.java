/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hispamundocliente.lanterna.pantallas;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.TextImage;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import hispamundocliente.Configuracion;
import hispamundocliente.lanterna.utilidades.Utilidades;
import hispamundocomunicacion.HispaMundoComunicacion;
import java.io.IOException;


/**
 *
 * @author ANONA
 */
public class Inicio {

    private final Utilidades utilidades = new Utilidades();
    private final Configuracion configuracion = new Configuracion();

    private HispaMundoComunicacion hmc;

    private Screen screen;
    
    private void dibujaEntrada(){
        
    }

    public void init() {
        try {
           

            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);

            //Setup a standard Screen
            screen.startScreen();
            screen.setCursorPosition(null);
            TextImage image = utilidades.dibujaArchivo();
            TextGraphics screenGraphics = screen.newTextGraphics();
            screenGraphics.setBackgroundColor(TextColor.Indexed.fromRGB(0, 0, 0));
            screenGraphics.fill(' ');
            //int medio = (terminal.getTerminalSize().getColumns() / 2) + 1;
            int center = ((terminal.getTerminalSize().getColumns() / 2) + 1) + (image.getSize().getColumns() / 2);

            for (int i = 0; i < center; i++) {
                //  medio = (terminal.getTerminalSize().getColumns() / 2) + 1;
                center = ((terminal.getTerminalSize().getColumns() / 2) + 1) + (image.getSize().getColumns() / 2);
                screenGraphics.drawImage(new TerminalPosition(terminal.getTerminalSize().getColumns() - i, 3), image);
                screen.refresh();
                Thread.sleep(5);
            }

            //medio = (terminal.getTerminalSize().getColumns() / 2) + 1;
            //center = ((terminal.getTerminalSize().getColumns() / 2) + 1) + (image.getSize().getColumns() / 2);
            String mensaje = "H I S P A M U N D O";

            int medio = (terminal.getTerminalSize().getColumns() / 2) + 1;
            for (int i = 0; i < mensaje.length(); i++) {
                String s = mensaje.substring(0, i + 1);
                screenGraphics.putString(medio - (s.length() / 2), 1, s);
                screen.refresh();
                Thread.sleep(5);
            }

            mensaje = " P R E S I O N A    U N A    T E C L A";
            for (int i = 0; i < mensaje.length(); i++) {
                screenGraphics.setBackgroundColor(TextColor.Indexed.fromRGB(utilidades.getDado(255), utilidades.getDado(255), utilidades.getDado(255)));

                String s = mensaje.substring(0, i + 1);
                screenGraphics.putString(medio - (s.length() / 2), 3, s);
                screen.refresh();
                Thread.sleep(2);

            }
            screen.readInput();

            // Create panel to hold components
            Panel panel = new Panel();
            //panel.setPosition( new TerminalPosition(10, 10) );
            panel.setLayoutManager(new GridLayout(2));
            final Label lblOutput = new Label("");
            panel.addComponent(new Label("Hostname"));
            final TextBox txtHost = new TextBox().addTo(panel);
            panel.addComponent(new Label("Port"));
            final TextBox txtPort = new TextBox().addTo(panel);
            panel.addComponent(new Label("Usuario"));
            final TextBox txtUser = new TextBox().addTo(panel);

            panel.addComponent(new Label("Password"));
            final TextBox txtPasss = new TextBox().addTo(panel);

            if (configuracion.isExisteArchivo()) {
                txtHost.setText(configuracion.getHost());
                txtPort.setText("" + configuracion.getPort());
                txtUser.setText(configuracion.getUsuario());
                txtPasss.setText(configuracion.getPassword());
            }

            panel.addComponent(new EmptySpace(new TerminalSize(0, 0)));
            new Button("Add!", new Runnable() {
                @Override
                public void run() {
                    configuracion.setHost(txtHost.getText());
                    configuracion.setPort(Integer.parseInt(txtPort.getText()));
                    configuracion.setUsuario(txtUser.getText());
                    configuracion.setPassword(txtPasss.getText());
                    configuracion.actualizaConfiguracion();

                    lblOutput.setText("Conecntado al host[" + txtHost.getText() + ":" + txtPort.getText() + "]");
                    hmc = new HispaMundoComunicacion(configuracion.getHost(), configuracion.getPort(), configuracion.getUsuario(), configuracion.getPassword());
                    //logica de conexion del socket
                    hmc.inicia();

                    if (hmc.estaConectado()) {
                        try {
                            screen.stopScreen();
                        } catch (IOException ex) {

                        }
                    } else {
                        lblOutput.setText("No se pudo Conectar al host[" + txtHost.getText() + ":" + txtPort.getText() + "]");
                        
                    }

                }
            }).addTo(panel);

            panel.addComponent(new EmptySpace(new TerminalSize(10, 5)));
            panel.addComponent(lblOutput);

            // Create window to hold the panel
            BasicWindow window = new BasicWindow();
            //window.setPosition(new TerminalPosition(10, 10));
            window.setComponent(panel);

            // Create gui and start gui
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK));
            gui.addWindowAndWait(window);

            screen.stopScreen();

        } catch (Exception ex) {
            System.err.println(ex.getCause());
        }
    }

    /**
     * @return the hmc
     */
    public HispaMundoComunicacion getHmc() {
        return hmc;
    }
    
    public String getUsuario(){
        return configuracion.getUsuario();
    }
}

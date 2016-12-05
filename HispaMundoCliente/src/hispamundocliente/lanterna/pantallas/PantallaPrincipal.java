/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hispamundocliente.lanterna.pantallas;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.TextImage;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import entidades.MensajeEnvio;
import entidades.MensajePeticion;
import hispamundocliente.lanterna.utilidades.Utilidades;
import hispamundocomunicacion.HispaMundoComunicacion;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANONA
 */
public class PantallaPrincipal {

    private final Utilidades u;
    private final HispaMundoComunicacion hmc;
    private Screen screen;
    private Terminal terminal;
    private TextGraphics screenGraphics;
    private final String usuario;
    private int posX;
    private int posY;
    private TerminalSize ts;
    private TerminalPosition tpCursor;

    private boolean salir = false;

    private Runnable hiloLeerTeclado;
    private Runnable hiloLeerComandos;
    private Runnable hiloResize;

    public PantallaPrincipal(HispaMundoComunicacion hmc, String usuario) {
        this.u = new Utilidades();
        this.hmc = hmc;
        this.usuario = usuario;

    }

    public void inicia() {
        try {
            try {
                terminal = new DefaultTerminalFactory().createTerminal();

                screen = new TerminalScreen(terminal);

                screen.startScreen();
                screen.setCursorPosition(null);
                Thread.sleep(500);
                MensajePeticion mp = new MensajePeticion();

                mp.setUsuario(usuario);
                mp.setSize_x(terminal.getTerminalSize().getColumns());
                mp.setSize_y(terminal.getTerminalSize().getRows());
                mp.setAccion("dameMapaInical");

                hmc.enviaPeticion(mp);
                ts = terminal.getTerminalSize();
                tpCursor = new TerminalPosition(terminal.getTerminalSize().getColumns() / 2, terminal.getTerminalSize().getRows() / 2);

            } catch (IOException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }

            // comienza los hilos
            hiloLeerTeclado = new Runnable() {
                @Override
                public void run() {

                    leerTeclas();

                }

            };
            new Thread(hiloLeerTeclado).start();

            // comienza los hilos
            hiloLeerComandos = new Runnable() {
                @Override
                public void run() {

                    procesaComandos();

                }

            };
            new Thread(hiloLeerComandos).start();

            // comienza los hilos
            hiloResize = new Runnable() {
                @Override
                public void run() {

                    resize();

                }

            };
            new Thread(hiloResize).start();

            while (!salir) {
                Thread.sleep(1000);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void procesaComandos() {
        while (true) {
            try {
                MensajeEnvio mensajeEnvio = hmc.getMensajeEnvio();
                System.out.println("Llega el comando ->[" + mensajeEnvio.getAccion() + "]");
                screen.clear();
                ts = terminal.getTerminalSize();
                if (mensajeEnvio.getFraccion().length > 0) {
                    posX = mensajeEnvio.getPos_x();
                    posY = mensajeEnvio.getPos_y();
                    TextImage image = u.dibujaFront(mensajeEnvio.getFraccion());
                    screen.doResizeIfNecessary();

                    screenGraphics = screen.newTextGraphics();
                    screen.doResizeIfNecessary();

                    System.out.println("screenGraphics.getSize().getColumns()->" + screenGraphics.getSize().getColumns());
                    System.out.println("screenGraphics.getSize().getRows()->" + screenGraphics.getSize().getRows());

                    screenGraphics.setBackgroundColor(TextColor.Indexed.fromRGB(0, 0, 0));
                    screenGraphics.fill(' ');
                    screenGraphics.drawImage(new TerminalPosition(0, 0), image);
                    moverCursor(0, 0);

                    try {
                        screen.refresh();
                    } catch (IOException ex) {
                        Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    private void leerTeclas() {

        OUTER:
        while (true) {
            try {
                KeyStroke key = screen.readInput();

                switch (key.getKeyType()) {
                    case F5:
                        MensajePeticion mp = new MensajePeticion();
                        System.out.println("posX->" + posX);
                        System.out.println("posY->" + posY);

                        mp.setUsuario(usuario);
                        mp.setPos_x(posX);
                        mp.setPos_y(posY);
                        mp.setSize_x(terminal.getTerminalSize().getColumns());
                        mp.setSize_y(terminal.getTerminalSize().getRows());
                        mp.setAccion("getMundo");
                        hmc.enviaPeticion(mp);
                        break;
                    case EOF:
                        salir = true;
                        break OUTER;
                    case ArrowRight:
                        moverCursor(1, 0);
                        break;
                    case ArrowLeft:
                        moverCursor(-1, 0);
                        break;
                    case ArrowUp:
                        moverCursor(0, -1);
                        break;
                    case ArrowDown:
                        moverCursor(0, 1);
                        break;
                    default:
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                break;

            }
        }

    }

    private void resize() {
        while (true) {
            try {
                if (!terminal.getTerminalSize().equals(ts)) {

                    tpCursor = new TerminalPosition(terminal.getTerminalSize().getColumns() / 2, terminal.getTerminalSize().getRows() / 2);
                    MensajePeticion mp = new MensajePeticion();
                    System.out.println("R-posX->" + posX);
                    System.out.println("R-posY->" + posY);

                    mp.setUsuario(usuario);
                    mp.setPos_x(posX);
                    mp.setPos_y(posY);
                    mp.setSize_x(terminal.getTerminalSize().getColumns());
                    mp.setSize_y(terminal.getTerminalSize().getRows());
                    mp.setAccion("getMundo");
                    hmc.enviaPeticion(mp);
                    Sleep(1000);

                }
            } catch (IOException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }

            Sleep(200);

        }
    }

    private void moverCursor(int posx, int posy) {

        try {
            screenGraphics.setCharacter(tpCursor, screenGraphics.getCharacter(tpCursor).withBackgroundColor(TextColor.ANSI.BLACK));

            if (tpCursor.getColumn() + posx > 2
                    && tpCursor.getColumn() + posx < terminal.getTerminalSize().getColumns() - 2
                    && tpCursor.getRow() + posy > 2
                    && tpCursor.getRow() + posy < terminal.getTerminalSize().getRows() - 2) {

                tpCursor = new TerminalPosition(tpCursor.getColumn() + posx, tpCursor.getRow() + posy);

            } else {
                // se pide una nueva pos depende si es negativo o Positivo
                MensajePeticion mp = new MensajePeticion();
                mp.setPos_x(posX + posx);
                mp.setPos_y(posY + posy);
                mp.setSize_x(terminal.getTerminalSize().getColumns());
                mp.setSize_y(terminal.getTerminalSize().getRows());
                mp.setAccion("getMundo");
                hmc.enviaPeticion(mp);

            }

            screenGraphics.setCharacter(tpCursor, screenGraphics.getCharacter(tpCursor).withBackgroundColor(TextColor.ANSI.YELLOW));
            screen.refresh();

        } catch (Exception ex) {
            Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void Sleep(int s) {
        try {
            Thread.sleep(s);
        } catch (InterruptedException ex) {
            Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // se le pasa el caracter
    private void getInformacion(){
        TextCharacter tc = screenGraphics.getCharacter(tpCursor);
        

    }
    
}

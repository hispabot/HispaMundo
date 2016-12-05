/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mundo;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author ANONA
 */
public class Mundo {
//Instrumentation instrumentation;

    private final HashMap<String, int[]> mapJugadores;
    private final int[][] mundo;
    private final int x = 5000;
    private final int y = 5000;

    public Mundo() {
        System.out.println("Generando mundo con [" + x * y + "] casillas ...");
        mundo = new int[x][y];
        mapJugadores = new HashMap<>();

    }

    public synchronized int[] agregaUsuario(String usuario) {

        if (mapJugadores.containsKey(usuario)) {
            return mapJugadores.get(usuario);
        }
        int[] regreso = new int[2];
        int posX = getDado(x);
        int posY = getDado(y);
        mundo[posX][posY] = 5;
        regreso[0] = posX;
        regreso[1] = posY;
        mapJugadores.put(usuario, regreso);
        return regreso;

    }

    public synchronized boolean movimientoUsuario(String usuario, int[] nuevaPos) {

        if (!mapJugadores.containsKey(usuario)) {
            return false;
        }
        int[] regreso = mapJugadores.get(usuario);

        int posX = regreso[0];
        int posY = regreso[1];
        if (mundo[nuevaPos[0]][nuevaPos[1]] == 0 || mundo[nuevaPos[0]][nuevaPos[1]] == 4) {
            mundo[nuevaPos[0]][nuevaPos[1]] = 5;
            mundo[posX][posY] = 4;
            mapJugadores.put(usuario, nuevaPos);
            return true;
        }

        return false;

    }

    public synchronized boolean cortaArbol(String usuario, int[] posArbol) {

        if (!mapJugadores.containsKey(usuario)) {
            return false;
        }
        int[] regreso = mapJugadores.get(usuario);

        if (mundo[posArbol[0]][posArbol[1]] == 1 && seAlcanzaCasillas(regreso, posArbol, 1)) {
            mundo[posArbol[0]][posArbol[1]] = 0;

            return true;
        }

        return false;

    }

    public synchronized boolean construyeCamino(String usuario, int[] posCamino) {

        if (!mapJugadores.containsKey(usuario)) {
            return false;
        }
        int[] regreso = mapJugadores.get(usuario);

        if (mundo[posCamino[0]][posCamino[1]] == 1 && seAlcanzaCasillas(regreso, posCamino, 1)) {
            mundo[posCamino[0]][posCamino[1]] = 4;

            return true;
        }

        return false;

    }

    private boolean seAlcanzaCasillas(int[] casillaActal, int[] casillaAfectada, int rango) {
        return Math.abs(casillaActal[0] - casillaAfectada[0]) <= rango && Math.abs(casillaActal[1] - casillaAfectada[1]) <= rango;

    }

    public void generaMundo() {

        System.out.println("Agregando mundo arboles...");
        algoritmo3();

        generaRios3();

        System.out.println("Se el Termino el mundo ...");
        imprime("Mundo_final");
    }

    private void algoritmo3() {
        System.out.println("Algoritmo 3 para la dispercion de arboles ...");
        int contador = 0;

        int porcentaje = (this.x * this.y * 20) / 100;
        int semilla = getDado(600) + 1;
        System.out.println("Semilla[" + semilla + "]...");
        for (int i = 0; i < semilla; i++) {
            contador++;
            mundo[(int) (Math.random() * this.x)][(int) (Math.random() * this.y)] = 1;
        }
        int[][] mundoarbol = mundo; // se hace una copia del mundo, esto con fines practicos para no contaminar la prueba con arboles a la derecha 
        int iteraciones = 0;
        while (porcentaje > contador) {
            System.out.println("Trabajando...");
            iteraciones++;
            for (int i = 0; i < this.x; i++) {
                for (int j = 0; j < this.y; j++) {
                    //si tiene un arbol
                    if (mundo[i][j] == 1) {
                        // se decide si que tan prolifico es, esto en un futuro se puede ser un factor
                        int[] pos = getPossArbol(i, j);
                        if (pos[0] >= 0) {
                            mundoarbol[pos[0]][pos[1]] = 1;
                            contador++;
                        }
                    }
                }
            }
            for (int i = 0; i < this.x; i++) {
                for (int j = 0; j < this.y; j++) {
                    if (mundoarbol[i][j] == 1) {
                        mundo[i][j] = 1;
                    }
                }
            }

        }
        System.out.println("Se generaon [" + semilla + contador + "] Arboles...");

    }

    private int[] getPossArbol(int x, int y) {
        int[] regreso = new int[2];
        int factorx = x + getFactorDispercion();
        int factory = y + getFactorDispercion();
        regreso[0] = -1;
        regreso[1] = -1;
        if (factorx < this.x && factorx > 0 && factory < this.y && factory > 0) {
            if (mundo[factorx][factory] == 0) {
                regreso[0] = factorx;
                regreso[1] = factory;
            }
        }

        return regreso;

    }

    private int getFactorDispercion() {
        //Random rand = new Random();
        int factor = 50;
        int ii = -factor + (int) (Math.random() * ((factor - (-factor)) + 1));
        //System.out.println(ii);
        return ii;
    }

    public synchronized int[][] getMundo(int posX, int posY, int sizeX, int sizeY) {
        
        System.out.println("posX-["+posX+"] posY["+posY+"] sizeX["+sizeX+"] sizey["+sizeY+"]");
        int localx = 0;
        int localy = 0;
        if (posX < 0 || posY < 0 || sizeX < 0 || sizeY < 0) {
            return new int[1][1];

        }
        if (posX + sizeX < this.x && posY + sizeY < this.y) {
            localx = posX + sizeX;
            localy = posY + sizeY;
        } else {
            localx = this.x;
            localy = this.y;
        }
        int[][] mapa = new int[localx - posX][localy - posY];

        for (int i = posX; i < localx; i++) {
            for (int j = posY; j < localy; j++) {
                mapa[i - posX][j - posY] = this.mundo[i][j];
            }

        }

        return mapa;

    }

    public synchronized void imprime(String nombre) {

        try {
            BufferedImage image = new BufferedImage(this.x, this.y, BufferedImage.TYPE_3BYTE_BGR);
            for (int i = 0; i < this.x; i++) {
                for (int j = 0; j < this.y; j++) {
                    int a = mundo[i][j];
                    Color newColor = new Color(a, a, a);
                    switch (a) {
                        case 0:
                            newColor = Color.WHITE;
                            break;
                        case 1:
                            newColor = Color.GREEN;
                            break;
                        case 2:
                            newColor = Color.YELLOW;
                            break;
                        case 3:
                            newColor = Color.BLUE;
                            break;
                        case 4:
                            newColor = Color.DARK_GRAY;
                            break;
                        case 5:
                            newColor = Color.RED;
                            break;
                        default:
                            break;
                    }

                    image.setRGB(i, j, newColor.getRGB());
                }
            }
            File output = new File(nombre + ".jpg");
            ImageIO.write(image, "jpg", output);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void generaRios2() {
        System.out.println("Agregando rios [Beta]...");
        int numRios = 3;
        double amplitud;
        double periodo;
        //semilla
        int size = 100;

        for (int i = 0; i < numRios; i++) {
            int y_temp = getDado(this.y);
            int x_temp = getDado(this.x);
            mundo[x_temp][y_temp] = 2;
            periodo = 0.10;
            amplitud = 100;
            if (getDado(10) % 2 == 0 || getDado(10) % 2 == 1) {
                for (int j = y_temp + 1; j < this.y; j++) {
                    int xtemp = x_temp + getCos(j, amplitud, periodo);
                    for (int k = 0; k < size; k++) {
                        if (xtemp + k > 0 && xtemp + k < this.x) {
                            mundo[xtemp + k][j] = 2;
                        }
                    }
                }
            }
        }
    }

    private int getDado(int i) {
        return (int) (Math.random() * i);
    }

    private int getCos(int x, double a, double b) {

        double regreso = a * Math.cos(x * b);
        return (int) regreso;
    }

    /**
     *
     * @param posJugador
     * @param size_x
     * @param size_y
     * @return
     */
    public synchronized int[][] getMundoInicial(int[] posJugador, int size_x, int size_y) {

        
        
        int xtem = posJugador[0]-(size_x/2);
        int ytem = posJugador[1]-(size_y/2);
        if(xtem < 0){
            xtem=0; 
        }
        if(ytem < 0){
            ytem=0; 
        }     
        return  getMundo(xtem, ytem, size_x, size_y);
    }

    private void generaRios3() {
        int numerorios = getDado(10) + 5;
        System.out.println("Se van a genererar[" + numerorios + "] Rios");
        for (int i = 0; i < numerorios; i++) {
            int posX = getDado(x);
            int posY = getDado(y);
            int ancho = 15 + getDado(7);
            generaRama(posX, posY, ancho, 0);
        }

    }

    private void generaRama(int posX, int posY, int ancho, int profundidad) {
        List<int[]> posHijos = new ArrayList<>();
        System.out.println(" Profundidad[" + profundidad + "] Generando Rio en la cordenada[" + posX + "][" + posY + "]");
        while (true) {
            int sizeX = getDado(50) + 10;
            int sizeY = getDado(100) + 10;
            int[] resultado = generaSeccion(posX, posY, sizeX, sizeY, ancho, true);
            conutSecciones++;
            //if(conutSecciones%10==0) imprime("Mundo_" + String.format("%09d", conutSecciones)+"_"+profundidad );
            if (resultado[3] == 0) {
                break;
            }
            posX = resultado[0];
            posY = resultado[1] + 1;
            if (resultado[2] != 0) {
                posHijos.add(resultado);
            }

        }

        if (profundidad < 6 && posHijos.size() > 0) {
            int prof = profundidad + 1;
            int anchoHijo = ancho - 1;
            //System.out.println("["+prof+"]Generando ramales [" + posHijos.size() + "]");
            for (int i = 0; i < posHijos.size(); i++) {
                int[] get = posHijos.get(i);
                //  System.out.println("Generando ramal [" + get[0] + "][" + get[1] + "] " + get[2]);
                generaRama(get[0], get[1], anchoHijo, prof);
            }

        }

    }
    int conutSecciones = 0;

    public int[] generaSeccion(int posX, int posY, int sizeX, int sizeY, int ancho, boolean generaRamas) {
        int[] regreso = new int[4];
        int d = 1;
        int tempY;
        int tempX;
        int numtoque = 0;
        double b = Math.random();
        double acum = 0;
        if (getDado(10) % 2 == 0) {
            d = -1;
        }
        for (int i = 0; i < sizeY; i++) {
            int curva = getDado(3) + 1;
            tempY = posY + i;

            int xmedio = ((ancho) / 2);
            int liminf;
            int limSup;
            acum += b;
            if (d > 0) {
                liminf = posX - xmedio + (int) Math.abs(acum) + curva;
                limSup = posX + xmedio + (int) Math.abs(acum) + curva;
            } else {
                liminf = posX - xmedio - xmedio - (int) Math.abs(acum) - curva;
                limSup = posX - (int) Math.abs(acum) - curva;
            }

            for (int j = liminf; j < limSup; j++) {
                tempX = j;
                if ((tempX >= 0 && tempX < x) && (tempY >= 0 && tempY < y)) {
                    if (tempY > sizeY + posY) {
                        regreso[3] = 1;
                        return regreso;
                    }
                    if (d > 0) {
                        regreso[0] = tempX;
                    } else {
                        regreso[0] = tempX - xmedio;
                    }
                    regreso[1] = tempY;

                    if (mundo[tempX][tempY] == 3) {
                        numtoque++;
                        if (numtoque % 3 == 0) {
                            break;
                        }

                    }
                    mundo[tempX][tempY] = 3;
                } else {
                    regreso[3] = 0;
                    return regreso;
                }
            }
            if (numtoque == 15) {
                regreso[3] = 0;
                return regreso;
            }
        }

        if (getDado(4) == 3) {
            regreso[2] = d;
        }

        regreso[3] = 1;

        return regreso;
    }

}

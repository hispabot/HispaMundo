/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hispamundocliente.lanterna.utilidades;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.TextImage;
import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 *
 * @author ANONA
 */
public class Utilidades {

    //private final HashMap<Integer, TextColor> hm;
    private final HashMap<Integer, TextCharacter> hc;
    private final HashMap<Character, String> info;

    public Utilidades() {
      
        hc = new HashMap<Integer, TextCharacter>();
        hc.put(0, new TextCharacter(' ').withForegroundColor(TextColor.ANSI.BLACK));
        hc.put(1, new TextCharacter(Symbols.ARROW_UP).withForegroundColor(TextColor.ANSI.GREEN));
        hc.put(2, new TextCharacter(Symbols.DIAMOND).withForegroundColor(TextColor.ANSI.BLACK));
        hc.put(3, new TextCharacter(Symbols.BLOCK_MIDDLE).withForegroundColor(TextColor.ANSI.CYAN));
        hc.put(4, new TextCharacter(Symbols.BLOCK_SOLID).withForegroundColor(TextColor.ANSI.WHITE));
        hc.put(5, new TextCharacter(Symbols.FACE_WHITE).withForegroundColor(TextColor.ANSI.RED));
        hc.put(6, new TextCharacter('*').withForegroundColor(TextColor.ANSI.YELLOW));
        hc.put(7, new TextCharacter('|').withForegroundColor(TextColor.ANSI.BLACK));
        
        info = new HashMap<Character, String>();
        info.put(' ', "Tierra vacia");
        info.put(Symbols.ARROW_UP, "Es un arbol");
        info.put(' ', "Espacio en blanco");
        info.put(' ', "Espacio en blanco");
        info.put(' ', "Espacio en blanco");
        info.put(' ', "Espacio en blanco");
        info.put(' ', "Espacio en blanco");
        

    }

    public int getDado(int i) {
        return (int) (Math.random() * i);
    }
    
    // esta recibe una matriz de numero y lo transforma en imagen par imprimir

    public TextImage dibujaFront(int[][] array) {
        
        int xLength = array.length;
        int yLength = array[0].length;
        
        System.out.println("x_Length->"+xLength);
        System.out.println("y_Length->"+yLength);

        TextImage image = new BasicTextImage(xLength, yLength);
        TextGraphics textGraphics = image.newTextGraphics();

        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                textGraphics.setCharacter(i, j, hc.get(array[i][j]));
            }
        }
        //se arma la imagen 
        //System.out.println("image.getSize().getColumns())->"+image.getSize().getColumns());
        //System.out.println("image.getSize().getRows(())->"+image.getSize().getRows());
        return image;

    }

    public TextImage dibujaArchivo() {

        int[][] arrayImagen = new int[1000][1000];

        int x = 0;
        int y = 0;

        try {
            InputStream in = getClass().getResourceAsStream("/hispamundocliente/lanterna/recursos/Logo.txt");

            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(in));
            //reader = new BufferedReader(new FileReader(nombreArchivo));

            String linea = reader.readLine();

            while (linea != null) {

                String[] array = linea.split("|");
                for (int i = 0; i < array.length; i++) {
                    x = i;
                    arrayImagen[x][y] = Integer.parseInt(array[i]);
                }

                y++;
                linea = reader.readLine();
            }

            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();

            System.exit(-1);
        }

        TextImage image = new BasicTextImage(x, y);
        TextCharacter imageCharacter = new TextCharacter(' ');
        TextGraphics textGraphics = image.newTextGraphics();

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (arrayImagen[i][j] == 1) {
                    textGraphics.setCharacter(i, j, imageCharacter.withBackgroundColor(TextColor.ANSI.RED));
                }
                if (arrayImagen[i][j] == 2) {
                    textGraphics.setCharacter(i, j, imageCharacter.withBackgroundColor(TextColor.ANSI.YELLOW));
                }

            }

        }

        //se arma la imagen 
        return image;

    }

}

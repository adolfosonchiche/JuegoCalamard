package com.adolfo.kod.cronometro;

import java.util.Observable;


/**
 *
 * @author hectoradolfosc
 */
public class RelojLimiteTiempo extends Observable implements Runnable  {

    private int horas;
    private  int minutos;
    private  int segundos;
    private  String tiempo;

    public  RelojLimiteTiempo (int horas, int minutos, int segundos) {
        this.horas = horas;
        this.minutos = minutos;
        this.segundos = segundos;
    }

    @Override
    public  void  run () {

        try {
            while (true) {
                tiempo = "";

                if (horas < 10) {
                    tiempo += "0" + horas;
                } else  {
                    tiempo += horas;
                }

                tiempo += ":";
                if (minutos < 10) {
                    tiempo += "0" + minutos;
                } else  {
                    tiempo += minutos;
                }

                tiempo += ":";
                if (segundos < 10) {
                    tiempo += "0" + segundos;
                } else  {
                    tiempo += segundos;
                }

                this.setChanged();
                this.notifyObservers(tiempo);
                this.clearChanged();
                Thread.sleep(1000);

                segundos++;
                if (segundos == 60) {
                    minutos++;
                    segundos = 00;
                    if (minutos  == 60) {
                        horas++;
                        minutos = 00;
                    }
                }

            }

        } catch (Exception e) {
            System.out.println("ERRORS: " + e);
        }
    }

    public int getHoras() {
        return horas;
    }

    public int getMinutos() {
        return minutos;
    }

    public int getSegundos() {
        return segundos;
    }

    public String getTiempo() {
        return tiempo;
    }
}

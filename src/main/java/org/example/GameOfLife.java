package org.example;


import java.util.HashMap;
import java.util.Map;

public class GameOfLife {
    public static void main(String[] args) {
        Map<String, String> params = new HashMap<>();
        if(args.length <6){
            System.err.println("Parametros insuficientes Use: w h g s m [n]");
            return;
        }

        int largura = 0;
        int altura = 0;
        int numeroGeracoes = 0;
        int tempoEspera = 0;
        String estadoInicial =  " ";
        int direcaoMovAnimais = 0;

        String[] w = args[0].split("=");
        largura = Integer.parseInt(w[1]);

        String[] h = args[1].split("=");
        altura = Integer.parseInt(h[1]);

        String[] g = args[2].split("=");
        numeroGeracoes = Integer.parseInt(g[1]);

        String[] s = args[3].split("=");
        tempoEspera = Integer.parseInt(s[1]);

        String[] n = args[4].split("=");
        direcaoMovAnimais = Integer.parseInt(n[1]);

        String[] m = args[5].split("=", 2);
        estadoInicial = m[1];


        if (!ValidarArgs(largura, altura, numeroGeracoes, tempoEspera, estadoInicial, direcaoMovAnimais)) {
            return;
        }

        Settings configuracao = new Settings(largura, altura, numeroGeracoes, tempoEspera, direcaoMovAnimais, estadoInicial);

        configuracao.mostrarConfiguracoes();
        configuracao.carregarMapa();
        System.out.println("=== Geração 0 ===");
        configuracao.mostrarMatriz();
        System.out.println("=== Geração 0 ===");
        configuracao.mostrarMatrizEmojis();

        for (int i = 0; i < configuracao.getNumeroGeracoes(); i++) {
            try {
                Thread.sleep(configuracao.getTempoEspera());
            } catch (InterruptedException e) {
                break;
            }

            configuracao.criarNovaGeracao();
            System.out.println("=== Geração " + configuracao.geracaoAtual + " ===");
            configuracao.mostrarMatrizEmojis();
        }
    }

    private static boolean ValidarArgs( int largura, int altura, int numeroGeracoes, int tempoEspera, String estadoInicial, int direcaoMovAnimais) {
        int [] validarLargura = {5, 10, 15, 20, 40, 80};
        int [] validarAltura = {5, 10, 15, 20, 40};
        int [] validarTempoEspera = {0, 250, 500, 1000, 5000};

        boolean LarguraValida = false;
        for (int l : validarLargura) {
            if (largura == l){
                LarguraValida = true;
            }
        }
        if (!LarguraValida){
            System.err.println("Largura invalida");
            return false;
        }
        boolean AlturaValida = false;
        for (int l : validarAltura) {
            if (altura == l){
                AlturaValida = true;
            }
        }
        if (!AlturaValida){
            System.err.println("Altura invalida");
            return false;
        }

        boolean TempoEsperaValida = false;
        for (int l : validarTempoEspera) {
            if (tempoEspera == l){
                TempoEsperaValida = true;
            }
        }
        if (!TempoEsperaValida){
            System.err.println("Tempo de espera invalido");
        }

        if (numeroGeracoes <0 || numeroGeracoes > 20){
            System.err.println("Numero de gerações invalida");
            return false;
        }
        if (direcaoMovAnimais < 1 || direcaoMovAnimais > 4){
            System.err.println("Direção do movimento invalida");
            return false;
        }

        return true;
    }
}

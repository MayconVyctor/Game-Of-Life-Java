package org.example;


public class Settings {
    public static final int nada = 0;
    public static final int arvore = 1;
    public static final int agua = 3;
    public static final int animal = 2;

    private int largura;
    private int altura;
    private int numeroGeracoes;
    public int geracaoAtual = 0;
    private int tempoEspera;
    private String estadoInicial;
    private int direcaoMovAnimais = 1;
    private int[][] matriz;

    public Settings(int largura, int altura, int numeroGeracoes, int tempoEspera, int direcaoMovAnimais, String estadoInicial) {
        this.largura = largura;
        this.altura = altura;
        this.numeroGeracoes = numeroGeracoes;
        this.tempoEspera = tempoEspera;
        this.direcaoMovAnimais = direcaoMovAnimais;
        this.estadoInicial = estadoInicial;
        this.matriz = new int[altura][largura];
    }

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getNumeroGeracoes() {
        return numeroGeracoes;
    }

    public void setNumeroGeracoes(int numeroGeracoes) {
        this.numeroGeracoes = numeroGeracoes;
    }

    public int getTempoEspera() {
        return tempoEspera;
    }

    public void setTempoEspera(int tempoEspera) {
        this.tempoEspera = tempoEspera;
    }

    public String getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(String estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public int getDirecaoMovAnimais() {
        return direcaoMovAnimais;
    }

    public void setDirecaoMovAnimais(int direcaoMovAnimais) {
        this.direcaoMovAnimais = direcaoMovAnimais;
    }

    public int[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(int[][] matriz) {
        this.matriz = matriz;
    }

    public int getCelula(int x, int y){
        return matriz[y][x];
    }

    public void setCelula(int x, int y, int celula){
        matriz[y][x] = celula;
    }

    private int contarVizinho(int x, int y, int valor){
        int contador = 0;

        for (int direcaoY = -1; direcaoY <= 1; direcaoY++ ) {
            for (int direcaoX = -1; direcaoX <= 1; direcaoX++ ) {
                if (direcaoX == 0 && direcaoY == 0) {
                    continue;
                }
                int novaX = x + direcaoX;
                int novaY = y + direcaoY;

                if(novaX >= 0 && novaX < largura && novaY >= 0 && novaY < altura){
                    if(matriz[novaY][novaX] == valor){
                        contador++;
                    }
                }
            }
        }
        return contador;
    }

    private void carregarMapaDeString(String input) {
        String[] linhas = input.split("#");

        for (int y = 0; y < linhas.length && y < altura; y++) {
            String linha = linhas[y];
            for (int x = 0; x < linha.length() && x < largura; x++) {
                char c = linha.charAt(x);
                if (c >= '0' && c <= '3') {
                    matriz[y][x] = c - '0';
                } else {
                    matriz[y][x] = 0;
                }
            }
            for (int x = linha.length(); x < largura; x++) {
                matriz[y][x] = 0;
            }
        }

        for (int y = linhas.length; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                matriz[y][x] = 0;
            }
        }
    }
    private void gerarMapaAleatorio() {
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                int r = (int)(Math.random() * 4);
                matriz[y][x] = r;
            }
        }
    }
    public void carregarMapa() {
        if ("rnd".equals(estadoInicial)) {
            gerarMapaAleatorio();
        } else {
            carregarMapaDeString(estadoInicial);
        }
    }
    public void criarNovaGeracao() { //Coracao da simulaçao
        geracaoAtual++;

        int[][] novaMatriz = new int[altura][largura];

        for (int y = 0; y < altura; y++) {
            for(int x = 0; x < largura; x++){
                novaMatriz[y][x] = matriz[y][x];
            }
        }
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                if (matriz[y][x] != 0)
                    continue;

                int arvores = contarVizinho(x, y, arvore);
                int animais = contarVizinho(x, y, animal);
                int aguas = contarVizinho(x, y, agua);

//Regras Nascimento Agua
                boolean temAguaAcima = false;
                for (int direcaoX = -1; direcaoX <= 1; direcaoX++) {
                    int novoX = x + direcaoX;
                    int novoY = y - 1;
                    if ( novoY >= 0 && novoX >= 0 && novoX < largura && matriz[novoY][novoX] == agua) {
                        temAguaAcima = true;
                    }
                }
                //Verifica se ha agua na linha de cima (regra de nascimento de agua)
                if (arvores >= 2) {
                    novaMatriz[y][x] = arvore;
                }
                else if (geracaoAtual % 2 == 0 && animais == 2 && arvores >= 1 && agua >= 1) {
                    novaMatriz[y][x] = animal;
                }
                else if (geracaoAtual % 3 == 0 && temAguaAcima) {
                    novaMatriz[y][x] = agua;
                }
            }
        }
// Sobrevivência de arvores e animais
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                if (matriz[y][x] == arvore) {
                    boolean temAguaPerto = false;
                    for (int direcaoY = -2; direcaoY <= 2; direcaoY++) {
                        for (int direcaoX = -2; direcaoX <= 2; direcaoX++) {
                            int novoX = x + direcaoX;
                            int novoY = y + direcaoY;
                            if (novoX >= 0 && novoX < largura && novoY >= 0 && novoY < altura) {
                                if (matriz[novoY][novoX] == agua) {
                                    temAguaPerto = true;
                                }
                            }
                        }
                    }
                    if (!temAguaPerto) {
                        novaMatriz[y][x] = nada;
                    }
                }
//   Arvore morre se não tiver água no perimetro
                else if (matriz[y][x] == animal) {
                    boolean temArvore = false, temAgua = false;
                    for (int dy = -2; dy <= 2; dy++) {
                        for (int dx = -2; dx <= 2; dx++) {
                            int nx = x + dx;
                            int ny = y + dy;
                            if (nx >= 0 && nx < largura && ny >= 0 && ny < altura) {
                                if (matriz[ny][nx] == arvore) temArvore = true;
                                if (matriz[ny][nx] == agua) temAgua = true;
                            }
                        }
                    }
                    if (!temArvore || !temAgua) {
                        novaMatriz[y][x] = nada;
                    }
                }
            }
        }
// Movimento de animais (só em geraçoes pares)
        if (geracaoAtual % 2 == 0) {
            moverAnimais(novaMatriz);
        }

        this.matriz = novaMatriz;
    }
    private void moverAnimais(int[][] novaMatriz) {
        int direcaoX = 0, direcaoY = 0;
        switch (direcaoMovAnimais) {
            case 1 -> direcaoX = 1;
            case 2 -> direcaoY = 1;
            case 3 -> direcaoX = -1;
            case 4 -> direcaoY = -1;
        }

        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                if (matriz[y][x] != animal)
                    continue;
                int novoX = x + direcaoX;
                int novoY = y + direcaoY;

                if (novoX < 0 || novoX >= largura || novoY < 0 || novoY >= altura)
                    continue;

                int destino = novaMatriz[novoY][novoX];

                if (destino == nada || destino == arvore) {
                    novaMatriz[novoY][novoX] = animal;
                    novaMatriz[y][x] = nada;

                    if (destino == arvore) {
                    }
                }
            }
        }
    }
    public void mostrarConfiguracoes() {
        System.out.println("width = [" + largura + "]");
        System.out.println("height = [" + altura + "]");
        System.out.println("generations = [" + numeroGeracoes + "]");
        System.out.println("speed = [" + tempoEspera + "]");
        System.out.println("map = [" + estadoInicial + "]");
        System.out.println("n = " + direcaoMovAnimais);
    }
    public void mostrarMatriz(){
        for(int y = 0; y <altura; y++){
            for(int x = 0; x <largura; x++){
                System.out.print(matriz[y][x] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    public void mostrarMatrizEmojis() {
        System.out.println("Com emojis:");
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                String celula;
                switch (matriz[y][x]) {
                    case 0 -> celula = " ⬜  ";
                    case 1 -> celula = " 🌳 ";
                    case 2 -> celula = " 🐑 ";
                    case 3 -> celula = " 💧 ";
                    default -> celula = " ? ";
                }
                System.out.print(celula);
            }
            System.out.println();
        }
        System.out.println();
    }
}


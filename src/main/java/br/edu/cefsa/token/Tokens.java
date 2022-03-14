/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.cefsa.token;

import br.edu.cefsa.file.ArquivoDAO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author losan
 */
public class Tokens {

    private static final String[] operadores = new String[]{"=", "+", "-", "*", "/", "%", "*", "<", ">", "&", "|", "^", "?", "!"};
    private static final String[] pontuacao = new String[]{"{", "}", "(", ")", "[", "]", ",", ".", ";", ":"};
    private static List<Token> tokens = new ArrayList<Token>();
    private static Token ultimoToken;

    public Token getUltimoToken() {
        return ultimoToken;
    }

    public List<Token> getTokens() {
        return tokens;
    }
    private static String[] palavrasChaves;

    public static boolean isOperador(char charAtual) {
        return Arrays.stream(operadores).anyMatch(operador -> operador.equals(String.valueOf(charAtual)));
    }

    public static boolean isPontuacao(char charAtual) {
        return Arrays.stream(pontuacao).anyMatch(operador -> operador.equals(String.valueOf(charAtual)));
    }

    public Tokens() throws Exception {
        palavrasChaves = ArquivoDAO.readTextFile("palavrasReservadas.txt");
    }

    public void addToken(String palavraAtual, int posicaoInicialPalavraAtual) {
        if (palavraAtual.equals(" ")) {
            return;
        }
        Token token = new Token(getTipo(palavraAtual), palavraAtual, posicaoInicialPalavraAtual);

        if (".".equals(token.getSimbolo()) && ultimoToken.getTipo() == ETipo.Int) {
            ultimoToken.setTipo(ETipo.Float);
        }

        if ((token.getTipo() == ETipo.Operador && ultimoToken.getTipo() == ETipo.Operador)
                || (token.getTipo() == ETipo.Int && ultimoToken.getTipo() == ETipo.Int)
                || (token.getTipo() == ETipo.Int && ultimoToken.getTipo() == ETipo.Float)
                || (".".equals(token.getSimbolo()) && ultimoToken.getTipo() == ETipo.Float)) {
            ultimoToken.setSimbolo(ultimoToken.getSimbolo() + token.getSimbolo());
            return;
        }

        if (token.getTipo() == ETipo.Variavel) {
            int index = getIndex(token.getSimbolo());
            token.setIndex(index == -1 ? token.getPosicao() : index);
        }

        if (token.getTipo() == ETipo.Float || token.getTipo() == ETipo.Int || token.getTipo() == ETipo.String) {
            token.setIndex(token.getPosicao());
        }

        tokens.add(token);
        ultimoToken = token;
    }

    private static ETipo getTipo(String palavra) {
        if (Arrays.stream(palavrasChaves).anyMatch(palavra::equals)) {
            return ETipo.PalavraChave;
        }

        if (Character.isAlphabetic(palavra.charAt(0)) && ultimoToken.getTipo() == ETipo.PalavraChave && "function".equals(ultimoToken.getSimbolo())) {
            return ETipo.Funcao;
        }

        if (Character.isAlphabetic(palavra.charAt(0))) {
            return ETipo.Variavel;
        }

        if ("\"".equals(palavra.substring(0, 1)) && "\"".equals(palavra.substring(palavra.length() - 1))) {
            return ETipo.String;
        }

        if (("[".equals(palavra.substring(0, 1)) && "]".equals(palavra.substring(palavra.length() - 1)))
                || ("{".equals(palavra.substring(0, 1)) && "}".equals(palavra.substring(palavra.length() - 1)))) {
            return ETipo.Objeto;
        }

        if (palavra.chars().allMatch(letter -> Character.isDigit(letter))) {
            return ETipo.Int;
        }

        if (palavra.length() == 1 && isPontuacao(palavra.charAt(0))) {
            return ETipo.Pontuacao;
        }

        if (palavra.length() == 1 && isOperador(palavra.charAt(0))) {
            return ETipo.Operador;
        }

        return ETipo.Outro;
    }

    private static int getIndex(String simbolo) {
        Token tempToken = tokens.stream().filter(token -> token.getSimbolo().equals(simbolo)).findFirst().orElse(null);

        return tempToken == null ? -1 : tempToken.getIndex();
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package br.edu.cefsa.analisadorlexico;

import br.edu.cefsa.token.Tokens;
import br.edu.cefsa.file.ArquivoDAO;
import br.edu.cefsa.token.ETipo;
import br.edu.cefsa.token.Token;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author losan
 */
public class AnalisadorLexico {

    private static final String morte = "\n\t";
    private static boolean inComentario = false;
    private static boolean inComentarioMultiline = false;
    private static boolean inString = false;
    private static boolean inChaves = false;
    private static boolean inColchetes = false;
    private static char charAnterior = Character.MIN_VALUE;
    private static char charAtual = Character.MIN_VALUE;
    private static char charProximo = Character.MIN_VALUE;
    private static Tokens tokens;

    public static void main(String[] args) throws Exception {
        tokens = new Tokens();
        InputStream inputstream = new FileInputStream("jquery.txt");
        StringBuilder inputText = new StringBuilder();

        byte[] allBytes = inputstream.readAllBytes();

        String palavraAtual = "";
        int posicaoAtual = 0;
        int posicaoInicialPalavraAtual = 0;
        for (int i = 0; i < allBytes.length; i++) {
            charAnterior = i == 0 ? '1' : (char) allBytes[i - 1];
            charAtual = (char) allBytes[i];
            charProximo = allBytes.length == i + 1 ? '1' : (char) allBytes[i + 1];

            if (!"\\".equals(String.valueOf(charAnterior)) && "\"".equals(String.valueOf(charAtual))) {
                inString = !inString;
            }

            if (!inString && !inComentarioMultiline && !inColchetes && !inChaves && "/".equals(String.valueOf(charAtual)) && "/".equals(String.valueOf(charProximo))) {
                inComentario = true;
            }

            if (inComentario && "\n".equals(String.valueOf(charAtual))) {
                inComentario = false;
                continue;
            }

            if (!inString && !inComentario && !inColchetes && !inChaves && "/".equals(String.valueOf(charAtual)) && "*".equals(String.valueOf(charProximo))) {
                inComentarioMultiline = true;
            }

            if (inComentarioMultiline && "*".equals(String.valueOf(charAtual)) && "/".equals(String.valueOf(charProximo))) {
                inComentarioMultiline = false;
                i += 2;
                continue;
            }

            if ("[".equals(String.valueOf(charAtual)) && !tokens.getUltimoToken().getSimbolo().chars().allMatch(Character::isLetterOrDigit)) {
                inColchetes = true;
            }
            if ("]".equals(String.valueOf(charAtual)) && inColchetes) {
                inColchetes = false;
            }

            if ("{".equals(String.valueOf(charAtual)) && (!tokens.getUltimoToken().getSimbolo().chars().allMatch(Character::isLetterOrDigit) && !")".equals(tokens.getUltimoToken().getSimbolo()))) {
                inChaves = true;
            }
            if ("}".equals(String.valueOf(charAtual)) && inChaves) {
                inChaves = false;
            }

            boolean isEspacoInvalido = !inString && !inComentario && !inComentarioMultiline && " ".equals(String.valueOf(charAtual)) && (!Character.isAlphabetic(charAnterior) || !Character.isAlphabetic(charProximo));

            if (morte.contains(String.valueOf(charAtual)) || inComentarioMultiline || inComentario || isEspacoInvalido) {
                continue;
            }

            posicaoAtual++;
            posicaoInicialPalavraAtual = palavraAtual.isEmpty() ? posicaoAtual : posicaoInicialPalavraAtual;
            palavraAtual += charAtual;
            if (!inString
                    && !inColchetes
                    && !inChaves
                    && (!Character.isAlphabetic(charAtual) || (Character.isAlphabetic(charAtual) && !Character.isAlphabetic(charProximo)))) {
                tokens.addToken(palavraAtual, posicaoInicialPalavraAtual);
                inputText.append(palavraAtual);
                palavraAtual = "";
            }
        }
        inputstream.close();
        ArquivoDAO.writeToExitFile(inputText, "saida.txt");
        ArquivoDAO.writeToExitFile(tokens.getTokens(), "tabelaLexica.txt");
    }

}

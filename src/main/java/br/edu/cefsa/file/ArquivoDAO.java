/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.cefsa.file;

import br.edu.cefsa.token.Token;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

/**
 *
 * @author losan
 */
public class ArquivoDAO {

    public static String[] readTextFile(String filePath) throws Exception {
        StringBuilder inputText;
        try ( BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            inputText = new StringBuilder();
            while ((line = br.readLine()) != null) {
                inputText.append(line);
                inputText.append("\n");
            }
        }
        return inputText.toString().split("\n");
    }

    public static void writeToExitFile(StringBuilder outputText, String fileName) throws Exception {
        File file = new File(fileName);
        try ( BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(outputText.toString());
        }
    }

    public static void writeToExitFile(List<Token> tokens, String fileName) throws Exception {
        StringBuilder builder = new StringBuilder();
        tokens.forEach(token -> builder.append(token.toString()).append("\n"));
        writeToExitFile(builder, fileName);
    }
}

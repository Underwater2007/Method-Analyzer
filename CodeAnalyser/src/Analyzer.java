
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Rushi patel
 */
public class Analyzer {

    public static void main(String[] args) {
        //file reading
        String fileName = "src/test.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                System.out.println("<----------->");
                System.out.println("Method: "+currentLine);
                System.out.println("Is mehtod valid: "+isMehtodDeclaration(currentLine));
                System.out.println("Count param: "+countParameter(currentLine));
                System.out.println("Mehtod name: "+extractMethodName(currentLine));
                System.out.println("Is getter/setter? "+isGetterSetter(currentLine, countParameter(currentLine)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static boolean isMehtodDeclaration(String line) {
        line = line.trim();
        int len = line.length();
        if (line.isEmpty()) {
            return false;
        }
        if (line.endsWith(";")) {
            return false;
        }
        String[] parts = line.split(" ");
        //check for (class/new/->)
        //check for(for/while/if/switch/catch
        if (parts[0].equals("if") || parts[0].equals("for") || parts[0].equals("while") || parts[0].equals("switch")
                || parts[0].equals("catch") || line.contains("class") || line.contains("new") || line.contains("->")) {
            return false;
        }
        //find (
        int openParenIndex = -1;
        for (int i = 0; i < len; i++) {
            if (line.charAt(i) == '(') {
                openParenIndex = i;
                break;
            }

        }
        //find ) after (
        int closeParenIndex = -1;
        for (int i = openParenIndex + 1; i < len; i++) {
            if (line.charAt(i) == ')') {
                closeParenIndex = i;
                break;
            }
        }
        if (closeParenIndex == -1) {
            return false;
        }

        //find { after )
        int braceIndex = -1;
        for (int i = closeParenIndex + 1; i < len; i++) {
            if (line.charAt(i) == '{') {
                braceIndex = i;
                break;
            }
        }
        if (braceIndex == -1) {
            return false;
        }
        //skip spaces
        int index = 0;
        if (index < 0 || index >= line.length()) {
            return false;
        }
        while (index < len && Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        if (index >= len) {
            return false;
        }
        //skip access modifiers
        int start = index;
        while (index < len && !Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        String modifiers = line.substring(start, index);
        if (!modifiers.equals("private") && !modifiers.equals("public") && !modifiers.equals("protected")) {
            return false;
        }
        //skip sapces after modifiers
        while (index < len && Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        if (index >= len) {
            return false;
        }
        if (openParenIndex == -1 || closeParenIndex == -1 || openParenIndex > closeParenIndex) {
            return false;
        }
        //skip retunrn types 
        int returnType = index;
        while (index < len && !Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        String type = line.substring(returnType, index);
        return !(!type.equals("int") && !type.equals("boolean") && !type.equals("String") && !type.equals("double")&&!type.equals("void"));
    }

    static int countParameter(String line) {
        line = line.trim();
        int len = line.length();
        //find (
        int openParenIndex = -1;
        for (int i = 0; i < len; i++) {
            if (line.charAt(i) == '(') {
                openParenIndex = i;
                break;
            }

        }
        //find ) after (
        int closeParenIndex = -1;
        for (int i = openParenIndex + 1; i < len; i++) {
            if (line.charAt(i) == ')') {
                closeParenIndex = i;
                break;
            }
        }
        if (openParenIndex == -1 || closeParenIndex == -1 || openParenIndex > closeParenIndex) {
            return 0;
        }
        String paramString = line.substring(openParenIndex + 1, closeParenIndex);
        int count = 0;
        if (paramString.equals("")) {
            return 0;
        } else if (!paramString.equals("") && !paramString.contains(",")) {
            return 1;
        } else if (paramString.contains(",")) {
            for (int i = 0; i < paramString.length(); i++) {
                if (paramString.charAt(i) == ',') {
                    count++;
                }
            }
            return count + 1;
        }
        return 0;
    }
    

    static String extractMethodName(String line) {
        line = line.trim();
        int len = line.length();
        if (line.isEmpty()) {
            return null;
        }
        if (line.endsWith(";")) {
            return null;
        }
        //find (
        int openParenIndex = -1;

        for (int i = 0; i < len; i++) {
            if (line.charAt(i) == '(') {
                openParenIndex = i;
                break;
            }

        }
        if (openParenIndex == -1) {
            return null;
        }

        //find ) after (
        int closeParenIndex = -1;
        for (int i = openParenIndex + 1; i < len; i++) {
            if (line.charAt(i) == ')') {
                closeParenIndex = i;
                break;
            }
        }
        if (closeParenIndex == -1) {
            return null;
        }

        //find { after )
        int braceIndex = -1;
        for (int i = closeParenIndex + 1; i < len; i++) {
            if (line.charAt(i) == '{') {
                braceIndex = i;
                break;
            }
        }
        if (braceIndex == -1) {
            return null;
        }

        //method line
        //skip spaces
        int index = 0;
        while (index < len && Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        if (index >= len) {
            return null;
        }
        //skip access modifiers
        while (index < len && !Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        //skip sapces after modifiers
        while (index < len && Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        if (index >= len) {
            return null;
        }
        //skip retunrn types 
        while (index < len && !Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        //skip space afte return type
        if (index < len && Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        if (index >= len) {
            return null;
        }

        //extract method name 
        int nameEnd = openParenIndex - 1;
        while (nameEnd >= index && Character.isWhitespace(line.charAt(nameEnd))) {
            nameEnd--;
        }
        if (nameEnd < index) {
            return null;
        }
        String methodName = line.substring(index, nameEnd + 1);
        char firstChar = methodName.charAt(0);
        if (!Character.isLowerCase(firstChar) && !Character.isJavaIdentifierStart(firstChar)) {
            return null;
        }
        return methodName;
    }

    static boolean isGetterSetter(String line, int paramCount) {
        paramCount=countParameter(line);
        if (!isMehtodDeclaration(line)) {
            return false;
        }

        String methodName = extractMethodName(line);
        if (methodName == null) {
            return false;
        }

        boolean isGetter = (methodName.startsWith("get") && methodName.length() > 3);
        if (isGetter && paramCount == 0) {
            return true;
        }
        boolean isSetter = (methodName.startsWith("set") && methodName.length() > 3);
        return isSetter && paramCount == 1;
    }
}

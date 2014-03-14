//package compiler_prj0;
/**
 * Usage:
 * Compile the source code firstly
 * $javac MiniParser.java
 * 
 * Run the test
 * $java MiniParser test1
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

//import compiler_prj0.Token.TokenType;

public class MiniParser {	
	public int index; //For iterate the token buffer
	private String[] tokenBuffer = null;
	
	public MiniParser() {
		tokenBuffer = new String[1000];
		index = 0;
	}
	
	public String nextToken() {
		index++;
		return tokenBuffer[index];
	}
	
	public void stringToTokenBuffer(String s) {
		int tokenIndex = 0;
		String[] splitedStr = s.split(" ");
		
		for(int i = 0; i < splitedStr.length; i++)
		{
			if(splitedStr[i].equals("")) { //Splitting sometimes generate weird blank character
				continue;
			}
			else if (splitedStr[i].equals("(")) {
				tokenIndex++;
				tokenBuffer[tokenIndex] = "(";
			}
			else if (splitedStr[i].equals(")")) {
				tokenIndex++;
				tokenBuffer[tokenIndex] = ")";
			}
			else if (splitedStr[i].equals("{")) {
				tokenIndex++;
				tokenBuffer[tokenIndex] = "{";
			}
			else if (splitedStr[i].equals("}")) {
				tokenIndex++;
				tokenBuffer[tokenIndex] = "}";
			}
			else if (splitedStr[i].equals("=")) {
				tokenIndex++;
				tokenBuffer[tokenIndex] = "=";
			}
			else if (splitedStr[i].equals(";")) {
				tokenIndex++;
				tokenBuffer[tokenIndex] = ";";
			}
			else if (splitedStr[i].equals("true")) {
				tokenIndex++;
				tokenBuffer[tokenIndex] = "true";
			}
			else if (splitedStr[i].equals("false")) {
				tokenIndex++;
				tokenBuffer[tokenIndex] = "false";
			}
			else if (splitedStr[i].equals("do")) {
				tokenIndex++;
				tokenBuffer[tokenIndex] = "do";
			}
			else if (splitedStr[i].equals("while")) {
				tokenIndex++;
				tokenBuffer[tokenIndex] = "while";
			}
			else if(!isIdentifier(splitedStr[i])) {
				System.out.println("Invalid"); //Find invaild inditifier, exit immediately
				System.exit(-1);
			}
			else {
				tokenIndex++;
				tokenBuffer[tokenIndex] = "identifier";
			}
		}
	}
	
	public boolean isIdentifier(String str) {		
		char[] strChar = str.toCharArray();
		if (!Character.isJavaIdentifierStart(strChar[0])) {
			return false;
		}
		
		for (int i = 1; i < strChar.length; i++) {
			if (!Character.isJavaIdentifierPart(strChar[i])) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isExpression() {
		int pointer = index;
		String newToken = nextToken();
		
		if (newToken.equals("(")) {
			boolean leftPart = isExpression();
			
			boolean rightPart = false;
			newToken = nextToken();
			if (newToken.equals(")")) {
				rightPart = true;
			}
			
			if (leftPart && rightPart) {
				return true;
			}
			else {
				index = pointer;
				return false;
			}
		}
		else if (newToken.equals("true") ||
					newToken.equals("false") ||
					newToken.equals("identifier")) {
			return true;
			
		}
		else {
			index = pointer;
			return false;
		}
	}
	
	public boolean isAssignment() {
		int pointer = index;
		String newToken = nextToken();
		
		if (!newToken.equals("identifier")) {
			index = pointer;
			return false;
		}
		
		newToken = nextToken();
		if (!newToken.equals("=")) {
			index = pointer;
			return false;
		}
		
		if (!isExpression()) {
			index = pointer;
			return false;
		}
		
		newToken = nextToken();
		if (!newToken.equals(";")) {
			index = pointer;
			return false;
		}
		
		return true;
	}

	public boolean isDoWhile() {
		int pointer = index;
		String newToken = nextToken();
		
		if (!newToken.equals("do")) {
			index = pointer;
			return false;
		}
		
		if (!isStatement()) {
			index = pointer;
			return false;
		}
		
		newToken = nextToken();
		if (!newToken.equals("while")) {
			index = pointer;
			return false;
		}
		
		newToken = nextToken();
		if (!newToken.equals("(")) {
			index = pointer;
			return false;
		}
		
		if (!isExpression()) {
			index = pointer;
			return false;
		}
		
		newToken = nextToken();
		if (!newToken.equals(")")) {
			index = pointer;
			return false;
		}
		
		newToken = nextToken();
		if (!newToken.equals(";")) {
			index = pointer;
			return false;
		}
		
		return true;
	}
	
	public boolean isBlock() {
		int pointer = index;
		String newToken = nextToken();
		
		if (!newToken.equals("{")) {
			index = pointer;
			return false;
		}
		
		while(isStatement()) {
			//do nothing
		}
        
		newToken = nextToken();
		if (!newToken.equals("}")) {
			index = pointer;
			return false;
		}
		
		return true;
	}
	
	public boolean isWhile() {
		int pointer = index;
		String newToken = nextToken();
		
		if (!newToken.equals("while")) {
			index = pointer;
			return false;
		}
		
		newToken = nextToken();
		if (!newToken.equals("(")) {
			index = pointer;
			return false;
		}
		
		if (!isExpression()) {
			index = pointer;
			return false;
		}
		
		newToken = nextToken();
		if (!newToken.equals(")")) {
			index = pointer;
			return false;
		}
		
		if (!isStatement())	{
			index = pointer;
			return false;
		}
		
		return true;
	}
	
	public boolean isStatement() {
		if (isAssignment() || isDoWhile() || isBlock() || isWhile()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static void main(String args[]) {
		FileInputStream inFile;
		InputStreamReader inStream;
		BufferedReader inBuffer;
		String fileStr = "";
		
		try {
			inFile = new FileInputStream(args[0]);
			inStream = new InputStreamReader(inFile);
			inBuffer = new BufferedReader(inStream);
			String line = "";
			
			while((line = inBuffer.readLine())!=null) {
			    String newLine = "";

			    //Add blanks to forward and afterward special symbols
			    for (int i = 0; i < line.length(); i++) {
				    if (Character.toString(line.charAt(i)).equals("\t")
						    || Character.toString(line.charAt(i)).equals("\n") 
						    || Character.toString(line.charAt(i)).equals("\r")
						    || Character.toString(line.charAt(i)).equals("\r\n")) {
					    newLine += " ";
				    }
				    else if(Character.toString(line.charAt(i)).equals("(")) {
					    newLine += " ( ";
				    }
				    else if(Character.toString(line.charAt(i)).equals(")")) {
					    newLine += " ) ";
				    }
				    else if(Character.toString(line.charAt(i)).equals("{")) {
					    newLine += " { ";
				    }
				    else if(Character.toString(line.charAt(i)).equals("}")) {
					    newLine += " } ";
				    }
				    else if(Character.toString(line.charAt(i)).equals("=")) {
					    newLine += " = ";
				    }
				    else if(Character.toString(line.charAt(i)).equals(";")) {
					    newLine += " ; ";
				    }
				    else {
					    newLine += line.charAt(i);
				    }
			    }
			    
			    fileStr += newLine;
			}
			
			inBuffer.close();
		}
		catch (Exception e) {
			System.out.println(e); 
			return;
		} 
		
		MiniParser miniParser = new MiniParser();
		miniParser.stringToTokenBuffer(fileStr);

		if(miniParser.isStatement())
			System.out.println("Valid");
		else
			System.out.println("Invalid");
	}
}

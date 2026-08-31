package test.com.cratinginterpreters.lox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import src.com.craftinginterpreters.lox.Scanner;
import src.com.craftinginterpreters.lox.Token;
import src.com.craftinginterpreters.lox.TokenType;

public class ScannerTest {

    @Test
    void shouldScanSingleCharacterTokens() {
        List<Token> tokens = new Scanner("(){},.-+*/><").scanTokens();

        assertEquals(TokenType.LEFT_PAREN, tokens.get(0).type);
        assertEquals(TokenType.RIGHT_PAREN, tokens.get(1).type);
        assertEquals(TokenType.LEFT_BRACE, tokens.get(2).type);
        assertEquals(TokenType.RIGHT_BRACE, tokens.get(3).type);
        assertEquals(TokenType.COMMA, tokens.get(4).type);
        assertEquals(TokenType.DOT, tokens.get(5).type);
        assertEquals(TokenType.MINUS, tokens.get(6).type);
        assertEquals(TokenType.PLUS, tokens.get(7).type);
        assertEquals(TokenType.STAR, tokens.get(8).type);
        assertEquals(TokenType.SLASH, tokens.get(9).type);
        assertEquals(TokenType.GREATER, tokens.get(10).type);
        assertEquals(TokenType.LESS, tokens.get(11).type);
    }

    @Test
    void shouldScanOperators() {
        List<Token> tokens = new Scanner("! != = == < <= > >=").scanTokens();

        assertEquals(TokenType.BANG, tokens.get(0).type);
        assertEquals(TokenType.BANG_EQUAL, tokens.get(1).type);
        assertEquals(TokenType.EQUAL, tokens.get(2).type);
        assertEquals(TokenType.EQUAL_EQUAL, tokens.get(3).type);
        assertEquals(TokenType.LESS, tokens.get(4).type);
        assertEquals(TokenType.LESS_EQUAL, tokens.get(5).type);
        assertEquals(TokenType.GREATER, tokens.get(6).type);
        assertEquals(TokenType.GREATER_EQUAL, tokens.get(7).type);
    }

    @Test
    void shouldScanNumbers() {
        List<Token> tokens = new Scanner("1 1.5").scanTokens();

        assertEquals(TokenType.NUMBER, tokens.get(0).type);
        assertEquals(1.0, tokens.get(0).literal);

        assertEquals(TokenType.NUMBER, tokens.get(1).type);
        assertEquals(1.5, tokens.get(1).literal);

        assertEquals(TokenType.EOF, tokens.get(2).type);
    }

    @Test
    void shouldScanStrings() {
        List<Token> tokens = new Scanner("\"Hello, World!\"").scanTokens();

        assertEquals(TokenType.STRING, tokens.get(0).type);
        assertEquals("Hello, World!", tokens.get(0).literal);

        assertEquals(TokenType.EOF, tokens.get(1).type);
    }

    @Test
    void shouldDistinguishKeywordsFromIdentifiersAndIgnoreComments() {
        List<Token> tokens = new Scanner("""
            // this is a comment
            var foo = true
        """).scanTokens();

        assertEquals(TokenType.VAR, tokens.get(0).type);
        assertEquals(TokenType.IDENTIFIER, tokens.get(1).type);
        assertEquals(TokenType.EQUAL, tokens.get(2).type);
        assertEquals(TokenType.TRUE, tokens.get(3).type);
        assertEquals(TokenType.EOF, tokens.get(4).type);
    }
}
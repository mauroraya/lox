package test.com.cratinginterpreters.lox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import src.com.craftinginterpreters.lox.Token;
import src.com.craftinginterpreters.lox.TokenType;
import src.com.craftinginterpreters.lox.Expr;
import src.com.craftinginterpreters.lox.Parser;

public class ParserTest {
    @Test
    void shouldParseLiteralExpressions() {
        Expr.Literal nilExpr = (Expr.Literal) new Parser(List.of(
            new Token(TokenType.NIL, "nil", null, 1),
            new Token(TokenType.EOF, "", null, 1)
        )).parse();

        assertEquals(null, nilExpr.value);

        Expr.Literal trueExpr = (Expr.Literal) new Parser(List.of(
            new Token(TokenType.TRUE, "true", true, 1),
            new Token(TokenType.EOF, "", null, 1)
        )).parse();

        assertEquals(true, trueExpr.value);

        Expr.Literal falseExpr = (Expr.Literal) new Parser(List.of(
            new Token(TokenType.FALSE, "false", false, 1),
            new Token(TokenType.EOF, "", null, 1)
        )).parse();

        assertEquals(false, falseExpr.value);

        Expr.Literal numberExpr = (Expr.Literal) new Parser(List.of(
            new Token(TokenType.NUMBER, "1", 1.0, 1),
            new Token(TokenType.EOF, "", null, 1)
        )).parse();

        assertEquals(1.0, numberExpr.value);

        Expr.Literal stringExpr = (Expr.Literal) new Parser(List.of(
            new Token(TokenType.STRING, "\"Hello, World!\"", "Hello, World!", 1),
            new Token(TokenType.EOF, "", null, 1)
        )).parse();

        assertEquals("Hello, World!", stringExpr.value);
    }

    @Test
    void shouldParseUnaryExpressions() {
        List<Token> tokens = List.of(
            new Token(TokenType.MINUS, "-", null, 1),
            new Token(TokenType.NUMBER, "1", 1.0, 1),
            new Token(TokenType.EOF, "", null, 1)
        );

        Parser parser = new Parser(tokens);
        Expr.Unary expression = (Expr.Unary) parser.parse();

        assertEquals(TokenType.MINUS, expression.operator.type);
        assertEquals(1.0, ((Expr.Literal) expression.right).value);
    }

    @Test
    void shouldParseBinaryExpressions() {
        List<Token> tokens = List.of(
            new Token(TokenType.NUMBER, "1", 1.0, 1),
            new Token(TokenType.PLUS, "+", null, 1),
            new Token(TokenType.NUMBER, "2", 2.0, 1),
            new Token(TokenType.EOF, "", null, 1)
        );

        Parser parser = new Parser(tokens);
        Expr.Binary expression = (Expr.Binary) parser.parse();

        assertEquals(TokenType.PLUS, expression.operator.type);
        assertEquals(1.0, ((Expr.Literal) expression.left).value);
        assertEquals(2.0, ((Expr.Literal) expression.right).value);
    }

    @Test
    void shouldParseGroupingExpressions() {
        List<Token> tokens = List.of(
            new Token(TokenType.LEFT_PAREN, "(", null, 1),
            new Token(TokenType.NUMBER, "1", 1.0, 1),
            new Token(TokenType.PLUS, "+", null, 1),
            new Token(TokenType.NUMBER, "2", 2.0, 1),
            new Token(TokenType.RIGHT_PAREN, ")", null, 1),
            new Token(TokenType.EOF, "", null, 1)
        );

        Parser parser = new Parser(tokens);
        Expr.Grouping expression = (Expr.Grouping) parser.parse();
        Expr.Binary inner = (Expr.Binary) expression.expression;

        assertEquals(TokenType.PLUS, inner.operator.type);
        assertEquals(1.0, ((Expr.Literal) inner.left).value);
        assertEquals(2.0, ((Expr.Literal) inner.right).value);
    }

    @Test
    void shouldParseComparisons() {
        List<Token> tokens = List.of(
            new Token(TokenType.NUMBER, "1", 1.0, 1),
            new Token(TokenType.GREATER_EQUAL, ">=", null, 1),
            new Token(TokenType.NUMBER, "1", 1.0, 1),
            new Token(TokenType.EOF, "", null, 1)
        );

        Parser parser = new Parser(tokens);
        Expr.Binary expression = (Expr.Binary) parser.parse();

        assertEquals(TokenType.GREATER_EQUAL, expression.operator.type);
        assertEquals(1.0, ((Expr.Literal) expression.left).value);
        assertEquals(1.0, ((Expr.Literal) expression.right).value);
    }

    @Test
    void shouldParseEquality() {
        List<Token> tokens = List.of(
            new Token(TokenType.NUMBER, "1", 1.0, 1),
            new Token(TokenType.EQUAL_EQUAL, "==", null, 1),
            new Token(TokenType.NUMBER, "1", 1.0, 1),
            new Token(TokenType.EOF, "", null, 1)
        );

        Parser parser = new Parser(tokens);
        Expr.Binary expression = (Expr.Binary) parser.parse();

        assertEquals(TokenType.EQUAL_EQUAL, expression.operator.type);
        assertEquals(1.0, ((Expr.Literal) expression.left).value);
        assertEquals(1.0, ((Expr.Literal) expression.right).value);
    }

    @Test
    void shouldParseLeftAssociatively() {
        List<Token> tokens = List.of(
            new Token(TokenType.NUMBER, "1", 1.0, 1),
            new Token(TokenType.MINUS, "-", null, 1),
            new Token(TokenType.NUMBER, "2", 2.0, 1),
            new Token(TokenType.MINUS, "-", null, 1),
            new Token(TokenType.NUMBER, "3", 3.0, 1),
            new Token(TokenType.EOF, "", null, 1)
        );

        Parser parser = new Parser(tokens);
        Expr.Binary expression = (Expr.Binary) parser.parse();

        assertEquals(TokenType.MINUS, expression.operator.type);

        Expr.Binary left = (Expr.Binary) expression.left;
        assertEquals(TokenType.MINUS, left.operator.type);
        assertEquals(1.0, ((Expr.Literal) left.left).value);
        assertEquals(2.0, ((Expr.Literal) left.right).value);

        Expr.Literal right = (Expr.Literal) expression.right;
        assertEquals(3.0, right.value);
    }

    @Test
    void shouldRespectOperatorPrecedence() {
        List<Token> tokens = List.of(
            new Token(TokenType.NUMBER, "1", 1.0, 1),
            new Token(TokenType.PLUS, "+", null, 1),
            new Token(TokenType.NUMBER, "2", 2.0, 1),
            new Token(TokenType.STAR, "*", null, 1),
            new Token(TokenType.NUMBER, "3", 3.0, 1),
            new Token(TokenType.EOF, "", null, 1)
        );

        Parser parser = new Parser(tokens);
        Expr.Binary expression = (Expr.Binary) parser.parse();

        assertEquals(TokenType.PLUS, expression.operator.type);

        Expr.Literal left = (Expr.Literal) expression.left;
        assertEquals(1.0, left.value);

        Expr.Binary right = (Expr.Binary) expression.right;
        assertEquals(TokenType.STAR, right.operator.type);
        assertEquals(2.0, ((Expr.Literal) right.left).value);
        assertEquals(3.0, ((Expr.Literal) right.right).value);
    }
}

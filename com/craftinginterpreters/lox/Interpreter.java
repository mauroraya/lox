package com.craftinginterpreters.lox;

public class Interpreter implements Expr.Visitor<Object> {
    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = expr.left;
        Object right = expr.right;

        switch (expr.operator.type) {
            case TokenType.PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }
                if (left instanceof String && right instanceof String) {
                    return (String)left + (String)right;
                }
                break;
            case TokenType.MINUS:
                return (double)left - (double)right;
            case TokenType.STAR:
                return (double)left * (double)right;
            case TokenType.SLASH:
                return (double)left / (double)right;
            case TokenType.GREATER:
                return (double)left > (double)right;
            case TokenType.GREATER_EQUAL:
                return (double)left >= (double)right;
            case TokenType.LESS:
                return (double)left < (double)right;
            case TokenType.LESS_EQUAL:
                return (double)left <= (double)right;
            case TokenType.EQUAL_EQUAL:
                return isEqual(left, right);
            case TokenType.BANG_EQUAL:
                return !isEqual(left, right);
            default:
                return null;
        }

        return null;
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case TokenType.BANG:
                return !isTruthy(right);
            case TokenType.MINUS:
                return -(double)right;
            default:
                return null;
        }
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;

        return a.equals(b);
    }
}

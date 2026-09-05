package src.com.craftinginterpreters.lox;
public abstract class Stmt {
    interface Visitor<R> {
        R visitExpressionStmt(Expression stmt);
        R visitPrintStmt(Print stmt);
    }
    abstract <R> R accept(Visitor<R> visitor);
    public static class Expression extends Stmt {
        public final Expr expression;
        Expression(Expr expression) {
            this.expression = expression;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }
    }
    public static class Print extends Stmt {
        public final Expr expression;
        Print(Expr expression) {
            this.expression = expression;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }
    }
}

package org.stepik.titova;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Tester {


    private Parser p = new Parser();

    private String execute(String expression) throws ParseException {
        return Simplifier.simplify(p.parse(expression)).toString();

    }

    private boolean check(String left, String expression) throws ParseException {
        String simplifiedExpression = execute(expression);
        String doubleSimplifiedExpression = execute(simplifiedExpression);
        return left.equals(simplifiedExpression) && simplifiedExpression.equals(doubleSimplifiedExpression);
    }

    @Test
    public void baseTest() throws ParseException {
        assertTrue(check("FALSE", "NOT TRUE"));
        assertTrue(check("TRUE", "NOT FALSE"));
        assertTrue(check("x", "TRUE AND x"));
        assertTrue(check("FALSE", "FALSE AND x"));
        assertTrue(check("TRUE", "TRUE OR x"));
        assertTrue(check("x", "FALSE OR x"));
        assertTrue(check("x", "(x)"));
        assertTrue(check("FALSE", "NOT TRUE"));
        assertTrue(check("x", "x OR x"));
        assertTrue(check("x OR y OR z", "x OR y OR z OR x"));
        assertTrue(check("x", "x AND x"));
        assertTrue(check("TRUE", "x OR NOT x"));
        assertTrue(check("FALSE", "x AND NOT x"));
        assertTrue(check("x", "NOT (NOT x)"));
        assertTrue(check("NOT x OR NOT y", "NOT (x AND y)"));
        assertTrue(check("NOT x AND NOT y", "NOT (x OR y)"));
    }

    @Test
    public void simpleTest() throws ParseException {
        assertTrue(check("TRUE", "x OR y OR TRUE"));
        assertTrue(check("x OR y", "((x OR y) AND TRUE) AND (x OR y)"));
        assertTrue(check("cat OR dog", "NOT(NOT dog AND NOT cat AND NOT FALSE)"));
        assertTrue(check("sjkahdgf AND t AND z", "(sjkahdgf AND t AND z) OR (sjkahdgf AND t AND z)"));
        assertTrue(check("sjkahdgf AND t AND z", "(sjkahdgf AND t AND    zANDt) OR (sjkahdgf AND t AND z)"));
        assertTrue(check("sjkahdgf AND t AND z", "(sjkahdgf AND t AND    zANDt) OR (sjkah AND NOTTRUE)"));
    }

    @Test
    public void crazySpacesTest() throws ParseException {
        assertTrue(check("t AND (t OR v AND x) AND x AND z", "xAND          t ANDz AND(   tOR v AND(x      ) )"));
        assertTrue(check("NOT z OR a AND t AND z OR x OR y", " t            ANDaAND t      AND z OR x OR yORNOTz"));
        assertTrue(check("x AND y", "x AND y AND x"));
        assertTrue(check("t AND (t OR v AND x) AND x AND z", "     xANDt AND  z AND(     tOR v AND(x))"));
        assertTrue(check("cat OR dog", "NOT     (NOTdog     AND NOT cat       AND NOT FALSE)"));
        assertTrue(check("cat OR dog", "     NOT (      NOT dog AND NOT cat)"));
        assertTrue(check("cat OR dog", "NOT(NOT       dog AND       NOT cat AND NOT FALSE)"));
        assertTrue(check("x OR y", "((x OR y) AND TRUE) AND (x OR y)"));
        assertTrue(check("TRUE", "x OR y OR TRUE"));
        assertTrue(check("x AND y", "x AND y AND x"));
        assertTrue(check("x AND y", "xANDyANDx"));
        assertTrue(check("TRUE", "xORyORTRUE"));
        assertTrue(check("x OR y", "((xORy)ANDTRUE)AND(xORy)"));
        assertTrue(check("cat OR dog", "NOT(NOT       dog ANDNOTcat AND NOTFALSE)"));
    }

    @Test(expected = ParseException.class)
    public void parseExceptionTest() throws ParseException {
        try {
            execute("(");
        } catch (ParseException e0) {
            try {
                execute(")))");
            } catch (ParseException e1) {
                try {
                    execute("x OR OR t");
                } catch (ParseException e2) {
                    try {
                        execute("fooBar");
                    } catch (ParseException e3) {
                        try {
                            execute(" t OR not t");
                        } catch (ParseException e4) {
                            try {
                                execute("r ANR false");
                            } catch (ParseException e5) {
                                try {
                                    execute("r ( f AND z)");
                                } catch (ParseException e6) {
                                    try {
                                        execute("()");
                                    } catch (ParseException e7) {
                                        execute("r OR NOT (t AND z");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


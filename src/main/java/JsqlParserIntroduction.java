import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.UserVariable;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

public class JsqlParserIntroduction {

    public final static String CORE_QUERY = "select report_id from report";

    public static String addNavCondition(String coreQuery){
        Select select = createSelect(coreQuery);
        GreaterThan greaterThan = new GreaterThan();
        greaterThan.setLeftExpression(new Column("NAV"));
        UserVariable userVariable =  new UserVariable();
        userVariable.setName("NavGUI");
        greaterThan.setRightExpression(userVariable);
        addWhereCondition(select, greaterThan);
        return select.toString();
    }

    public static String addStatusCondition(String coreQuery, String status){
        Select select = createSelect(coreQuery);
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(new Column("status"));
        equalsTo.setRightExpression(new StringValue(status));
        addWhereCondition(select, equalsTo);
        return select.toString();
    }

    public static String buildQuery(){
        return addStatusCondition(addNavCondition(CORE_QUERY), "'IN_PROGRESS'");
    }

    public static void main(String... args){
        buildQuery();
    }

    private static EqualsTo equalsTo(Expression left, Expression right) {
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(left);
        equalsTo.setRightExpression(right);
        return equalsTo;
    }

    private static GreaterThan greaterThan(Expression left, Expression right) {
        GreaterThan greaterThan = new GreaterThan();
        greaterThan.setLeftExpression(left);
        greaterThan.setRightExpression(right);
        return greaterThan;
    }


    public static Select createSelect(String sql) {
        try {
            return (Select) CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            throw new IllegalStateException("SQL parsing problem!", e);
        }
    }

    public static void addWhereCondition(Select select, Expression condition) {
        addWhereCondition(getPlainSelect(select), condition);
    }

    private static void addWhereCondition(PlainSelect plainSelect, Expression condition) {
        if (plainSelect.getWhere() == null) {
            plainSelect.setWhere(condition);
            return;
        }
        AndExpression andExpression = new AndExpression(plainSelect.getWhere(), condition);
        plainSelect.setWhere(andExpression);
    }

    private static PlainSelect getPlainSelect(Select select) {
        if (select.getSelectBody() instanceof PlainSelect) {
            return (PlainSelect) select.getSelectBody();
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }










}

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

public class JsqParserIntroductionTest {

    private String query = "SELECT report_id FROM report WHERE NAV > @NavGUI AND status = 'IN_PROGRESS'";

    @Mock
    private JsqlParserIntroduction jsqlParserIntroduction;

    @Test
    public void verifySQLBuilder(){
        assertEquals(query,jsqlParserIntroduction.buildQuery());
    }
}

package Model;

import model.ScoringToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoringTokenTest {
    ScoringToken sc1 = new ScoringToken(4, 1);

    @Test
    void getValue() {
        assertEquals(4, sc1.getValue());
    }

    @Test
    void getRomanNumber() {
        assertEquals(1, sc1.getRomanNumber());
    }
}

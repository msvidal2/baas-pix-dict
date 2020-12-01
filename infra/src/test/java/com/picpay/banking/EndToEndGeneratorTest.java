package com.picpay.banking;

import com.picpay.banking.util.EndToEndGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 01/12/20
 */
public class EndToEndGeneratorTest {

    @Test
    void generate_end_toEnd() {
        System.out.println(EndToEndGenerator.generate("22896431"));
        Assertions.assertThat(EndToEndGenerator.generate("22896431"))
                .containsPattern("(^E)([0-9]{20})(([a-zA-Z0-9]{0,11})$)");
    }

}

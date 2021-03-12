package com.picpay.banking.pix.core.domain;

import com.picpay.banking.pix.core.domain.reconciliation.VsyncBitwiseXOR;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class VsyncBitwiseXORTest {

    final VsyncBitwiseXOR vsyncBitwiseXOR = new VsyncBitwiseXOR();
    final String vsyncInitialValue = "0000000000000000000000000000000000000000000000000000000000000000";

    @Test
    void calculate() {
        var cids = Stream.of(
            "b2b3779042fba1a7ebec59cebdee70837c8e393782071d1c02ae33384f8c41c4",
            "cf14ca82e3ee2362e1ff71185856c6feb2ccc18ec5c1673e50f6446ae964ad48",
            "f39e7a44303b782c5c971f8ceba172a2cba74da76940f20107356634af46b647");

        var vsync = cids.parallel().reduce(vsyncInitialValue, vsyncBitwiseXOR);
        var expectedVsync = "8e39c756912efae95684375a0e19c4df05e5b51e2e868823556d116609ae5acb";
        assertThat(vsync).isEqualTo(expectedVsync);
    }

}

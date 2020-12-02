package com.picpay.banking.pix.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class VsyncTest {

    @Test
    @DisplayName("Calcula o Vsync")
    public void calculateVsync_success() {
        final List<ContentIdentifier> contentIdentifiers = List.of(
            ContentIdentifier.builder().cid("28c06eb41c4dc9c3ae114831efcac7446c8747777fca8b145ecd31ff8480ae88").build(),
            ContentIdentifier.builder().cid("4d4abb9168114e349672b934d16ed201a919cb49e28b7f66a240e62c92ee007f").build(),
            ContentIdentifier.builder().cid("fce514f84f37934bc8aa0f861e4f7392273d71b9d18e8209d21e4192a7842058").build());

        Vsync vsync = Vsync.builder().build();

        vsync.calculateVsync(contentIdentifiers);

        String expectedVsync = "996fc1dd3b6b14bcf0c9fe8320eb66d7e2a3fd874ccf767b2e939641b1ea8eaf";
        assertThat(vsync.getVsync()).isEqualTo(expectedVsync);
    }

    @Test
    @DisplayName("Calcula o Vsync de forma cumulativa")
    public void calculateVsyncCumulative_success() {
        Vsync vsync = Vsync.builder().build();
        vsync.calculateVsync(List.of(ContentIdentifier.builder().cid("28c06eb41c4dc9c3ae114831efcac7446c8747777fca8b145ecd31ff8480ae88").build()));
        vsync.calculateVsync(List.of(ContentIdentifier.builder().cid("fce514f84f37934bc8aa0f861e4f7392273d71b9d18e8209d21e4192a7842058").build()));
        vsync.calculateVsync(List.of(ContentIdentifier.builder().cid("4d4abb9168114e349672b934d16ed201a919cb49e28b7f66a240e62c92ee007f").build()));

        String expectedVsync = "996fc1dd3b6b14bcf0c9fe8320eb66d7e2a3fd874ccf767b2e939641b1ea8eaf";
        assertThat(vsync.getVsync()).isEqualTo(expectedVsync);
    }

}

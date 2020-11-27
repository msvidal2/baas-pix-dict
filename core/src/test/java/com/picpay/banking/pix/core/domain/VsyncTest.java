package com.picpay.banking.pix.core.domain;

import com.picpay.banking.pix.core.domain.ContentIdentifier.ContentIdentifierType;
import com.picpay.banking.pix.core.domain.Vsync.VsyncResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class VsyncTest {

    @Test
    @DisplayName("Calcula o Vsync com sucesso seguindo exemplo do BACEN")
    public void calculateVsync_success() {
        final List<ContentIdentifier> contentIdentifiers = List.of(
            createContentIdentifier("28c06eb41c4dc9c3ae114831efcac7446c8747777fca8b145ecd31ff8480ae88"),
            createContentIdentifier("4d4abb9168114e349672b934d16ed201a919cb49e28b7f66a240e62c92ee007f"),
            createContentIdentifier("fce514f84f37934bc8aa0f861e4f7392273d71b9d18e8209d21e4192a7842058"));

        Vsync vsync = Vsync.builder().build();

        vsync.calculateVsync(contentIdentifiers);

        String expectedVsync = "996fc1dd3b6b14bcf0c9fe8320eb66d7e2a3fd874ccf767b2e939641b1ea8eaf";
        assertThat(vsync.getVsync()).isEqualTo(expectedVsync);
    }

    @Test
    @DisplayName("Calcula o Vsync de forma cumulativa")
    public void calculateVsyncCumulative_success() {
        Vsync vsync = Vsync.builder().build();
        vsync.calculateVsync(List.of(createContentIdentifier("28c06eb41c4dc9c3ae114831efcac7446c8747777fca8b145ecd31ff8480ae88")));
        vsync.calculateVsync(List.of(createContentIdentifier("fce514f84f37934bc8aa0f861e4f7392273d71b9d18e8209d21e4192a7842058")));
        vsync.calculateVsync(List.of(createContentIdentifier("4d4abb9168114e349672b934d16ed201a919cb49e28b7f66a240e62c92ee007f")));

        String expectedVsync = "996fc1dd3b6b14bcf0c9fe8320eb66d7e2a3fd874ccf767b2e939641b1ea8eaf";
        assertThat(vsync.getVsync()).isEqualTo(expectedVsync);
    }

    @Test
    @DisplayName("Calcula o Vsync usando dados reais coletados via txt")
    public void calculates_vsync_with_real_data() throws FileNotFoundException {
        FileReader evp = new FileReader("src/test/java/com/picpay/banking/pix/core/domain/evp.txt");
        Scanner fileReaderScan = new Scanner(evp);

        List<ContentIdentifier> contentIdentifiers = new ArrayList<>();
        while (fileReaderScan.hasNextLine()) {
            contentIdentifiers.add(createContentIdentifier(fileReaderScan.next()));
        }

        Vsync vsync = Vsync.builder().build();

        vsync.calculateVsync(contentIdentifiers);

        String expectedVsync = "7a15a318271483cddd00f9b81c3470849ef09c16517657fd2137cc490d1e149e";
        assertThat(vsync.getVsync()).isEqualTo(expectedVsync);
    }

    @Test
    @DisplayName("Quando OK, define o resultado e atualiza a data do sincronismo")
    public void when_resultOK_expected_synchronizedAt_updated() {
        final LocalDateTime lastSyncDone = LocalDateTime.now();

        Vsync vsync = Vsync.builder()
            .synchronizedAt(lastSyncDone)
            .build();
        vsync.syncVerificationResult(VsyncResult.OK);

        assertThat(vsync.getVsyncResult()).isEqualTo(VsyncResult.OK);
        assertThat(vsync.getSynchronizedAt()).isNotEqualTo(LocalDateTime.now());
        assertThat(vsync.isNOk()).isEqualTo(false);
    }

    @Test
    @DisplayName("Quando NOK, define o resultado e mantêm a data do ultimo sincronismo")
    public void when_resultNOK_expected_synchronizedAt_notUpdated() {
        final LocalDateTime lastSyncDone = LocalDateTime.now();

        Vsync vsync = Vsync.builder()
            .synchronizedAt(lastSyncDone)
            .build();
        vsync.syncVerificationResult(VsyncResult.NOK);

        assertThat(vsync.getVsyncResult()).isEqualTo(VsyncResult.NOK);
        assertThat(vsync.getSynchronizedAt()).isEqualTo(lastSyncDone);
        assertThat(vsync.isNOk()).isEqualTo(true);
    }

    @Test
    @DisplayName("Deve agrupar os eventos por cid e pegar a data mais recente. Depois gerar ações apenas para os tipos ADD.")
    public void should_generate_4_actions_add() {
        Vsync vsync = Vsync.builder().build();

        final List<ContentIdentifier> events = List.of(
            createContentIdentifier("1"),
            createContentIdentifier("1", ContentIdentifierType.REMOVE),
            createContentIdentifier("1"),
            createContentIdentifier("2", ContentIdentifierType.REMOVE),
            createContentIdentifier("3"),
            createContentIdentifier("4"),
            createContentIdentifier("5"),
            createContentIdentifier("6", ContentIdentifierType.REMOVE));

        final Set<Vsync.Action> actions = vsync.identifyActions(events, new ArrayList<>());

        assertThat(actions.size()).isEqualTo(6);
        assertThat(actions.stream().filter(action -> action.getActionType().equals(Vsync.ActionType.ADD)).count()).isEqualTo(4);
    }

    @Test
    @DisplayName("Quando não tem eventos, mas tem CIDs, todos devem gerar ações de REMOVE")
    public void should_generate_5_actions_remove() {
        Vsync vsync = Vsync.builder().build();

        final List<ContentIdentifier> contentIdentifiers = List.of(
            createContentIdentifier("1"),
            createContentIdentifier("2"),
            createContentIdentifier("3"),
            createContentIdentifier("4"),
            createContentIdentifier("5"));

        final Set<Vsync.Action> actions = vsync.identifyActions(new ArrayList<>(), contentIdentifiers);

        assertThat(actions.size()).isEqualTo(5);
        assertThat(actions.stream().filter(action -> action.getActionType().equals(Vsync.ActionType.REMOVE)).count()).isEqualTo(5);
    }

    @Test
    @DisplayName("Os CIDs 2 e 6 devem gerar ações para REMOVE.")
    public void should_generate_action_to_remove_for_2_and_6() {
        Vsync vsync = Vsync.builder().build();

        final List<ContentIdentifier> bacenCids = List.of(
            createContentIdentifier("1"),
            createContentIdentifier("1", ContentIdentifierType.REMOVE),
            createContentIdentifier("1"),
            createContentIdentifier("2", ContentIdentifierType.REMOVE),
            createContentIdentifier("3"),
            createContentIdentifier("4"),
            createContentIdentifier("5"),
            createContentIdentifier("6", ContentIdentifierType.REMOVE));

        final List<ContentIdentifier> databaseEvents = List.of(
            createContentIdentifier("1"),
            createContentIdentifier("2"),
            createContentIdentifier("3"),
            createContentIdentifier("4"),
            createContentIdentifier("5"),
            createContentIdentifier("6"));

        final Set<Vsync.Action> actions = vsync.identifyActions(bacenCids, databaseEvents);

        assertThat(actions.size()).isEqualTo(2);
        assertThat(actions.stream().filter(action -> action.getCid().equals("2")).findFirst().get().getActionType())
            .isEqualTo(Vsync.ActionType.REMOVE);
        assertThat(actions.stream().filter(action -> action.getCid().equals("6")).findFirst().get().getActionType())
            .isEqualTo(Vsync.ActionType.REMOVE);
    }

    @Test
    @DisplayName("Os CIDs 2 e 6 devem gerar ações para REMOVE. Os CIDs 3 e 7 devem gerar ações para ADD.")
    public void should_generate_action_to_remove_for_2_and_6_and_add_3_and_7() {
        Vsync vsync = Vsync.builder().build();

        final List<ContentIdentifier> bacenCids = List.of(
            createContentIdentifier("1"),
            createContentIdentifier("1", ContentIdentifierType.REMOVE),
            createContentIdentifier("1"),
            createContentIdentifier("2", ContentIdentifierType.REMOVE),
            createContentIdentifier("3"),
            createContentIdentifier("4"),
            createContentIdentifier("5"),
            createContentIdentifier("6", ContentIdentifierType.REMOVE),
            createContentIdentifier("7", ContentIdentifierType.REMOVE),
            createContentIdentifier("7"),
            createContentIdentifier("8", ContentIdentifierType.REMOVE),
            createContentIdentifier("8"),
            createContentIdentifier("8", ContentIdentifierType.REMOVE));

        final List<ContentIdentifier> databaseCids = List.of(
            createContentIdentifier("1"),
            createContentIdentifier("1", ContentIdentifierType.REMOVE),
            createContentIdentifier("2"),
            createContentIdentifier("4"),
            createContentIdentifier("5"),
            createContentIdentifier("6"),
            createContentIdentifier("7", ContentIdentifierType.REMOVE),
            createContentIdentifier("8", ContentIdentifierType.REMOVE),
            createContentIdentifier("9", ContentIdentifierType.REMOVE),
            createContentIdentifier("10"));

        final Set<Vsync.Action> actions = vsync.identifyActions(bacenCids, databaseCids);

        //assertThat(actions.size()).isEqualTo(4);
        assertThat(actions.stream().filter(action -> action.getCid().equals("1")).findFirst().get().getActionType())
            .isEqualTo(Vsync.ActionType.ADD);
        assertThat(actions.stream().filter(action -> action.getCid().equals("2")).findFirst().get().getActionType())
            .isEqualTo(Vsync.ActionType.REMOVE);
        assertThat(actions.stream().filter(action -> action.getCid().equals("3")).findFirst().get().getActionType())
            .isEqualTo(Vsync.ActionType.ADD);
        assertThat(actions.stream().filter(action -> action.getCid().equals("4")).count())
            .isEqualTo(0);
        assertThat(actions.stream().filter(action -> action.getCid().equals("5")).count())
            .isEqualTo(0);
        assertThat(actions.stream().filter(action -> action.getCid().equals("6")).findFirst().get().getActionType())
            .isEqualTo(Vsync.ActionType.REMOVE);
        assertThat(actions.stream().filter(action -> action.getCid().equals("7")).findFirst().get().getActionType())
            .isEqualTo(Vsync.ActionType.ADD);
        assertThat(actions.stream().filter(action -> action.getCid().equals("8")).count())
            .isEqualTo(0);
        assertThat(actions.stream().filter(action -> action.getCid().equals("9")).findFirst().get().getActionType())
            .isEqualTo(Vsync.ActionType.REMOVE);
        assertThat(actions.stream().filter(action -> action.getCid().equals("10")).findFirst().get().getActionType())
            .isEqualTo(Vsync.ActionType.REMOVE);
    }

    private ContentIdentifier createContentIdentifier(String cid) {
        return ContentIdentifier.builder()
            .cid(cid)
            .contentIdentifierType(ContentIdentifierType.ADD)
            .dateTime(LocalDateTime.now())
            .build();
    }

    private ContentIdentifier createContentIdentifier(String cid, ContentIdentifierType contentIdentifierType) {
        return ContentIdentifier.builder()
            .cid(cid)
            .contentIdentifierType(contentIdentifierType)
            .dateTime(LocalDateTime.now())
            .build();
    }

}

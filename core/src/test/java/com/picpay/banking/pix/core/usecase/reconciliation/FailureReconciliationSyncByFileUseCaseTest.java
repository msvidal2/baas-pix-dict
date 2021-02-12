package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.common.Pagination;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.ContentIdentifierFileAction;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ReconciliationLockPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Luis Silva
 * @version 1.0 25/11/2020
 */
@ExtendWith(MockitoExtension.class)
class FailureReconciliationSyncByFileUseCaseTest {

    @Mock
    private BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    @Mock
    private DatabaseContentIdentifierPort databaseContentIdentifierPort;
    @Mock
    private BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    @Mock
    private SavePixKeyPort createPixKeyPort;
    @Mock
    private RemovePixKeyPort removePixKeyPort;
    @Mock
    private FindPixKeyPort findPixKeyPort;
    @Mock
    private PixKeyEventPort pixKeyEventPort;
    @Mock
    private ReconciliationLockPort lockPort;
    @Mock
    private SyncVerifierPort syncVerifierPort;
    @Mock
    private BacenSyncVerificationsPort bacenSyncVerificationsPort;
    @Mock
    private SyncVerifierHistoricPort syncVerifierHistoricPort;

    private FailureReconciliationSyncByFileUseCase failureReconciliationSyncByFileUseCase;

    private ContentIdentifierFile cidFile;
    private List<String> cids;
    private PixKey pixKey;

    @BeforeEach
    public void init() {
//        this.failureReconciliationSyncByFileUseCase = new FailureReconciliationSyncByFileUseCase(
//            22896431, bacenContentIdentifierEventsPort, databaseContentIdentifierPort, bacenPixKeyByContentIdentifierPort,
//            createPixKeyPort, findPixKeyPort, removePixKeyPort, pixKeyEventPort,lockPort, syncVerifierPort, bacenSyncVerificationsPort,
//            syncVerifierHistoricPort
//        );

        this.cidFile = ContentIdentifierFile.builder()
            .id(1)
            .keyType(KeyType.CPF)
            .length(32000L)
            .sha256("")
            .status(ContentIdentifierFile.StatusContentIdentifierFile.PROCESSING)
            .requestTime(LocalDateTime.now())
            .url("url")
            .build();

        this.pixKey = PixKey.builder()
            .type(KeyType.EMAIL)
            .key("joao@picpay.com")
            .ispb(1)
            .nameIspb("Empresa Picpay")
            .branchNumber("1")
            .accountType(AccountType.SALARY)
            .accountNumber("1")
            .accountOpeningDate(LocalDateTime.now())
            .personType(PersonType.INDIVIDUAL_PERSON)
            .taxId("57950197048")
            .name("Joao da Silva")
            .fantasyName("Nome Fantasia")
            .endToEndId("endToEndId")
            .requestId(UUID.randomUUID()).build();

        this.cids = List.of(
            "ae843d282551398d7d201be38cb2f6472cfed56aa8a1234612780f9618ec017a",
            "b42a52ec1ce47529d9cade36c9a4b6d89c512ab0660662b4b9a0949055d7c935",
            "abc1260b7384d1bae04b1f940f60875596b0fb3d1ef3a01fa9373f3668c925b8",
            "84d8fe5be5d4677b93ac21e27f405be0b255cf896f0b8504b35fa0886ee8b8b8",
            "5d40ec236d61653c9832a750440cd141ab7d2ddae2cf1e20cb615cc5d3dee354",
            "f18d43a22a7e8c89dc40ef45b56a0c24b1bd05ea1baa11a6f10f0e0d2bc809f8",
            "26983484cd03d5b6bbeb9bc026965292bdcf6f3622ec1a0ae0e61a1bd1e57c93",
            "68dfd643dccd68990540bd1508ea7f48e1784270e6633305c6fc904d1862b376",
            "bc915cf1fb2c7b4937343e44b4b60f44dd7859c0600e4ef38868a21a12f3a1a4",
            "11c7b17e78383318e5dead150ecfd4e0c3e6ef0539f6d4d3df4749cbe02689e5",
            "db01597005d44ed5151886cd705a9147be0336e4cbe70ff4592cc31646dd4e33",
            "1a87970249c347b0f0f04261d86b6888ed46bd4462890d4f198ccea093c8b53f",
            "477622204f462e36b951f3793ce37639fd45344cd47b2ce4586ca286a976ec91",
            "4a41744ae57a1892cb74e6a049ba8d4061028d75e17036e77da77237e5789f28",
            "3b9124780499e81f39dec26a4563a28a4590c084fb26b8fd1ebc9f675b09ece7",
            "269dd2e8f0f88972eeb3e2c07870265eccfb02135181cd4a1a535a6768c2171e",
            "c945904b5ac1d71b9c25702af87ca4250803acabb0effefa7b268f38629e6447",
            "54faaec2ff82c5f7e11b4b7a8d2f524350442c5873091f7d09fcad6c01bbe344",
            "56c2ad6a4e5a87e522dfeac51e643e6cea6edbd9a8078c65ed2aacd365cac721",
            "b2bba811decb1d101e78301f185e195e0c2fb281b67cea37d918912868560d85",
            "5e7c8424fa5d3b91426888e224d632a8ccdafe7ca2e2584fd9ec093f21faf424",
            "e6a9cd9b738bb943b373a78f6e5da0ae23ffb43844e3bb60164e770772b675d3",
            "db9b06bfb3bd28ffafa1cc546108d591e2982be07b7716e24d8caa2edf7939f3",
            "aa64d85d762485fffa232764e0da18d62b1e959c2161171f237374b6d3642679",
            "ef4a13d15b7b8c1c06fd4395605c13d65c14fb5bfba4afc37e0f732701e8961e",
            "6bf6c2717aade1dcd0a6107bc6b247d6f9db782fb645dfa243748aacdd81dcd0",
            "bc11168c3a441b20148ceb5046cdab17c8b6409d0e4064c5693341edb584706d",
            "a52fdbc13f026f8b1d5f9928e433369e0cc02c1b1f7291f29c9be9be439e0817",
            "83e7e9461138e16ff3935b2082f7d382a92485a0692d0036f4871b849a345ebb",
            "a5b1b88555c5fea63cfc0d3b5d8be5771e600452fe4acd57995b5be6d6957800",
            "1ef2348a23aa673e9c57905c85ad0322c296e13747c836401f48299bc7c2dfbf",
            "6bf5d133c436d51fb833013182f9e01b73f6b49052f61db4f7395b6f973621ce",
            "1ec93549de59192fa34bca48644c0d96d8d537b5a2579650e5bd3e244b41989a",
            "70e173febb6ada0b2de005a739441d1fc801468c1589502091ae00cc6319ca8c",
            "44f9bed17c0b6690c72d94f097a48753ce09163f2d653343d4fb420d3a7828ce",
            "9b139ec4dddb7601f2aa5409ae299eccf55b8b8fe228369cd9332b65c181df17",
            "52f858365aa2b5aa86dbc1d3ae0b46c343393dee3c85b1b1f4b9c4c654f787b4",
            "6f50cfbe1107e0fee4fdd41a28fe00532035d1347092123a0e6cc30bdf40e942",
            "18d9e3ae3af99617fa34b863c1bed19985653446bf8a9d236eb803f93525da23",
            "7cfeaccad7dc5fd66b5b1cde01a912741cdae5388362863fa56e71a748f4e17a",
            "fdbdd17771d179a04292245e6b19704c438500b44db29c91636ad671ebe99c15",
            "69db9b861be66f434f8ef722f480195b56aaadb7135b9089c53e161000af41da");
    }

    @Test
    void dontProcessWhenNotExistFilesRequestedInBacen() {
        when(this.databaseContentIdentifierPort.findLastFileRequested(any())).thenReturn(Optional.empty());

        this.failureReconciliationSyncByFileUseCase.execute(KeyType.CPF);

        verify(this.databaseContentIdentifierPort).findLastFileRequested(any());
        verify(this.bacenContentIdentifierEventsPort, never()).getContentIdentifierFileInBacen(anyInt());
        verify(this.bacenContentIdentifierEventsPort, never()).downloadCidsFromBacen(anyString());
        verify(this.findPixKeyPort, never()).findAllByKeyType(any(),any(),anyInt());
        verify(this.databaseContentIdentifierPort, never()).saveFile(any());
        verify(lockPort,never()).lock();
    }

    @Test
    void dontInsertKeysFromBacenWhenFileIsAlreadyProcessed() {
        final var doneFile = cidFile.toBuilder().status(
            ContentIdentifierFile.StatusContentIdentifierFile.DONE).build();
        when(this.databaseContentIdentifierPort.findLastFileRequested(any())).thenReturn(Optional.of(doneFile));

        this.failureReconciliationSyncByFileUseCase.execute(KeyType.CPF);

        verify(this.databaseContentIdentifierPort).findLastFileRequested(any());
        verify(this.bacenContentIdentifierEventsPort,never()).getContentIdentifierFileInBacen(anyInt());
        verify(this.bacenContentIdentifierEventsPort, never()).downloadCidsFromBacen(anyString());
        verify(this.findPixKeyPort, never()).findAllByKeyType(any(),any(),anyInt());
        verify(this.databaseContentIdentifierPort, never()).saveFile(any());
        verify(lockPort,never()).lock();
        verify(lockPort).unlock();

    }

    @Test
    void dontInsertKeysFromBacenWhenFileNotAvailable() {
        when(this.databaseContentIdentifierPort.findLastFileRequested(any())).thenReturn(Optional.of(cidFile));
        when(this.bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(anyInt())).thenReturn(cidFile);

        this.failureReconciliationSyncByFileUseCase.execute(KeyType.CPF);

        verify(this.databaseContentIdentifierPort).findLastFileRequested(any());
        verify(this.bacenContentIdentifierEventsPort).getContentIdentifierFileInBacen(anyInt());
        verify(this.bacenContentIdentifierEventsPort, never()).downloadCidsFromBacen(anyString());
        verify(this.findPixKeyPort, never()).findAllByKeyType(any(),any(),anyInt());
        verify(this.databaseContentIdentifierPort, never()).saveFile(any());
        verify(lockPort,never()).lock();
        verify(lockPort).unlock();

    }

    @Test
    void dontInsertKeysFromBacenWhenFileIsNull() {
        when(this.databaseContentIdentifierPort.findLastFileRequested(any())).thenReturn(Optional.of(cidFile));
        when(this.bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(anyInt())).thenReturn(null);

        this.failureReconciliationSyncByFileUseCase.execute(KeyType.CPF);

        verify(this.databaseContentIdentifierPort).findLastFileRequested(any());
        verify(lockPort,never()).lock();
        verify(this.bacenContentIdentifierEventsPort).getContentIdentifierFileInBacen(anyInt());
        verify(this.bacenContentIdentifierEventsPort, never()).downloadCidsFromBacen(anyString());
        verify(this.findPixKeyPort, never()).findAllByKeyType(any(),any(),anyInt());
        verify(this.databaseContentIdentifierPort, never()).saveFile(any());
        verify(lockPort).unlock();
    }

    @Test
    void insertKeysFromBacen() {
        final var availableFile = cidFile.toBuilder().status(ContentIdentifierFile.StatusContentIdentifierFile.AVAILABLE).build();

        when(this.databaseContentIdentifierPort.findLastFileRequested(any())).thenReturn(Optional.of(cidFile));
        doNothing().when(lockPort).lock();
        when(this.bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(anyInt())).thenReturn(availableFile);
        when(this.bacenContentIdentifierEventsPort.downloadCidsFromBacen(anyString())).thenReturn(cids);
        when(this.findPixKeyPort.findAllByKeyType(any(),any(),anyInt())).then(this::generatePixkKeyToInsert);
        when(this.bacenPixKeyByContentIdentifierPort.getPixKey(anyString())).then(this::generatePixKeyFromCID);
        when(this.createPixKeyPort.savePixKey(any(), any())).then(this::generatePixKeyFromDomain);
        doNothing().when(this.databaseContentIdentifierPort).saveAction(anyInt(), any(), anyString(), any());
        doNothing().when(this.pixKeyEventPort).pixKeyWasCreated(any());
        doNothing().when(this.databaseContentIdentifierPort).saveFile(any());
        doNothing().when(lockPort).unlock();

        this.failureReconciliationSyncByFileUseCase.execute(KeyType.CPF);

        verify(this.databaseContentIdentifierPort).findLastFileRequested(any());
        verify(lockPort).lock();
        verify(this.bacenContentIdentifierEventsPort).getContentIdentifierFileInBacen(anyInt());
        verify(this.bacenContentIdentifierEventsPort).downloadCidsFromBacen(anyString());
        verify(this.findPixKeyPort).findAllByKeyType(any(),any(),anyInt());
        verify(this.bacenPixKeyByContentIdentifierPort, times(5)).getPixKey(anyString());
        verify(this.createPixKeyPort, times(5)).savePixKey(any(), any());
        verify(this.databaseContentIdentifierPort, times(5)).saveAction(anyInt(), any(), anyString(),
            argThat(contentIdentifierAction -> contentIdentifierAction.equals(ContentIdentifierFileAction.ADDED)));
        verify(this.pixKeyEventPort,times(5)).pixKeyWasCreated(any());
        verify(this.databaseContentIdentifierPort).saveFile(any());
        verify(lockPort).unlock();
    }

    @Test
    void updateKeysFromBacen() {
        final var availableFile = cidFile.toBuilder().status(ContentIdentifierFile.StatusContentIdentifierFile.AVAILABLE).build();

        when(this.databaseContentIdentifierPort.findLastFileRequested(any())).thenReturn(Optional.of(cidFile));
        doNothing().when(lockPort).lock();
        when(this.bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(anyInt())).thenReturn(availableFile);
        when(this.bacenContentIdentifierEventsPort.downloadCidsFromBacen(anyString())).thenReturn(cids);
        when(this.findPixKeyPort.findAllByKeyType(any(),any(),anyInt())).then(this::generatePixkKeyToInsert);
        when(this.bacenPixKeyByContentIdentifierPort.getPixKey(anyString())).then(this::generatePixKeyFromCID);
        when(this.createPixKeyPort.savePixKey(any(), any())).then(this::generatePixKeyFromDomain);
        when(this.findPixKeyPort.findPixKey(any())).thenReturn(Optional.of(pixKey));
        doNothing().when(this.databaseContentIdentifierPort).saveAction(anyInt(), any(), anyString(), any());
        doNothing().when(this.pixKeyEventPort).pixKeyWasUpdated(any());
        doNothing().when(this.databaseContentIdentifierPort).saveFile(any());
        doNothing().when(lockPort).unlock();

        this.failureReconciliationSyncByFileUseCase.execute(KeyType.CPF);

        verify(this.databaseContentIdentifierPort).findLastFileRequested(any());
        verify(lockPort).lock();
        verify(this.bacenContentIdentifierEventsPort).getContentIdentifierFileInBacen(anyInt());
        verify(this.bacenContentIdentifierEventsPort).downloadCidsFromBacen(anyString());
        verify(this.findPixKeyPort).findAllByKeyType(any(),any(),anyInt());
        verify(this.bacenPixKeyByContentIdentifierPort, times(5)).getPixKey(anyString());
        verify(this.createPixKeyPort, times(5)).savePixKey(any(), any());
        verify(this.findPixKeyPort, times(5)).findPixKey(any());
        verify(this.databaseContentIdentifierPort, times(5)).saveAction(anyInt(), any(), anyString(),
            argThat(contentIdentifierAction -> contentIdentifierAction.equals(ContentIdentifierFileAction.UPDATED)));
        verify(this.pixKeyEventPort,times(5)).pixKeyWasUpdated(any());
        verify(this.databaseContentIdentifierPort).saveFile(any());
        verify(lockPort).unlock();
    }

    @Test
    void removeKeysFromBacen() {
        final var availableFile = this.cidFile.toBuilder().status(ContentIdentifierFile.StatusContentIdentifierFile.AVAILABLE).build();
        final var contentIdentifiersToRemove = pixKey.toBuilder().cid("a").build();
        final var contentIdentifiers = this.cids.stream().map(cid -> PixKey.builder()
            .cid(cid)
            .type(KeyType.CPF).key(UUID.randomUUID().toString()).build()).collect(Collectors.toList());
        contentIdentifiers.add(contentIdentifiersToRemove);

        when(this.databaseContentIdentifierPort.findLastFileRequested(any())).thenReturn(Optional.of(cidFile));
        doNothing().when(lockPort).lock();
        when(this.bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(anyInt())).thenReturn(availableFile);
        when(this.bacenContentIdentifierEventsPort.downloadCidsFromBacen(anyString())).thenReturn(cids);
        when(this.findPixKeyPort.findAllByKeyType(any(),any(),anyInt())).thenReturn(Pagination.<PixKey>builder().currentPage(1).hasNext(false).result(contentIdentifiers).build());
        when(this.bacenPixKeyByContentIdentifierPort.getPixKey(anyString())).thenReturn(Optional.empty());
        when(this.findPixKeyPort.findByCid(anyString())).thenReturn(Optional.of(contentIdentifiersToRemove));
        when(this.removePixKeyPort.remove(any(), anyInt())).thenReturn(Optional.of(PixKey.builder().build()));
        doNothing().when(this.databaseContentIdentifierPort).saveAction(anyInt(), any(), anyString(), any());
        doNothing().when(this.pixKeyEventPort).pixKeyWasRemoved(any());
        doNothing().when(this.databaseContentIdentifierPort).saveFile(any());
        doNothing().when(lockPort).unlock();

        this.failureReconciliationSyncByFileUseCase.execute(KeyType.CPF);

        verify(this.databaseContentIdentifierPort).findLastFileRequested(any());
        verify(lockPort).lock();
        verify(this.bacenContentIdentifierEventsPort).getContentIdentifierFileInBacen(anyInt());
        verify(this.bacenContentIdentifierEventsPort).downloadCidsFromBacen(anyString());
        verify(this.findPixKeyPort).findAllByKeyType(any(),any(),anyInt());
        verify(this.bacenPixKeyByContentIdentifierPort, times(2)).getPixKey(anyString());
        verify(this.createPixKeyPort, never()).savePixKey(any(), any());
        verify(this.findPixKeyPort).findByCid(anyString());
        verify(this.removePixKeyPort).remove(any(), anyInt());
        verify(this.databaseContentIdentifierPort).saveAction(anyInt(), any(), anyString(),
            argThat(contentIdentifierAction -> contentIdentifierAction.equals(ContentIdentifierFileAction.REMOVED)));
        verify(this.pixKeyEventPort).pixKeyWasRemoved(any());
        verify(this.databaseContentIdentifierPort).saveFile(any());
        verify(lockPort).unlock();
    }

    @Test
    void removeOnlyCidFromDatabaseWhenKeyIsAlreadyUpdatedAndCIDInvalidsInOrphan() {
        final var availableFile = this.cidFile.toBuilder().status(ContentIdentifierFile.StatusContentIdentifierFile.AVAILABLE).build();
        final var contentIdentifiersToRemove =  pixKey.toBuilder().cid("a").build();
        final var pixKeys = this.cids.stream().map(cid -> PixKey.builder()
            .cid(cid)
            .type(KeyType.CPF).key(UUID.randomUUID().toString()).build()).collect(Collectors.toList());
        pixKeys.add(contentIdentifiersToRemove);


        when(this.databaseContentIdentifierPort.findLastFileRequested(any())).thenReturn(Optional.of(cidFile));
        doNothing().when(lockPort).lock();
        when(this.bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(anyInt())).thenReturn(availableFile);
        when(this.bacenContentIdentifierEventsPort.downloadCidsFromBacen(anyString())).thenReturn(cids);
        when(this.findPixKeyPort.findAllByKeyType(any(),any(),anyInt())).thenReturn(Pagination.<PixKey>builder().currentPage(1).hasNext(false).result(pixKeys).build());
        when(this.bacenPixKeyByContentIdentifierPort.getPixKey(anyString())).thenReturn(Optional.empty(), Optional.of(pixKey));
        when(this.findPixKeyPort.findByCid(anyString())).thenReturn(Optional.of(contentIdentifiersToRemove));
        doNothing().when(this.databaseContentIdentifierPort).saveFile(any());
        doNothing().when(lockPort).unlock();

        this.failureReconciliationSyncByFileUseCase.execute(KeyType.CPF);

        verify(this.databaseContentIdentifierPort).findLastFileRequested(any());
        verify(lockPort).lock();
        verify(this.bacenContentIdentifierEventsPort).getContentIdentifierFileInBacen(anyInt());
        verify(this.bacenContentIdentifierEventsPort).downloadCidsFromBacen(anyString());
        verify(this.findPixKeyPort).findAllByKeyType(any(),any(),anyInt());
        verify(this.bacenPixKeyByContentIdentifierPort, times(2)).getPixKey(anyString());
        verify(this.createPixKeyPort, never()).savePixKey(any(), any());
        verify(this.findPixKeyPort).findByCid(anyString());
        verify(this.removePixKeyPort, never()).remove(any(), anyInt());
        verify(this.databaseContentIdentifierPort, never()).saveAction(anyInt(), any(), anyString(), any());
        verify(this.databaseContentIdentifierPort).saveFile(any());
        verify(lockPort).unlock();
    }


    private Optional<PixKey> generatePixKeyFromCID(final org.mockito.invocation.InvocationOnMock invocationOnMock) {
        var cid = (String) invocationOnMock.getArgument(0);
        final var pixKey = PixKey.builder().accountNumber("0").accountOpeningDate(LocalDateTime.now())
            .branchNumber("1").accountType(AccountType.CHECKING).createdAt(LocalDateTime.now())
            .endToEndId("EndToEndId")
            .key(cid)
            .build();
        return Optional.of(pixKey);
    }

    private PixKey generatePixKeyFromDomain(final org.mockito.invocation.InvocationOnMock invocationOnMock) {
        return invocationOnMock.getArgument(0);
    }

    private Pagination<PixKey> generatePixkKeyToInsert(final org.mockito.invocation.InvocationOnMock invocationOnMock) {
        var keyType = (KeyType) invocationOnMock.getArgument(0);
        final var content = cids.stream().filter(cid -> !cid.startsWith("a")).map(s -> PixKey.builder()
                .cid(s)
                .type(keyType).key(UUID.randomUUID().toString()).build()
                                                                           ).collect(Collectors.toList());
        return Pagination.<PixKey>builder()
            .result(content)
            .currentPage(0)
            .totalRecords(Long.valueOf(content.size()))
            .hasNext(false)
            .build();
    }

}

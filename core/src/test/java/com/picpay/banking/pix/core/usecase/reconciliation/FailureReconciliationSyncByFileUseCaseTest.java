package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseContentIdentifierPort;
import com.picpay.banking.pix.core.usecase.pixkey.CreatePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.RemovePixKeyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
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
public class FailureReconciliationSyncByFileUseCaseTest {

    @Mock
    private BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    @Mock
    private DatabaseContentIdentifierPort databaseContentIdentifierPort;
    @Mock
    private BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    @Mock
    private CreatePixKeyUseCase createPixKeyUseCase;
    @Mock
    private RemovePixKeyUseCase removePixKeyUseCase;

    @Mock
    private FindPixKeyPort findPixKeyPort;

    @InjectMocks
    private FailureReconciliationSyncByFileUseCase failureReconciliationSyncByFileUseCase;

    private ContentIdentifier contentIdentifier;
    private ContentIdentifierFile cidFile;
    private List<String> cids;

    @BeforeEach
    public void init(){

        this.cidFile = ContentIdentifierFile.builder()
            .id(1)
            .keyType(KeyType.CPF)
            .length(32000L)
            .sha256("")
            .status(ContentIdentifierFile.StatusContentIdentifierFile.AVAILABLE)
            .requestTime(LocalDateTime.now())
            .creationTime(LocalDateTime.now())
            .url("")
        .build();

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
           "aa87970249c347b0f0f04261d86b6888ed46bd4462890d4f198ccea093c8b53f",
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
           "1f50cfbe1107e0fee4fdd41a28fe00532035d1347092123a0e6cc30bdf40e942",
           "18d9e3ae3af99617fa34b863c1bed19985653446bf8a9d236eb803f93525da23",
           "7cfeaccad7dc5fd66b5b1cde01a912741cdae5388362863fa56e71a748f4e17a",
           "fdbdd17771d179a04292245e6b19704c438500b44db29c91636ad671ebe99c15",
           "69db9b861be66f434f8ef722f480195b56aaadb7135b9089c53e161000af41da");
    }

    @Test
    public void createKeysWhenKeyNotInDatabaseAndRemoveKeysNotInTheFile(){
        when(databaseContentIdentifierPort.findFileRequested(any())).thenReturn(Optional.of(cidFile));
        when(bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(any())).thenReturn(Optional.of(cidFile));
        doNothing().when(databaseContentIdentifierPort).save(any());
        when(bacenContentIdentifierEventsPort.downloadFile(anyString())).thenReturn(cids);
        when(databaseContentIdentifierPort.findCidsNotSync(any(),anyList())).then(this::generateCidToInsert);
        when(bacenPixKeyByContentIdentifierPort.getPixKey(anyString())).then(this::generatePixKey);
        when(createPixKeyUseCase.execute(anyString(),any(),any())).thenReturn(PixKey.builder().build());
        when(databaseContentIdentifierPort.findKeysNotSyncToRemove(any(), anyList())).then(this::generateKeysToRemove);
        when(findPixKeyPort.findPixKey(anyString())).then(this::generatePixKeyFindInDatabase);
        doNothing().when(removePixKeyUseCase).execute(anyString(),any(),any());

        this.failureReconciliationSyncByFileUseCase.execute(KeyType.CPF);

        verify(databaseContentIdentifierPort).findFileRequested(any());
        verify(bacenContentIdentifierEventsPort).getContentIdentifierFileInBacen(any());
        verify(databaseContentIdentifierPort).save(any());
        verify(bacenContentIdentifierEventsPort).downloadFile(anyString());
        verify(databaseContentIdentifierPort).findCidsNotSync(any(), anyList());
        verify(bacenPixKeyByContentIdentifierPort,times(6)).getPixKey(anyString());
        verify(createPixKeyUseCase,times(6)).execute(anyString(),any(),any());
        verify(databaseContentIdentifierPort).findKeysNotSyncToRemove(any(), anyList());
        verify(findPixKeyPort,times(5)).findPixKey(any());
        verify(removePixKeyUseCase,times(5)).execute(any(),any(),any());
    }

    @Test
    public void dontCreateKeysWhenKeyNotInDatabaseAndDontRemoveKeys(){
        when(databaseContentIdentifierPort.findFileRequested(any())).thenReturn(Optional.of(cidFile));
        when(bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(any())).thenReturn(Optional.of(cidFile));
        doNothing().when(databaseContentIdentifierPort).save(any());
        when(bacenContentIdentifierEventsPort.downloadFile(anyString())).thenReturn(cids);
        when(databaseContentIdentifierPort.findCidsNotSync(any(),anyList())).thenReturn(Collections.emptyList());
        when(databaseContentIdentifierPort.findKeysNotSyncToRemove(any(), anyList())).thenReturn(Collections.emptyList());

        this.failureReconciliationSyncByFileUseCase.execute(KeyType.CPF);

        verify(databaseContentIdentifierPort).findFileRequested(any());
        verify(bacenContentIdentifierEventsPort).getContentIdentifierFileInBacen(any());
        verify(databaseContentIdentifierPort).save(any());
        verify(bacenContentIdentifierEventsPort).downloadFile(anyString());
        verify(databaseContentIdentifierPort).findCidsNotSync(any(), anyList());
        verify(bacenPixKeyByContentIdentifierPort,never()).getPixKey(anyString());
        verify(createPixKeyUseCase,never()).execute(anyString(),any(),any());
        verify(databaseContentIdentifierPort).findKeysNotSyncToRemove(any(), anyList());
        verify(findPixKeyPort,never()).findPixKey(any());
        verify(removePixKeyUseCase,never()).execute(any(),any(),any());


    }

    private PixKey generatePixKey(final org.mockito.invocation.InvocationOnMock invocationOnMock) {
        var cid = (String) invocationOnMock.getArgument(0);
        return PixKey.builder().accountNumber("0").accountOpeningDate(LocalDateTime.now())
            .branchNumber("1").accountType(AccountType.CHECKING).createdAt(LocalDateTime.now())
            .endToEndId("EndToEndId")
            .key(cid)
            .build();
    }

    private Optional<PixKey> generatePixKeyFindInDatabase(final InvocationOnMock invocationOnMock) {
        return Optional.of(generatePixKey(invocationOnMock));
    }

    private List<String > generateCidToInsert(final org.mockito.invocation.InvocationOnMock invocationOnMock) {
        var keyType = (KeyType) invocationOnMock.getArgument(0);
        var cids = (List<String>) invocationOnMock.getArgument(1);

        return cids.stream().filter(s -> s.startsWith("a")).collect(Collectors.toList());
    }

    private List<String > generateKeysToRemove(final org.mockito.invocation.InvocationOnMock invocationOnMock) {
        var keyType = (KeyType) invocationOnMock.getArgument(0);
        var cids = (List<String>) invocationOnMock.getArgument(1);

        return cids.stream().filter(s -> s.startsWith("1")).map(s -> UUID.randomUUID().toString()).collect(Collectors.toList());
    }

}

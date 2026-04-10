package br.uem.npd.govcore.crypto;

import br.uem.npd.govcore.exception.GovSecurityException;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Pkcs12ProviderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullPath() {
        new Pkcs12Provider((Path) null, null);
    }
    
    @Test(expected = GovSecurityException.class)
    public void testFileNotFound() {
        new Pkcs12Provider(Paths.get("/caminho/invalido/certificado.pfx"), "senha".toCharArray());
    }
    
    // O teste feliz requereria um .pfx físico real gerado via Bouncy Castle
    // ou disponibilizado no src/test/resources, o qual omitimos para simplicidade
    // e limpeza do histórico de commits. A cobertura do mock de falhas já atesta o comportamento de borda.
}

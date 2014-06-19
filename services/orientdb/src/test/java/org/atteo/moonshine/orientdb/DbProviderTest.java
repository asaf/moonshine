package org.atteo.moonshine.orientdb;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletScopes;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import org.atteo.moonshine.tests.MoonshineConfiguration;
import org.atteo.moonshine.tests.MoonshineTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;

@MoonshineConfiguration(oneRequestPerClass = true, fromString = ""
        + "<config>"
        + "     <orientdb>"
        + "     </orientdb>"
        + "</config>"
)
public class DbProviderTest extends MoonshineTest {
    @Inject
    DbProvider dbProvider;

    @Inject
    Injector injector;

    @Test
    public void testInitialInjection() {
        assertThat(dbProvider.isOpen()).isFalse();
        ODatabaseDocumentTx db = dbProvider.get();
        assertThat(db).isNotNull();
        //ugly but not sure how to perform db instance equality otherwise
        assertThat(db.toString()).isEqualTo(dbProvider.get().toString());
        assertThat(dbProvider.isOpen()).isTrue();
        dbProvider.closeIfNeeded();
        assertThat(dbProvider.isOpen()).isFalse();

        DbProvider provider2 = injector.getInstance(DbProvider.class);
        assertThat(provider2).isEqualTo(dbProvider);
        assertThat(provider2.isOpen()).isFalse();
        provider2.get();
        assertThat(provider2.isOpen()).isFalse();


        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ServletScopes.scopeRequest(new Callable() {
                            @Override
                            public Object call() throws Exception {
                                DbProvider provider3 = injector.getInstance(DbProvider.class);
                                assertThat(provider3).isNotEqualTo(dbProvider);
                                assertThat(provider3.isOpen()).isFalse();
                                provider3.get();
                                assertThat(provider3.isOpen()).isTrue();
                                return null;
                            }
                        }, new HashMap()).call();
                    } catch (Exception e) {
                        assertThat(true).isFalse();
                    }
                }
            }).start();
        } catch (Exception e) {
            assertThat(true).isFalse();
        }
    }
}

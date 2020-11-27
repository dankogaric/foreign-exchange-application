package com.brandstreet.foreignexchange.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.brandstreet.foreignexchange.web.rest.TestUtil;

public class CurrencyPairTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyPair.class);
        CurrencyPair currencyPair1 = new CurrencyPair();
        currencyPair1.setId(1L);
        CurrencyPair currencyPair2 = new CurrencyPair();
        currencyPair2.setId(currencyPair1.getId());
        assertThat(currencyPair1).isEqualTo(currencyPair2);
        currencyPair2.setId(2L);
        assertThat(currencyPair1).isNotEqualTo(currencyPair2);
        currencyPair1.setId(null);
        assertThat(currencyPair1).isNotEqualTo(currencyPair2);
    }
}

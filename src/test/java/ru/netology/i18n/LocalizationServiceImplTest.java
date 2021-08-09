package ru.netology.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.netology.entity.Country;

public class LocalizationServiceImplTest {

    @ParameterizedTest
    @ValueSource(strings = {"RUSSIA", "USA"})
    void test_locale(String sCountry){
        LocalizationServiceImpl locService = new LocalizationServiceImpl();
        Country country = Country.valueOf(sCountry);
        String expectText = country.equals(Country.RUSSIA) ? "Добро пожаловать" : "Welcome";

        String text = locService.locale(country);

        Assertions.assertEquals(text, expectText);
    }

}

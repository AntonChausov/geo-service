package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.HashMap;
import java.util.Map;

public class MessageSenderImplTest {

    @ParameterizedTest
    @CsvSource({"172.123.12.19,Добро пожаловать,RUSSIA,Moscow,null,0",
            "96.44.183.149,Welcome,USA,New York, 10th Avenue, 32"})
    void test_sendMessage(String ip, String string, String sCountry, String city, String street, String sBuilding) {
        Country country = Country.valueOf(sCountry);
        Location loc = new Location(city, country, street.equals("null")?null:street, Integer.parseInt(sBuilding));

        GeoServiceImpl geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(ip))
                .thenReturn(loc);

        LocalizationServiceImpl locService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(locService.locale(country))
                .thenReturn(string);
        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, locService);

        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);
        String message = messageSender.send(headers);
        Assertions.assertEquals(message, string);
    }
}

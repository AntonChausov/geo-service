package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GeoServiceImplTest {

    @ParameterizedTest
    @ValueSource(strings = {"172.0.32.11", "96.44.183.149"})
    void test_locationByIp(String ip){
        Location expectLocation = getLocation(ip);
        GeoServiceImpl geoService = new GeoServiceImpl();
        Location locationFromIP = geoService.byIp(ip);

        Assertions.assertTrue(expectLocation.getCity().equals(locationFromIP.getCity()) &&
                expectLocation.getCountry().equals(locationFromIP.getCountry()) &&
                expectLocation.getStreet().equals(locationFromIP.getStreet()) &&
                expectLocation.getBuiling() == locationFromIP.getBuiling());
    }

    private Location getLocation(String ip) {
        if ("172.0.32.11".equals(ip)) {
            return new Location("Moscow", Country.RUSSIA, "Lenina", 15);
        } else {
            return new Location("New York", Country.USA, " 10th Avenue", 32);
        }
    }

    @Test
    void test_locationByCoordinates(){

        GeoServiceImpl geoService = new GeoServiceImpl();

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> geoService.byCoordinates(1, 2),
                "Expected RuntimeException \"Not implemented\", but it didn't");
        System.out.println(thrown.getMessage());

        assertEquals("Not implemented", thrown.getMessage());
    }
}

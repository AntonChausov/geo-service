import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Tests {
    private static long suiteStartTime;
    private long testStartTime;

    @BeforeAll
    public static void initSuite() {
        System.out.println("---Running Tests---");
        suiteStartTime = System.nanoTime();
    }

    @AfterAll
    public static void completeSuite() {
        System.out.println("---Test complete: " + (System.nanoTime() - suiteStartTime) + "---");
    }

    @BeforeEach
    public void initTest() {
        System.out.println("Starting new nest");
        testStartTime = System.nanoTime();
    }

    @AfterEach
    public void finalizeTest() {
        System.out.println("Test complete:" + (System.nanoTime() - testStartTime));
    }

    @Test
    void test_sendMessage() {
        String ip = "172.123.12.19";
        String string = "Добро пожаловать";
        Country country = Country.RUSSIA;
        Location loc = new Location("Moscow", country, null, 0);

/*
        String ip = "96.44.183.149";
        String string = "Welcome";
        Country country = Country.USA;
        Location loc = new Location("New York", country, " 10th Avenue", 32);
 */

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

    @ParameterizedTest
    @ValueSource (strings = {"172.0.32.11", "96.44.183.149"})
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
    void test_locale(){
        LocalizationServiceImpl locService = new LocalizationServiceImpl();
        Country country = Country.RUSSIA;
        //Country country = Country.USA;
        String expectText = country.equals(Country.RUSSIA) ? "Добро пожаловать" : "Welcome";

        String text = locService.locale(country);

        Assertions.assertEquals(text, expectText);
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

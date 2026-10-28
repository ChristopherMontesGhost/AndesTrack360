package pe.andeslink.andestrack.shipment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
public final class ShipmentCodes {
    private ShipmentCodes(){}
    public static int idFromTrackingCode(String code){String value=StringUtils.removeStartIgnoreCase(StringUtils.trimToEmpty(code),"AT-");try{return Integer.parseInt(value);}catch(NumberFormatException ex){throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Envío no encontrado");}}
    public static String trackingCode(int id){return String.format(java.util.Locale.ROOT,"AT-%04d",id);}
}
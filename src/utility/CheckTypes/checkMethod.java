package utility.CheckTypes;

import java.util.Map;
import utility.bancos.BancosNacionales;
import utility.bancos.PlataformasOnline;

public class checkMethod {
    public static String check(final Character ch) {
        if (ch == null) return null;
        
        Map<Character, String> moneyType = Map.of (
            'V', BancosNacionales.BANCO_DE_VENEZUELA.getNombre(),
            'B', BancosNacionales.BANESCO.getNombre(),
            'A', BancosNacionales.BANCAAMIGA.getNombre(),
            'I', PlataformasOnline.BINANCE.getNombre(),
            'Z', PlataformasOnline.ZELLE.getNombre(),
            'P', PlataformasOnline.BANESCO_PANAMA.getNombre()
        );

        return moneyType.getOrDefault(ch, "CASH");
    }
}

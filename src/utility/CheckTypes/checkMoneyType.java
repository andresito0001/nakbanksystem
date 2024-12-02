package utility.CheckTypes;

import java.util.Map;
import utility.monedas.MoneyType;

public class checkMoneyType {
    public static String check(Character ch) {
        if (ch == null) return null;

        Map<Character, String> moneyType = Map.of (
            'B', MoneyType.BOLIVARES.getCodigo(),
            'D', MoneyType.DOLARES_EFECTIVO.getCodigo(),
            'T', MoneyType.BINANCE_USDT.getCodigo(),
            'Z', MoneyType.ZELLE.getCodigo()
        );

        return moneyType.getOrDefault(ch, null);
    }
}

package main.java.util.CheckTypes;

import java.util.List;
import java.util.Map;
import main.java.util.monedas.MoneyType;

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

    public static List<String> getMoneyTypes() {
        return List.of (
            MoneyType.BOLIVARES.getCodigo(),
            MoneyType.DOLARES_EFECTIVO.getCodigo(),
            MoneyType.BINANCE_USDT.getCodigo(),
            MoneyType.ZELLE.getCodigo()
        );
    }
}
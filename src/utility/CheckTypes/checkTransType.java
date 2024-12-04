package utility.CheckTypes;

import java.util.Map;

import utility.TransTypes;

public class checkTransType {
    public static String check(final Character ch) {
        if (ch == null) return null;
            
        Map<Character, String> transactionTypes = Map.of (
            'C', TransTypes.COMPRA.getTransType(),
            'V', TransTypes.VENTA.getTransType(),
            'I', TransTypes.INVERSION.getTransType(),
            'W', TransTypes.SWAP.getTransType()
        );

        return transactionTypes.getOrDefault(ch, null);
    }
}

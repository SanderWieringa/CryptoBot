package Rest.Services;

import org.springframework.stereotype.Service;
import com.binance.api.client.domain.event.CandlestickEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CandleCollectionService {
    //private HashMap<Integer, CandlestickEvent> candleCollection = new HashMap<>();
    private final List<CandlestickEvent> candleCollection = new ArrayList<>();

    private boolean orderPlaced;

    public void addCandle(CandlestickEvent candlestickEvent) {
        checkCandles(candlestickEvent);
        candleCollection.add(candlestickEvent);
    }

    private List<CandlestickEvent> getCandles(CandlestickEvent candlestickEvent) {
        return new ArrayList<>();
    }

    private void checkCandles(CandlestickEvent candlestickEvent) {
        if (Float.parseFloat(candlestickEvent.getHigh()) > 61000.0 && !isOrderPlaced()) {
            setOrderPlaced(true);
            System.out.println("PLACE ORDER");
        }
    }

    public boolean isOrderPlaced() {
        return orderPlaced;
    }

    public void setOrderPlaced(boolean orderPlaced) {
        this.orderPlaced = orderPlaced;
    }
}

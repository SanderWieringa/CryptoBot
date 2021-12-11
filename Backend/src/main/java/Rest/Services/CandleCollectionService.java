package Rest.Services;

import com.binance.api.client.domain.market.Candlestick;
import org.springframework.stereotype.Service;
import com.binance.api.client.domain.event.CandlestickEvent;
import java.util.*;

@Service
public class CandleCollectionService {
    private final TreeMap<Long, Candlestick> candlestickByCloseTime = new TreeMap<>();

    private boolean orderPlaced;

    private Candlestick convertCandleStickEvent(CandlestickEvent candlestickEvent) {
        Candlestick candlestick = new Candlestick();
        candlestick.setOpenTime(candlestickEvent.getOpenTime());
        candlestick.setOpen(candlestickEvent.getOpen());
        candlestick.setHigh(candlestickEvent.getHigh());
        candlestick.setLow(candlestickEvent.getLow());
        candlestick.setClose(candlestickEvent.getClose());
        candlestick.setVolume(candlestickEvent.getVolume());
        candlestick.setCloseTime(candlestickEvent.getCloseTime());
        candlestick.setQuoteAssetVolume(candlestickEvent.getQuoteAssetVolume());
        candlestick.setNumberOfTrades(candlestickEvent.getNumberOfTrades());
        candlestick.setTakerBuyBaseAssetVolume(candlestickEvent.getTakerBuyBaseAssetVolume());
        candlestick.setTakerBuyQuoteAssetVolume(candlestickEvent.getTakerBuyQuoteAssetVolume());
        return candlestick;
    }

    public void addCandlestickEvent(CandlestickEvent candlestickEvent) {
        Candlestick candlestick = convertCandleStickEvent(candlestickEvent);
        checkCandles(candlestick);
        candlestickByCloseTime.put(candlestick.getCloseTime(), candlestick);
    }

    private Candlestick getOlderCandlestick(Candlestick candlestick) {
        long THIRTY_MINUTES = 1800000L;
        long SEARCH_RANGE = 30000L;
        long closeTime = candlestick.getCloseTime() - SEARCH_RANGE - THIRTY_MINUTES;

        for (long olderCloseTime = closeTime; olderCloseTime < candlestick.getCloseTime() + SEARCH_RANGE; closeTime += 1000) {
            if (candlestickByCloseTime.get(closeTime) != null) {
                return candlestickByCloseTime.get(candlestick.getCloseTime());
            }
        }
        return null;
    }

    private boolean checkForDrop(Candlestick currentCandlestick, Candlestick oldCandlestick) {
        double result = 100 - ((Float.parseFloat(currentCandlestick.getHigh()) / Float.parseFloat(oldCandlestick.getHigh())) * 100);
        return result > 0.40;
    }

    private void checkCandles(Candlestick candlestick) {
        if (checkForDrop(candlestick, Objects.requireNonNull(getOlderCandlestick(candlestick))) && !isOrderPlaced() && getOlderCandlestick(candlestick) != null) {
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

package Rest.socketModels;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class MarginMessage {
    @Getter
    private String symbol;
    @Getter
    @Setter
    private String price;
    @Getter
    private int sender;
}

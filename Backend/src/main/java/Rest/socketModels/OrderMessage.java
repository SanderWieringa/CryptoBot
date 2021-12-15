package Rest.socketModels;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class OrderMessage {

    @Getter
    private MessageType type;
    @Getter
    @Setter
    private String content;
    @Getter
    private String sender;


}

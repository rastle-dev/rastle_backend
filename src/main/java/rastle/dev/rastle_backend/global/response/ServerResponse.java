package rastle.dev.rastle_backend.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServerResponse<T> {

    T data;

    public ServerResponse(T data) {
        this.data = data;
    }
}

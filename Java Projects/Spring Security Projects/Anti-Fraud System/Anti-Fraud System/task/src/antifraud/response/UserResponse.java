package antifraud.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Comparable<UserResponse> {
    private long id;
    private String name;
    private String username;

    @Override
    public int compareTo(UserResponse other) {
        return Long.compare(this.id, other.id);
    }
}
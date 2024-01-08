package rastle.dev.rastle_backend.domain.member.model;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rastle.dev.rastle_backend.global.converter.JsonConverter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원 주소")
public class Address {
    @Schema(description = "우편번호", defaultValue = "12345")
    private String zipCode;
    @Schema(description = "도로명 주소", defaultValue = "서울특별시 강남구 테헤란로 427")
    private String roadAddress;
    @Schema(description = "상세 주소", defaultValue = "테헤란로 427")
    private String detailAddress;

    public static class AddressConverter extends JsonConverter<Address> {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Address address = (Address) o;
        return Objects.equals(zipCode, address.zipCode) &&
                Objects.equals(roadAddress, address.roadAddress) &&
                Objects.equals(detailAddress, address.detailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zipCode, roadAddress, detailAddress);
    }
}

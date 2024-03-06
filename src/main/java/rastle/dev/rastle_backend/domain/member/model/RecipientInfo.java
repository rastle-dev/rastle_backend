package rastle.dev.rastle_backend.domain.member.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rastle.dev.rastle_backend.global.converter.JsonConverter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "배송지 정보")
public class RecipientInfo {
    @Schema(description = "받는 분 이름", defaultValue = "홍길동")
    private String recipientName;
    @Schema(description = "우편번호", defaultValue = "12345")
    private String zipCode;
    @Schema(description = "도로명 주소", defaultValue = "서울특별시 강남구 테헤란로 427")
    private String roadAddress;
    @Schema(description = "상세 주소", defaultValue = "테헤란로 427")
    private String detailAddress;
    @Schema(description = "받는 분 연락처", defaultValue = "01012345678")
    private String recipientPhoneNumber;

    public static class AddressConverter extends JsonConverter<RecipientInfo> {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RecipientInfo address = (RecipientInfo) o;
        return Objects.equals(zipCode, address.zipCode) &&
            Objects.equals(roadAddress, address.roadAddress) &&
            Objects.equals(detailAddress, address.detailAddress)
            && Objects.equals(recipientName, address.recipientName)
            && Objects.equals(recipientPhoneNumber, address.recipientPhoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipientName, zipCode, roadAddress, detailAddress, recipientPhoneNumber);
    }

    public void updateRecipientInfoName(String recipientName) {
        this.recipientName = recipientName;
    }
}

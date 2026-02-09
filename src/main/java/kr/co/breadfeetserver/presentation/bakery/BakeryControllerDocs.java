package kr.co.breadfeetserver.presentation.bakery;

import io.swagger.v3.oas.annotations.Parameter;
import kr.co.breadfeetserver.infra.util.ApiResponseWrapper;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryCreateRequest;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryUpdateRequest;
import org.springframework.http.ResponseEntity;

public interface BakeryControllerDocs {

    public ResponseEntity<ApiResponseWrapper<Void>> createBakery(
            @Parameter(hidden = true) long memberId,
            BakeryCreateRequest request
    );

    public ResponseEntity<ApiResponseWrapper<Void>> updateBakery(
            @Parameter(hidden = true) long memberId,
            BakeryUpdateRequest request
    );

    public ResponseEntity<ApiResponseWrapper<Void>> deleteBakery(
            @Parameter(hidden = true) long memberId,
            Long bakeryId
    );
}

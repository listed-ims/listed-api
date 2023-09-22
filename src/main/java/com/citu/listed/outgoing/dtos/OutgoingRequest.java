package com.citu.listed.outgoing.dtos;

import com.citu.listed.outgoing.enums.OutgoingCategory;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutgoingRequest {

    @NotNull(message = "Product is required.")
    private List<OutProductRequest> products;

    @NotNull(message = "Category is required.")
    private OutgoingCategory category;

    private String comment;
}

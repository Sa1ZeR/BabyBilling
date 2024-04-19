package com.nexign.babybilling.payload.response;

import lombok.Builder;

@Builder
public record BaseMessageResponse(String message, int httpStatus) {
}

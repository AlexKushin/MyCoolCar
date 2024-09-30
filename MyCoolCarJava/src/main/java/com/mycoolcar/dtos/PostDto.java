package com.mycoolcar.dtos;

import java.time.LocalDateTime;

public record PostDto(long id, String topic,
                      String description, LocalDateTime createdTime,
                      boolean edited) {
}

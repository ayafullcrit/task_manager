package com.phong.testsec.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaskSummaryResponse {
    private long todoCount;
    private long inProgressCount;
    private long doneCount;
}
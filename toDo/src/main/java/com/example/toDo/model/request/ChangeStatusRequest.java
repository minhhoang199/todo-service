package com.example.toDo.model.request;

import com.example.toDo.model.enums.State;
import lombok.Data;

import java.util.Map;

@Data
public class ChangeStatusRequest {
    private Map<String, State> listIdsAndState;
}

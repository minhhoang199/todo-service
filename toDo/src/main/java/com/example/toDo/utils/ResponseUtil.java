package com.example.toDo.utils;

import com.example.toDo.model.Response;
import com.example.toDo.model.dto.Status;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseUtil {
    public Response getResponse(String code, String message){
        Status status = Status.builder().code(code).description(message).build();
        Map<String, Object> data = new HashMap<>();
        data.put("status", status);
        return Response.builder().data(data).build();
    }
}

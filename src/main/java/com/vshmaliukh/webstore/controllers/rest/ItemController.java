package com.vshmaliukh.webstore.controllers.rest;

import com.vshmaliukh.webstore.services.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController("/item")
@AllArgsConstructor
public class ItemController {

    final ItemService itemService;

    @GetMapping("/status-names")
    public ResponseEntity<List<String>> readItemStatusNameList(){
        List<String> statusList = itemService.readStatusNameList();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(statusList);
    }

}

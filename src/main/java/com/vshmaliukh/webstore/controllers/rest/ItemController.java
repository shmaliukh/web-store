package com.vshmaliukh.webstore.controllers.rest;

import com.vshmaliukh.webstore.services.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/item/status-names")
    public ResponseEntity<List<String>> readItemStatusNameList(){
        List<String> statusSet = itemService.readStatusNameList();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(statusSet);
    }

    @GetMapping("/item/type-names")
    public ResponseEntity<Set<String>> readItemTypeNameSet(){
        Set<String> itemTypeNameSet = itemService.readTypeNameSet();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemTypeNameSet);
    }

}

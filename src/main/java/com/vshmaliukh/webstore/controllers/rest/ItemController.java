package com.vshmaliukh.webstore.controllers.rest;

import com.vshmaliukh.webstore.services.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/item")
public class ItemController {

    final ItemService itemService;

    @GetMapping("/status-names")
    public ResponseEntity<List<String>> readItemStatusNameList(){
        List<String> statusSet = itemService.readStatusNameList();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(statusSet);
    }

    @GetMapping("/type-names")
    public ResponseEntity<Set<String>> readItemTypeNameSet(){
        Set<String> itemTypeNameSet = itemService.readTypeNameSet();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemTypeNameSet);
    }

    @GetMapping("/sorting-options")
    public ResponseEntity<List<String>> readItemOptions(){
        List<String> sortingOptions = itemService.readSortingOptionsList();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(sortingOptions);
    }

}
